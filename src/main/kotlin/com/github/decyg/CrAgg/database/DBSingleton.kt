package com.github.decyg.CrAgg.database

import com.github.decyg.CrAgg.database.implementation.COD
import com.github.decyg.CrAgg.database.query.enums.CommonQueryTerm
import com.github.decyg.CrAgg.utils.Constants
import java.io.File
import kotlin.reflect.KClass



/**
 * In a way this is the central class for the backend as it handles the majority of the interfacing between all the
 * database objects and the rest of the program.
 */
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

                    ),
                    getLocalStorageForSource(COD::class)

            )

    )

    // Util methods for accessability

    /**
     * Get a [DBAbstraction] by the [DBSource]
     *
     * @param source the db source
     * @return the [DBAbstraction] found by that source
     */
    fun getDBBySource(dbSource : DBSource) : DBAbstraction = datasetMap[dbSource]!!

    /**
     * Gets a [DBSource] object based off of the name of the implementation class
     *
     * @param dbName the name of the class to look for
     * @return the [DBSource] object by that name
     */
    fun getDBSourceByName(dbName : String) : DBSource? = datasetMap.keys.find { it.simpleName == dbName }

    /**
     * Gets a File representing a local cif storage, in most cases this will just be a folder like cifstorage/COD
     * this folder contains the entirety of the cifService storage.
     *
     * @param dbSource the source db to manage
     * @return the db folder
     */
    fun getLocalStorageForSource(dbSource : DBSource) : File {

        val rootFolder = File(Constants.CIF_STORAGE_FOLDER)

        if(!rootFolder.exists())
            rootFolder.mkdir()

        val subFolder = File(rootFolder, dbSource.simpleName)

        if(!subFolder.exists())
            subFolder.mkdir()

        return subFolder
    }

}

typealias DBSource = KClass<out DBAbstraction>
