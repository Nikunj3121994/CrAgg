package com.github.decyg.CrAgg.spring.session

import com.github.decyg.CrAgg.cif.results.CIF_ID
import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.utils.Constants
import java.io.File

/**
 * Singleton that handles the majority of the in-session data storage and cache handling of CIF files on disk
 */
object CIFSessionHandler {

    val cacheFolder = File(Constants.CIF_CACHE_FOLDER)

    init {

        if(!cacheFolder.exists())
            cacheFolder.mkdir()

    }

    // Internal

    /**
     * Wrapper to convert a cif id into a File path
     *
     * @param cif_ID the id to get
     * @return the file represented by the cif id
     */
    private fun idAsFile(cif_ID: CIF_ID) = File(cacheFolder, "${cif_ID.db.simpleName}-${cif_ID.id}.cif")

    // External

    /**
     * Thread safe function to download a CIF file given a [cif_ID] to the local storage
     *
     * @param cif_ID the cif id to download
     */
    fun addCIFToCache(cif_ID: CIF_ID){
        synchronized(this, {
            if(!cacheHasCIF(cif_ID)){
                val cifFileOut = idAsFile(cif_ID).outputStream()
                val cifFileIn = DBSingleton.getDBBySource(cif_ID.db).getStreamForID(cif_ID)

                cifFileIn.copyTo(cifFileOut)

                cifFileIn.close()
                cifFileOut.close()

            }
        })
    }

    /**
     * Thread safe function to remove a downloaded CIF file given a [cif_ID]
     *
     * @param cif_ID the id to delete
     */
    fun removeCIFFromCache(cif_ID: CIF_ID){
        synchronized(this, {
            if (cacheHasCIF(cif_ID)) {
                idAsFile(cif_ID).delete()
            }
        })
    }

    /**
     * Check function to see whether or not the datastore contains a cached CIF
     *
     * @param cif_ID the id to search for
     * @return true if it does, false if not
     */
    fun cacheHasCIF(cif_ID: CIF_ID) : Boolean {
        synchronized(this, {
            return cacheFolder.listFiles { curFile -> curFile == idAsFile(cif_ID) }.isNotEmpty()
        })
    }

    /**
     * Get the [cif_ID] as a file from the datastore, will download if it doesn't exist
     *
     * @param cif_ID the id to get
     * @return the file
     */
    fun getCIFFromCacheAsFile(cif_ID: CIF_ID) : File {
        synchronized(this, {
            if (!cacheHasCIF(cif_ID)) {
                addCIFToCache(cif_ID)
            }

            return idAsFile(cif_ID)
        })
    }
}