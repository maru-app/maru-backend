package me.daegyeo.maru

import io.jsonwebtoken.*
import me.daegyeo.maru.auth.application.domain.AccessTokenPayload
import me.daegyeo.maru.auth.application.domain.RegisterTokenPayload
import me.daegyeo.maru.auth.application.error.AuthError
import me.daegyeo.maru.auth.application.port.`in`.ParseJWTUseCase
import me.daegyeo.maru.auth.application.port.`in`.command.RegisterUserCommand
import me.daegyeo.maru.auth.application.service.*
import me.daegyeo.maru.shared.constant.Vendor
import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.`in`.CreateUserUseCase
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import java.security.SecureRandom
import java.time.ZonedDateTime
import java.util.Base64
import java.util.UUID
import javax.crypto.SecretKey

@Suppress("NonAsciiCharacters")
@ExtendWith(MockitoExtension::class)
class AuthUnitTest {
    private val jwtBuilder = mock(Jwts.builder()::class.java)
    private val jwtParserBuilder = mock(Jwts.parser()::class.java)
    private val jwtParser = mock(JwtParser::class.java)
    private val jws: Jws<Claims> = mock(Jws::class.java) as Jws<Claims>
    private val createUserUseCase = mock(CreateUserUseCase::class.java)
    private val parseJWTService = ParseJWTService()
    private val validateJWTService = ValidateJWTService()
    private val generateJWTService = GenerateJWTService()
    private val getUserUseCase = mock(GetUserUseCase::class.java)
    private val parseJWTUseCase = mock(ParseJWTUseCase::class.java)
    private val getAuthInfoService = GetAuthInfoService(getUserUseCase, parseJWTUseCase)

    @BeforeEach
    fun setup() {
        val random = SecureRandom()
        val bytes = ByteArray(256)
        random.nextBytes(bytes)
        val secret = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)

        val accessTokenSecretField = GenerateJWTService::class.java.getDeclaredField("accessTokenSecret")
        accessTokenSecretField.isAccessible = true
        accessTokenSecretField.set(generateJWTService, secret)

        val accessTokenSecretField2 = ValidateJWTService::class.java.getDeclaredField("accessTokenSecret")
        accessTokenSecretField2.isAccessible = true
        accessTokenSecretField2.set(validateJWTService, secret)

        val accessTokenSecretField3 = ParseJWTService::class.java.getDeclaredField("accessTokenSecret")
        accessTokenSecretField3.isAccessible = true
        accessTokenSecretField3.set(parseJWTService, secret)

        val accessTokenExpirationField = GenerateJWTService::class.java.getDeclaredField("accessTokenExpiration")
        accessTokenExpirationField.isAccessible = true
        accessTokenExpirationField.set(generateJWTService, "1d")

