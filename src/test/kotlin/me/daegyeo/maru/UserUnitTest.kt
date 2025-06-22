package me.daegyeo.maru

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.`in`.DeleteDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetAllDiaryUseCase
import me.daegyeo.maru.shared.constant.Vendor
import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.error.UserError
import me.daegyeo.maru.user.application.port.`in`.command.CreateUserUseCaseCommand
import me.daegyeo.maru.user.application.port.`in`.command.UpdateUserUseCaseCommand
import me.daegyeo.maru.user.application.port.out.CreateUserPort
import me.daegyeo.maru.user.application.port.out.DeleteUserPort
import me.daegyeo.maru.user.application.port.out.ReadUserPort
import me.daegyeo.maru.user.application.port.out.UpdateUserPort
import me.daegyeo.maru.user.application.port.out.command.CreateUserPortCommand
import me.daegyeo.maru.user.application.service.CreateUserService
import me.daegyeo.maru.user.application.service.DeleteUserService
import me.daegyeo.maru.user.application.service.GetUserService
import me.daegyeo.maru.user.application.service.UpdateUserService
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.ZonedDateTime
import java.util.UUID

@Suppress("NonAsciiCharacters")
@ExtendWith(MockitoExtension::class)
class UserUnitTest {
    private val createUserPort = mock(CreateUserPort::class.java)
    private val readUserPort = mock(ReadUserPort::class.java)
    private val deleteUserPort = mock(DeleteUserPort::class.java)
    private val updatedUserPort = mock(UpdateUserPort::class.java)
    private val getUserUseCase = mock(GetUserService::class.java)
    private val getAllDiaryUseCase = mock(GetAllDiaryUseCase::class.java)
    private val deleteDiaryUseCase = mock(DeleteDiaryUseCase::class.java)
    private val createUserService = CreateUserService(createUserPort, readUserPort)
    private val getUserService = GetUserService(readUserPort)
    private val updateUserService = UpdateUserService(updatedUserPort, getUserUseCase)
    private val deleteUserService =
        DeleteUserService(deleteUserPort, getUserUseCase, getAllDiaryUseCase, deleteDiaryUseCase)

    @Test
    fun `이미 존재하는 이메일로 회원가입 시 오류를 반환함`() {
        val input = CreateUserUseCaseCommand(email = "test@example.com", vendor = Vendor.GOOGLE, nickname = "TestUser")
        `when`(readUserPort.readUserByEmail(input.email)).thenReturn(
            User(
                userId = UUID.randomUUID(),
                email = "test@example.com",
                vendor = Vendor.GOOGLE,
                nickname = "TestUser",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                isPublicRanking = false,
                deletedAt = null,
            ),
        )

        val exception =
            assertThrows(ServiceException::class.java) {
                createUserService.createUser(input)
            }
        assert(exception.error == UserError.USER_ALREADY_EXISTS)
    }

    @Test
    fun `새로운 사용자가 성공적으로 회원가입함`() {
        val input =
            CreateUserUseCaseCommand(email = "newuser@example.com", vendor = Vendor.GOOGLE, nickname = "NewUser")
        `when`(readUserPort.readUserByEmail(input.email)).thenReturn(null)
        val createUserDto =
            CreateUserPortCommand(email = "newuser@example.com", vendor = Vendor.GOOGLE, nickname = "NewUser")
        `when`(createUserPort.createUser(createUserDto)).thenReturn(
            User(
                userId = UUID.randomUUID(),
                email = "newuser@example.com",
                vendor = Vendor.GOOGLE,
                nickname = "NewUser",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                isPublicRanking = false,
                deletedAt = null,
            ),
        )

        val result = createUserService.createUser(input)

        verify(createUserPort).createUser(createUserDto)
        assert(result.email == input.email)
        assert(result.vendor == input.vendor)
        assert(result.nickname == input.nickname)
    }

    @Test
    fun `존재하지 않는 사용자를 가져오면 오류를 반환함`() {
        val userId = UUID.randomUUID()
        `when`(readUserPort.readUser(userId)).thenReturn(null)

        val exception =
            assertThrows(ServiceException::class.java) {
                getUserService.getUser(userId)
            }
        assert(exception.error == UserError.USER_NOT_FOUND)
    }

    @Test
    fun `존재하는 사용자를 성공적으로 가져옴`() {
        val userId = UUID.randomUUID()
        `when`(readUserPort.readUser(userId)).thenReturn(
            User(
                userId = userId,
                email = "foobar@acme.com",
                vendor = Vendor.GOOGLE,
                nickname = "FooBar",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                isPublicRanking = false,
                deletedAt = null,
            ),
        )

        val result = getUserService.getUser(userId)

        verify(readUserPort).readUser(userId)
        assert(result.userId == userId)
    }

