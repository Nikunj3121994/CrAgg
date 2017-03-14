package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.cif.results.CIFBriefResult
import com.github.decyg.CrAgg.cif.results.CIF_ID
import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.spring.models.BriefResultsModel
import org.apache.commons.io.IOUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 * Created by declan on 11/03/2017.
 */
@Controller
@Service
class RESTController {


    @RequestMapping(value = "/api/starResult", method = arrayOf(RequestMethod.PUT))
    open fun starResult(
            @RequestBody source : CIF_ID,
            session : HttpSession
    ) : ResponseEntity<Void> {

        println(session.getAttribute("briefResultModel"))

        if(session.getAttribute("briefResultModel") != null){

            val briefResModel = session.getAttribute("briefResultModel") as BriefResultsModel

            val res = briefResModel.briefResults.find { it.cif_ID == source }

            if (session.getAttribute("starredResults") == null)
                session.setAttribute("starredResults", mutableListOf<CIFBriefResult>())

            val starredResults = session.getAttribute("starredResults") as MutableList<CIFBriefResult>

            if (res != null && !starredResults.contains(res)) {
                starredResults.add(res)
            }

            session.setAttribute("starredResults", starredResults)
        }

        println(session.getAttribute("starredResults"))

        return ResponseEntity(HttpStatus.CREATED)

    }

    @RequestMapping(value = "/api/unStarResult", method = arrayOf(RequestMethod.PUT))
    open fun unStarResult(
            @RequestBody source : CIF_ID,
            session : HttpSession
    ) : ResponseEntity<Void> {

        println(session.getAttribute("briefResultModel"))

        if(session.getAttribute("briefResultModel") != null){

            val briefResModel = session.getAttribute("briefResultModel") as BriefResultsModel

            val res = briefResModel.briefResults.find { it.cif_ID == source }

            if (session.getAttribute("starredResults") == null)
                session.setAttribute("starredResults", mutableListOf<CIFBriefResult>())

            val starredResults = session.getAttribute("starredResults") as MutableList<CIFBriefResult>

            if (res != null && starredResults.contains(res)) {
                starredResults.remove(res)
            }

            session.setAttribute("starredResults", starredResults)
        }

        println(session.getAttribute("starredResults"))

        return ResponseEntity(HttpStatus.CREATED)

    }

    @RequestMapping(value = "/api/downloadResult/{dbsource}/{dbid}", method = arrayOf(RequestMethod.GET))
    open fun downloadResult(
            resp : HttpServletResponse,
            @PathVariable dbsource : String,
            @PathVariable dbid : String
    ) : ResponseEntity<Array<Byte>> {

        val dbSourceObj = DBSingleton.getDBSourceByName(dbsource)!!
        val dbAbs = DBSingleton.getDBByName(dbsource)!!

        resp.contentType = "txt/plain"
        resp.addHeader("Content-Disposition", "attachment; filename=$dbid.cif")

        IOUtils.copy(dbAbs.getStreamForID(CIF_ID(dbSourceObj, dbid)), resp.outputStream)

        resp.flushBuffer()

        return ResponseEntity(HttpStatus.OK)

    }

}