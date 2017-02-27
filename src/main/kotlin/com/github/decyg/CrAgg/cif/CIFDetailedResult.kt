package com.github.decyg.CrAgg.cif

import java.io.File

/**
 * This is a layer on top of the CIFNode type to hide the raw map from the users, this object represents it in a
 * no nonsense, hierarchical parsed format where a user for example can do .getCif.getBodyByID("bodyid")
 *
 * Takes in a File, turns it into a CIFNode, traverses it and populates its' root CIF value
 * Created by declan on 27/02/2017.
 */
class CIFDetailedResult(input : File) {

    data class DataItem(val valueMap : Map<String, String>)

    data class SaveFrame(val dataItems : List<DataItem>)

    data class DataBlock(val dataItems : List<DataItem>, val saveFrames : Map<String, SaveFrame>)

    data class CIF(val dataBlocks : Map<String, DataBlock>)

    var cifResult : CIF = CIF(emptyMap())

    init {

    }

}