package com.github.decyg.CrAgg.spring.models

import com.github.decyg.CrAgg.cif.results.CIFBriefResult
import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.DBSource
import com.github.decyg.CrAgg.database.query.AND
import com.github.decyg.CrAgg.database.query.QueryExpression
import com.github.decyg.CrAgg.database.query.TERM
import com.github.decyg.CrAgg.database.query.enums.AllowableQueryType
import com.github.decyg.CrAgg.database.query.enums.CommonQueryTerm
import com.github.decyg.CrAgg.database.query.enums.QueryQuantifier

/**
 * Wrapper object for a variety of inputs to be used from the client
 *
 * @param dbList a list of datasources that the user can search on
 * @param termMap a map of [CommonQueryTerm] objects to [String] that the user chose
 * @param quantifierMap a map of [CommonQueryTerm] objects to [QueryQuantifier] that the user chose
 */
data class SearchResultModel(
        var dbList : List<String> = mutableListOf(),
        var termMap: Map<CommonQueryTerm, String> = mutableMapOf(),
        var quantifierMap : Map<CommonQueryTerm, QueryQuantifier> = mutableMapOf()
) {

    /**
     * Does the main brunt of the work in converting a [SearchResultModel] into a [BriefResultsModel]
     * It squashes the various inputs into a single query using [AND]s but can be far more modular in the future.
     * And then it does the actual work of querying all the selected data sources
     *
     * @return the [BriefResultsModel] representing the list of results of a query
     */
    fun toBriefResultsModel(): BriefResultsModel {
        // First generate the query expression object from the input enums
        // Right now this is pretty basic and just chains ANDS but it has enough
        // modularity to support complex queries in the future

        var qExp : QueryExpression? = null

        this.termMap
                .filter { it.value != null && it.value.trim() != "" }
                .forEach { queryTerm, userTerm ->
                    val quant = this.quantifierMap[queryTerm]!!
                    val term : TERM = when(queryTerm.fieldType){

                        AllowableQueryType.NUMERICAL -> TERM(queryTerm, quant, numericalTerm = userTerm.toDouble())
                        AllowableQueryType.TEXT -> TERM(queryTerm, quant, textTerm = userTerm)
                        AllowableQueryType.MULTI_SINGLE_CHOICE -> TERM(queryTerm, quant, textTerm = userTerm)
                        AllowableQueryType.MULTI_MANY_CHOICE -> TERM(queryTerm, quant, textTerm = userTerm)

                    }

                    if(qExp == null) {
                        qExp = QueryExpression(term)
                    } else {
                        qExp = QueryExpression(AND(qExp!!.expression, term))
                    }

                }

        // Then perform the queries and compound them into one big list

        val sourcesToQuery : List<DBSource> = this.dbList.map { strInput -> DBSingleton.datasetMap.keys.find { it.simpleName == strInput }!! }
        val aggregateResults : MutableList<CIFBriefResult> = mutableListOf()


        sourcesToQuery
                .map { DBSingleton.getDBBySource(it).queryDatabase(qExp!!) }
                .forEach { aggregateResults.addAll(it) }



        return BriefResultsModel(aggregateResults)
    }
}

