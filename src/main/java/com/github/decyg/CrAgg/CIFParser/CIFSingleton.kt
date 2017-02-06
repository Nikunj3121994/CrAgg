package main.java.com.github.decyg.CrAgg.CIFParser

import com.github.decyg.CrAgg.CIFParser.CIFParseException
import java.io.File

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
     * Below are the specific rules required for the detailed expressions above
     */

    // Character sets & basic characters

    private val RX_SP =     Regex("\\x20")                      // Space
    private val RX_HT =     Regex("\\x09")                      // Horizontal tab
    private val RX_EOL =    Regex("(?:\\x0A|\\x0D\\x0A|\\x0D)")     // Platform agnostic line separator
    private val RX_NEOL =   Regex("(?:[^\\x0A]|[^\\x0D\\x0A][^\\x0A\\x0D]|[^\\x0D])")                 // Anything that's not an EOL
    private val RX_SQ =     Regex("'")                          // Single quote
    private val RX_DQ =     Regex("\"")                         // Double quote

    private val RX_OrdinaryChar =   Regex("[A-Za-z!%&()*+,\\-./0-9:<=>?@^`{|}~]")
    private val RX_NonBlankChar =   Regex("(?:$RX_OrdinaryChar|$RX_DQ|#|\\$|$RX_SQ|_|;|\\[|\\])")
    private val RX_TextLeadChar =   Regex("(?:$RX_OrdinaryChar|$RX_DQ|#|\\$|$RX_SQ|_|$RX_SP|$RX_HT|\\[|\\])")
    private val RX_AnyPrintChar =   Regex("(?:$RX_OrdinaryChar|$RX_DQ|#|\\$|$RX_SQ|_|$RX_SP|$RX_HT|;|\\[|\\])")

    private val RX_DATA_ =          Regex("(?:[dD][aA][tT][aA]_)")
    private val RX_LOOP_ =          Regex("(?:[lL][oO][oO][pP]_)")
    private val RX_GLOBAL_ =        Regex("(?:[gG][lL][oO][bB][aA][lL]_)")
    private val RX_SAVE_ =          Regex("(?:[sS][aA][vV][eE]_)")
    private val RX_STOP_ =          Regex("(?:[sS][tT][oO][pP]_)")

    // Complex strings, text fields and numbers

    open val RX_Comments =               Regex("(?:#($RX_AnyPrintChar*))")
    open val RX_TokenizedComments =      Regex("(?:(?:$RX_SP|$RX_HT)+$RX_Comments)")
    open val RX_WhiteSpace =             Regex("(?:(?:$RX_SP|$RX_HT|$RX_EOL|(?:$RX_TokenizedComments))+)")

    open val RX_SemiColonTextField =     Regex("(?:$RX_EOL;(?:(?:$RX_AnyPrintChar)*$RX_EOL(?:(?:$RX_TextLeadChar(?:$RX_AnyPrintChar)*)?$RX_EOL)*);)")
    open val RX_TextField =              RX_SemiColonTextField
    open val RX_DoubleQuotedString =     Regex("(?:$RX_DQ(?:$RX_AnyPrintChar)*$RX_DQ$RX_WhiteSpace)")
    open val RX_SingleQuotedString =     Regex("(?:$RX_SQ(?:$RX_AnyPrintChar)*$RX_SQ$RX_WhiteSpace)")
    open val RX_UnquotedString =         Regex("(?:$RX_EOL$RX_OrdinaryChar(?:$RX_NonBlankChar)*|(?:$RX_NEOL)(?:(?:$RX_OrdinaryChar)|;)(?:$RX_NonBlankChar)*)")
    open val RX_CharString =             Regex("(?:(?:$RX_UnquotedString)|(?:$RX_SingleQuotedString)|(?:$RX_DoubleQuotedString))")

    open val RX_Digit =              Regex("(?:[0-9])")
    open val RX_UnsignedInteger =    Regex("(?:(?:$RX_Digit)+)")
    open val RX_Exponent =           Regex("(?:(?:[eE]|[eE][+-])$RX_UnsignedInteger)")
    open val RX_Integer =            Regex("(?:[+-]?$RX_UnsignedInteger)")
    open val RX_Float =              Regex("(?:(?:$RX_Integer$RX_Exponent)|(?:[+-]?$RX_Digit*\\.$RX_UnsignedInteger)|(?:$RX_Digit+\\.(?:$RX_Exponent)?))")
    open val RX_Number =             Regex("(?:$RX_Integer|$RX_Float)")
    open val RX_Numeric =            Regex("(?:$RX_Number)|(?:$RX_Number\\($RX_UnsignedInteger\\))")

    open val RX_Tag =    Regex("(?:_(?:$RX_NonBlankChar)+)")
    open val RX_Value =  Regex("(?:\\.|\\?|(?:$RX_Numeric)|(?:$RX_CharString)|(?:$RX_TextField))")

    /**
     * Below are the core regex expressions that will do the majority of the parsing
     * These expressions are mostly dependant on the terminating values they consume such as
     * comments, nonblankchars, tags and values
     */

    open val RX_LoopHeader = Regex("$RX_LOOP_(?:$RX_WhiteSpace$RX_Tag)+")
    open val RX_LoopBody = Regex("$RX_Value(?:$RX_WhiteSpace$RX_Value)*")
    open val RX_DataItems = Regex("(?:$RX_Tag$RX_WhiteSpace$RX_Value)|(?:$RX_LoopHeader$RX_LoopBody)")
    open val RX_SaveFrameHeading = Regex("$RX_SAVE_(?:$RX_NonBlankChar)+")
    open val RX_SaveFrame = Regex("$RX_SaveFrameHeading(?:$RX_WhiteSpace$RX_DataItems)+$RX_WhiteSpace$RX_SAVE_")
    open val RX_DataBlockHeading = Regex("$RX_DATA_(?:$RX_NonBlankChar)+")
    open val RX_DataBlock = Regex("$RX_DataBlockHeading(?:$RX_WhiteSpace(?:(?:$RX_DataItems)|(?:$RX_SaveFrame)))*")
    open val RX_CIF = Regex("(?:$RX_Comments)?(?:$RX_WhiteSpace)?(?:$RX_DataBlock(?:$RX_WhiteSpace$RX_DataBlock)*(?:$RX_WhiteSpace)?)?")



    /**
     * Below are the data classes representing the hierarchical data structure of CIF objects
     */
    data class CIF(var comments : String = "", var dataBlocks : List<DataBlock> = emptyList())

    data class DataBlock(val heading : String, var dataItems : List<DataItems>, var saveFrames : List<SaveFrame>)

    data class SaveFrame(val heading : String, var dataItems : List<DataItems>)

    data class DataItems(var tagMap : MutableMap<String, String>)

    /**
     * The core parser function that takes in a String and parses it into the object representation that's operable.
     */

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

    // Utility and core functions


    var fileLines = mutableListOf<String>()
    var lastParsedResult = CIF()

    fun parse(inputFile : File) : CIF{

        fileLines = inputFile.readLines().toMutableList()

        try {
            parseCIF()
        } catch (e : CIFParseException) {
            println("Parsing failed due to error: ${e.message}")
        }


        return lastParsedResult

    }

    fun hasNextMatch(regex : Regex, optional : Boolean = false) : Boolean {
        return if (fileLines.size > 0) {
            fileLines.first().matches(regex)
        } else {
            if (optional) {
                false
            } else {
                throw CIFParseException("Parsing error when trying to read rule")
            }
        }
    }

    fun extractMatch(regex : Regex) : String {

        val result = regex.matchEntire(fileLines.removeAt(0))

        return result?.groupValues?.get(1) ?: ""
    }

    // Top level parsing functions

    fun parseCIF() {

        println(RX_Comments)
        parseComments()
        parseWhiteSpace()

    }

    // Component parsing functions

    fun parseComments(){
        while (hasNextMatch(RX_Comments)) { lastParsedResult.comments += extractMatch(RX_Comments) + '\n' }
    }

    fun parseWhiteSpace(){
        while (hasNextMatch(RX_WhiteSpace)) { extractMatch(RX_WhiteSpace) }
    }

}

