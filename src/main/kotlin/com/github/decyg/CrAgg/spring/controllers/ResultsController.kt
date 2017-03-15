package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.spring.models.BriefResultsModel
import com.github.decyg.CrAgg.spring.models.SearchResultModel
import com.github.decyg.CrAgg.utils.Constants
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpSession
import javax.validation.Valid

/**
 * Spring controller that represents the results page
 */
@Controller
@Service
open class ResultsController {

    /**
     * Represents a result page in which the core is represented by the BriefResultModel and the full result model
     * this could probably be more elegant but i'm not a spring developer first and foremost. The client uses the
     * resultmodel to show a list of results
     */
    @RequestMapping(value = "/results", method = arrayOf(RequestMethod.POST))
    open fun resultsPage(
            @Valid @ModelAttribute("resultList") resultModel : SearchResultModel,
            @RequestParam("page", required = false) pageNum : Int?,
            bindingResult : BindingResult,
            pageModel : Model,
            session : HttpSession
    ) : String {

        val briefResModel = resultModel.toBriefResultsModel()

        pageModel.addAttribute("pageNum", pageNum ?: 1)
        pageModel.addAttribute("totalNumResults", briefResModel.briefResults.size / Constants.RESULTS_PER_PAGE)
        pageModel.addAttribute("totalMaxResults", briefResModel.briefResults.size)
        pageModel.addAttribute("briefResultModel", briefResModel.paginate())
        pageModel.addAttribute("starredModel", StarredController.getStarredResults(session))

        session.setAttribute("briefResultModel", briefResModel)
        session.setAttribute("resultList", resultModel)

        return "results"
    }

    /**
     * Identical to above except it accepts a page index to paginate the result
     */
    @RequestMapping(value = "/results", method = arrayOf(RequestMethod.GET))
    open fun resultsPageByPage(
            //@Valid @ModelAttribute("resultList") resultModel : SearchResultModel,
            @RequestParam("page") pageNum : Int,
            pageModel : Model,
            session : HttpSession
    ) : String {

        if(session.getAttribute("briefResultModel") == null || session.getAttribute("resultList") == null)
            return "index"

        val maxPages = Constants.TOTAL_RESULTS / Constants.RESULTS_PER_PAGE
        val minPages = 1

        val limitedPage = pageNum.coerceIn(minPages, maxPages)

        val resModel : BriefResultsModel = session.getAttribute("briefResultModel") as BriefResultsModel

        pageModel.addAttribute("pageNum", limitedPage)
        pageModel.addAttribute("totalNumResults", resModel.briefResults.size / Constants.RESULTS_PER_PAGE)
        pageModel.addAttribute("totalMaxResults", resModel.briefResults.size)
        pageModel.addAttribute("briefResultModel", resModel.paginate(limitedPage))
        pageModel.addAttribute("starredModel", StarredController.getStarredResults(session))


        return "results"
    }

}