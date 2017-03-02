package com.github.decyg.CrAgg.database.query

/**
 * This is a wrapper object that encapsulates a section of a users search, an example instantiation would be as follows
 * QueryWrapper(QueryQuantifier.CONTAINS, CommonQueryTerm.AUTHOR, "edmund") and that will whitelist all terms that
 * fufill these requirements.
 *
 * Created by declan on 27/02/2017.
 */
data class QueryWrapper(val quantifier: QueryQuantifier, val field : CommonQueryTerm, val term : String)