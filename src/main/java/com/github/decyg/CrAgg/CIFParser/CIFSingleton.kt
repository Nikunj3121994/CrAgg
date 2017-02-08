package main.java.com.github.decyg.CrAgg.CIFParser

/**
 * A singleton designed to centralise the parsing logic for CIF files into its' component data classes.
 *
 * Parser conforms to syntax outlined in http://www.iucr.org/resources/cif/spec/version1.1/cifsyntax
 *
 * Created by Declan Neilson.
 */
object CIFSingleton {

    /**
     * This section is for thoughts, design explanations and external accesses
     * I'll need a top level object called CIF, a CIF may contain data blocks.
     * A data block may contain data items or save frames.
     * A data item may contain a data name and value
     * A save frame is a collection of data items within a data block, observe nesting.
     *
     */

    /**
     * Below are the data classes representing the hierarchical data structure of CIF objects
     */
    data class CIF(var comments : String = "", var dataBlocks : List<DataBlock> = emptyList())

    data class DataBlock(val heading : String, var dataItems : List<DataItems> = emptyList(), var saveFrames : List<SaveFrame> = emptyList())

    data class SaveFrame(val heading : String, var dataItems : List<DataItems> = emptyList())

    data class DataItems(var tagMap : MutableMap<String, String>)


}

