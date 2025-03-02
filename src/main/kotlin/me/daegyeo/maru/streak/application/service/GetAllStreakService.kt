package me.daegyeo.maru.streak.application.service

import me.daegyeo.maru.streak.application.domain.StreakGroupByDate
import me.daegyeo.maru.streak.application.port.`in`.GetAllStreakUseCase
import me.daegyeo.maru.streak.application.port.out.ReadAllStreakPort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetAllStreakService(private val readAllStreakPort: ReadAllStreakPort) : GetAllStreakUseCase {
    override fun getAllStreak(
        userId: UUID,
        year: Int,
    ): List<StreakGroupByDate> {
        return readAllStreakPort.readAllStreakByUserIdAndYearGroupByDate(userId, year)
    }
}
