package com.github.decyg.CrAgg.database.indexer

import com.github.decyg.CrAgg.cif.results.CIFDetailedResult

/**
 * Created by declan on 17/03/2017.
 */
interface CIFService {

    fun save(cifRes : CIFDetailedResult) : CIFDetailedResult

    fun delete(cifRes: CIFDetailedResult)

    fun findAll() : Iterable<CIFDetailedResult>
}