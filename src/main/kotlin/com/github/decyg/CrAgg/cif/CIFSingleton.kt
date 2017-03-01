package com.github.decyg.CrAgg.cif

import org.parboiled.Parboiled
import org.parboiled.errors.ErrorUtils
import org.parboiled.parserunners.ReportingParseRunner
import org.parboiled.support.ParsingResult
import java.io.File

/**
 * A singleton designed to centralise the parsing logic for CIF_Node files into its' component data classes.
 *
 * Parser conforms to syntax outlined in http://www.iucr.org/resources/cif/spec/version1.1/cifsyntax
 *
 * Created by Declan Neilson.
 */
object CIFSingleton {

    class ParseException(message: String?) : Throwable(message)

    val pParser : CIFParser = Parboiled.createParser(CIFParser::class.java)

    fun parseCIF(cifFile : File) : CIFParser.CIFNode {
        val parseRes : ParsingResult<CIFParser.CIFNode> = ReportingParseRunner<CIFParser.CIFNode>(pParser.CIF_Node())
                .run(cifFile.readText())

        if (parseRes.parseErrors.isEmpty().not()){
            throw ParseException(ErrorUtils.printParseError(parseRes.parseErrors?.get(0)))
        }

        return parseRes.resultValue
    }



}

