package me.daegyeo.maru.diary.application.port.`in`

fun interface DecryptDiaryUseCase {
    fun decryptDiary(encryptedContent: String): String
}
