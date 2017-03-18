package com.github.decyg.CrAgg.database.indexer

import com.github.decyg.CrAgg.SpringRunner.Companion.cifService
import com.github.decyg.CrAgg.cif.CIFSingleton
import com.github.decyg.CrAgg.cif.results.CIFDetailedResult
import com.github.decyg.CrAgg.cif.results.CIF_ID
import com.github.decyg.CrAgg.database.DBSingleton
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.Executors

/**
 * This is a singleton to manage interfacing with the ElasticSearch API for cifService specific stuff
 */
@Component
object ElasticSingleton {


    val tPool = Executors.newCachedThreadPool()!!




    /**
     * Takes in a [File] object referring to a new locally stored cif entry, reads it, turns it into json by encoding
     * a CIFDetailedResult object
     */
    fun updateIndexForFile(id : CIF_ID, inFile : String){

        if(!inFile.endsWith("cif"))
            return

        tPool.execute {

            var cifFile : File

            while (true){
                try {
                    cifFile = File("${DBSingleton.getLocalStorageForSource(id.db).path}\\$inFile")

                    if (cifFile.exists())
                        break
                } catch (e : Exception){ }
                Thread.sleep(2)
            }

            println("elastic: $cifFile")


            val fileText = cifFile.readText()
            try {

                cifService.save(CIFDetailedResult().populateCIF(id, CIFSingleton.parseCIF(fileText)))

                /** val cif = jacksonObjectMapper().writeValueAsString(CIFDetailedResult(CIFSingleton.parseCIF(fileText)))

                val resp = ecTransport.prepareIndex(source.simpleName!!.toLowerCase(), "cifService")
                        .setSource(cif)
                        .get()


                println(resp)
                 **/
            } catch (ex : CIFSingleton.ParseException){
                // Need to log failed parses
                //println(ex)
            }
        }

    }
}