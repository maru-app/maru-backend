package me.daegyeo.maru

import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.user.application.constant.Vendor
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.`in`.dto.CreateUserUseCaseDto
import me.daegyeo.maru.user.application.port.out.CreateUserPort
import me.daegyeo.maru.user.application.port.out.ReadUserPort
import me.daegyeo.maru.user.application.port.out.dto.CreateUserDto
import me.daegyeo.maru.user.application.service.CreateUserService
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.ZonedDateTime
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class UserDomainTest {
    private val createUserPort = mock(CreateUserPort::class.java)
    private val readUserPort = mock(ReadUserPort::class.java)
    private val createUserService = CreateUserService(createUserPort, readUserPort)

    @Test
    fun `이미 존재하는 이메일로 회원가입 시 오류를 반환함`() {
        val input = CreateUserUseCaseDto(email = "test@example.com", vendor = Vendor.GOOGLE, nickname = "TestUser")
        `when`(readUserPort.readUserByEmail(input.email)).thenReturn(
            User(
                userId = UUID.randomUUID(),
                email = "test@example.com",
                vendor = Vendor.GOOGLE,
                nickname = "TestUser",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                deletedAt = null,
            ),
        )

        assertThrows(ServiceException::class.java) {
            createUserService.createUser(input)
        }
    }

    @Test
    fun `새로운 사용자가 성공적으로 회원가입함`() {
        val input = CreateUserUseCaseDto(email = "newuser@example.com", vendor = Vendor.GOOGLE, nickname = "NewUser")
        `when`(readUserPort.readUserByEmail(input.email)).thenReturn(null)
        val createUserDto = CreateUserDto(email = "newuser@example.com", vendor = Vendor.GOOGLE, nickname = "NewUser")
        `when`(createUserPort.createUser(createUserDto)).thenReturn(
            User(
                userId = UUID.randomUUID(),
                email = "newuser@example.com",
                vendor = Vendor.GOOGLE,
                nickname = "NewUser",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
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
        error("Not implemented")
    }

    @Test
    fun `존재하는 사용자를 성공적으로 가져옴`() {
        error("Not implemented")
    }

    @Test
    fun `인증되지 않은 사용자가 타인의 정보를 가져오면 오류를 반환함`() {
        error("Not implemented")
    }

    @Test
    fun `사용자 정보를 성공적으로 수정함`() {
        error("Not implemented")
    }

    @Test
    fun `인증되지 않은 사용자가 타인의 정보를 수정하면 오류를 반환함`() {
        error("Not implemented")
    }

    @Test
    fun `사용자가 성공적으로 탈퇴함`() {
        error("Not implemented")
    }

    @Test
    fun `인증되지 않은 사용자가 타인의 정보로 탈퇴하면 오류를 반환함`() {
        error("Not implemented")
    }

    @Test
    fun `탈퇴한 사용자 정보를 가져오면 오류를 반환함`() {
        error("Not implemented")
    }
}
