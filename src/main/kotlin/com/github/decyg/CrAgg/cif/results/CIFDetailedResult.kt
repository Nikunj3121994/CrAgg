package com.github.decyg.CrAgg.cif.results

import com.github.decyg.CrAgg.cif.CIFSingleton
import java.io.File

/**
 * This is a layer on top of the CIFNode type to hide the raw map from the users, this object represents it in a
 * no nonsense, hierarchical parsed format where a user for example can do .getCif.getBodyByID("bodyid")
 *
 * Takes in a File, turns it into a CIFNode, traverses it and populates its' root CIF_Node value
 * Created by declan on 27/02/2017.
 */
class CIFDetailedResult(input : File) {

    // In the abstraction i want to seperate the level of a regular top level dataitem on a datablock
    // from a looped data item, this is because a looped data item is essentially a table and representing this
    // by dumping all the values on the same level makes it INCREDIBLY messy

    // i'm representing the list of data items as a map of
    // strings to list of strings, that covers both loops and singles

    // Note that i'm using maps but also storing the key inside the stored value, this is potentially redundant
    // but i did it this way to be consistent across all wrapped objects and make accesses easy and straight forward

    var cifResult : CIF = CIF(mutableMapOf())

    init {

        val cifNode = CIFSingleton.parseCIF(input)

        // Note for the implementation below, it's not a recursive function as it doesn't need to be. It just needs
        // to break down the Node representation into the user readable format

        // For all datablocks on the top level
        // All children are guaranteed to be DataBlock_Nodes

        for(dataBlock in cifNode.children) {

            var curHeading = ""
            val curBlock = DataBlock("", mutableMapOf(), mutableListOf(), mutableMapOf())

            for (dataBlockChild in dataBlock.children) {

                when (dataBlockChild.value) {

                    "DataBlockHeading" -> {
                        curHeading = dataBlockChild.children[0].value
                        curBlock.dataBlockHeader = curHeading
                    }

                    "LoopedDataItem" -> {

                        // In this case, a looped data item will contain two children, a loopheader and a body.
                        // Loops are curious in that the number of times it tries to apply the header to a value is dictated
                        // by the number of headers. For example with one header, every value will be mapped to that header
                        // However with five headers, value 1, 2, 3, 4, 5 will be mapped to header 1, 2, 3, 4, 5 and on the next
                        // iteration, value 6, 7, 8, 9, 10 will be mapped again to header 1, 2, 3, 4, 5 respectively

                        // The first index should be LoopHeader and the second should be LoopBody

                        // a looped data item is a list of maps, where the maps represent a key being mapped to a
                        // loopeddataitem

                        val loopHeader = dataBlockChild.children[0]
                        val loopBody = dataBlockChild.children[1]
                        var loopCounter = 0
                        val loopGroup : MutableMap<String, LoopedDataItem> = mutableMapOf()

                        for (loopItem in loopBody.children) {

                            val tag = loopHeader.children[loopCounter].value
                            val value = loopItem.value

                            if(!loopGroup.containsKey(tag)){
                                loopGroup.put(
                                        tag,
                                        LoopedDataItem(tag, mutableListOf())
                                )
                            }

                            loopGroup.getValue(tag).value.add(value)

                            if (++loopCounter == loopHeader.children.count())
                                loopCounter = 0

                        }

                        curBlock.loopedDataItems.add(loopGroup)

                    }

                    "DataItem" -> {
                        // Just compose it into the component object and add it to the curblock
                        curBlock.dataItems.put(
                                dataBlockChild.children[0].value,
                                DataItem(dataBlockChild.children[0].value, dataBlockChild.children[1].value)
                        )
                    }

                    "SaveFrame" -> {
                        // ???
                    }

                }

            }

            cifResult.dataBlocks.put(curHeading, curBlock)

        }

    }

}

// Inner definitions down here for tidyness

typealias LoopedDataItemGroup = MutableMap<String, LoopedDataItem>

data class LoopedDataItem(
        val tag : String,
        val value : MutableList<String>
)

data class DataItem(
        val tag : String,
        val value : String
)

data class SaveFrame(
        var saveFrameHeader : String,
        var dataItems : MutableMap<String, DataItem>, // the data item wrapper arguably isn't needed but it's consistent
        var loopedDataItems : MutableList<LoopedDataItemGroup>
)

data class DataBlock(
        var dataBlockHeader : String, // consistency
        var dataItems : MutableMap<String, DataItem>, // the data item wrapper arguably isn't needed but it's consistent
        var loopedDataItems : MutableList<LoopedDataItemGroup>,
        var saveFrames : MutableMap<String, SaveFrame>
)

data class CIF(
        var dataBlocks : MutableMap<String, DataBlock>
)