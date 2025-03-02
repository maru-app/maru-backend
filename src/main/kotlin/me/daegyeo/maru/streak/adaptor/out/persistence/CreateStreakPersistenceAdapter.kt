package me.daegyeo.maru.streak.adaptor.out.persistence

import me.daegyeo.maru.streak.adaptor.out.mapper.StreakMapper
import me.daegyeo.maru.streak.application.domain.Streak
import me.daegyeo.maru.streak.application.persistence.StreakEntity
import me.daegyeo.maru.streak.application.port.out.CreateStreakPort
import me.daegyeo.maru.streak.application.port.out.dto.CreateStreakDto
import org.springframework.stereotype.Component

@Component
class CreateStreakPersistenceAdapter(
    private val streakRepository: StreakRepository,
    private val streakMapper: StreakMapper,
) : CreateStreakPort {
    override fun createStreak(input: CreateStreakDto): Streak {
        val streak =
            StreakEntity(
                userId = input.userId,
                streak = input.streak,
                bestStreak = input.bestStreak,
            )

        val saved = streakRepository.save(streak)

        return streakMapper.toDomain(saved)
    }
}
