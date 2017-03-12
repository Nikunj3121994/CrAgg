package com.github.decyg.CrAgg.cif.results

import com.github.decyg.CrAgg.database.DB_UUID
import com.github.decyg.CrAgg.database.query.CommonQueryTerm

/**
 * This class represents a more brief overview of a CIF_Node result.
 *
 * The structure of the fields here are kindly lifted from how the CCDC displays their results as i'm
 * not too experienced with chemistry so i wouldn't know what's common
 * Created by declan on 27/02/2017.
 */
data class CIFBriefResult(
        val id : DB_UUID,
        val spaceGroup : String, // The space group of a crystal
        val unitCell : Map<CommonQueryTerm, Double>, // A Map describing the unit cell of something
        val compoundName : String, // The name of the compound
        val chemFormula : String, // The formula of the compound
        val authors : String, // The authors
        val journalTitle : String // The journal
) {
    fun cellAsString() : String {

        return  "a: ${unitCell[CommonQueryTerm.A_LENGTH]} Å " +
                "b: ${unitCell[CommonQueryTerm.B_LENGTH]} Å " +
                "c: ${unitCell[CommonQueryTerm.C_LENGTH]} Å " +
                "α: ${unitCell[CommonQueryTerm.ALPHA_LENGTH]} ° " +
                "β: ${unitCell[CommonQueryTerm.BETA_LENGTH]} ° " +
                "γ: ${unitCell[CommonQueryTerm.GAMMA_LENGTH]} ° "
    }
}