package me.daegyeo.maru.streak.adapter.out.persistence

import me.daegyeo.maru.streak.application.domain.StreakGroupByDate
import me.daegyeo.maru.streak.application.domain.StreakRank
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

    @Query(
        """
       SELECT s FROM StreakEntity s 
        WHERE s.userId = :userId AND 
            FUNCTION('DATE', s.createdAt) = FUNCTION('DATE', :createdAt) 
        ORDER BY s.createdAt DESC
        LIMIT 1
    """,
    )
    fun findFirstByUserIdAndCreatedAtOrderByCreatedAtDesc(
        userId: UUID,
        createdAt: ZonedDateTime,
    ): Optional<StreakEntity>

    @Query(
        """
        SELECT new me.daegyeo.maru.streak.application.domain.StreakRank(
            RANK() OVER (ORDER BY s.streak DESC),
            s.user.isPublicRanking,
            CASE WHEN s.user.isPublicRanking THEN s.user.nickname ELSE '' END,
            s.streak,
            s.bestStreak
        )
        FROM StreakEntity s
        WHERE s.createdAt = (
            SELECT MAX(s2.createdAt) 
            FROM StreakEntity s2 
            WHERE s2.user = s.user
        )
        AND EXTRACT(YEAR FROM s.createdAt) = :year
        """,
    )
    fun findAllRankByYear(year: Int): List<StreakRank>
}
