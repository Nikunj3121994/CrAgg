package com.github.decyg.CrAgg.database.indexer

import com.fasterxml.jackson.databind.DeserializationFeature
import com.github.decyg.CrAgg.cif.CIFSingleton
import com.github.decyg.CrAgg.cif.parser.CIFDetailedResult
import com.github.decyg.CrAgg.cif.parser.CIF_ID
import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.utils.GeneralConstants
import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.ServerAddress
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import fr.javatic.mongo.jacksonCodec.JacksonCodecProvider
import fr.javatic.mongo.jacksonCodec.ObjectMapperFactory
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import java.io.File
import java.util.concurrent.Executors

/**
 * This is a singleton to manage interfacing with the MongoDB API for CIF specific things
 */
object MongoSingleton {


    val tPool = Executors.newCachedThreadPool()!!

    val jacksonObjMapper = ObjectMapperFactory.createObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    val mongoCodecRegistry : CodecRegistry = CodecRegistries.fromRegistries(
            MongoClient.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(
                    JacksonCodecProvider(jacksonObjMapper)
            )
    )
    val mongoOptions : MongoClientOptions = MongoClientOptions.builder()
            .codecRegistry(mongoCodecRegistry)
            .build()


    val mongoClient = MongoClient(ServerAddress(), mongoOptions)
    val mongoDB : MongoDatabase = mongoClient.getDatabase(GeneralConstants.MONGODB_NAME)
    val mongoCol : MongoCollection<CIFDetailedResult> = mongoDB.getCollection(GeneralConstants.MONGODB_COLLECTION, CIFDetailedResult::class.java)


    /**
     * Takes in a [File] object referring to a new locally stored cif entry, reads it, turns it into json by encoding
     * a CIFDetailedResult object
     */
    fun updateIndexForFile(id : CIF_ID, inFile : String){

        if(!inFile.endsWith("cif"))
            return

        tPool.execute body@{

            var cifFile : File? = null
            var fileText : String = ""

            while (fileText == ""){
                try {
                    cifFile = File("${DBSingleton.getLocalStorageForSource(id.asDBSource()).path}\\$inFile")

                    fileText = cifFile.readText()
                } catch (e : Exception){ }
                Thread.sleep(10)
            }

            try {
                if(fileText != "") {

                    if(cifFile != null)

                        mongoCol.insertOne(CIFSingleton.getPopulatedCIF(id, CIFSingleton.parseCIF(fileText)))

                        println("success $cifFile")

                }

            } catch (ex : CIFSingleton.ParseException){
                // Need to log failed parses
                //println(ex)
            }
        }

    }
}