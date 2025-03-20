package me.daegyeo.maru.streak.adapter.`in`.web

import me.daegyeo.maru.auth.application.domain.CustomUserDetails
import me.daegyeo.maru.streak.application.domain.StreakGroupByDate
import me.daegyeo.maru.streak.application.port.`in`.GetAllStreakUseCase
import me.daegyeo.maru.streak.application.port.`in`.GetStreakUseCase
import me.daegyeo.maru.streak.application.port.`in`.result.GetStreakResult
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/streak")
class StreakController(
    private val getAllStreakUseCase: GetAllStreakUseCase,
    private val getStreakUseCase: GetStreakUseCase,
) {
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    fun getAllStreak(
        @RequestParam("year") year: Int,
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): List<StreakGroupByDate> {
        return getAllStreakUseCase.getAllStreak(auth.userId, year)
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/best")
    fun getBestStreak(
        @RequestParam("date") date: String,
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): GetStreakResult {
        val zonedDateTime =
            LocalDate
                .parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
                .atStartOfDay()
                .atZone(ZoneId.of("Asia/Seoul"))
        return getStreakUseCase.getStreak(auth.userId, zonedDateTime)
    }
}
