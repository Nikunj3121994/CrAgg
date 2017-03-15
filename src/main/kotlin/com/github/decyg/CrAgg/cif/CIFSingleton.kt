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
     */
    fun parseCIF(cifText : String) : CIFParser.Companion.CIFNode {
        val parseRes : ParsingResult<CIFParser.Companion.CIFNode> = ReportingParseRunner<CIFParser.Companion.CIFNode>(pParser.CIF_Node())
                .run(cifText)

        if (parseRes.parseErrors.isEmpty().not()){
            throw ParseException(ErrorUtils.printParseError(parseRes.parseErrors?.get(0)))
        }

        return parseRes.resultValue
    }

}

