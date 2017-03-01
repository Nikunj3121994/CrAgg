package com.github.decyg.CrAgg.database

import com.github.decyg.CrAgg.database.implementation.COD
import com.github.decyg.CrAgg.database.query.CommonQueryTerm

/**
 * Central class to handle dispatching and managing of various implementations
 * Created by declan on 27/02/2017.
 */
object DBSingleton {

    val datasetMap = mapOf<DBSource, DBAbstraction>(

            DBSource.COD to COD(mapOf(
                    CommonQueryTerm.ID to "file",
                    CommonQueryTerm.AUTHOR to "authors"
            ))

    )

    fun getDBBySource(source : DBSource) : DBAbstraction {
        return datasetMap[source]!!
    }
}

/**
 * Internal declaration of a database source, here for organisation
 */
enum class DBSource {
    COD, CCDC, ICSD
}