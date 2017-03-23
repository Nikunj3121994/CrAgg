package com.github.decyg.CrAgg.database.graphing.enums

import com.mongodb.client.model.Filters.regex
import org.bson.conversions.Bson

/**
 * This is an enum designed to create an extensible map of fields that the user can search on for the graphing
 * analysis section. It's designed to be completely modular and extensible.
 */
enum class ChemField(
        val prettyName : String,
        val exampleText : String,
        val fieldString : String,
        val transformInput : (String) -> Bson
) {

    // Input is of the form Cu, Fe, etc
    CHEMICAL_FORMULA_SUM_HAS_ELEMENT(
            "Chemical Formula contains element",
            "Cu",
            "cifResult.dataBlocks.dataItems.chemical_formula_sum",
            { input ->
                regex(
                        "cifResult.dataBlocks.dataItems.chemical_formula_sum",
                        "$input\\d+"
                )
            }
    ),
    // Input will be of the form 1, 2, 3, 4, 5
    CHEMICAL_FORMULA_SUM_HAS_N_ELEMENTS(
            "Chemical Formula sum has N elements",
            "3",
            "cifResult.dataBlocks.dataItems.chemical_formula_sum",
            { input ->
                var resRegex = ""

                for(i in 1..input.toInt()){
                    resRegex += " \\w+\\d*"
                }

                println("^${resRegex.trim()}$")
                regex(
                        "cifResult.dataBlocks.dataItems.chemical_formula_sum",
                        "^${resRegex.trim()}$"
                )
            }
    ),


}