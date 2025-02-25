package me.daegyeo.maru.diary.application.persistence

import jakarta.persistence.*
import me.daegyeo.maru.file.application.persistence.FileEntity
import me.daegyeo.maru.shared.entity.AuditDateTimeEntity

@Entity
@Table(name = "diary_files")
class DiaryFileEntity(
    @ManyToOne(targetEntity = DiaryEntity::class)
    @JoinColumn(name = "diary_id", nullable = false, insertable = false, updatable = false)
    val diary: DiaryEntity? = null,

    @Column(name = "diary_id", nullable = false)
    val diaryId: Long,

    @OneToOne(targetEntity = FileEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false, insertable = false, updatable = false)
    val file: FileEntity? = null,

    @Column(name = "file_id", nullable = false)
    val fileId: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_file_id")
    val diaryFileId: Long? = null,
) : AuditDateTimeEntity()
