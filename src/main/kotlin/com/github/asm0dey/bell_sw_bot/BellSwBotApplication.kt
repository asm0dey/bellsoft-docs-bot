package com.github.asm0dey.bell_sw_bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


@SpringBootApplication
@EnableConfigurationProperties(UnstructuredConfig::class)
class BellSwBotApplication

fun main(args: Array<String>) {
    runApplication<BellSwBotApplication>(*args)
}


