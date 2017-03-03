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

    ID("Identifier", "", AllowableQueryType.Numerical),
    AUTHOR("Author", "_publ_author_name", AllowableQueryType.Text),
    ARTICLE("Article Title", "", AllowableQueryType.Text),
    JOURNAL("Journal Title", "_journal_name_full", AllowableQueryType.Text),
    JOURNAL_DOI("Journal DOI", "_journal_paper_doi", AllowableQueryType.Text),
    YEAR("Year of Publication", "_journal_year", AllowableQueryType.Numerical),

    // Cell

    A_LENGTH("Cell Length a", "_cell_length_a", AllowableQueryType.Numerical),
    B_LENGTH("Cell Length b", "_cell_length_b", AllowableQueryType.Numerical),
    C_LENGTH("Cell Length c", "_cell_length_c", AllowableQueryType.Numerical),
    ALPHA_LENGTH("Cell Angle alpha", "_cell_angle_alpha", AllowableQueryType.Numerical),
    BETA_LENGTH("Cell Angle beta", "_cell_angle_beta", AllowableQueryType.Numerical),
    GAMMA_LENGTH("Cell Angle gamma", "_cell_angle_gamma", AllowableQueryType.Numerical),
    CELL_VOLUME("Cell Volume", "_cell_volume", AllowableQueryType.Numerical),

    // Chemistry

    STRUCT_FORMULA("Structural Formula", "_chemical_formula_sum", AllowableQueryType.Text),
    CHEM_NAME("Chemical Name", "_chemical_name_systematic", AllowableQueryType.Text),
    MINERAL("Mineral Name", "_chemical_name_mineral", AllowableQueryType.Text),

    // Symmetry

    SPACE_GROUP("H-M Space Group", "_symmetry_space_group_name_H-M", AllowableQueryType.Text),

    // Crystal Chemistry

    FLAG_COORDINATE("Has Coordinates", "", AllowableQueryType.Boolean),
    FLAG_DISORDER("Has Disorder", "", AllowableQueryType.Boolean),
    FLAG_FOBS("Has Fobs", "", AllowableQueryType.Boolean);


    var prettyName = pName // Pretty name for readability
    var cifID = cID // the ID that this corresponding field would be found in a CIF document
    var fieldType = fType // The type of this field

    /**
     * This is a helper class to encapsulate comparable data without having to use classes
     */
    enum class AllowableQueryType {
        Numerical, Text, Boolean
    }

}

