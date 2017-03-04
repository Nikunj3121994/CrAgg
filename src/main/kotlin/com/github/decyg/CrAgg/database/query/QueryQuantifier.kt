package com.github.decyg.CrAgg.database.query

/**
 * Created by declan on 27/02/2017.
 */
enum class QueryQuantifier {

    // Text

    CONTAINS, IS_EXACT,

    // Numerical

    GREATER_THAN, GREATER_THAN_EQUAL, EQUAL, LESS_THAN_EQUAL, LESS_THAN

}