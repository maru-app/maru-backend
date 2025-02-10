package me.daegyeo.maru.file.application.persistence

import jakarta.persistence.*
import me.daegyeo.maru.shared.entity.AuditDateTimeEntity
import me.daegyeo.maru.user.application.persistence.UserEntity
import java.util.UUID

@Entity
@Table(name = "files")
class FileEntity(
    @Column(columnDefinition = "TEXT", nullable = false)
    val path: String,

    @Column(columnDefinition = "TEXT", nullable = false, name = "original_path")
    val originalPath: String,

    @ManyToOne(targetEntity = UserEntity::class)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    val user: UserEntity? = null,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    val fileId: Long? = null,
) : AuditDateTimeEntity()
