package com.github.decyg.CrAgg.database.query.enums

/**
 * This is a helper class to encapsulate comparable data without having to use classes.
 * Takes in a [List] of [QueryQuantifier] objects that define how a field can be quantified
 *
 * @property allowedQuantifiers a [List] of [QueryQuantifier]
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

    /**
     * Allows info to actually be stored within an instance of an [AllowableQueryType] object to represent
     * multiple choices
     *
     * @param pChoices a map of choices
     * @return this
     */
    fun withChoices(pChoices : Map<String, String>): AllowableQueryType {
        presetChoices = pChoices

        return this
    }

}