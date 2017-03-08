package com.github.decyg.CrAgg.database.query

import com.github.decyg.CrAgg.database.DBSource
import com.github.decyg.CrAgg.database.implementation.COD

/**
 * Created by declan on 27/02/2017.
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