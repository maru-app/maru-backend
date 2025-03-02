package me.daegyeo.maru.streak.application.service

import me.daegyeo.maru.streak.application.port.`in`.GetStreakUseCase
import me.daegyeo.maru.streak.application.port.`in`.result.GetStreakResult
import me.daegyeo.maru.streak.application.port.out.ReadStreakPort
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.UUID

@Service
class GetStreakService(
    private val readStreakPort: ReadStreakPort,
) : GetStreakUseCase {
    override fun getStreak(
        userId: UUID,
        date: ZonedDateTime,
    ): GetStreakResult {
        val streak = readStreakPort.readLatestStreakByUserIdAndCreatedAt(userId, date)
        val latestStreak = readStreakPort.readLatestStreakByUserId(userId)
        return GetStreakResult(
            streak = streak?.streak ?: 0,
            bestStreak = latestStreak?.streak ?: 0,
        )
    }
}
