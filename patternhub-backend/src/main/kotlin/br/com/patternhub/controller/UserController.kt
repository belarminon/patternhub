package br.com.patternhub.controller

import br.com.patternhub.dto.UserCreateDTO
import br.com.patternhub.model.User
import br.com.patternhub.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val service: UserService) {

    @PostMapping
    fun create(@Valid @RequestBody dto: UserCreateDTO): ResponseEntity<User> {
        val user = User(name = dto.name, email = dto.email)
        val created = service.create(user)
        return ResponseEntity.ok(created)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody dto: UserCreateDTO): ResponseEntity<User> {
        val existing = service.findById(id)
        return if (existing.isPresent) {
            val u = existing.get().copy(name = dto.name, email = dto.email)
            ResponseEntity.ok(service.create(u))
        } else ResponseEntity.notFound().build()
    }

    @PutMapping("/{id}/inactivate")
    fun inactivate(@PathVariable id: Long): ResponseEntity<Void> =
        service.findById(id).
            map { user ->
                service.inactivateUser(id)
                ResponseEntity.noContent().build<Void>()
            }
        }.orElseGet{ ResponseEntity.notFound().build() }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        val existing = service.findById(id)
        return if (existing.isPresent) {
            service.delete(id)
            ResponseEntity.noContent().build()
        } else ResponseEntity.notFound().build()
    }

    @GetMapping
    fun list(): List<User> = service.listAll()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<User> =
        service.findById(id).map { ResponseEntity.ok(it) }.orElse(ResponseEntity.notFound().build())
}
