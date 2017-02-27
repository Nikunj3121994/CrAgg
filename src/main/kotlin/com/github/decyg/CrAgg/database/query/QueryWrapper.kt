package com.github.decyg.CrAgg.database.query

/**
 *
 * Created by declan on 27/02/2017.
 */
data class QueryWrapper(val field : String, val term : String, val quantifier: QueryQuantifier)