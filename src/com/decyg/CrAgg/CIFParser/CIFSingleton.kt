package com.decyg.CrAgg.CIFParser

/**
 * A singleton designed to centralise the parsing logic for CIF files into its' component data classes.
 *
 * Parser conforms to syntax outlined in http://www.iucr.org/resources/cif/spec/version1.1/cifsyntax
 *
 * Created by Declan Neilson.
 */
object CIFSingleton {

    /**
     * This section is for thoughts and design explanations
     * I'll need a top level object called CIF, a CIF may contain data blocks.
     * A data block may contain data items or save frames.
     * A data item may contain a data name and value
     * A save frame is a collection of data items within a data block, observe nesting.
     *
     */

    /**
     * Below are the data classes representing the
     */
    data class CIF(val comments : String = "", var dataBlocks : List<DataBlock>)

    data class DataBlock(val heading : String, var dataItems : List<DataItems>, var saveFrames : List<SaveFrame>)

    data class SaveFrame(val heading : String, var dataItems : List<DataItems>)

    data class DataItems(var tagMap : MutableMap<String, String>)

    /**
     * The core parser function that takes in a String and parses it into the object representation that's operable.
     */

    fun parse(inputStr : String){

        /**
         * parse delegates to the parseCIF sub function which starts the recursive
         *
         * in each parseXXX function it's a series of functions containing a consumeToken
         * function, consumetoken will fail if the pattern it's trying to consume is not matched and it's marked
         * as non optional. If consumetoken was successful it will return a typed result before recursively delegating
         * and consuming to the next parseXXX function, for example
         *
         *
         * parseCIF will consume comments optionally, then it'll look for and consume many datablocks
         * if it finds a datablock worth consuming, it'll call the parseDataBlock function which then expects a datablock
         * heading etc, parseDataBlock will return a DataBlock function which gets compiled into a list by the parseCIF
         *
         * This continues all the way down until it's fully populated
         */
    }

}