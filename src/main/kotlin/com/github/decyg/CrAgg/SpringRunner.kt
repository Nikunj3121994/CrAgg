package com.github.decyg.CrAgg

import com.github.decyg.CrAgg.database.graphmappers.ChemField
import com.github.decyg.CrAgg.database.graphmappers.ChemGraphs
import com.github.decyg.CrAgg.database.graphmappers.GraphSingleton
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


    /*println(jacksonObjectMapper().writeValueAsString(
            CIFDetailedResult().populateCIF(
                    CIF_ID("COD", "22"),
                    CIFSingleton.parseCIF(
                            File("cifcache/COD-1517271.cif").readText()
                    )
            )
    ))*/
    //DBSingleton.getDBBySource(COD::class).updateLocalCIFStorage()


    //println(SpringRunner.cifService.findAll().count())

    /**println(MongoSingleton.mongoCol.find(regex("cifResult.dataBlocks.dataItems.chemical_formula_sum", "Ca\\d*")
    ).projection(fields(include(
            "cifResult.dataBlocks.dataItems.chemical_formula_sum",
            "cifResult.dataBlocks.loopedDataItems.atom_type_symbol",
            "cifResult.dataBlocks.loopedDataItems.atom_type_oxidation_number"
    ))).first()["cifResult"])**/

    val testOut = GraphSingleton.processGraphToClientJson(ChemGraphs.BASIC_RATIO, listOf(
            Pair(ChemField.CHEMICAL_FORMULA_SUM_HAS_ELEMENT, "C"),
            Pair(ChemField.CHEMICAL_FORMULA_SUM_HAS_ELEMENT, "O"),
            Pair(ChemField.CHEMICAL_FORMULA_SUM_HAS_ELEMENT, ""),
            Pair(ChemField.CHEMICAL_FORMULA_SUM_HAS_N_ELEMENTS, "3")

    ))

    println()


    //"{cifResult.dataBlocks.dataItems.chemical_formula_sum: {$regex : \"Ca\\d*\"}}"


}

