package me.daegyeo.maru.diary.application.port.`in`

fun interface GetImagePathInContentUseCase {
    fun getImagePathInContent(content: String): List<String>
}
