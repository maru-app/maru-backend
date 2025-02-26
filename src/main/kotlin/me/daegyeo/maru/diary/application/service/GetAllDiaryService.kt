package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.`in`.GetAllDiaryUseCase
import me.daegyeo.maru.diary.application.port.out.ReadAllDiaryPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetAllDiaryService(private val readAllDiaryPort: ReadAllDiaryPort) : GetAllDiaryUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    override fun getAllDiaryByUserId(userId: UUID): List<Diary> {
        logger.info("User의 전체 Diary 데이터를 조회했습니다. userId: $userId")
        return readAllDiaryPort.readAllDiaryByUserId(userId)
    }
}
