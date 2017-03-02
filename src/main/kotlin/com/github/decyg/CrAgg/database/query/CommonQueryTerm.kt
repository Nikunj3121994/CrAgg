package com.github.decyg.CrAgg.database.query

/**
 * This is an enum intended to encapsulate the possible common queries made
 * These are such things as id (specific to source), space group, author, compound name and cell
 * Created by declan on 01/03/2017.
 */
enum class CommonQueryTerm(pName : String) {

    ID("Identifier"),
    SPACE_GROUP("Space Group"),
    AUTHOR("Author"),
    FORMULA("Chemical Formula"),
    COMP_NAME("Compound Name");


    var prettyName = ""

    init {
        prettyName = pName
    }
}