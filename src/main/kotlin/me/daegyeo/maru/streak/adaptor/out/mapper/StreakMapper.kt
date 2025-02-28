package me.daegyeo.maru.streak.adaptor.out.mapper

import me.daegyeo.maru.streak.application.domain.Streak
import me.daegyeo.maru.streak.application.persistence.StreakEntity
import org.springframework.stereotype.Component

@Component
class StreakMapper {
    fun toDomain(streakEntity: StreakEntity): Streak {
        return Streak(
            streak = streakEntity.streak,
            bestStreak = streakEntity.bestStreak,
            userId = streakEntity.userId,
            createdAt = streakEntity.createdAt!!,
            updatedAt = streakEntity.updatedAt!!,
        )
    }
}
