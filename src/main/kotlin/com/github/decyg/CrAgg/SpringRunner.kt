package com.github.decyg.CrAgg

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching

/**
 * Core runner for the application, very simple and could probably be moved somewhere else eventually
 * Created by declan on 16/02/2017.
 */

@SpringBootApplication
@EnableCaching
open class SpringRunner

fun main(args: Array<String>) {

    SpringApplication.run(SpringRunner::class.java, *args)

}

