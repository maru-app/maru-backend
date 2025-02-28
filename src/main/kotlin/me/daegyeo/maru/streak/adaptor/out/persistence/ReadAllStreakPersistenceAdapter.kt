package me.daegyeo.maru.streak.adaptor.out.persistence

import me.daegyeo.maru.streak.adaptor.out.mapper.StreakMapper
import me.daegyeo.maru.streak.application.domain.Streak
import me.daegyeo.maru.streak.application.port.out.ReadAllStreakPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ReadAllStreakPersistenceAdapter(
    private val streakRepository: StreakRepository,
    private val streakMapper: StreakMapper,
) : ReadAllStreakPort {
    override fun readAllStreakByUserId(userId: UUID): List<Streak> {
        return streakRepository.findAllByUserId(userId).map {
            streakMapper.toDomain(it)
        }
    }
}
