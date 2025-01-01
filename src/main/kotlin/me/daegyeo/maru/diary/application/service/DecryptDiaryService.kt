package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.port.`in`.DecryptDiaryUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
class DecryptDiaryService : DecryptDiaryUseCase {
    @Value("\${diary.secret}")
    private lateinit var secret: String

    private val ivLength = 12

    override fun decryptDiary(encryptedContent: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val decodedData = Base64.getDecoder().decode(encryptedContent)
        val iv = decodedData.copyOfRange(0, ivLength)
        val cipherText = decodedData.copyOfRange(ivLength, decodedData.size)
        val decodedSecret = Base64.getDecoder().decode(secret)

        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(decodedSecret, 0, decodedSecret.size, "AES"), spec)

        return cipher.doFinal(cipherText).toString(Charsets.UTF_8)
    }
}
