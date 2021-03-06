package com.github.decyg.CrAgg.database.graphing.enums

import com.github.decyg.CrAgg.cif.parser.CIFDetailedResult
import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.toJsonArray
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mongodb.client.FindIterable

/**
 * This is an enum that encapsulates a "graph" from beginning to end on the client view. The termlist dictates
 * what terms to be used as input fields. Depending on how detailed these graphs get it may need to be pulled out to its own file
 */
enum class ChemGraphs(
        val prettyName : String,
        val prettyDescription : String,
        val termList : List<ChemField>,
        val projectionKeys : List<String>,
        val documentToJson : (List<Pair<ChemField, String>>, FindIterable<CIFDetailedResult>) -> Pair<String, String>
) {

    BASIC_RATIO(
            "Atom Ratio",
            "Plots the ratio of the selected elements to the total number of atoms in the structural formula." +
                    " Note that at least two chemical formula sum has element fields must be populated for use as axis.",
            listOf(
                    ChemField.CHEMICAL_FORMULA_SUM_HAS_ELEMENT,
                    ChemField.CHEMICAL_FORMULA_SUM_HAS_ELEMENT,
                    ChemField.CHEMICAL_FORMULA_SUM_HAS_ELEMENT,
                    ChemField.CHEMICAL_FORMULA_SUM_HAS_N_ELEMENTS
            ),
            listOf(
                    "stringifiedID",
                    "cifResult.dataBlocks.dataItems.chemical_formula_sum",
                    "cifResult.dataBlocks.dataItems.symmetry_space_group_name_H-M"
            ),
            graphProc@{ res, doc ->

                // need a map of string (space group) to coordinates(x, y, id)
                // each space group is a different trace

                if(res.size < 2 || doc.count() == 0)
                    return@graphProc Pair("", "")

                val traceMap : MutableMap<String, Triple<MutableList<String>, MutableList<String>, MutableList<String>>> = mutableMapOf()
                val xElement = res[0].second
                val yElement = res[1].second

                doc.forEach {

                    val stringID = it.stringifiedID
                    val spaceGroup = it.cifResult.dataBlocks[0].dataItems["symmetry_space_group_name_H-M"]?.trim() ?: ""
                    val chemFormula = it.cifResult.dataBlocks[0].dataItems["chemical_formula_sum"] ?: ""

                    if(!traceMap.containsKey(spaceGroup)) // x, y, text
                        traceMap[spaceGroup] = Triple(mutableListOf(), mutableListOf(), mutableListOf())

                    var runTotal : Double = 0.0

                    val countReg = "[a-zA-Z]+(\\d*)".toRegex()
                    countReg.findAll(chemFormula).forEach {
                        if(it.groups[1] != null){
                            runTotal += it.groups[1]!!.value.toDoubleOrNull() ?: 1.0
                        } else {
                            runTotal += 1
                        }
                    }

                    val xMatch = "$xElement(\\d+)".toRegex().find(chemFormula)
                    val xCount : Double = if(xMatch == null) 1.0 else xMatch.groups[1]!!.value.toDouble()
                    val yMatch = "$yElement(\\d+)".toRegex().find(chemFormula)
                    val yCount : Double = if(yMatch == null) 1.0 else yMatch.groups[1]!!.value.toDouble()

                    traceMap[spaceGroup]!!.first.add((xCount / runTotal).toString())
                    traceMap[spaceGroup]!!.second.add((yCount / runTotal).toString())
                    traceMap[spaceGroup]!!.third.add("$stringID<br>$chemFormula")

                }

                val outData : JsonArray = jsonArray()

                traceMap.forEach { spaceGroup, xychem ->
                    outData.add(jsonObject(
                            "mode" to "markers",
                            "type" to "scatter",
                            "name" to spaceGroup,
                            "x" to xychem.first.toJsonArray(),
                            "y" to xychem.second.toJsonArray(),
                            "text" to xychem.third.toJsonArray()
                    ))
                }

                val layout : JsonObject = jsonObject(
                        "xaxis" to jsonObject(
                                "title" to "Ratio of $xElement atoms to total number of atoms"
                        ),
                        "yaxis" to jsonObject(
                                "title" to "Ratio of $yElement atoms to total number of atoms"
                        ),
                        "paper_bgcolor" to "rgba(250,250,250,0)"
                )

                Pair(outData.toString(), layout.toString())
            }
    ),
   // RADIUS_RATIO()
}