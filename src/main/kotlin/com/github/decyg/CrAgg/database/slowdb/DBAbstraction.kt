package com.github.decyg.CrAgg.database.slowdb

import com.github.decyg.CrAgg.cif.CIFBriefResult
import com.github.decyg.CrAgg.cif.CIFDetailedResult
import com.github.decyg.CrAgg.cif.CIF_ID
import com.github.decyg.CrAgg.database.query.QueryExpression
import com.github.decyg.CrAgg.database.query.enums.CommonQueryTerm
import java.io.File
import java.io.InputStream

/**
 * This is an abstraction of a datasource implementation, it's intentionally left vague/brief to allow for a variety of
 * data sources and implementations to be used, for example this could easily be adapted to support any type of SQL
 * like database for an implementation and it could even work with a RESTful API.
 */
interface DBAbstraction {

    /**
     * A map accessable from the implementation that maps [CommonQueryTerm] objects to their fields in whatever
     * format the datasource is in, eg with SQL it maps [CommonQueryTerm] to [String] field headers
     */
    val queryMap : Map<CommonQueryTerm, String>

    /**
     * A [File] object pointing to the folder where all the cifService files should be stored
     */
    val dataFolder : File

    /**
     * Takes in a list of [QueryExpression] objects which encapsulate a "term" and queries the data source in the source specific
     * way, it returns a list of [CIFBriefResult] objects
     *
     * @param query the query object
     * @return a [List] of [CIFBriefResult] object
     */
    fun queryDatabase(query : QueryExpression) : List<CIFBriefResult>

    /**
     * Takes in a single [CIFBriefResult] object which represents a previous result and returns a [CIFDetailedResult]
     * object which is a direct abstracted mapping of the CIF object this result represents
     *
     * @param specificResult the result to use for the specific query
     * @return a [CIFDetailedResult] object representing the more detailed version of the brief result
     */
    fun queryDatabaseSpecific(specificResult : CIFBriefResult) : CIFDetailedResult

    /**
     * Takes in the [CIF_ID] of the entry and streams the CIF file to it, wherever it comes from
     *
     * @param cifID the ID of the CIF entry to query
     * @return an [InputStream] to be used however of the CIF file
     */
    fun getStreamForID(cifID : CIF_ID) : InputStream

    /**
     * This implements the source specific method for updating the local cifService storage, the storage of all cifService files in this
     * source. This will nominally be called on all implementations every day at midnight or on initial startup of the
     * program.
     */
    fun updateLocalCIFStorage()

}