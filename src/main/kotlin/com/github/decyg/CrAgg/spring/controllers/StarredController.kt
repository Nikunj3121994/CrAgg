package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.cif.results.CIFBriefResult
import com.github.decyg.CrAgg.spring.models.BriefResultsModel
import com.github.decyg.CrAgg.spring.models.SearchResultModel
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpSession

/**
 * Created by declan on 16/02/2017.
 */

@Controller
open class StarredController {

    @ModelAttribute("resultList")
    fun getResultList() = SearchResultModel()



    @RequestMapping(value = "/starred")
    open fun starredPage(
            pageModel : Model,
            session : HttpSession
    ) : String {

        val starredModel = StarredController.getStarredResults(session)

        pageModel.addAttribute("totalMaxResults", starredModel.briefResults.size)
        pageModel.addAttribute("starredModel", starredModel)

        return "starred"
    }

    companion object {
        fun getStarredResults(session : HttpSession) : BriefResultsModel {
            val starredModel : BriefResultsModel

            if(session.getAttribute("starredResults") != null){

                val starredResults = session.getAttribute("starredResults") as MutableList<CIFBriefResult>

                starredModel = BriefResultsModel(starredResults)

            } else {
                starredModel = BriefResultsModel(mutableListOf())
            }

            return starredModel
        }
    }
}