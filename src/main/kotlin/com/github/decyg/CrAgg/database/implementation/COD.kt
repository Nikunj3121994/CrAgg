package com.github.decyg.CrAgg.database.implementation

import com.github.decyg.CrAgg.cif.results.CIFBriefResult
import com.github.decyg.CrAgg.cif.results.CIFDetailedResult
import com.github.decyg.CrAgg.database.DBAbstraction
import com.github.decyg.CrAgg.database.query.CommonQueryTerm
import com.github.decyg.CrAgg.database.query.QueryWrapper

/**
 * An implementation for the COD (Crystallographic Open Database)
 * Created by declan on 01/03/2017.
 */
class COD(override val queryMap: Map<CommonQueryTerm, String>) : DBAbstraction {
    override fun queryDatabaseSpecific(specificResult: CIFBriefResult): CIFDetailedResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun queryDatabase(queries: List<QueryWrapper>): List<CIFBriefResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}