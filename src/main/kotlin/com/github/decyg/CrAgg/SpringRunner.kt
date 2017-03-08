package com.github.decyg.CrAgg

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Core runner for the application, very simple and could probably be moved somewhere else eventually
 * Created by declan on 16/02/2017.
 */

@SpringBootApplication
open class SpringRunner

fun main(args: Array<String>) {

    SpringApplication.run(SpringRunner::class.java, *args)

}

