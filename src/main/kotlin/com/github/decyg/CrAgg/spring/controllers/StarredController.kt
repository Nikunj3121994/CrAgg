package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.cif.parser.CIFBriefResult
import com.github.decyg.CrAgg.cif.parser.CIF_ID
import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.query.QueryExpression
import com.github.decyg.CrAgg.database.query.TERM
import com.github.decyg.CrAgg.database.query.enums.CommonQueryTerm
import com.github.decyg.CrAgg.database.query.enums.QueryQuantifier
import com.github.decyg.CrAgg.spring.models.BriefResultsModel
import com.github.decyg.CrAgg.spring.models.SearchResultModel
import com.github.decyg.CrAgg.spring.session.CIFSessionHandler
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

        fun addStarredResult(cif_ID: CIF_ID, session: HttpSession) : Boolean {

            val briefResList = DBSingleton.getDBBySource(cif_ID.asDBSource()).queryDatabase(
                    QueryExpression(
                            TERM(CommonQueryTerm.ID, QueryQuantifier.IS_EXACT, cif_ID.id)
                    )
            )

            if(briefResList.size != 1)
                return false

            val res = briefResList[0]

            if (session.getAttribute("starredResults") == null)
                session.setAttribute("starredResults", mutableListOf<CIFBriefResult>())

            val starredResults = session.getAttribute("starredResults") as MutableList<CIFBriefResult>

            if (!starredResults.contains(res)) {
                starredResults.add(res)

                if(!CIFSessionHandler.cacheHasCIF(cif_ID)) {
                    CIFSessionHandler.addCIFToCache(cif_ID)
                }
            }

            session.setAttribute("starredResults", starredResults)

            return true
        }

        fun removeStarredResult(cif_ID: CIF_ID, session: HttpSession) : Boolean {

            val briefResList = DBSingleton.getDBBySource(cif_ID.asDBSource()).queryDatabase(
                    QueryExpression(
                            TERM(CommonQueryTerm.ID, QueryQuantifier.IS_EXACT, cif_ID.id)
                    )
            )

            if(briefResList.size != 1)
                return false

            val res = briefResList[0]

            if (session.getAttribute("starredResults") == null)
                session.setAttribute("starredResults", mutableListOf<CIFBriefResult>())

            val starredResults = session.getAttribute("starredResults") as MutableList<CIFBriefResult>

            if (starredResults.contains(res)) {
                starredResults.add(res)

                if(CIFSessionHandler.cacheHasCIF(cif_ID)) {
                    CIFSessionHandler.removeCIFFromCache(cif_ID)
                }
            }

            session.setAttribute("starredResults", starredResults)

            return true
        }
    }
}