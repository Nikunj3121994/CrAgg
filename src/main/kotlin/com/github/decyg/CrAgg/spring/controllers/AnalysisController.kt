package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.cif.results.CIF_ID
import com.github.decyg.CrAgg.spring.models.SearchResultModel
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpSession

/**
 * Created by declan on 16/02/2017.
 */

@Controller
open class AnalysisController {

    @ModelAttribute("resultList")
    fun getResultList() = SearchResultModel()



    @RequestMapping(value = "/analyse")
    open fun analysisPage(
            pageModel : Model,
            session : HttpSession,
            @RequestParam("focusElement", required = false) firstEl : CIF_ID?
            ) : String {

        val starredModel = StarredController.getStarredResults(session)

        pageModel.addAttribute("totalMaxResults", starredModel.briefResults.size)
        pageModel.addAttribute("starredModel", starredModel)

        return "analyse"
    }

}