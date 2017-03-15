package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.spring.models.SearchModel
import com.github.decyg.CrAgg.spring.models.SearchResultModel
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Spring controller that represents the index page
 */
@Controller
open class HomeController {

    @ModelAttribute("resultList")
    fun getResultList() = SearchResultModel()

    /**
     * Represents the index page, passes in the search model object to be acted upon and passed back
     */
    @RequestMapping(value = "/")
    open fun indexPage(pageModel : Model) : String {

        pageModel.addAttribute("searchmodel", SearchModel())

        return "index"
    }

}