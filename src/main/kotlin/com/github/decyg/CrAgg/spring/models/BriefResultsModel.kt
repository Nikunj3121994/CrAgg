package com.github.decyg.CrAgg.spring.models

import com.github.decyg.CrAgg.cif.CIFBriefResult
import com.github.decyg.CrAgg.utils.GeneralConstants
import org.springframework.cache.annotation.Cacheable

/**
 * Provides a spring object for a list of [CIFBriefResult] objects
 */
@Cacheable("resultModel")
data class BriefResultsModel(val briefResults : MutableList<CIFBriefResult>) {

    /**
     * Paginates the result and uses [GeneralConstants] to provide a selection of results given how many results are specified
     *
     * @param page what page of results to show
     * @return the brief results model
     */
    fun paginate(page : Int = 1): BriefResultsModel {

        // chunk it into pages for pagination, see GeneralConstants for limits
        // can get the index to start at with (page - 1) * RESULTS_PER_PAGE

        if(briefResults.size == 0)
            return BriefResultsModel(mutableListOf())

        val startIndex = (page - 1) * GeneralConstants.RESULTS_PER_PAGE
        val endIndex = (startIndex + GeneralConstants.RESULTS_PER_PAGE).coerceAtMost(briefResults.size)

        return BriefResultsModel(briefResults.subList(startIndex, endIndex))

    }

}