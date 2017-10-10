package com.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.ServletComponentScan


@SpringBootApplication
@ServletComponentScan
class DemoWebApplication

fun main(args: Array<String>) {
    SpringApplication.run(DemoWebApplication::class.java, *args)
}

