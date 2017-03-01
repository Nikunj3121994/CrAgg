package com.github.decyg.CrAgg.cif

import com.github.decyg.CrAgg.database.DBSource

/**
 * This class represents a more brief overview of a CIF_Node result.
 *
 * The structure of the fields here are kindly lifted from how the CCDC displays their results as i'm
 * not too experienced with chemistry so i wouldn't know what's common
 * Created by declan on 27/02/2017.
 */
data class CIFBriefResult(
        val source : DBSource, // The type of the source, could do this through generics but it'd be messy
        val id : String, // The id of the entry found in the original database
        val spaceGroup : String, // The space group of a crystal
        val unitCell : Map<String, Double>, // A Map describing the unit cell of something
        val compoundName : String // The name of the compound
)