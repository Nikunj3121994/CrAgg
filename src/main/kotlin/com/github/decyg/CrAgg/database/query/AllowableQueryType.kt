package com.github.decyg.CrAgg.database.query

/**
 * This is a helper class to encapsulate comparable data without having to use classes
 */
enum class AllowableQueryType(vararg allowedQuantifier: QueryQuantifier) {


    NUMERICAL(QueryQuantifier.GREATER_THAN, QueryQuantifier.GREATER_THAN_EQUAL, QueryQuantifier.EQUAL, QueryQuantifier.LESS_THAN_EQUAL, QueryQuantifier.LESS_THAN),
    TEXT(QueryQuantifier.CONTAINS, QueryQuantifier.IS_EXACT),
    BOOLEAN()

}