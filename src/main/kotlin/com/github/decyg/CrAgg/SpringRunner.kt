package com.github.decyg.CrAgg

import com.github.decyg.CrAgg.database.indexer.CIFService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import javax.annotation.PostConstruct

/**
 * Core runner for the application.
 * >spring
 */
@SpringBootApplication
@EnableCaching
open class SpringRunner{

    @Autowired
    lateinit var tempCIF : CIFService

    @PostConstruct
    fun init() {
        SpringRunner.cifService = tempCIF
    }

    companion object {
        lateinit var cifService: CIFService
    }


}

fun main(args: Array<String>) {

    SpringApplication.run(SpringRunner::class.java, *args)


    //println(jacksonObjectMapper().writeValueAsString(CIFDetailedResult(CIFSingleton.parseCIF(File("cifcache/COD-1517271.cif").readText()))))
   // DBSingleton.getDBBySource(COD::class).updateLocalCIFStorage()


    println(SpringRunner.cifService.findAll().count())



}

