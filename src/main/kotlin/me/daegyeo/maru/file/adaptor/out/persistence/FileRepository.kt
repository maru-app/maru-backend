package me.daegyeo.maru.file.adaptor.out.persistence

import me.daegyeo.maru.file.application.persistence.FileEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository : JpaRepository<FileEntity, Long>
