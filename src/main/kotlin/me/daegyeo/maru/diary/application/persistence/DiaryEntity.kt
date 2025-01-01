package me.daegyeo.maru.diary.application.persistence

import jakarta.persistence.*
import me.daegyeo.maru.shared.entity.AuditDateTimeEntity
import me.daegyeo.maru.user.application.persistence.UserEntity
import java.util.UUID

@Entity
@Table(name = "diaries")
class DiaryEntity(
    @Column(nullable = false)
    var content: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    val user: UserEntity? = null,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    val diaryId: Long? = null,
) : AuditDateTimeEntity()
