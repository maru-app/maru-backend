package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.port.`in`.EncryptDiaryUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
class EncryptDiaryService : EncryptDiaryUseCase {
    @Value("\${diary.secret}")
    private lateinit var secret: String

    private val ivLength = 12

    override fun encryptDiary(rawContent: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = ByteArray(ivLength).apply { SecureRandom().nextBytes(this) }
        val spec = GCMParameterSpec(128, iv)
        val decodedSecret = Base64.getDecoder().decode(secret)

        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(decodedSecret, 0, decodedSecret.size, "AES"), spec)
        val encryptedData = cipher.doFinal(rawContent.toByteArray())

        return Base64.getEncoder().encodeToString(iv + encryptedData)
    }
}
