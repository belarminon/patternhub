package br.com.patternhub.service

import br.com.patternhub.model.User
import br.com.patternhub.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(private val repository: UserRepository) {

    fun create(user: User): User = repository.save(user)

    fun listAll(): List<User> = repository.findAll()

    fun findById(id: Long): Optional<User> = repository.findById(id)

    fun delete(id: Long) = repository.deleteById(id)

    @Transactional
    fun toggleUserStatus(id: Long): Int = repository.toggleUserStatusById(id) 
}
