package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.spring.models.SearchModel
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by declan on 16/02/2017.
 */

@Controller
open class HomeController {

    @RequestMapping(value = "/")
    open fun indexPage(pageModel : Model) : String {

        pageModel.addAttribute("searchmodel", SearchModel())

        //TermCategory.valu
        //CommonQueryTerm.
        return "index"
    }

}