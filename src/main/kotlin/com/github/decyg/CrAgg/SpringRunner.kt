package com.github.decyg.CrAgg

import com.github.decyg.CrAgg.cif.CIFSingleton
import com.github.decyg.CrAgg.database.query.CommonQueryTerm
import com.github.decyg.CrAgg.database.query.QueryExpression
import com.github.decyg.CrAgg.database.query.QueryQuantifier
import com.github.decyg.CrAgg.database.query.TERM
import org.springframework.boot.SpringApplication
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

    //var res = CIFDetailedResult(File("1517271.cif"))
    println("yo")

    SpringApplication.run(SpringRunner::class.java, *args)

    val QE = QueryExpression(
            TERM(
                        CommonQueryTerm.AUTHOR, QueryQuantifier.CONTAINS, textTerm = "thompson"
                )
    )


   // val dbRes = DBSingleton.getDBBySource(COD::class).queryDatabase(QE)

   // val dbResSpec = DBSingleton.getDBBySource(COD::class).queryDatabaseSpecific(dbRes[0])

  //  println(dbResSpec)

    println("all done")
}

