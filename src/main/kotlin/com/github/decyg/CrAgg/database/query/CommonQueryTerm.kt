package com.github.decyg.CrAgg.database.query

/**
 * This is an enum intended to encapsulate the possible common queries made
 * These are such things as id (specific to source), space group, author, compound name and cell
 *
 * The fields used here were heavily inspired by the ICSD's fields, i'm not very knowledgable about chemistry so some
 * of these fields may end up not even being implemented
 *
 * Created by declan on 01/03/2017.
 */
enum class CommonQueryTerm(pName : String, cID : String, fType : AllowableQueryType) {

    // Bibliography (covers data pertaining to publication)

    ID("Identifier", "", AllowableQueryType.NUMERICAL),
    AUTHOR("Author", "_publ_author_name", AllowableQueryType.TEXT),
    ARTICLE("Article Title", "", AllowableQueryType.TEXT),
    JOURNAL("Journal Title", "_journal_name_full", AllowableQueryType.TEXT),
    JOURNAL_DOI("Journal DOI", "_journal_paper_doi", AllowableQueryType.TEXT),
    YEAR("Year of Publication", "_journal_year", AllowableQueryType.NUMERICAL),

    // Cell

    A_LENGTH("Cell Length a", "_cell_length_a", AllowableQueryType.NUMERICAL),
    B_LENGTH("Cell Length b", "_cell_length_b", AllowableQueryType.NUMERICAL),
    C_LENGTH("Cell Length c", "_cell_length_c", AllowableQueryType.NUMERICAL),
    ALPHA_LENGTH("Cell Angle alpha", "_cell_angle_alpha", AllowableQueryType.NUMERICAL),
    BETA_LENGTH("Cell Angle beta", "_cell_angle_beta", AllowableQueryType.NUMERICAL),
    GAMMA_LENGTH("Cell Angle gamma", "_cell_angle_gamma", AllowableQueryType.NUMERICAL),
    CELL_VOLUME("Cell Volume", "_cell_volume", AllowableQueryType.NUMERICAL),

    // Chemistry

    STRUCT_FORMULA("Structural Formula", "_chemical_formula_sum", AllowableQueryType.TEXT),
    CHEM_NAME("Chemical Name", "_chemical_name_systematic", AllowableQueryType.TEXT),
    MINERAL("Mineral Name", "_chemical_name_mineral", AllowableQueryType.TEXT),

    // Symmetry

    SPACE_GROUP("H-M Space Group", "_symmetry_space_group_name_H-M", AllowableQueryType.TEXT),

    // Crystal Chemistry

    FLAG_COORDINATE("Has Coordinates", "", AllowableQueryType.BOOLEAN),
    FLAG_DISORDER("Has Disorder", "", AllowableQueryType.BOOLEAN),
    FLAG_FOBS("Has Fobs", "", AllowableQueryType.BOOLEAN);


    var prettyName = pName // Pretty name for readability
    var cifID = cID // the ID that this corresponding field would be found in a CIF document
    var fieldType = fType // The type of this field

}

