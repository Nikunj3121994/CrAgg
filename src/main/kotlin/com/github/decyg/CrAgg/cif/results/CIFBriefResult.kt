package com.github.decyg.CrAgg.cif.results

import com.github.decyg.CrAgg.database.query.enums.CommonQueryTerm

/**
 * This class represents a brief mapping of a resulting query from the selected datasource given a mapping of the
 * pregenerated [CommonQueryTerm] entries to [String] (the result)
 *
 * @property cif_ID the ID object of this result
 * @property resultMap the Mapping of [CommonQueryTerm] entries to [String]
 */
data class CIFBriefResult(
        val cif_ID: CIF_ID,
        val resultMap : MutableMap<CommonQueryTerm, String>
) {
    fun cellAsString() : String {

        return  "a: ${resultMap[CommonQueryTerm.A_LENGTH]} Å " +
                "b: ${resultMap[CommonQueryTerm.B_LENGTH]} Å " +
                "c: ${resultMap[CommonQueryTerm.C_LENGTH]} Å " +
                "α: ${resultMap[CommonQueryTerm.ALPHA_LENGTH]} ° " +
                "β: ${resultMap[CommonQueryTerm.BETA_LENGTH]} ° " +
                "γ: ${resultMap[CommonQueryTerm.GAMMA_LENGTH]} ° "
    }

    fun prettyPrintResultMap() : String {

        var outS : String = ""

        resultMap.forEach { t, u ->
            outS += "<br>${t.prettyName}: $u"
        }

        return outS
    }
}

