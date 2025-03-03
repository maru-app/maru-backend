package me.daegyeo.maru.diary.application.port.out

import me.daegyeo.maru.diary.application.domain.Diary
import org.springframework.data.domain.Page
import java.util.UUID

interface ReadAllDiaryPort {
    fun readAllDiaryByUserId(userId: UUID): List<Diary>

    fun readAllDiaryByUserIdWithPagination(
        userId: UUID,
        page: Int,
        size: Int,
    ): Page<Diary>
}
