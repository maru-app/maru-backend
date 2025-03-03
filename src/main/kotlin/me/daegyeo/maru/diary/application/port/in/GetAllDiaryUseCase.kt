package me.daegyeo.maru.diary.application.port.`in`

import me.daegyeo.maru.diary.application.domain.Diary
import org.springframework.data.domain.Page
import java.util.UUID

interface GetAllDiaryUseCase {
    fun getAllDiaryByUserId(userId: UUID): List<Diary>

    fun getAllDiaryByUserIdWithPagination(
        userId: UUID,
        page: Int,
        size: Int,
    ): Page<Diary>
}
