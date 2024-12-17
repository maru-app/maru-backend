package me.daegyeo.maru

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class MaruBackendApplication

fun main(args: Array<String>) {
    runApplication<MaruBackendApplication>(*args)
}
