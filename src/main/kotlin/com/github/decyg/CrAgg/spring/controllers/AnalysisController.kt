package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.cif.results.CIF_ID
import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.query.QueryExpression
import com.github.decyg.CrAgg.database.query.TERM
import com.github.decyg.CrAgg.database.query.enums.CommonQueryTerm
import com.github.decyg.CrAgg.database.query.enums.QueryQuantifier
import com.github.decyg.CrAgg.spring.models.SearchResultModel
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpSession

/**
 * Spring controller that represents the various analysis endpoints
 */
@Controller
open class AnalysisController {

    @ModelAttribute("resultList")
    fun getResultList() = SearchResultModel()

    /**
     * Handles getting the "analyse" page given a database source and id as strings.
     * This controller stars the selected item if not already starred, then it gets the brief and detailed
     * objects by querying the datasource implementation for the specific id and it passes all that to the client
     */
    @RequestMapping(value = "/analyse/{dbSource}/{dbID}")
    open fun analysisPage(
            pageModel : Model,
            session : HttpSession,
            @PathVariable dbSource : String,
            @PathVariable dbID : String
            ) : String {

        val cif_ID = CIF_ID(dbSource, dbID)

        val isStarred = StarredController.getStarredResults(session).briefResults.any { it.cif_ID == cif_ID }

        if(!isStarred) {
            StarredController.addStarredResult(cif_ID, session)
        }

        val starredModel = StarredController.getStarredResults(session)

        val briefResList = DBSingleton.getDBBySource(cif_ID.db).queryDatabase(
                QueryExpression(
                        TERM(CommonQueryTerm.ID, QueryQuantifier.IS_EXACT, cif_ID.id)
                )
        )

        if(briefResList.size == 1){
            val detailedModel = DBSingleton.getDBBySource(cif_ID.db).queryDatabaseSpecific(briefResList[0])
            pageModel.addAttribute("briefCIF", briefResList[0])
            pageModel.addAttribute("detailedCIF", detailedModel)
        }

        pageModel.addAttribute("curCIFID", cif_ID)
        pageModel.addAttribute("totalMaxResults", starredModel.briefResults.size)
        pageModel.addAttribute("starredModel", starredModel)

        return "analyse"
    }

}