    @Test
    fun `존재하지 않는 이메일로 사용자를 가져오면 오류를 반환함`() {
        val email = "notfound@acme.com"
        `when`(readUserPort.readUserByEmail(email)).thenReturn(null)

        val exception =
            assertThrows(ServiceException::class.java) {
                getUserService.getUserByEmail(email)
            }
        assert(exception.error == UserError.USER_NOT_FOUND)
    }

    @Test
    fun `존재하는 이메일로 사용자를 성공적으로 가져옴`() {
        val email = "foobar@acme.com"
        `when`(readUserPort.readUserByEmail(email)).thenReturn(
            User(
                userId = UUID.randomUUID(),
                email = email,
                vendor = Vendor.GOOGLE,
                nickname = "FooBar",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                isPublicRanking = false,
                deletedAt = null,
            ),
        )

        val result = getUserService.getUserByEmail(email)

        verify(readUserPort).readUserByEmail(email)
        assert(result.email == email)
    }

    @Test
    fun `사용자 정보를 성공적으로 수정함`() {
        val input = UpdateUserUseCaseCommand(nickname = "NewNickname", isPublicRanking = true)
        val userId = UUID.randomUUID()
        val oldUser =
            User(
                userId = userId,
                email = "",
                vendor = Vendor.GOOGLE,
                nickname = "OldNickname",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                isPublicRanking = true,
                deletedAt = null,
            )
        val updatedUser =
            User(
                userId = userId,
                email = "",
                vendor = Vendor.GOOGLE,
                nickname = "NewNickname",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                isPublicRanking = true,
                deletedAt = null,
            )

        `when`(getUserUseCase.getUser(userId)).thenReturn(oldUser)
        `when`(updatedUserPort.updateUser(userId, oldUser)).thenReturn(updatedUser)

        val result = updateUserService.updateUser(userId, input)

        verify(getUserUseCase).getUser(userId)
        verify(updatedUserPort).updateUser(userId, oldUser)
        assert(result.nickname == "NewNickname")
    }

    @Test
    fun `존재하지 않는 사용자 정보를 수정하면 오류를 반환함`() {
        val input = UpdateUserUseCaseCommand(nickname = "NewNickname", isPublicRanking = false)
        val userId = UUID.randomUUID()
        val updatedUser =
            User(
                userId = userId,
                email = "",
                vendor = Vendor.GOOGLE,
                nickname = "NOT_FOUND_USER",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                isPublicRanking = false,
                deletedAt = null,
            )

        `when`(getUserUseCase.getUser(userId)).thenThrow(ServiceException(UserError.USER_NOT_FOUND))
        `when`(updatedUserPort.updateUser(userId, updatedUser)).thenReturn(null)

        val exception =
            assertThrows(ServiceException::class.java) {
                updateUserService.updateUser(userId, input)
            }
        assert(exception.error == UserError.USER_NOT_FOUND)
    }

    @Test
    fun `사용자가 성공적으로 탈퇴함`() {
        val userId = UUID.randomUUID()
        val user =
            User(
                userId = userId,
                email = "foobar@acme.com",
                vendor = Vendor.GOOGLE,
                nickname = "FooBar",
                isPublicRanking = false,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                deletedAt = null,
            )
        val diaries =
            listOf(
                Diary(
                    diaryId = 1L,
                    title = "제목",
                    content = "ENCRYPTED_CONTENT",
                    emoji = "😊",
                    createdAt = ZonedDateTime.now(),
                    updatedAt = ZonedDateTime.now(),
                    deletedAt = null,
                ),
            )

        `when`(getUserUseCase.getUser(userId)).thenReturn(user)
        `when`(getAllDiaryUseCase.getAllDiaryByUserId(userId)).thenReturn(diaries)

        deleteUserService.deleteUser(userId)

        verify(deleteDiaryUseCase).deleteDiary(1L, userId)
        verify(deleteUserPort).deleteUser(userId)
    }

    @Test
    fun `탈퇴한 사용자 정보를 가져오면 오류를 반환함`() {
        val email = "foobar@acme.com"
        `when`(readUserPort.readUserByEmail(email)).thenReturn(null)

        val exception =
            assertThrows(ServiceException::class.java) {
                getUserService.getUserByEmail(email)
            }
        assert(exception.error == UserError.USER_NOT_FOUND)
    }
}
