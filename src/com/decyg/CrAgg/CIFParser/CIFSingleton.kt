package com.decyg.CrAgg.CIFParser

/**
 * A singleton designed to centralise the parsing logic for CIF files into its' component data classes.
 *
 * Parser conforms to syntax outlined in http://www.iucr.org/resources/cif/spec/version1.1/cifsyntax
 * Created by declan on 01/02/2017.
 */
object CIFSingleton {

    /**
     * This section is for thoughts and design explanations
     * I'll need a top level object called CIF, a CIF may contain data blocks.
     * A data block may contain data items or save frames.
     * A data item may contain a data name and value
     * A save frame is a collection of data items within a data block, observe nesting.
     */


    /**
     * The core parser function that takes in a String and parses it into the object representation that's operable.
     */
    fun parse(inputStr : String){

    }

}