package com.github.decyg.CrAgg.database

import com.github.decyg.CrAgg.database.implementation.COD
import com.github.decyg.CrAgg.database.query.CommonQueryTerm
import kotlin.reflect.KClass

/**
 * Central class to handle dispatching and managing of various implementations
 * Created by declan on 27/02/2017.
 */

typealias DBSource = KClass<out DBAbstraction>

object DBSingleton {

    /**
     * How this works...
     * It provides a mapping for a common "internal" tag represented as the CommonQueryTerm enum to a field header, key
     * or whatever that may store that information in the respective dataset, this is to enhance modularity in case
     * of adoption of other databases in the future
     */
    val datasetMap = mapOf<DBSource, DBAbstraction>(

            COD::class to COD(
                    mapOf(
                        CommonQueryTerm.ID to "file",
                        CommonQueryTerm.AUTHOR to "authors",
                        CommonQueryTerm.ARTICLE to "title",
                        CommonQueryTerm.JOURNAL to "journal",
                        CommonQueryTerm.JOURNAL_DOI to "doi",
                        CommonQueryTerm.YEAR to "year",

                        CommonQueryTerm.A_LENGTH to "a",
                        CommonQueryTerm.B_LENGTH to "b",
                        CommonQueryTerm.C_LENGTH to "c",
                        CommonQueryTerm.ALPHA_LENGTH to "alpha",
                        CommonQueryTerm.BETA_LENGTH to "beta",
                        CommonQueryTerm.GAMMA_LENGTH to "gamma",
                        CommonQueryTerm.CELL_VOLUME to "vol",

                        CommonQueryTerm.STRUCT_FORMULA to "formula",
                        CommonQueryTerm.CHEM_NAME to "chemname",
                        CommonQueryTerm.MINERAL to "mineral",

                        CommonQueryTerm.SPACE_GROUP to "sg"

                    )
            )

    )

    // Util methods

    fun getDBBySource(source : DBSource) : DBAbstraction {
        return datasetMap[source]!!
    }

    fun getMapForSource(source : DBSource): Map<CommonQueryTerm, String> {
        return datasetMap[source]!!.queryMap
    }

    fun getMappingForSource(source : DBSource, key : CommonQueryTerm) : String {
        return datasetMap[source]!!.queryMap[key]!!
    }

    fun getDBSourceByName(dbName : String) : DBSource? = datasetMap.keys.find { it.simpleName == dbName }

    fun getDBByName(dbName : String) : DBAbstraction? = datasetMap[getDBSourceByName(dbName)]

}