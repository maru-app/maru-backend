package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.`in`.GetAllDiaryUseCase
import me.daegyeo.maru.diary.application.port.out.ReadAllDiaryPort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetAllDiaryService(private val readAllDiaryPort: ReadAllDiaryPort) : GetAllDiaryUseCase {
    override fun getAllDiaryByUserId(userId: UUID): List<Diary> {
        return readAllDiaryPort.readAllDiaryByUserId(userId)
    }
}
