package me.daegyeo.maru.diary.application.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import me.daegyeo.maru.shared.entity.AuditDateTimeEntity
import me.daegyeo.maru.user.application.persistence.UserEntity

@Entity
@Table(name = "diaries")
class DiaryEntity(
    @Column(nullable = false)
    val content: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    val diaryId: Long? = null,
) : AuditDateTimeEntity()
