package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.cif.results.CIF_ID
import com.github.decyg.CrAgg.spring.session.CIFSessionHandler
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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

    data class CIF_Result(val db : String, val id : String){
        fun toID() : CIF_ID {
            return CIF_ID(db, id)
        }
    }

    @RequestMapping(value = "/api/starResult", method = arrayOf(RequestMethod.PUT))
    open fun starResult(
            @RequestBody source : CIF_Result,
            session : HttpSession
    ) : ResponseEntity<Void> {

        if(StarredController.addStarredResult(source.toID(), session)){
            return ResponseEntity(HttpStatus.CREATED)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

    }

    @RequestMapping(value = "/api/unStarResult", method = arrayOf(RequestMethod.PUT))
    open fun unStarResult(
            @RequestBody source : CIF_Result,
            session : HttpSession
    ) : ResponseEntity<Void> {

        if(StarredController.removeStarredResult(source.toID(), session)){
            return ResponseEntity(HttpStatus.CREATED)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

    }

    @RequestMapping(value = "/api/downloadResult/{dbsource}/{dbid}", method = arrayOf(RequestMethod.GET), produces = arrayOf("txt/plain"))
    open fun downloadResult(
            resp : HttpServletResponse,
            @PathVariable dbsource : String,
            @PathVariable dbid : String,
            session : HttpSession
    ) : ResponseEntity<InputStreamResource> {

        val cif_ID = CIF_ID(dbsource, dbid)

        val cifFile = CIFSessionHandler.getCIFFromCacheAsFile(cif_ID) ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        return ResponseEntity
                .ok()
                .header("Content-disposition", "attachment;filename=${cifFile.name}")
                .contentLength(cifFile.length())
                .contentType(MediaType.parseMediaType("txt/plain"))
                .body(InputStreamResource(cifFile.inputStream()))

    }

}