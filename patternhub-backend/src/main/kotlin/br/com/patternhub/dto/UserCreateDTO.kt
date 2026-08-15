package br.com.patternhub.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserCreateDTO(
    @field:NotBlank(message = "name is required")
    val name: String,

    @field:NotBlank(message = "email is required")
    @field:Email(message = "invalid email")
    val email: String
)
