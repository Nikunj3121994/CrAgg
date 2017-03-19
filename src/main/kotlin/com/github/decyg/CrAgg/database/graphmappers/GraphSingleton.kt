package com.github.decyg.CrAgg.database.graphmappers

import com.github.decyg.CrAgg.database.indexer.MongoSingleton
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import org.bson.conversions.Bson

/**
 * Created by declan on 19/03/2017.
 */
object GraphSingleton {

    fun processGraphToClientJson(graphEnum : ChemGraphs, termInputs : List<Pair<ChemField, String>>) : Pair<String, String> {

        if(graphEnum.termList.count() != termInputs.count())
            return Pair("", "")


        val termInputsSanitised = termInputs.filter { it.second != "" }
        val searchList : MutableList<Bson> = mutableListOf()

        termInputsSanitised.forEach { (field, value) ->
            searchList.add(field.transformInput(value))
        }

        graphEnum.projectionKeys.forEach {
            searchList.add(fields(exists(it), ne(it, null)))
        }

        val searchBson : Bson = and(searchList)
        val projectionBson : Bson = fields(include(graphEnum.projectionKeys))

        return graphEnum.documentToJson(termInputsSanitised, MongoSingleton.mongoCol.find(searchBson).projection(projectionBson))
    }
}