package me.daegyeo.maru

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MaruBackendApplication

fun main(args: Array<String>) {
    runApplication<MaruBackendApplication>(*args)
}
