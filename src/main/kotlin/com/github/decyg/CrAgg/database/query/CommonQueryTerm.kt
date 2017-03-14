package com.github.decyg.CrAgg.database.query

/**
 * This is an enum intended to encapsulate the possible common queries made
 * These are such things as cif_ID (specific to source), space group, author, compound name and cell
 *
 * The fields used here were heavily inspired by the ICSD's fields, i'm not very knowledgable about chemistry so some
 * of these fields may end up not even being implemented
 *
 * Created by declan on 01/03/2017.
 */
enum class CommonQueryTerm(val cifID: String, val prettyName: String, val example: String, val fieldType: AllowableQueryType) {

    // Bibliography (covers data pertaining to publication)

    ID(
            "",
            "Identifier",
            "database source specific ID",
            AllowableQueryType.TEXT
    ),
    AUTHOR(
            "_publ_author_name",
            "Author",
            "Laval",
            AllowableQueryType.TEXT
    ),
    ARTICLE(
            "",
            "Article Title",
            "Synthesis and crystal structure of NaSrAlF~6~",
            AllowableQueryType.TEXT
    ),
    JOURNAL(
            "_journal_name_full",
            "Journal Title",
            "European Journal of Solid State Inorganic Chemistry",
            AllowableQueryType.TEXT
    ),
    JOURNAL_DOI(
            "_journal_paper_doi",
            "Journal DOI",
            "10.1016/0022-4596(90)90080-H",
            AllowableQueryType.TEXT
    ),
    YEAR(
            "_journal_year",
            "Year of Publication",
            "2000",
            AllowableQueryType.NUMERICAL
    ),

    // Cell

    A_LENGTH(
            "_cell_length_a",
            "Cell Length a",
            "a double value",
            AllowableQueryType.NUMERICAL
    ),
    ALPHA_LENGTH(
            "_cell_angle_alpha",
            "Cell Angle alpha",
            "a double value",
            AllowableQueryType.NUMERICAL
    ),
    B_LENGTH(
            "_cell_length_b",
            "Cell Length b",
            "a double value",
            AllowableQueryType.NUMERICAL
    ),
    BETA_LENGTH(
            "_cell_angle_beta",
            "Cell Angle beta",
            "a double value",
            AllowableQueryType.NUMERICAL
    ),
    C_LENGTH(
            "_cell_length_c",
            "Cell Length c",
            "a double value",
            AllowableQueryType.NUMERICAL
    ),
    GAMMA_LENGTH(
            "_cell_angle_gamma",
            "Cell Angle gamma",
            "a double value",
            AllowableQueryType.NUMERICAL
    ),
    CELL_VOLUME(
            "_cell_volume",
            "Cell Volume",
            "a double value",
            AllowableQueryType.NUMERICAL
    ),

    // Chemistry

    STRUCT_FORMULA(
            "_chemical_formula_sum",
            "Structural Formula",
            "B6 La",
            AllowableQueryType.TEXT
    ),
    CHEM_NAME(
            "_chemical_name_systematic",
            "Chemical Name",
            "Magnesium Hydroxide",
            AllowableQueryType.TEXT
    ),
    MINERAL(
            "_chemical_name_mineral",
            "Mineral Name",
            "Cassiterite",
            AllowableQueryType.TEXT
    ),

    // Symmetry

    SPACE_GROUP(
            "_symmetry_space_group_name_H-M",
            "H-M Space Group",
            "P m 3 m",
            AllowableQueryType.TEXT
    ),

    // Crystal Chemistry

    FLAGS(
            "",
            "Various Flags",
            "",
            AllowableQueryType.MULTI_MANY_CHOICE.withChoices(
                    mapOf(
                            "has coordinates" to "Has Coordinates",
                            "has disorder" to "Has Disorder",
                            "has Fobs" to "Has Fobs"
                    )
            )
    ),

    //FLAG_COORDINATE("Has Coordinates", "", AllowableQueryType.BOOLEAN),
    //FLAG_DISORDER("Has Disorder", "", AllowableQueryType.BOOLEAN),
    //FLAG_FOBS("Has Fobs", "", AllowableQueryType.BOOLEAN);


}

