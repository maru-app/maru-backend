package me.daegyeo.maru.streak.application.service

import me.daegyeo.maru.streak.application.port.`in`.GetBestStreakUseCase
import me.daegyeo.maru.streak.application.port.out.ReadStreakPort
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.UUID

@Service
class GetBestStreakService(
    private val readStreakPort: ReadStreakPort,
) : GetBestStreakUseCase {
    override fun getBestStreakByDate(
        userId: UUID,
        date: ZonedDateTime,
    ): Int {
        val streak = readStreakPort.readLatestStreakByUserIdAndCreatedAt(userId, date)
        return streak?.bestStreak ?: 0
    }
}
