package com.github.decyg.CrAgg.cif.results

import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.DBSource
import com.github.decyg.CrAgg.database.query.CommonQueryTerm

/**
 * This class represents a more brief overview of a CIF_Node result.
 *
 * The structure of the fields here are kindly lifted from how the CCDC displays their results as i'm
 * not too experienced with chemistry so i wouldn't know what's common
 * Created by declan on 27/02/2017.
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

data class CIF_ID(val db : DBSource, val id : String){
    constructor(db : String, id : String) : this(
            DBSingleton.getDBSourceByName(db)!!,
            id
    )

    override fun toString(): String {
        return "${db.simpleName} : $id"
    }
}