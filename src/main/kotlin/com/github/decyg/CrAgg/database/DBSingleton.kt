package com.github.decyg.CrAgg.database

import com.github.decyg.CrAgg.database.implementation.COD
import com.github.decyg.CrAgg.database.query.CommonQueryTerm
import kotlin.reflect.KProperty1

/**
 * Central class to handle dispatching and managing of various implementations
 * Created by declan on 27/02/2017.
 */

typealias DBSource = KProperty1<out DBAbstraction, Class<out DBAbstraction>>

object DBSingleton {

    /**
     * How this works...
     * It provides a mapping for a common "internal" tag represented as the CommonQueryTerm enum to a field header, key
     * or whatever that may store that information in the respective dataset, this is to enhance modularity in case
     * of adoption of other databases in the future
     */
    val datasetMap = mapOf<DBSource, DBAbstraction>(

            COD::javaClass to COD(mapOf(
                    CommonQueryTerm.ID to "file",
                    CommonQueryTerm.AUTHOR to "authors",
                    CommonQueryTerm.SPACE_GROUP to "sg",
                    CommonQueryTerm.COMP_NAME to "chemname",
                    CommonQueryTerm.FORMULA to "formula"
            ))

    )

    fun getDBBySource(source : DBSource) : DBAbstraction {
        return datasetMap[source]!!
    }

    fun getMapForSource(source : DBSource): Map<CommonQueryTerm, String> {
        return datasetMap[source]!!.queryMap
    }

    fun getMappingForSource(source : DBSource, key : CommonQueryTerm) : String {
        return datasetMap[source]!!.queryMap[key]!!
    }
}