package br.com.patternhub.controller

import br.com.patternhub.dto.RequestCreateDTO
import br.com.patternhub.model.Request
import br.com.patternhub.service.RequestService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("/api/requests")
class RequestController(private val service: RequestService) {

    @PostMapping
    fun create(@Valid @RequestBody dto: RequestCreateDTO): ResponseEntity<Request> {
        // val created = service.submit(dto)
        // return ResponseEntity.ok(created)

        val createdRequest = service.createFromDto(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest)
    }

    @GetMapping
    fun list(): List<Request> = service.listAll()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<Request> =
        service.findById(id).map { ResponseEntity.ok(it) }.orElse(ResponseEntity.notFound().build())
}
