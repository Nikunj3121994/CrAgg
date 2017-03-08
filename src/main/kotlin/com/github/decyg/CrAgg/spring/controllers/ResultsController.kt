package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.cif.results.CIFBriefResult
import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.DBSource
import com.github.decyg.CrAgg.database.query.AND
import com.github.decyg.CrAgg.database.query.AllowableQueryType
import com.github.decyg.CrAgg.database.query.QueryExpression
import com.github.decyg.CrAgg.database.query.TERM
import com.github.decyg.CrAgg.spring.models.SearchResultModel
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.validation.Valid

/**
 * Created by declan on 16/02/2017.
 */

@Controller
open class ResultsController {

    @RequestMapping(value = "/results", method = arrayOf(RequestMethod.POST))
    open fun indexPage(@Valid @ModelAttribute("resultList") resultModel : SearchResultModel, bindingResult : BindingResult) : String {

        println(resultModel)


        // First generate the query expression object from the input enums
        // Right now this is pretty basic and just chains ANDS but it has enough
        // modularity to support complex queries in the future

        var qExp : QueryExpression? = null

        resultModel.termMap
                .filter { it.value != null && it.value.trim() != "" }
                .forEach { queryTerm, userTerm ->
                    val quant = resultModel.quantifierMap[queryTerm]!!
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

        val sourcesToQuery : List<DBSource> = resultModel.dbList.map { strInput -> DBSingleton.datasetMap.keys.find { it.simpleName == strInput }!! }
        val aggregateResults : MutableList<CIFBriefResult> = mutableListOf()


        sourcesToQuery
                .map { DBSingleton.getDBBySource(it).queryDatabase(qExp!!) }
                .forEach { aggregateResults.addAll(it) }

        println(aggregateResults)
        //pageModel.addAttribute("searchmodel", SearchModel())

        //TermCategory.valu
        //CommonQueryTerm.
        return "results"
    }

}