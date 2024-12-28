package me.daegyeo.maru

import io.jsonwebtoken.*
import me.daegyeo.maru.auth.application.domain.AccessTokenPayload
import me.daegyeo.maru.auth.application.error.AuthError
import me.daegyeo.maru.auth.application.service.GenerateJWTService
import me.daegyeo.maru.auth.application.service.ParseJWTService
import me.daegyeo.maru.auth.application.service.ValidateJWTService
import me.daegyeo.maru.shared.exception.ServiceException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKey

@Suppress("NonAsciiCharacters")
@ExtendWith(MockitoExtension::class)
class AuthUnitTest {
    private val jwtBuilder = mock(Jwts.builder()::class.java)
    private val jwtParserBuilder = mock(Jwts.parser()::class.java)
    private val jwtParser = mock(JwtParser::class.java)
    private val jws: Jws<Claims> = mock(Jws::class.java) as Jws<Claims>
    private val parseJWTService = ParseJWTService()
    private val validateJWTService = ValidateJWTService()
    private val generateJWTService = GenerateJWTService()

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
    }

    @Test
    fun `AccessToken을 성공적으로 생성함`() {
        val mockedJwts = mockStatic(Jwts::class.java)
        mockedJwts.`when`<JwtBuilder> { Jwts.builder() }.thenReturn(jwtBuilder)

        val input = AccessTokenPayload(email = "foobar@acme.com", vendor = "google")
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
        assert(result.vendor == "google")
    }
}
