package com.github.decyg.CrAgg.spring.models

import com.github.decyg.CrAgg.cif.results.CIFBriefResult
import com.github.decyg.CrAgg.utils.Constants
import org.springframework.cache.annotation.Cacheable

/**
 * Created by declan on 08/03/2017.
 */
@Cacheable("resultModel")
//@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
//@Component
data class BriefResultsModel(val briefResults : MutableList<CIFBriefResult>) {

    fun paginate(page : Int = 1): BriefResultsModel {

        // chunk it into pages for pagination, see Constants for limits
        // can get the index to start at with (page - 1) * RESULTS_PER_PAGE

        if(briefResults.size == 0)
            return BriefResultsModel(mutableListOf())

        val startIndex = (page - 1) * Constants.RESULTS_PER_PAGE
        val endIndex = (startIndex + Constants.RESULTS_PER_PAGE).coerceAtMost(briefResults.size)

        return BriefResultsModel(briefResults.subList(startIndex, endIndex))

    }

}