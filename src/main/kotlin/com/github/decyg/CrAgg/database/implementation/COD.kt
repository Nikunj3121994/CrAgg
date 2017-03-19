package com.github.decyg.CrAgg.database.implementation

import com.github.andrewoma.kwery.core.DefaultSession
import com.github.andrewoma.kwery.core.dialect.MysqlDialect
import com.github.decyg.CrAgg.cif.CIFSingleton
import com.github.decyg.CrAgg.cif.results.CIFBriefResult
import com.github.decyg.CrAgg.cif.results.CIFDetailedResult
import com.github.decyg.CrAgg.cif.results.CIF_ID
import com.github.decyg.CrAgg.database.DBAbstraction
import com.github.decyg.CrAgg.database.indexer.MongoSingleton
import com.github.decyg.CrAgg.database.query.*
import com.github.decyg.CrAgg.database.query.enums.AllowableQueryType
import com.github.decyg.CrAgg.database.query.enums.CommonQueryTerm
import com.github.decyg.CrAgg.database.query.enums.QueryQuantifier
import com.github.decyg.CrAgg.utils.Constants
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.sql.DriverManager
import java.util.*



/**
 * An implementation for the COD (Crystallographic Open Database)
 * Created by declan on 01/03/2017.
 */
class COD(
        override val queryMap: Map<CommonQueryTerm, String>,
        override val dataFolder: File
) : DBAbstraction {

    override fun updateLocalCIFStorage() {

        // thanks to the joys of rsync on window, the standard path isn't good enough so i need to convert
        // can do this programmatically and system agnostic by attempting to run it, if i get a 1 error code, it's
        // the syntax and i need to rerun it
        // C:\WINDOWS\BLAH\BLAH
        // to /cygdrive/c/WINDOWS/BLAH

        var dataPath = dataFolder.absolutePath

        if(System.getProperty("os.name").toLowerCase().contains("win"))
            dataPath = dataPath.replace('\\', '/').replace("C:", "/cygdrive/c");


        val cmd = arrayOf("rsync", "-av", "--delete", "rsync://www.crystallography.net/cif/", dataPath)
        val pb = ProcessBuilder()
                .command(*cmd)
                .start()

        val reader = BufferedReader(InputStreamReader(pb.inputStream))

        while(true){
            val line : String? = reader.readLine()
            if(line != null){
                val idFound = "([0-9]+)\\.cif".toRegex().toPattern()

                val matcher = idFound.matcher(line)

                if(matcher.find()){
                    MongoSingleton.updateIndexForFile(CIF_ID(this::class, matcher.group(1)), line.replace('/', '\\'))
                }

            } else {
                break
            }
        }

        reader.close()

    }

    override fun getStreamForID(cifID: CIF_ID): InputStream {

        return URL("http://www.crystallography.net/cod/${cifID.id}.cif").openStream()

    }

    override fun queryDatabaseSpecific(specificResult: CIFBriefResult): CIFDetailedResult {

        val cifText = URL("http://www.crystallography.net/cod/${specificResult.cif_ID.id}.cif").readText()

        return CIFDetailedResult().populateCIF(specificResult.cif_ID, CIFSingleton.parseCIF(cifText))

    }

    override fun queryDatabase(query: QueryExpression): List<CIFBriefResult> {

        val connectionProp = Properties()
        connectionProp.put("user", "cod_reader")

        val con = DriverManager.getConnection("jdbc:mysql://www.crystallography.net:3306/cod", connectionProp)

        val curSession = DefaultSession(con, MysqlDialect())

        val sql = "SELECT * FROM data WHERE ${queryExpressionToSQL(query)} LIMIT ${Constants.TOTAL_RESULTS}"

        val dataResults = curSession.select(sql) { row ->

            val outMap : MutableMap<CommonQueryTerm, String> = mutableMapOf()

            CommonQueryTerm.values().forEach {
                outMap[it] =
                    when(it.fieldType){
                        AllowableQueryType.TEXT -> row.stringOrNull(queryMap[it]!!) ?: "N/A"
                        AllowableQueryType.NUMERICAL -> row.doubleOrNull(queryMap[it]!!).toString() ?: "N/A"
                        else -> "N/A"
                    }

            }

            CIFBriefResult(
                    CIF_ID(this::class, row.int(queryMap[CommonQueryTerm.ID]!!).toString()),
                    outMap

            )

        }

        return dataResults

    }

    /**
     * This is a function to convert the generic [Expression] object into a specific SQL representation
     *
     * @param query the [Expression] object to parse
     * @return a SQL [String]
     */
    fun queryExpressionToSQL(query : Expression) : String {

        when(query) {

            is QueryExpression -> return "(${queryExpressionToSQL(query.expression)})"
            is AND -> return "(${queryExpressionToSQL(query.leftExp)} AND ${queryExpressionToSQL(query.rightExp)})"
            is OR -> return "(${queryExpressionToSQL(query.leftExp)} OR ${queryExpressionToSQL(query.rightExp)})"
            is NOT -> return "(NOT ${queryExpressionToSQL(query.expression)})"
            is TERM -> {

                // Need to split term encoding into numerical and text types and map the three

                val queryColumn = queryMap[query.key]

                val compText : String = query.quantifier.dbMap[this::class]!!

                when(query.key.fieldType) {

                    AllowableQueryType.TEXT -> {

                        val queryText = when (query.quantifier) {
                            QueryQuantifier.CONTAINS -> "%${query.textTerm}%"
                            else -> query.textTerm
                        }

                        return "($queryColumn $compText '$queryText')"
                    }

                    AllowableQueryType.NUMERICAL -> return "($queryColumn $compText ${query.numericalTerm})"

                }

            }

        }

        return ""

    }

}

