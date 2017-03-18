package com.github.decyg.CrAgg.cif.results

import com.github.decyg.CrAgg.cif.CIFParser
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

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
@Document(indexName = "cif_files", type = "cif")
class CIFDetailedResult(
        @Id var stringifiedID : String = "",
        var cifResult : CIF = CIF(mutableMapOf())
) {

    // In the abstraction i want to seperate the level of a regular top level dataitem on a datablock
    // from a looped data item, this is because a looped data item is essentially a table and representing this
    // by dumping all the values on the same level makes it INCREDIBLY messy

    // i'm representing the list of data items as a map of
    // strings to list of strings, that covers both loops and singles

    // Note that i'm using maps but also storing the key inside the stored value, this is potentially redundant
    // but i did it this way to be consistent across all wrapped objects and make accesses easy and straight forward


    fun populateCIF(cif_ID : CIF_ID, cifNode : CIFParser.Companion.CIFNode) : CIFDetailedResult {

        this.stringifiedID = cif_ID.toString()

        // Note for the implementation below, it's not a recursive function as it doesn't need to be. It just needs
        // to break down the Node representation into the user readable format

        // For all datablocks on the top level
        // All children are guaranteed to be DataBlock_Nodes

        if(cifNode != null) {

            for (dataBlock in cifNode.children) {

                var curHeading = ""
                val curBlock = DataBlock(mutableMapOf(), mutableListOf(), mutableMapOf())

                for (dataBlockChild in dataBlock.children) {

                    when (dataBlockChild.value) {

                        "DataBlockHeading" -> {
                            curHeading = dataBlockChild.children[0].value
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
                            val loopGroup: MutableMap<String, MutableList<String>> = mutableMapOf()

                            for (loopItem in loopBody.children) {

                                val tag = loopHeader.children[loopCounter].value
                                val value = loopItem.value

                                if (!loopGroup.containsKey(tag)) {
                                    loopGroup.put(
                                            tag,
                                            mutableListOf()
                                    )
                                }

                                loopGroup.getValue(tag).add(value)

                                if (++loopCounter == loopHeader.children.count())
                                    loopCounter = 0

                            }

                            curBlock.loopedDataItems.add(loopGroup)

                        }

                        "DataItem" -> {
                            // Just compose it into the component object and add it to the curblock
                            curBlock.dataItems.put(
                                    dataBlockChild.children[0].value,
                                    dataBlockChild.children[1].value
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

        return this

    }

}

// Inner definitions for organisation

/**
 * A small typealias to wrap a map in a single name as there's really no point making a whole new object for it.
 */
typealias LoopedDataItemGroup = MutableMap<String, MutableList<String>>


/**
 * Represents a save frame. As per the spec, stores its header as a [String], a [Map] of [String] to [DataItem]s and
 * a [List] of [LoopedDataItemGroup]
 */
data class SaveFrame(
        var dataItems : MutableMap<String, String> = mutableMapOf(), // the data item wrapper arguably isn't needed but it's consistent
        var loopedDataItems : MutableList<LoopedDataItemGroup> = mutableListOf()
)

/**
 * Represents a datablock, note that here and [SaveFrame] both contain their own headers as strings even though they're
 * stored in a map, this is just to make it easier to access the header without referring the the containing map.
 *
 */
data class DataBlock(
        var dataItems : MutableMap<String, String> = mutableMapOf(), // the data item wrapper arguably isn't needed but it's consistent
        var loopedDataItems : MutableList<LoopedDataItemGroup> = mutableListOf(),
        var saveFrames : MutableMap<String, SaveFrame> = mutableMapOf()
)

/**
 * Top level object that stores a map of data block ids to [DataBlock] objects
 */
data class CIF(
        var dataBlocks : MutableMap<String, DataBlock> = mutableMapOf()
)