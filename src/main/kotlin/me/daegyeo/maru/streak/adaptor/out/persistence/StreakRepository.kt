package me.daegyeo.maru.streak.adaptor.out.persistence

import me.daegyeo.maru.streak.application.domain.StreakGroupByDate
import me.daegyeo.maru.streak.application.persistence.StreakEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.ZonedDateTime
import java.util.Optional
import java.util.UUID

interface StreakRepository : JpaRepository<StreakEntity, Long> {
    fun findAllByUserId(userId: UUID): List<StreakEntity>

    fun findFirstByUserIdOrderByCreatedAtDesc(userId: UUID): Optional<StreakEntity>

    @Query(
        """
        SELECT new me.daegyeo.maru.streak.application.domain.StreakGroupByDate(
            streak.userId,
            CAST(DATE(streak.createdAt) AS string),
            COUNT(*)
        )
        FROM StreakEntity streak
        WHERE EXTRACT(YEAR FROM streak.createdAt) = :year and streak.userId = :userId
        GROUP BY DATE(streak.createdAt), streak.userId
        ORDER BY DATE(streak.createdAt) DESC
    """,
    )
    fun countAllByUserIdAndCreatedAtGroupByDate(
        userId: UUID,
        year: String,
    ): List<StreakGroupByDate>

    fun findFirstByUserIdAndCreatedAtOrderByCreatedAtDesc(
        userId: UUID,
        createdAt: ZonedDateTime,
    ): Optional<StreakEntity>
}
