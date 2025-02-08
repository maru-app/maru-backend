package me.daegyeo.maru.diary.adaptor.`in`.web

import me.daegyeo.maru.auth.application.domain.CustomUserDetails
import me.daegyeo.maru.diary.adaptor.`in`.web.dto.CreateDiaryDto
import me.daegyeo.maru.diary.adaptor.`in`.web.dto.UpdateDiaryDto
import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.`in`.CreateDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetAllDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.UpdateDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.command.CreateDiaryCommand
import me.daegyeo.maru.diary.application.port.`in`.command.UpdateDiaryCommand
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/diary")
class DiaryController(
    private val createDiaryUseCase: CreateDiaryUseCase,
    private val getAllDiaryUseCase: GetAllDiaryUseCase,
    private val getDiaryUseCase: GetDiaryUseCase,
    private val updateDiaryUseCase: UpdateDiaryUseCase,
) {
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    fun createDiary(
        @RequestBody body: CreateDiaryDto,
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): Diary {
        return createDiaryUseCase.createDiary(
            CreateDiaryCommand(
                title = body.title,
                content = body.content,
                userId = auth.userId,
            ),
        )
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    fun getAllDiary(
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): List<Diary> {
        return getAllDiaryUseCase.getAllDiaryByUserId(auth.userId)
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{diaryId}")
    fun getDiary(
        @PathVariable diaryId: Long,
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): Diary {
        return getDiaryUseCase.getDiaryByDiaryId(diaryId, auth.userId)
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{diaryId}")
    fun updateDiary(
        @PathVariable diaryId: Long,
        @RequestBody body: UpdateDiaryDto,
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): Boolean {
        return updateDiaryUseCase.updateDiary(
            diaryId = diaryId,
            userId = auth.userId,
            input =
                UpdateDiaryCommand(
                    title = body.title,
                    content = body.content,
                ),
        )
    }
}
