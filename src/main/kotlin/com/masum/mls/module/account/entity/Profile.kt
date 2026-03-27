package com.masum.mls.module.account.entity

import com.masum.mls.module.user.entity.User
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "m_profiles", schema = "mls")
data class Profile(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    val user: User,

    @Column(length = 100)
    val fullName: String? = null,

    @Column(length = 255)
    val avatarUrl: String? = null,

    @Column(length = 500)
    val address: String? = null,

    @Column(length = 50)
    val city: String? = null,

    @Column(length = 20)
    val postalCode: String? = null,

    @Column(length = 255)
    val bio: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null
)
