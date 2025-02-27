package me.daegyeo.maru.user.application.service

import me.daegyeo.maru.diary.application.port.`in`.DeleteDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetAllDiaryUseCase
import me.daegyeo.maru.user.application.port.`in`.DeleteUserUseCase
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import me.daegyeo.maru.user.application.port.out.DeleteUserPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeleteUserService(
    private val deleteUserPort: DeleteUserPort,
    private val getUserUseCase: GetUserUseCase,
    private val getAllDiaryUseCase: GetAllDiaryUseCase,
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
) : DeleteUserUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun deleteUser(userId: UUID) {
        val isExistsUser = getUserUseCase.getUser(userId)

        val diaries = getAllDiaryUseCase.getAllDiaryByUserId(isExistsUser.userId)
        diaries.forEach {
            deleteDiaryUseCase.deleteDiary(it.diaryId, isExistsUser.userId)
        }
        deleteUserPort.deleteUser(isExistsUser.userId)

        logger.info("User 데이터를 삭제하고 탈퇴했습니다. $userId")
    }
}
