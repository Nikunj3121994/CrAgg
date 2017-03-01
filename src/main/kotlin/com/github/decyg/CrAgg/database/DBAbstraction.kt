package com.github.decyg.CrAgg.database

import com.github.decyg.CrAgg.cif.results.CIFBriefResult
import com.github.decyg.CrAgg.cif.results.CIFDetailedResult
import com.github.decyg.CrAgg.database.query.CommonQueryTerm
import com.github.decyg.CrAgg.database.query.QueryWrapper

/**
 * This is an abstraction of the database access process, there's two main methods.
 * QueryDataBase and QueryDatabaseSpecific
 *
 * The DBAbstraction is typed to the specific implementation of itself to allow for typed returns
 * Created by declan on 16/02/2017.
 */
interface DBAbstraction {

    val queryMap : Map<CommonQueryTerm, String>

    /**
     * Should take in a list of query wrappers, form them into a query of the relevant format and then
     * return a list of wrapped CIFResults formed from the result set
     */
    abstract fun queryDatabase(queries : List<QueryWrapper>) : List<CIFBriefResult>

    /**
     * Takes in a single CIFBriefResult selected from the queryDatabase function above, then returns a parsed CIF_Node file in
     * the format of a CIFNode tree
     */
    abstract fun queryDatabaseSpecific(specificResult : CIFBriefResult) : CIFDetailedResult

}