package me.daegyeo.maru.streak.adaptor.out.persistence

import me.daegyeo.maru.streak.adaptor.out.mapper.StreakMapper
import me.daegyeo.maru.streak.application.domain.Streak
import me.daegyeo.maru.streak.application.port.out.ReadStreakPort
import org.springframework.stereotype.Component
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
}
