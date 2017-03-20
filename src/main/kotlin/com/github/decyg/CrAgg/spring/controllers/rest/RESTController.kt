package com.github.decyg.CrAgg.spring.controllers.rest

import com.github.decyg.CrAgg.cif.CIF_ID
import com.github.decyg.CrAgg.spring.controllers.StarredController
import com.github.decyg.CrAgg.spring.session.CIFSessionHandler
import org.springframework.core.io.ByteArrayResource
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
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 * Spring controller that represents the various REST endpoints
 */
@Controller
@Service
class RESTController {

    // POJOs for jackson

    /**
     * Acts as a wrapper for use with Jackson in Spring, can't use CIF_ID as the primary constructor takes in a
     * DBSource object
     *
     * @property db the datasource
     * @property id the id of the item
     */
    data class CIF_Result(val db : String, val id : String){
        fun toID() : CIF_ID {
            return CIF_ID(db, id)
        }
    }

    // Actual Endpoints

    /**
     * Exposes an endpoint that accepts the json
     * {
     *      "db" : "dbname",
     *      "id" : "id"
     * }
     * And adds that result to the starred list for that users session
     */
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

    /**
     * Exposes an endpoint that accepts the json
     * {
     *      "db" : "dbname",
     *      "id" : "id"
     * }
     * And removes that result to the starred list for that users session
     */
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

    /**
     * Represents a way to download a specific CIF file to the client or for use in the frontend given a
     * dbsource and a dbid.
     */
    @RequestMapping(value = "/api/downloadResult/{dbsource}/{dbid}", method = arrayOf(RequestMethod.GET), produces = arrayOf("txt/plain"))
    open fun downloadResult(
            resp : HttpServletResponse,
            @PathVariable dbsource : String,
            @PathVariable dbid : String,
            session : HttpSession
    ) : ResponseEntity<InputStreamResource> {

        val cif_ID = CIF_ID(dbsource, dbid)

        val cifFile = CIFSessionHandler.getCIFFromCacheAsFile(cif_ID)

        return ResponseEntity
                .ok()
                .header("Content-disposition", "attachment;filename=${cifFile.name}")
                .contentLength(cifFile.length())
                .contentType(MediaType.parseMediaType("txt/plain"))
                .body(InputStreamResource(cifFile.inputStream()))

    }

    /**
     * Represents a way to download many CIF files as a zip file, takes in an array of CIF_Result json objects
     */
    @RequestMapping(value = "/api/downloadMany", method = arrayOf(RequestMethod.POST), produces = arrayOf("application/zip"))
    open fun downloadMany(
            resp : HttpServletResponse,
            @RequestBody source : Array<CIF_Result>,
            session : HttpSession
    ) : ResponseEntity<ByteArrayResource> {

        val baos = ByteArrayOutputStream()
        val zipOut = ZipOutputStream(baos)

        source.forEach {
            val cifFile = CIFSessionHandler.getCIFFromCacheAsFile(it.toID())
            val zipEntry = ZipEntry(cifFile.name)

            zipOut.putNextEntry(zipEntry)
            zipOut.write(cifFile.readBytes())
            zipOut.closeEntry()
        }

        zipOut.close()

        return ResponseEntity
                .ok()
                .header("Content-disposition", "attachment;filename=cifdownload.zip")
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(ByteArrayResource(baos.toByteArray()))

    }

}