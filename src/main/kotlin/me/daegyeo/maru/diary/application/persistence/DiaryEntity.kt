package me.daegyeo.maru.diary.application.persistence

import jakarta.persistence.*
import me.daegyeo.maru.shared.entity.AuditDateTimeEntity
import me.daegyeo.maru.user.application.persistence.UserEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "diaries")
@SQLDelete(sql = "UPDATE diaries SET deleted_at = now() WHERE diary_id = ?")
@SQLRestriction("deleted_at IS NULL")
class DiaryEntity(
    @Column(columnDefinition = "TEXT", nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String,

    @ManyToOne(targetEntity = UserEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    val user: UserEntity? = null,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(nullable = true, name = "deleted_at")
    var deletedAt: ZonedDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    val diaryId: Long? = null,
) : AuditDateTimeEntity()
