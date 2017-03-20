package com.github.decyg.CrAgg.database.indexer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.decyg.CrAgg.cif.CIFSingleton
import com.github.decyg.CrAgg.cif.results.CIFDetailedResult
import com.github.decyg.CrAgg.cif.results.CIF_ID
import com.github.decyg.CrAgg.database.DBSingleton
import com.mongodb.MongoClient
import org.bson.Document

import java.io.File
import java.util.concurrent.Executors

/**
 * This is a singleton to manage interfacing with the MongoDB API for CIF specific things
 */
object MongoSingleton {


    val tPool = Executors.newCachedThreadPool()!!

    val mongoClient = MongoClient()
    val mongoDB = mongoClient.getDatabase("CrAgg")
    val mongoCol = mongoDB.getCollection("cifdetailedresult")


    /**
     * Takes in a [File] object referring to a new locally stored cif entry, reads it, turns it into json by encoding
     * a CIFDetailedResult object
     */
    fun updateIndexForFile(id : CIF_ID, inFile : String){

        if(!inFile.endsWith("cif"))
            return

        tPool.execute {

            var cifFile : File? = null
            var fileText : String = ""

            while (fileText == ""){
                try {
                    cifFile = File("${DBSingleton.getLocalStorageForSource(id.db).path}\\$inFile")

                    fileText = cifFile.readText()
                } catch (e : Exception){ }
                Thread.sleep(10)
            }

            try {
                if(fileText != "") {

                    if(cifFile != null)

                        mongoCol.insertOne(Document.parse(jacksonObjectMapper().writeValueAsString(CIFDetailedResult().populateCIF(id, CIFSingleton.parseCIF(fileText)))))

                        println("success $cifFile")

                }

            } catch (ex : CIFSingleton.ParseException){
                // Need to log failed parses
                //println(ex)
            }
        }

    }
}