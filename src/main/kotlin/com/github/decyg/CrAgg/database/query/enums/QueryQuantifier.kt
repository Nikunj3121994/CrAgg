package com.github.decyg.CrAgg.database.query.enums

import com.github.decyg.CrAgg.database.DBSource
import com.github.decyg.CrAgg.database.slowdb.implementation.COD

/**
 * Defines Quantifier objects and their translations for each data source. I could have made this more generic
 * and attached it to a language rather than a data source but that requires a bit more boilerplate and this way is
 * still fairly elegant.
 *
 * In the future it might be more prudent to make each quantifier take in the objects it represents and contain
 * a transformative function that acts on them, rather than just having a string which gets used for comparison. In
 * the case that the data source may not have a query like lang
 *
 * @property dbMap a map of [DBAbstration] implementations to the actual query terms
 */
enum class QueryQuantifier(val dbMap : Map<DBSource, String>){

    // Text
    CONTAINS(mapOf(
            COD::class to "LIKE"
    )),

    IS_EXACT(mapOf(
            COD::class to "="
    )),

    // Numerical

    EQUAL(mapOf(
            COD::class to "="
    )),

    GREATER_THAN(mapOf(
            COD::class to ">"
    )),

    GREATER_EQUAL(mapOf(
            COD::class to ">="
    )),

    LESS_THAN(mapOf(
            COD::class to "<"
    )),

    LESS_EQUAL(mapOf(
            COD::class to "<="
    ));

}