        val registerTokenSecretField = ParseJWTService::class.java.getDeclaredField("registerTokenSecret")
        registerTokenSecretField.isAccessible = true
        registerTokenSecretField.set(parseJWTService, secret)
    }

    @Test
    fun `AccessToken을 성공적으로 생성함`() {
        val mockedJwts = mockStatic(Jwts::class.java)
        mockedJwts.`when`<JwtBuilder> { Jwts.builder() }.thenReturn(jwtBuilder)

        val input = AccessTokenPayload(email = "foobar@acme.com", vendor = Vendor.GOOGLE)
        `when`(jwtBuilder.subject(input.email)).thenReturn(jwtBuilder)
        `when`(jwtBuilder.claim("vendor", input.vendor)).thenReturn(jwtBuilder)
        `when`(jwtBuilder.issuedAt(any())).thenReturn(jwtBuilder)
        `when`(jwtBuilder.expiration(any())).thenReturn(jwtBuilder)
        `when`(jwtBuilder.signWith(any())).thenReturn(jwtBuilder)
        `when`(jwtBuilder.compact()).thenReturn("token")

        val result = generateJWTService.generateAccessToken(input)

        mockedJwts.close()

        verify(jwtBuilder).compact()
        assert(result == "token")
    }

    @Test
    fun `AccessToken을 성공적으로 검증함`() {
        val mockedJwts = mockStatic(Jwts::class.java)
        mockedJwts.`when`<JwtParserBuilder> { Jwts.parser() }.thenReturn(jwtParserBuilder)

        `when`(jwtParserBuilder.verifyWith(any<SecretKey>())).thenReturn(jwtParserBuilder)
        `when`(jwtParserBuilder.build()).thenReturn(jwtParser)
        `when`(jwtParser.parseSignedClaims(any())).thenReturn(jws)

        val result = validateJWTService.validateAccessToken("token")

        mockedJwts.close()

        assert(result)
    }

    @Test
    fun `AccessToken 검증에 실패함`() {
        val mockedJwts = mockStatic(Jwts::class.java)
        mockedJwts.`when`<JwtParserBuilder> { Jwts.parser() }.thenReturn(jwtParserBuilder)

        `when`(jwtParserBuilder.verifyWith(any<SecretKey>())).thenReturn(jwtParserBuilder)
        `when`(jwtParserBuilder.build()).thenReturn(jwtParser)
        `when`(jwtParser.parseSignedClaims(any())).thenThrow(JwtException::class.java)

        val exception =
            assertThrows(ServiceException::class.java) {
                validateJWTService.validateAccessToken("token")
            }
        assert(exception.error == AuthError.PERMISSION_DENIED)

        mockedJwts.close()
    }

    @Test
    fun `AccessToken을 성공적으로 파싱함`() {
        val mockedJwts = mockStatic(Jwts::class.java)
        mockedJwts.`when`<JwtParserBuilder> { Jwts.parser() }.thenReturn(jwtParserBuilder)

        val mockClaims = mock(Claims::class.java)
        `when`(mockClaims["sub"]).thenReturn("foobar@acme.com")
        `when`(mockClaims["vendor"]).thenReturn("google")

        `when`(jwtParserBuilder.verifyWith(any<SecretKey>())).thenReturn(jwtParserBuilder)
        `when`(jwtParserBuilder.build()).thenReturn(jwtParser)
        `when`(jwtParser.parseSignedClaims(any())).thenReturn(jws)
        `when`(jws.payload).thenReturn(mockClaims)

        val result = parseJWTService.parseAccessToken("token")

        mockedJwts.close()

        assert(result.email == "foobar@acme.com")
        assert(result.vendor == Vendor.GOOGLE)
    }

    @Test
    fun `RegisterToken을 사용해서 성공적으로 회원가입함`() {
        val mockParseJWTUseCase = mock(ParseJWTUseCase::class.java)
        val registerUserService = RegisterUserService(mockParseJWTUseCase, createUserUseCase)

        val input =
            RegisterUserCommand(
                registerToken = "token",
                nickname = "foobar",
            )
        val payload =
            RegisterTokenPayload(
                email = "foobar@acme.com",
                vendor = Vendor.GOOGLE,
            )
        `when`(mockParseJWTUseCase.parseRegisterToken(input.registerToken)).thenReturn(payload)

        val result = registerUserService.registerUser(input)

        assert(result)
        verify(createUserUseCase).createUser(any())
    }

    @Test
    fun `AccessToken을 사용해서 성공적으로 내 정보를 가져옴`() {
        val accessToken = "token"
        val email = "foobar@acme.com"
        val nickname = "foobar"
        val vendor = Vendor.GOOGLE
        val createdAt = ZonedDateTime.now()

        val user =
            User(
                userId = UUID.randomUUID(),
                email = email,
                vendor = vendor,
                nickname = nickname,
                createdAt = createdAt,
                updatedAt = createdAt,
                deletedAt = null,
            )
        val payload = AccessTokenPayload(email = email, vendor = vendor)

        `when`(parseJWTUseCase.parseAccessToken(accessToken)).thenReturn(payload)
        `when`(getUserUseCase.getUserByEmail(email)).thenReturn(user)

        val result = getAuthInfoService.getAuthInfo(accessToken)

        assert(email == result.email)
        assert(nickname == result.nickname)
        assert(vendor == result.vendor)
        assert(createdAt == result.createdAt)
    }

    @Test
    fun `올바르지 않거나 만료된 AccessToken으로 내 정보를 가져오면 오류를 반환함`() {
        val accessToken = "invalid.token"

        `when`(parseJWTUseCase.parseAccessToken(accessToken)).thenThrow(ServiceException::class.java)

        assertThrows(ServiceException::class.java) {
            getAuthInfoService.getAuthInfo(accessToken)
        }
    }
}
