package me.daegyeo.maru.diary.application.port.`in`

fun interface EncryptDiaryUseCase {
    fun encryptDiary(rawContent: String): String
}
