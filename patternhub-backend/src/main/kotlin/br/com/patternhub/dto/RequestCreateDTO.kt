package br.com.patternhub.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RequestCreateDTO(
    @field:NotNull(message = "userId is required")
    val userId: Long,

    @field:NotBlank(message = "type is required")
    val type: String,

    @field:NotBlank(message = "description is required")
    val description: String,

    val priority: String = "NORMAL"
)
