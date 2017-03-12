package com.github.decyg.CrAgg.spring.controllers

import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.DB_UUID
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

/**
 * Created by declan on 11/03/2017.
 */
@Controller
@Service
class RESTController {


    data class ResultSelection(val db : String = "", val id : String = "")

    @RequestMapping(value = "/api/selectResult", method = arrayOf(RequestMethod.PUT))
    open fun selectResult(@RequestBody source : ResultSelection) : ResponseEntity<Void> {

        println(source)

        return ResponseEntity(HttpStatus.CREATED)

    }

    @RequestMapping(value = "/api/starResult", method = arrayOf(RequestMethod.PUT))
    open fun starResult(@RequestBody source : ResultSelection) : ResponseEntity<Void> {

        println(source)

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

        IOUtils.copy(dbAbs.getStreamForID(DB_UUID(dbSourceObj, dbid)), resp.outputStream)

        resp.flushBuffer()

        return ResponseEntity(HttpStatus.OK)

    }

}