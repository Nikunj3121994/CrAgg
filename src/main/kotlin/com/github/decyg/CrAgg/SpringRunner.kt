package com.github.decyg.CrAgg

import com.github.decyg.CrAgg.database.DBSingleton
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching

/**
 * Core runner for the application.
 * >spring
 */
@SpringBootApplication
@EnableCaching
open class SpringRunner

fun main(args: Array<String>) {

    SpringApplication.run(SpringRunner::class.java, *args)

    // On first run update the local cache for all implementations

    DBSingleton.datasetMap.forEach {
        it.value.updateLocalCIFStorage()
    }

}

