package com.github.decyg.CrAgg.spring.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by declan on 16/02/2017.
 */

@Controller
open class HomeController {

    @RequestMapping("/")
    open fun indexPage() : String {
        return "index"
    }

}