package me.daegyeo.maru.streak.adapter.out.persistence

import me.daegyeo.maru.streak.adapter.out.mapper.StreakMapper
import me.daegyeo.maru.streak.application.domain.Streak
import me.daegyeo.maru.streak.application.port.out.ReadStreakPort
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class ReadStreakPersistenceAdapter(
    private val streakRepository: StreakRepository,
    private val streakMapper: StreakMapper,
) : ReadStreakPort {
    override fun readLatestStreakByUserId(userId: UUID): Streak? {
        val streak = streakRepository.findFirstByUserIdOrderByCreatedAtDesc(userId).getOrNull()
        return streak?.let {
            streakMapper.toDomain(it)
        }
    }

    override fun readLatestStreakByUserIdAndCreatedAt(
        userId: UUID,
        createdAt: ZonedDateTime,
    ): Streak? {
        val streak = streakRepository.findFirstByUserIdAndCreatedAtOrderByCreatedAtDesc(userId, createdAt).getOrNull()
        return streak?.let {
            streakMapper.toDomain(it)
        }
    }
}
