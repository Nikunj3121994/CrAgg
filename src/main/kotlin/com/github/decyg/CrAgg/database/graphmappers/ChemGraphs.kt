package com.github.decyg.CrAgg.database.graphmappers

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.toJsonArray
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mongodb.client.FindIterable
import org.bson.Document

/**
 * This is an enum that encapsulates a "graph" from beginning to end on the client view. The termlist dictates
 * what terms to be used as input fields,
 */
enum class ChemGraphs(
        val termList : List<ChemField>,
        val projectionKeys : List<String>,
        val documentToJson : (List<Pair<ChemField, String>>, FindIterable<Document>) -> Pair<String, String>
) {

    BASIC_RATIO(
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
            { res, doc ->

                // need a map of string (space group) to coordinates(x, y, id)
                // each space group is a different trace

                val traceMap : MutableMap<String, Triple<MutableList<String>, MutableList<String>, MutableList<String>>> = mutableMapOf()
                val xElement = res[0].second
                val yElement = res[1].second


                doc.forEach {


                    val spaceGroup = it
                            .get("cifResult", Document::class.java)
                            .get("dataBlocks", List::class.java)[0].let { it as Document }
                            .get("dataItems", Document::class.java)
                            .getString("symmetry_space_group_name_H-M")

                    val chemFormula = it
                            .get("cifResult", Document::class.java)
                            .get("dataBlocks", List::class.java)[0].let { it as Document }
                            .get("dataItems", Document::class.java)
                            .getString("chemical_formula_sum")


                    if(!traceMap.containsKey(spaceGroup)) // x, y, text
                        traceMap[spaceGroup] = Triple(mutableListOf(), mutableListOf(), mutableListOf())

                    val xMatch = "$xElement(\\d+)".toRegex().find(chemFormula)
                    val xCount : String = if(xMatch == null) "1" else xMatch.groups[1]!!.value
                    val yMatch = "$yElement(\\d+)".toRegex().find(chemFormula)
                    val yCount : String = if(yMatch == null) "1" else yMatch.groups[1]!!.value

                    traceMap[spaceGroup]!!.first.add(xCount)
                    traceMap[spaceGroup]!!.second.add(yCount)
                    traceMap[spaceGroup]!!.third.add(chemFormula)

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
                        "title" to "test",
                        "xaxis" to jsonObject(
                                "title" to xElement
                        ),
                        "yaxis" to jsonObject(
                                "title" to yElement
                        )
                )

                Pair(outData.toString(), layout.toString())
            }
    ),
   // RADIUS_RATIO()
}