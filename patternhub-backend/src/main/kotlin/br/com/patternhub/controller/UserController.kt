package br.com.patternhub.controller

import br.com.patternhub.model.User
import br.com.patternhub.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val service: UserService) {

    @PostMapping
    fun create(@RequestBody user: User): ResponseEntity<User> {
        val created = service.create(user)
        return ResponseEntity.ok(created)
    }

    @GetMapping
    fun list(): List<User> = service.listAll()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<User> =
        service.findById(id).map { ResponseEntity.ok(it) }.orElse(ResponseEntity.notFound().build())
}
