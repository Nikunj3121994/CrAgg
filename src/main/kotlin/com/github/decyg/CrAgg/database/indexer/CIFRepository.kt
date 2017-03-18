package com.github.decyg.CrAgg.database.indexer

import com.github.decyg.CrAgg.cif.results.CIFDetailedResult
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Created by declan on 17/03/2017.
 */
interface CIFRepository : ElasticsearchRepository<CIFDetailedResult, String>