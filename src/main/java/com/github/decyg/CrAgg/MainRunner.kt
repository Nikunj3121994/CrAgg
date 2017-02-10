package com.github.decyg.CrAgg

import com.github.decyg.CrAgg.CIFParser.CIFParser
import org.parboiled.Parboiled
import org.parboiled.errors.ErrorUtils
import org.parboiled.parserunners.ReportingParseRunner
import org.parboiled.support.ParseTreeUtils
import org.parboiled.support.ParsingResult
import java.io.File

/**
 * Created by declan on 01/02/2017.
 */


fun main(args : Array<String>){

    val pParser = Parboiled.createParser(CIFParser::class.java)


    val res : ParsingResult<CIFParser.CIFNode> = ReportingParseRunner<CIFParser.CIFNode>(pParser.CIF()).run(File("1517271.cif").readText())

    //val res : ParsingResult<Any> = ReportingParseRunner<Any>(pParser.TestFirstOf()).run(test)


    if (res.parseErrors.isEmpty().not()){

        println(ErrorUtils.printParseError(res.parseErrors?.get(0)))
        println(res)
    }
    println(ParseTreeUtils.printNodeTree(res))

    //println(CIFSingleton.parse(File("1517271.cif")))

}
