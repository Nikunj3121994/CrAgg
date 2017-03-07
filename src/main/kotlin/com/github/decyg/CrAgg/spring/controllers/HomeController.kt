package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.database.query.CommonQueryTerm
import com.github.decyg.CrAgg.database.query.TermCategory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by declan on 16/02/2017.
 */

@Controller
open class HomeController {

    data class SearchModel(val queryTerms : Map<TermCategory, List<CommonQueryTerm>>)

    @RequestMapping("/")
    open fun indexPage(pageModel : Model) : String {

        //CommonQueryTerm.
        return "index"
    }

}