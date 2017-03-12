package com.github.decyg.CrAgg.database.implementation

import com.github.andrewoma.kwery.core.DefaultSession
import com.github.andrewoma.kwery.core.dialect.MysqlDialect
import com.github.decyg.CrAgg.cif.CIFSingleton
import com.github.decyg.CrAgg.cif.results.CIFBriefResult
import com.github.decyg.CrAgg.cif.results.CIFDetailedResult
import com.github.decyg.CrAgg.database.DBAbstraction
import com.github.decyg.CrAgg.database.DB_UUID
import com.github.decyg.CrAgg.database.query.*
import com.github.decyg.CrAgg.utils.Constants
import java.io.InputStream
import java.net.URL
import java.sql.DriverManager
import java.util.*

/**
 * An implementation for the COD (Crystallographic Open Database)
 * Created by declan on 01/03/2017.
 */
class COD(override val queryMap: Map<CommonQueryTerm, String>) : DBAbstraction {


    override fun getStreamForID(cifID: DB_UUID): InputStream {

        val cifFile = URL("http://www.crystallography.net/cod/${cifID.second}.cif").readText()

        println(cifFile)

        return cifFile.byteInputStream()

    }

    override fun queryDatabaseSpecific(specificResult: CIFBriefResult): CIFDetailedResult {

        val cifText = URL("http://www.crystallography.net/cod/${specificResult.id}.cif").readText()

        return CIFDetailedResult(CIFSingleton.parseCIF(cifText))

    }

    override fun queryDatabase(query: QueryExpression): List<CIFBriefResult> {

        val connectionProp = Properties()
        connectionProp.put("user", "cod_reader")

        val con = DriverManager.getConnection("jdbc:mysql://www.crystallography.net:3306/cod", connectionProp)

        val curSession = DefaultSession(con, MysqlDialect())

        val sql = "SELECT * FROM data WHERE ${queryExpressionToSQL(query)} LIMIT ${Constants.TOTAL_RESULTS}"

        val dataResults = curSession.select(sql) {

            row -> CIFBriefResult(
                DB_UUID(this::class, row.int(queryMap[CommonQueryTerm.ID]!!).toString()),
                row.stringOrNull(queryMap[CommonQueryTerm.SPACE_GROUP]!!) ?: "N/A",
                mapOf(
                        CommonQueryTerm.A_LENGTH to row.double(queryMap[CommonQueryTerm.A_LENGTH]!!),
                        CommonQueryTerm.B_LENGTH to row.double(queryMap[CommonQueryTerm.B_LENGTH]!!),
                        CommonQueryTerm.C_LENGTH to row.double(queryMap[CommonQueryTerm.C_LENGTH]!!),
                        CommonQueryTerm.ALPHA_LENGTH to row.double(queryMap[CommonQueryTerm.ALPHA_LENGTH]!!),
                        CommonQueryTerm.BETA_LENGTH to row.double(queryMap[CommonQueryTerm.BETA_LENGTH]!!),
                        CommonQueryTerm.GAMMA_LENGTH to row.double(queryMap[CommonQueryTerm.GAMMA_LENGTH]!!),
                        CommonQueryTerm.CELL_VOLUME to row.double(queryMap[CommonQueryTerm.CELL_VOLUME]!!)
                ),
                row.stringOrNull(queryMap[CommonQueryTerm.CHEM_NAME]!!) ?: "N/A",
                row.stringOrNull(queryMap[CommonQueryTerm.STRUCT_FORMULA]!!) ?: "N/A",
                row.stringOrNull(queryMap[CommonQueryTerm.AUTHOR]!!) ?: "N/A",
                row.stringOrNull(queryMap[CommonQueryTerm.JOURNAL]!!) ?: "N/A"
                )

        }

        return dataResults

    }

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

