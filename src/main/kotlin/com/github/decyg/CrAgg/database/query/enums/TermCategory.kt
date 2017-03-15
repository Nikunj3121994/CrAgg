package com.github.decyg.CrAgg.database.query.enums

/**
 * Represents the category of a term object, this is mainly used for organisation and displaying on whatever the frontend
 * is
 *
 * @property prettyName the user readable category name
 * @property prettyDescription the user readable category description
 * @property catItems a [Set] of [CommonQueryTerm] objects, defining what this can store
 */
enum class TermCategory(val prettyName: String, val prettyDescription: String, val catItems : Set<CommonQueryTerm>) {

    BIBLIOGRAPHY(
            "Bibliography",
            "Fields pertaining to the written aspect of a submission",
            sortedSetOf(
                    CommonQueryTerm.ID,
                    CommonQueryTerm.AUTHOR,
                    CommonQueryTerm.ARTICLE,
                    CommonQueryTerm.JOURNAL,
                    CommonQueryTerm.JOURNAL_DOI,
                    CommonQueryTerm.YEAR
            )
    ),

    CELL(
            "Cell",
            "Something about cells",
            sortedSetOf(
                    CommonQueryTerm.A_LENGTH,
                    CommonQueryTerm.B_LENGTH,
                    CommonQueryTerm.C_LENGTH,
                    CommonQueryTerm.ALPHA_LENGTH,
                    CommonQueryTerm.BETA_LENGTH,
                    CommonQueryTerm.GAMMA_LENGTH,
                    CommonQueryTerm.CELL_VOLUME
            )
    ),

    CHEMISTRY(
            "Chemistry",
            "Chemistry things",
            sortedSetOf(
                    CommonQueryTerm.STRUCT_FORMULA,
                    CommonQueryTerm.CHEM_NAME,
                    CommonQueryTerm.MINERAL
            )

    ),

    SYMMETRY(
            "Symmetry",
            "Symmetrical aspects",
            sortedSetOf(
                    CommonQueryTerm.SPACE_GROUP
            )
    ),

    CRYSTAL_CHEM(
            "Crystal Chemistry",
            "Toggles",
            sortedSetOf(
                    CommonQueryTerm.FLAGS
            )
    ) // maybe not
}