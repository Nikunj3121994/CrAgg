package com.github.decyg.CrAgg.cif.parser

import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.DBSource
import com.github.decyg.CrAgg.database.query.enums.CommonQueryTerm

/**
 * This is a small data class designed to represent a joined ID of any CIF object, it needs to be a mix between
 * a [db] and an [id] as multiple datasources may have conflicts
 *
 * @property db The database source as a [DBSource] object or a [String] which gets converted automatically
 * @property id The id of the entry
 */
data class CIF_ID(val db : DBSource, val id : String){
    /**
     * Identical to the primary constructor except it takes a [String] as the db object and uses the [DBSingleton]
     * to convert it into a [DBSource] object
     */
    constructor(db : String, id : String) : this(
            DBSingleton.getDBSourceByName(db)!!,
            id
    )

    override fun toString(): String {
        return "${db.simpleName} : $id"
    }
}

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

    /**
     * Very basic function that returns a string representation of the cell.
     * Ideally this would be moved somewhere else.
     *
     * @return the nice looking cell formatted with veluaes
     */
    fun cellAsString() : String {

        return  "a: ${resultMap[CommonQueryTerm.A_LENGTH]} Å " +
                "b: ${resultMap[CommonQueryTerm.B_LENGTH]} Å " +
                "c: ${resultMap[CommonQueryTerm.C_LENGTH]} Å " +
                "α: ${resultMap[CommonQueryTerm.ALPHA_LENGTH]} ° " +
                "β: ${resultMap[CommonQueryTerm.BETA_LENGTH]} ° " +
                "γ: ${resultMap[CommonQueryTerm.GAMMA_LENGTH]} ° "
    }

    /**
     * Similar to above, pretty prints the resultmap for client use.
     * Should also be moved
     *
     * @return the nice looking result map
     */
    fun prettyPrintResultMap() : String {

        var outS : String = ""

        resultMap.forEach { t, u ->
            outS += "<br>${t.prettyName}: $u"
        }

        return outS
    }
}

/**
 * This is a layer on top of the [CIFParser.CIFNode] type to hide the raw nodes from the programmers, it allows a user to
 * easily access various components of the result with well named objects such as [CIF], [DataBlock], [SaveFrame],
 * [DataItem], [LoopedDataItem] and finally [LoopedDataItemGroup]
 *
 * More details on the CIF syntax this abstracts can be found here https://www.iucr.org/resources/cif/spec/version1.1/cifsyntax
 *
 * @property cifNode the raw [CIFParser.CIFNode] object to parse
 * @constructor does the parsing
 *
 */

data class CIFDetailedResult(
        var stringifiedID: String = "",
        var cifResult: CIF = CIF(mutableListOf())
)

// Inner definitions for organisation
// These are all relevant to the objects stored within a detailedresult object

typealias LoopedDataItem = MutableMap<String, MutableList<String>>
/**
 * Represents a looped data item group where the [headers] are the "headers" and the [values]
 * are a 2D array to infer a table
 */

class LoopedDataItemGroup(
        val headers: MutableList<String> = mutableListOf(),
        val values: MutableList<MutableList<String>> = mutableListOf()
)

/**
 * Represents a save frame. As per the spec, stores its header as a [String], a [Map] of [String] to [DataItem]s and
 * a [List] of [LoopedDataItemGroup]
 */

data class SaveFrame(
        var saveFrameHeader: String = "",
        var dataItems: MutableMap<String, String> = mutableMapOf(), // the data item wrapper arguably isn't needed but it's consistent
        var loopedDataItems: MutableList<LoopedDataItem> = mutableListOf()
)

/**
 * Represents a datablock, note that here and [SaveFrame] both contain their own headers as strings even though they're
 * stored in a map, this is just to make it easier to access the header without referring the the containing map.
 *
 */

data class DataBlock(
        var dataBlockHeader: String = "", // consistency
        var dataItems: MutableMap<String, String> = mutableMapOf(), // the data item wrapper arguably isn't needed but it's consistent
        var loopedDataItems: MutableList<LoopedDataItem> = mutableListOf(),
        var saveFrames: MutableList<SaveFrame> = mutableListOf()
)

/**
 * Top level object that stores a map of data block ids to [DataBlock] objects
 */

data class CIF(
        var dataBlocks: MutableList<DataBlock> = mutableListOf()
)

