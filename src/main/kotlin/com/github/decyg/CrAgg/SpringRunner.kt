package com.github.decyg.CrAgg

import com.github.decyg.CrAgg.cif.results.CIFDetailedResult
import com.github.decyg.CrAgg.cif.CIFSingleton
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.io.File

/**
 * Created by declan on 16/02/2017.
 */

@SpringBootApplication
open class SpringRunner

fun main(args: Array<String>) {

    val oNode = CIFSingleton.parseCIF(File("1517271.cif"))

    var res = CIFDetailedResult(File("1517271.cif"))
    println("yo")
    //SpringApplication.run(SpringRunner::class.java, *args)
}

