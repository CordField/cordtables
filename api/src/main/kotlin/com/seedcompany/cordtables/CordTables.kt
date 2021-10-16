package com.seedcompany.cordtables

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class CordTables

fun main(args: Array<String>) {
    runApplication<CordTables>(*args)
}
