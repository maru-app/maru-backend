package me.daegyeo.maru.streak.adaptor.`in`.web

import me.daegyeo.maru.auth.application.domain.CustomUserDetails
import me.daegyeo.maru.streak.application.domain.StreakGroupByDate
import me.daegyeo.maru.streak.application.port.`in`.GetAllStreakUseCase
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/streak")
class StreakController(
    private val getAllStreakUseCase: GetAllStreakUseCase,
) {
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    fun getAllStreak(
        @RequestParam("year") year: Int,
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): List<StreakGroupByDate> {
        return getAllStreakUseCase.getAllStreak(auth.userId, year)
    }
}
