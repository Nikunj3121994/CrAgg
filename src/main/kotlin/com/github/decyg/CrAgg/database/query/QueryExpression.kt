package com.github.decyg.CrAgg.database.query

/**
 *
 * This class is designed to act as a wrapper for a Query Expression simply put.
 * It acts as a common interface and provides the children to form the structure
 * a QueryExpression object could be represented as so
 * QueryExpression(
 *     AND(
 *         CommonQueryTerm()
 *         CommonQueryTerm()
 *         CommonQueryTerm()
 *     )
 * )
 *
 * QueryExpression encapsualtes a Statement, a Statement can either be AND, OR, NOT or CommonQueryTerm
 * AND takes any number of Statements and would evaluate true if all are satisfied
 * OR takes any number of Statements and would evaluate true if any are satisfied
 * NOT takes one Statements and returns the inverse of the statement
 *
 * Created by declan on 03/03/2017.
 */


/**
 * The top level Expression interface that defines the inheritance behaviour
 */
interface Expression

data class QueryExpression(val expression : Expression){

    class AND(vararg expressions : Expression) : Expression

    class OR(vararg expressions : Expression) : Expression

    class NOT(expression : Expression) : Expression

    class TERM(key : CommonQueryTerm, quantifier : QueryQuantifier, term : String) : Expression

}
