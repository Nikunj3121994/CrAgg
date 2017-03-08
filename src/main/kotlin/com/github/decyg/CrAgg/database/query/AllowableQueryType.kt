package com.github.decyg.CrAgg.database.query

/**
 * This is a helper class to encapsulate comparable data without having to use classes
 */
enum class AllowableQueryType(val allowedQuantifiers : List<QueryQuantifier>) {

    NUMERICAL(
            listOf(
                    QueryQuantifier.EQUAL,
                    QueryQuantifier.GREATER_EQUAL,
                    QueryQuantifier.GREATER_THAN,
                    QueryQuantifier.LESS_EQUAL,
                    QueryQuantifier.LESS_THAN
            )
    ),

    TEXT(
            listOf(
                    QueryQuantifier.CONTAINS,
                    QueryQuantifier.IS_EXACT
            )
    ),

    MULTI_SINGLE_CHOICE(emptyList()),

    MULTI_MANY_CHOICE(emptyList());

    // Helper function to support storage of info

    var presetChoices : Map<String, String> = emptyMap()

    fun withChoices(pChoices : Map<String, String>): AllowableQueryType {
        presetChoices = pChoices

        return this
    }

}