package com.github.decyg.CrAgg.database.implementation

import com.github.andrewoma.kwery.core.DefaultSession
import com.github.andrewoma.kwery.core.dialect.MysqlDialect
import com.github.decyg.CrAgg.cif.results.CIFBriefResult
import com.github.decyg.CrAgg.cif.results.CIFDetailedResult
import com.github.decyg.CrAgg.database.DBAbstraction
import com.github.decyg.CrAgg.database.query.*
import java.sql.DriverManager
import java.util.*

/**
 * An implementation for the COD (Crystallographic Open Database)
 * Created by declan on 01/03/2017.
 */
class COD(override val queryMap: Map<CommonQueryTerm, String>) : DBAbstraction {

    data class COD_Res(val fileID: Int, val author: String, val spaceGroup: String?, val compoundName: String?, val chemFormula: String?)

    override fun queryDatabaseSpecific(specificResult: CIFBriefResult): CIFDetailedResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun queryDatabase(query: QueryExpression): List<CIFBriefResult> {


        val connectionProp = Properties()
        connectionProp.put("user", "cod_reader")

        val con = DriverManager.getConnection("jdbc:mysql://www.crystallography.net:3306/cod", connectionProp)

        val curSession = DefaultSession(con, MysqlDialect())

        val sql = "SELECT * FROM data WHERE ${queryExpressionToSQL(query)}"

        println("query: $sql")

        val dataResults = curSession.select(sql) {

            row -> COD_Res(
                row.int(queryMap[CommonQueryTerm.ID]!!),
                row.string(queryMap[CommonQueryTerm.AUTHOR]!!),
                row.stringOrNull(queryMap[CommonQueryTerm.SPACE_GROUP]!!),
                row.stringOrNull(queryMap[CommonQueryTerm.CHEM_NAME]!!),
                row.stringOrNull(queryMap[CommonQueryTerm.STRUCT_FORMULA]!!)
                )

        }

        println(dataResults)

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

                val compText : String = when (query.quantifier) {

                    QueryQuantifier.CONTAINS -> "LIKE"
                    QueryQuantifier.IS_EXACT -> "="
                    QueryQuantifier.LESS_THAN -> "<"
                    QueryQuantifier.LESS_THAN_EQUAL -> "<="
                    QueryQuantifier.EQUAL -> "="
                    QueryQuantifier.GREATER_THAN_EQUAL -> ">="
                    QueryQuantifier.GREATER_THAN -> ">"

                }

                when(query.key.fieldType) {

                    AllowableQueryType.TEXT -> {

                        val queryText = when (query.quantifier) {
                            QueryQuantifier.CONTAINS -> "%${query.textTerm}%"
                            else -> "query.textTerm"
                        }

                        return "($queryColumn $compText '$queryText')"
                    }

                    AllowableQueryType.NUMERICAL -> return "($queryColumn $compText ${query.numericalTerm})"

                }

            }

        }

        return ""

    }

    /**
     * "file",
    CommonQueryTerm.AUTHOR to "authors",
    CommonQueryTerm.SPACE_GROUP to "sg",
    CommonQueryTerm.COMP_NAME to "chemname",
    CommonQueryTerm.FORMULA to "formula"

    object data : Table() {

        val queryMap = DBSingleton.getMapForSource(COD::class)

        val fileID = integer(queryMap[CommonQueryTerm.ID]!!).primaryKey()
        val author = text(queryMap[CommonQueryTerm.AUTHOR]!!)
        val spaceGroup = varchar(queryMap[CommonQueryTerm.SPACE_GROUP]!!, 32).nullable()
        val compoundName = varchar(queryMap[CommonQueryTerm.COMP_NAME]!!, 2048).nullable()
        val chemFormula = varchar(queryMap[CommonQueryTerm.FORMULA]!!, 255).nullable()

    }
    */
}

