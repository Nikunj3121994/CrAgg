package com.github.decyg.CrAgg.utils

/**
 * Basic constants singleton for use with the Spring controllers mainly.
 */
object GeneralConstants {

    // Frontend

    val RESULTS_PER_PAGE = 5
    val TOTAL_RESULTS = 50

    // Backend

    val CIF_CACHE_FOLDER = "cifcache"
    val CIF_STORAGE_FOLDER = "cifstorage"

    // Mongo

    val MONGODB_NAME = "CrAgg"
    val MONGODB_COLLECTION = "cifdetailedresult"

}