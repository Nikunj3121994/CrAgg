package com.github.decyg.CrAgg.database.implementation

import com.github.andrewoma.kwery.core.DefaultSession
import com.github.andrewoma.kwery.core.dialect.MysqlDialect
import com.github.decyg.CrAgg.cif.results.CIFBriefResult
import com.github.decyg.CrAgg.cif.results.CIFDetailedResult
import com.github.decyg.CrAgg.database.DBAbstraction
import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.query.CommonQueryTerm
import com.github.decyg.CrAgg.database.query.QueryWrapper
import org.jetbrains.exposed.sql.Table
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

    override fun queryDatabase(queries: List<QueryWrapper>): List<CIFBriefResult> {

        val connectionProp = Properties()
        connectionProp.put("user", "cod_reader")

        val con = DriverManager.getConnection("jdbc:mysql://www.crystallography.net:3306/cod", connectionProp)

        val curSession = DefaultSession(con, MysqlDialect())

        val sql = "select * from data limit 5"

        val dataResults = curSession.select(sql) {

            row -> COD_Res(
                row.int(queryMap[CommonQueryTerm.ID]!!),
                row.string(queryMap[CommonQueryTerm.AUTHOR]!!),
                row.stringOrNull(queryMap[CommonQueryTerm.SPACE_GROUP]!!),
                row.stringOrNull(queryMap[CommonQueryTerm.COMP_NAME]!!),
                row.stringOrNull(queryMap[CommonQueryTerm.FORMULA]!!)
                )

        }

        println(dataResults)

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * "file",
    CommonQueryTerm.AUTHOR to "authors",
    CommonQueryTerm.SPACE_GROUP to "sg",
    CommonQueryTerm.COMP_NAME to "chemname",
    CommonQueryTerm.FORMULA to "formula"
     */
    object data : Table() {

        val queryMap = DBSingleton.getMapForSource(COD::javaClass)

        val fileID = integer(queryMap[CommonQueryTerm.ID]!!).primaryKey()
        val author = text(queryMap[CommonQueryTerm.AUTHOR]!!)
        val spaceGroup = varchar(queryMap[CommonQueryTerm.SPACE_GROUP]!!, 32).nullable()
        val compoundName = varchar(queryMap[CommonQueryTerm.COMP_NAME]!!, 2048).nullable()
        val chemFormula = varchar(queryMap[CommonQueryTerm.FORMULA]!!, 255).nullable()

    }
}

