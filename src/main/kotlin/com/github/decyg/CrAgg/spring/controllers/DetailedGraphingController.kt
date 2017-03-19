package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.database.graphmappers.ChemField
import com.github.decyg.CrAgg.database.graphmappers.ChemGraphs
import com.github.decyg.CrAgg.database.graphmappers.GraphSingleton
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpSession
import javax.validation.Valid

/**
 * Spring controller that represents the various analysis endpoints
 */
@Controller
open class DetailedGraphingController {

    /**
     * Workaround class for spring/kotlin integration
     */
    data class GraphPageModel(val graphOptions: Array<ChemGraphs> = ChemGraphs.values())

    /**
     * Result class to encapsulate the users input on the graph page
     */
    data class GraphResultModel(var resultMap : MutableList<String> = mutableListOf())

    @ModelAttribute("graphResultList")
    fun getGraphResultList() = GraphResultModel()

    @RequestMapping(value = "/graphing")
    open fun detailedGraphPage() : ModelAndView = ModelAndView("redirect:/graphing/${ChemGraphs.values()[0].name}")


    @RequestMapping(value = "/graphing/{graphChoice}", method = arrayOf(RequestMethod.GET))
    open fun detailedGraphPage(
            pageModel : Model,
            session : HttpSession,
            @PathVariable graphChoice : String
            ) : String {

        val chosenGraph = ChemGraphs.valueOf(graphChoice)

        pageModel.addAttribute("graphOptions", GraphPageModel())
        pageModel.addAttribute("curGraph", chosenGraph)


        return "graphing"
    }

    @RequestMapping(value = "/graphing/{graphChoice}", method = arrayOf(RequestMethod.POST))
    open fun detailedGraphPageResults(
            @Valid @ModelAttribute("graphResultList") resultModel : GraphResultModel,
            pageModel : Model,
            session : HttpSession,
            @PathVariable graphChoice : String
    ) : String {

        val chosenGraph = ChemGraphs.valueOf(graphChoice)

        pageModel.addAttribute("graphOptions", GraphPageModel())
        pageModel.addAttribute("curGraph", chosenGraph)

        // the resultmodel is an indexed list of strings where it corresponds to the other of the enums in

        val resultPair : List<Pair<ChemField, String>> = resultModel
                .resultMap.mapIndexed { i, s -> Pair(chosenGraph.termList[i], s) }

        val graphOutput = GraphSingleton.processGraphToClientJson(chosenGraph, resultPair)

        pageModel.addAttribute("graphData", graphOutput.first)
        pageModel.addAttribute("graphFormat", graphOutput.second)

        return "graphing"
    }

}