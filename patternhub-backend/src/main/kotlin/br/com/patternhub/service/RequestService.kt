package br.com.patternhub.service

import br.com.patternhub.model.Request
import br.com.patternhub.repository.RequestRepository
import org.springframework.stereotype.Service

@Service
class RequestService(
    private val repository: RequestRepository,
    private val strategies: Map<String, ProcessingStrategy>
) {
    fun submit(request: Request): Request {
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
