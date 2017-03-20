package com.github.decyg.CrAgg.cif

import org.parboiled.Parboiled
import org.parboiled.errors.ErrorUtils
import org.parboiled.parserunners.ReportingParseRunner
import org.parboiled.support.ParsingResult

/**
 * A singleton designed to centralise the parsing logic for CIF_Node files into its' component data classes.
 *
 * Parser conforms to syntax outlined in http://www.iucr.org/resources/cif/spec/version1.1/cifsyntax
 *
 */
object CIFSingleton {

    /**
     * Wrapper for the parse exception.
     */
    class ParseException(message: String?) : Throwable(message)

    // The Parboiled parser object
    val pParser : CIFParser = Parboiled.createParser(CIFParser::class.java)

    /**
     * Does the majority of the parsing logic in that it defers it to Parboiled and checks for results or errors
     *
     * @param cifText the text of the CIF document
     * @return CIFNode representation of the text
     */
    fun parseCIF(cifText : String) : CIFParser.Companion.CIFNode {
        val parseRes : ParsingResult<CIFParser.Companion.CIFNode> = ReportingParseRunner<CIFParser.Companion.CIFNode>(pParser.CIF_Node())
                .run(cifText)

        if (parseRes.parseErrors.isEmpty().not()){
            throw ParseException(ErrorUtils.printParseError(parseRes.parseErrors?.get(0)))
        }

        return parseRes.resultValue
    }

    /**
     * This function does the bulk logic of the process of converting a pre-processed [CIFNode] object into
     * the detailed result object
     */
    fun getPopulatedCIF(cif_ID: CIF_ID, cifNode: CIFParser.Companion.CIFNode): CIFDetailedResult {

        val stringifiedID = cif_ID.toString()
        val cifResult : CIF = CIF(mutableListOf())

        // Note for the implementation below, it's not a recursive function as it doesn't need to be. It just needs
        // to break down the Node representation into the user readable format

        // For all datablocks on the top level
        // All children are guaranteed to be DataBlock_Nodes

        if (cifNode != null) {

            for (dataBlock in cifNode.children) {

                var curHeading = ""
                val curBlock = DataBlock("", mutableMapOf(), mutableListOf(), mutableListOf())

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

                            // for deep storage it actually needs to be a

                            // for each loopheader i need to iterate through

                            if(dataBlockChild.children.size == 2) {

                                val loopHeader = dataBlockChild.children[0] // can just inject this directly into the LoopedDataItemGroup instance
                                val loopBody = dataBlockChild.children[1]
                                var loopCounter = 0

                                val loopedDataItemsMap : LoopedDataItem = mutableMapOf() // map of header -> list of values

                                for (loopItem in loopBody.children) {

                                    // for each value i need to put it in loopedDataItemsMap[header] as a new entry

                                    val curHeaderNode = loopHeader.children[loopCounter] ?: break

                                    if(!loopedDataItemsMap.containsKey(curHeaderNode.value))
                                        loopedDataItemsMap[curHeaderNode.value] = mutableListOf()

                                    loopedDataItemsMap[curHeaderNode.value]!!.add(loopItem.value)

                                    if (++loopCounter == loopHeader.children.count())
                                        loopCounter = 0

                                }

                                curBlock.loopedDataItems.add(loopedDataItemsMap)

                            }
                        }

                        "DataItem" -> {
                            // Just compose it into the component object and add it to the curblock
                            curBlock.dataItems.put(dataBlockChild.children[0].value, dataBlockChild.children[1].value)

                        }

                        "SaveFrame" -> {
                            // Not implemented due to the lack of need for it, also can't find any examples of CIF files
                            // actually using it, apparently it's a holdover from the format that CIF was inherited from
                        }

                    }

                }

                cifResult.dataBlocks.add(curBlock)

            }

        }

        return CIFDetailedResult(stringifiedID, cifResult)

    }

}

