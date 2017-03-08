package com.github.decyg.CrAgg.spring.models

import com.github.decyg.CrAgg.database.query.CommonQueryTerm
import com.github.decyg.CrAgg.database.query.QueryQuantifier

/**
 * Created by declan on 08/03/2017.
 */

data class SearchResultModel(
        var dbList : List<String> = mutableListOf(),
        var termMap: Map<CommonQueryTerm, String> = mutableMapOf(),
        var quantifierMap : Map<CommonQueryTerm, QueryQuantifier> = mutableMapOf()
)