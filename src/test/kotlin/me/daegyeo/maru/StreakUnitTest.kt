package me.daegyeo.maru

import me.daegyeo.maru.streak.application.domain.Streak
import me.daegyeo.maru.streak.application.domain.StreakGroupByDate
import me.daegyeo.maru.streak.application.domain.StreakRank
import me.daegyeo.maru.streak.application.port.out.CreateStreakPort
import me.daegyeo.maru.streak.application.port.out.ReadAllStreakPort
import me.daegyeo.maru.streak.application.port.out.ReadStreakPort
import me.daegyeo.maru.streak.application.port.out.dto.CreateStreakDto
import me.daegyeo.maru.streak.application.service.AddTodayStreakService
import me.daegyeo.maru.streak.application.service.GetAllStreakService
import me.daegyeo.maru.streak.application.service.GetStreakRankingService
import me.daegyeo.maru.streak.application.service.GetStreakService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.ZonedDateTime
import java.util.UUID

@Suppress("NonAsciiCharacters")
@ExtendWith(MockitoExtension::class)
class StreakUnitTest {
    private val readStreakPort = mock(ReadStreakPort::class.java)
    private val readAllStreakPort = mock(ReadAllStreakPort::class.java)
    private val createStreakPort = mock(CreateStreakPort::class.java)

    private val getStreakService = GetStreakService(readStreakPort)
    private val getAllStreakService = GetAllStreakService(readAllStreakPort)
    private val addTodayStreakService = AddTodayStreakService(createStreakPort, readStreakPort)
    private val getStreakRankingStreak = GetStreakRankingService(readAllStreakPort)

    @Test
    fun `userId로 현재 연속 기록과 최장 연속 기록을 가져옴`() {
        val userId = UUID.randomUUID()
        val date = ZonedDateTime.now()
        val streak =
            Streak(
                userId = userId,
                streak = 1,
                bestStreak = 2,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )

        `when`(readStreakPort.readLatestStreakByUserIdAndCreatedAt(userId, date)).thenReturn(streak)
        `when`(readStreakPort.readLatestStreakByUserId(userId)).thenReturn(streak)

        val result = getStreakService.getStreak(userId, date)

        assert(result.streak == 1)
        assert(result.bestStreak == 2)
    }

    @Test
    fun `userId와 특정 연도의 모든 연속 기록을 가져옴`() {
        val userId = UUID.randomUUID()
        val year = 2021
        val streaks =
            listOf(
                StreakGroupByDate(
                    userId = userId,
                    date = "2021-01-01",
                    count = 1,
                ),
            )

        `when`(readAllStreakPort.readAllStreakByUserIdAndYearGroupByDate(userId, year)).thenReturn(streaks)

        val result = getAllStreakService.getAllStreak(userId, year)

        assert(result.size == 1)
        assert(result[0].count == 1L)
    }

    @Test
    fun `새 연속 기록을 추가함`() {
        val userId = UUID.randomUUID()
        val streak =
            Streak(
                userId = userId,
                streak = 1,
                bestStreak = 2,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )
        val createStreakDto =
            CreateStreakDto(
                userId = userId,
                streak = 1,
                bestStreak = 2,
            )

        `when`(readStreakPort.readLatestStreakByUserId(userId)).thenReturn(streak)
        `when`(createStreakPort.createStreak(createStreakDto)).thenReturn(streak)

        val result = addTodayStreakService.addTodayStreak(userId)

        assert(result.streak == 1)
        assert(result.bestStreak == 2)
    }

    @Test
    fun `매일 연속 기록을 추가하면 streak이 증가함`() {
        val userId = UUID.randomUUID()
        val latestStreak =
            Streak(
                userId = userId,
                streak = 1,
                bestStreak = 1,
                createdAt = ZonedDateTime.now().minusDays(1),
                updatedAt = ZonedDateTime.now().minusDays(1),
            )
        val streak =
            Streak(
                userId = userId,
                streak = 2,
                bestStreak = 2,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )
        val createStreakDto =
            CreateStreakDto(
                userId = userId,
                streak = 2,
                bestStreak = 2,
            )

        `when`(readStreakPort.readLatestStreakByUserId(userId)).thenReturn(latestStreak)
        `when`(createStreakPort.createStreak(createStreakDto)).thenReturn(streak)

        val result = addTodayStreakService.addTodayStreak(userId)

        assert(result.streak == 2)
        assert(result.bestStreak == 2)
    }

    @Test
    fun `연속 기록이 끊기면 1부터 다시 시작함`() {
        val userId = UUID.randomUUID()
        val latestStreak =
            Streak(
                userId = userId,
                streak = 3,
                bestStreak = 3,
                createdAt = ZonedDateTime.now().minusDays(3),
                updatedAt = ZonedDateTime.now().minusDays(3),
            )
        val streak =
            Streak(
                userId = userId,
                streak = 1,
                bestStreak = 3,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )
        val createStreakDto =
            CreateStreakDto(
                userId = userId,
                streak = 1,
                bestStreak = 3,
            )

        `when`(readStreakPort.readLatestStreakByUserId(userId)).thenReturn(latestStreak)
        `when`(createStreakPort.createStreak(createStreakDto)).thenReturn(streak)

        val result = addTodayStreakService.addTodayStreak(userId)

        assert(result.streak == 1)
        assert(result.bestStreak == 3)
    }

    @Test
    fun `기존 연속 기록이 없으면 1으로 시작함`() {
        val userId = UUID.randomUUID()
        val streak =
            Streak(
                userId = userId,
                streak = 1,
                bestStreak = 1,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )
        val createStreakDto =
            CreateStreakDto(
                userId = userId,
                streak = 1,
                bestStreak = 1,
            )

        `when`(readStreakPort.readLatestStreakByUserId(userId)).thenReturn(null)
        `when`(createStreakPort.createStreak(createStreakDto)).thenReturn(streak)

        val result = addTodayStreakService.addTodayStreak(userId)

        assert(result.streak == 1)
        assert(result.bestStreak == 1)
    }

    @Test
    fun `페이지네이션을 사용해서 연속 기록 랭킹 정보를 가져옴`() {
        val year = 2025
        val page = 0
        val size = 10
        val streaks =
            listOf(
                StreakRank(
                    rank = 1,
                    isPublicRanking = false,
                    nickname = "",
                    streak = 1,
                    bestStreak = 2,
                ),
            )
        val paginationStreaks = PageImpl(streaks, Pageable.unpaged(), streaks.size.toLong())

        `when`(readAllStreakPort.readAllStreakRankOrderByStreakDesc(year, page, size)).thenReturn(paginationStreaks)

        val result = getStreakRankingStreak.getRanking(year, page, size)

        assert(result.size == 1)
        assert(result.content[0].streak == 1)
        assert(result.content[0].bestStreak == 2)
    }
}
