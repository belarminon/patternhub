package br.com.patternhub.service

import br.com.patternhub.dto.RequestCreateDTO
import br.com.patternhub.model.Request
import br.com.patternhub.repository.RequestRepository
import br.com.patternhub.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RequestService(
    private val repository: RequestRepository,
    private val userRepository: UserRepository,
    private val strategies: Map<String, ProcessingStrategy>
) {
    fun submit(dto: RequestCreateDTO): Request {
        val user = userRepository.findById(dto.userId).orElseThrow { IllegalArgumentException("user not found: ${dto.userId}") }
        val request = Request(user = user, type = dto.type, description = dto.description, priority = dto.priority)
        val saved = repository.save(request)
        val strategyKey = when (saved.priority.uppercase()) {
            "PRIORITY", "URGENT" -> "priorityStrategy"
            else -> "normalStrategy"
        }
        strategies[strategyKey]?.process(saved)
        return saved
    }

    fun findById(id: Long) = repository.findById(id)
    fun listAll() = repository.findAll()
}
