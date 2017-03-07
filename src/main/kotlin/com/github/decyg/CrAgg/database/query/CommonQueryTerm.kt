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
enum class CommonQueryTerm(val prettyName : String, val cifID : String, val fieldType : AllowableQueryType, val category : TermCategory) {

    // Bibliography (covers data pertaining to publication)

    ID("Identifier", "", AllowableQueryType.TEXT, TermCategory.BIBLIOGRAPHY),
    AUTHOR("Author", "_publ_author_name", AllowableQueryType.TEXT, TermCategory.BIBLIOGRAPHY),
    ARTICLE("Article Title", "", AllowableQueryType.TEXT, TermCategory.BIBLIOGRAPHY),
    JOURNAL("Journal Title", "_journal_name_full", AllowableQueryType.TEXT, TermCategory.BIBLIOGRAPHY),
    JOURNAL_DOI("Journal DOI", "_journal_paper_doi", AllowableQueryType.TEXT, TermCategory.BIBLIOGRAPHY),
    YEAR("Year of Publication", "_journal_year", AllowableQueryType.NUMERICAL, TermCategory.BIBLIOGRAPHY),

    // Cell

    A_LENGTH("Cell Length a", "_cell_length_a", AllowableQueryType.NUMERICAL, TermCategory.CELL),
    B_LENGTH("Cell Length b", "_cell_length_b", AllowableQueryType.NUMERICAL, TermCategory.CELL),
    C_LENGTH("Cell Length c", "_cell_length_c", AllowableQueryType.NUMERICAL, TermCategory.CELL),
    ALPHA_LENGTH("Cell Angle alpha", "_cell_angle_alpha", AllowableQueryType.NUMERICAL, TermCategory.CELL),
    BETA_LENGTH("Cell Angle beta", "_cell_angle_beta", AllowableQueryType.NUMERICAL, TermCategory.CELL),
    GAMMA_LENGTH("Cell Angle gamma", "_cell_angle_gamma", AllowableQueryType.NUMERICAL, TermCategory.CELL),
    CELL_VOLUME("Cell Volume", "_cell_volume", AllowableQueryType.NUMERICAL, TermCategory.CELL),

    // Chemistry

    STRUCT_FORMULA("Structural Formula", "_chemical_formula_sum", AllowableQueryType.TEXT, TermCategory.CHEMISTRY),
    CHEM_NAME("Chemical Name", "_chemical_name_systematic", AllowableQueryType.TEXT, TermCategory.CHEMISTRY),
    MINERAL("Mineral Name", "_chemical_name_mineral", AllowableQueryType.TEXT, TermCategory.CHEMISTRY),

    // Symmetry

    SPACE_GROUP("H-M Space Group", "_symmetry_space_group_name_H-M", AllowableQueryType.TEXT, TermCategory.SYMMETRY),

    // Crystal Chemistry

    FLAG_COORDINATE("Has Coordinates", "", AllowableQueryType.BOOLEAN, TermCategory.CRYSTAL_CHEM),
    FLAG_DISORDER("Has Disorder", "", AllowableQueryType.BOOLEAN, TermCategory.CRYSTAL_CHEM),
    FLAG_FOBS("Has Fobs", "", AllowableQueryType.BOOLEAN, TermCategory.CRYSTAL_CHEM);

}

enum class TermCategory(val prettyName: String, val prettyDescription: String) {
    BIBLIOGRAPHY("Bibliography", "Fields pertaining to the written aspect of a submission"),
    CELL("Cell", "Something about cells"),
    CHEMISTRY("Chemistry", "chemistry things"),
    SYMMETRY("Symmetry", "Symmetrical aspects"),
    CRYSTAL_CHEM("Crystal Chemistry", "Toggles")
}