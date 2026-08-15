package br.com.patternhub.repository

import br.com.patternhub.model.Request
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestRepository : JpaRepository<Request, Long> {
    fun findByStatus(status: String): List<Request>
    fun findByPriority(priority: String): List<Request>
    fun findByUserId(userId: Long): List<Request>
}
