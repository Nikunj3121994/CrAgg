package com.github.decyg.CrAgg.database.query

import com.github.decyg.CrAgg.database.query.enums.CommonQueryTerm
import com.github.decyg.CrAgg.database.query.enums.QueryQuantifier

/**
 *
 * This class is designed to act as a wrapper for a Query Expression.
 * It acts as a common interface and provides the children to form the structure
 * a QueryExpression object could be represented as so
 * QueryExpression(
 *     AND(
 *         TERM()
 *         TERM()
 *         TERM()
 *     )
 * )
 *
 * [QueryExpression] encapsualtes an [Expression], an [Expression] can either be [AND], [OR], [NOT] or [TERM]
 * [AND] takes two [Expression]s and would evaluate true if all are satisfied
 * [OR] takes two [Expression]s and would evaluate true if any are satisfied
 * [NOT] takes one [Expression] and returns the inverse of the result
 *
 *
 */


/*
The below objects are fairly self explanatory given the spiel above, they represent a pretty basic logic expression
to encapsualte terms and allow for an abstract translation of an Expression object to the resulting query language
or logic
 */

interface Expression

data class QueryExpression(val expression : Expression) : Expression

data class AND(val leftExp : Expression, val rightExp : Expression) : Expression

data class OR(val leftExp : Expression, val rightExp : Expression) : Expression

data class NOT(val expression : Expression) : Expression

class TERM(
        val key : CommonQueryTerm,
        val quantifier : QueryQuantifier,
        val textTerm : String = "",
        val numericalTerm : Double = 0.0,
        val booleanTerm : Boolean = false
) : Expression