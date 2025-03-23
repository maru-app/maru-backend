package me.daegyeo.maru

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.TimeZone

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
class MaruBackendApplication

fun main(args: Array<String>) {
    runApplication<MaruBackendApplication>(*args)
}

@PostConstruct
fun started() {
    val tz = System.getenv("TZ") ?: "Asia/Seoul"
    TimeZone.setDefault(TimeZone.getTimeZone(tz))
}
