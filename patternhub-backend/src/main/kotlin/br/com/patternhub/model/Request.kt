package br.com.patternhub.model

import java.time.Instant
import jakarta.persistence.*

@Entity
@Table(name = "requests")
data class Request(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    val user: User,
    val type: String,
    val description: String,
    val priority: String = "NORMAL",
    val status: String = "OPEN",
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

{
    // JPA requires a no-arg constructor. Provide one for Hibernate.
    constructor(): this(null, User(), "", "", "NORMAL", "OPEN", Instant.now(), Instant.now())
}
