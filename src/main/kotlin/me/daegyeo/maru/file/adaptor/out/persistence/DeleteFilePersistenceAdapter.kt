package me.daegyeo.maru.file.adaptor.out.persistence

import me.daegyeo.maru.file.application.port.out.DeleteFilePort
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class DeleteFilePersistenceAdapter(private val fileRepository: FileRepository) : DeleteFilePort {
    override fun deleteFile(fileId: Long): Boolean {
        val file = fileRepository.findById(fileId).getOrNull()
        return file?.let {
            fileRepository.delete(it)
            true
        } ?: false
    }
}
