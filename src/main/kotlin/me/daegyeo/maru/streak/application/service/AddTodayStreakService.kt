package me.daegyeo.maru.streak.application.service

import me.daegyeo.maru.streak.application.domain.Streak
import me.daegyeo.maru.streak.application.port.`in`.AddTodayStreakUseCase
import me.daegyeo.maru.streak.application.port.out.CreateStreakPort
import me.daegyeo.maru.streak.application.port.out.ReadStreakPort
import me.daegyeo.maru.streak.application.port.out.dto.CreateStreakDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.math.max

@Service
class AddTodayStreakService(
    private val createStreakPort: CreateStreakPort,
    private val readStreakPort: ReadStreakPort,
) : AddTodayStreakUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun addTodayStreak(userId: UUID): Streak {
        val latest = readStreakPort.readLatestStreakByUserId(userId)
        val streak =
            latest?.let {
                val today = ZonedDateTime.now()
                val isSameDate = it.createdAt.toLocalDate() == today.toLocalDate()
                val isInARow = it.createdAt.toLocalDate().plusDays(1) == today.toLocalDate()
                if (isSameDate) {
                    it.streak
                } else {
                    if (isInARow) {
                        it.streak + 1
                    } else {
                        1
                    }
                }
            } ?: 1
        val bestStreak =
            latest?.let {
                max(it.bestStreak, streak)
            } ?: 1

        val result =
            createStreakPort.createStreak(
                CreateStreakDto(
                    userId = userId,
                    streak = streak,
                    bestStreak = bestStreak,
                ),
            )

        logger.info("연속 기록을 추가했습니다. userId: $userId")

        return Streak(
            streak = result.streak,
            bestStreak = result.bestStreak,
            userId = result.userId,
            createdAt = result.createdAt,
            updatedAt = result.updatedAt,
        )
    }
}
