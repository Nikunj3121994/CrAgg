package com.github.decyg.CrAgg

import com.github.decyg.CrAgg.cif.CIFSingleton
import com.github.decyg.CrAgg.cif.results.CIFDetailedResult
import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.implementation.COD
import com.github.decyg.CrAgg.database.query.CommonQueryTerm
import com.github.decyg.CrAgg.database.query.QueryExpression
import com.github.decyg.CrAgg.database.query.QueryQuantifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.io.File

/**
 * Core runner for the application, very simple and could probably be moved somewhere else eventually
 * Created by declan on 16/02/2017.
 */

@SpringBootApplication
open class SpringRunner

fun main(args: Array<String>) {

    val oNode = CIFSingleton.parseCIF(File("1517271.cif"))

    var res = CIFDetailedResult(File("1517271.cif"))
    println("yo")

    DBSingleton.getDBBySource(COD::class).queryDatabase(emptyList())
    //SpringApplication.run(SpringRunner::class.java, *args)

    QueryExpression(
            QueryExpression.AND(
                    QueryExpression.TERM(
                            CommonQueryTerm.AUTHOR, QueryQuantifier.CONTAINS, "edmund"
                    ),
                    QueryExpression.TERM()
            )
    )
}

