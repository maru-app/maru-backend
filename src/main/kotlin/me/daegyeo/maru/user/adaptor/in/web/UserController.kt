package me.daegyeo.maru.user.adaptor.`in`.web

import me.daegyeo.maru.auth.application.domain.CustomUserDetails
import me.daegyeo.maru.user.adaptor.`in`.web.dto.UpdateUserDto
import me.daegyeo.maru.user.application.port.`in`.DeleteUserUseCase
import me.daegyeo.maru.user.application.port.`in`.UpdateUserUseCase
import me.daegyeo.maru.user.application.port.`in`.command.UpdateUserUseCaseCommand
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
) {
    @PreAuthorize("hasRole('USER')")
    @PutMapping
    fun updateUser(
        @RequestBody body: UpdateUserDto,
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): Boolean {
        updateUserUseCase.updateUser(
            auth.userId,
            UpdateUserUseCaseCommand(
                nickname = body.nickname,
            ),
        )
        return true
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    fun deleteUser(
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): Boolean {
        deleteUserUseCase.deleteUser(auth.userId)
        return true
    }
}
