package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.port.`in`.GetImagePathInContentUseCase
import org.springframework.stereotype.Service

@Service
class GetImagePathInContentService : GetImagePathInContentUseCase {
    override fun getImagePathInContent(content: String): List<String> {
        val imageRegex = "\\[image\\|([^]]+)]".toRegex()
        var matchResult = imageRegex.find(content)
        val imagePaths = mutableListOf<String>()
        while (matchResult != null) {
            val fileName = matchResult.groupValues[1]
            imagePaths.add(fileName)
            matchResult = matchResult.next()
        }
        return imagePaths
    }
}
