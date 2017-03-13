package com.github.decyg.CrAgg.spring.models

import com.github.decyg.CrAgg.cif.results.CIFBriefResult
import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.DBSource
import com.github.decyg.CrAgg.database.query.*

/**
 * Created by declan on 08/03/2017.
 */

data class SearchResultModel(
        var dbList : List<String> = mutableListOf(),
        var termMap: Map<CommonQueryTerm, String> = mutableMapOf(),
        var quantifierMap : Map<CommonQueryTerm, QueryQuantifier> = mutableMapOf()
) {

    fun toBriefResultsModel(page : Int = 1): BriefResultsModel {
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

        println(qExp)


        // Then perform the queries and compound them into one big list

        val sourcesToQuery : List<DBSource> = this.dbList.map { strInput -> DBSingleton.datasetMap.keys.find { it.simpleName == strInput }!! }
        val aggregateResults : MutableList<CIFBriefResult> = mutableListOf()


        sourcesToQuery
                .map { DBSingleton.getDBBySource(it).queryDatabase(qExp!!) }
                .forEach { aggregateResults.addAll(it) }



        return BriefResultsModel(aggregateResults)
    }
}

