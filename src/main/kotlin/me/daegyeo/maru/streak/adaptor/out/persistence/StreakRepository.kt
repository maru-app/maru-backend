package me.daegyeo.maru.streak.adaptor.out.persistence

import me.daegyeo.maru.streak.application.persistence.StreakEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StreakRepository : JpaRepository<StreakEntity, Long> {
    fun findAllByUserId(userId: UUID): List<StreakEntity>
}
