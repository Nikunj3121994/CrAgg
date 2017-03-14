package com.github.decyg.CrAgg.spring.session

import com.github.decyg.CrAgg.cif.results.CIF_ID
import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.utils.Constants
import java.io.File

/**
 * Created by declan on 13/03/2017.
 */
object CIFSessionHandler {

    val cacheFolder = File(Constants.CIF_CACHE_FOLDER)

    init {

        if(!cacheFolder.exists())
            cacheFolder.mkdir()


    }

    private fun idAsFile(cif_ID: CIF_ID) = File(cacheFolder, "${cif_ID.db.simpleName}-${cif_ID.id}.cif")

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

    fun removeCIFFromCache(cif_ID: CIF_ID){
        synchronized(this, {
            if (cacheHasCIF(cif_ID)) {
                idAsFile(cif_ID).delete()
            }
        })
    }

    fun cacheHasCIF(cif_ID: CIF_ID) : Boolean {
        synchronized(this, {
            return cacheFolder.listFiles { curFile -> curFile == idAsFile(cif_ID) }.isNotEmpty()
        })
    }

    fun getCIFFromCacheAsFile(cif_ID: CIF_ID) : File? {
        synchronized(this, {
            if (cacheHasCIF(cif_ID)) {
                return idAsFile(cif_ID)
            } else {
                return null
            }
        })
    }
}