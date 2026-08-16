package br.com.patternhub.model

import java.time.Instant
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var name: String = "",
    var email: String = "",
    val createdAt: Instant = Instant.now(),
    val status: String = "ACTIVE"
)

{
    // JPA requires a no-arg constructor. Provide one for Hibernate.
    constructor(): this(null, "", "", Instant.now(), "ACTIVE")
}
