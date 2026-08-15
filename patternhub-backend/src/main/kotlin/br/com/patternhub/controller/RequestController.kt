package br.com.patternhub.controller

import br.com.patternhub.model.Request
import br.com.patternhub.service.RequestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/requests")
class RequestController(private val service: RequestService) {

    @PostMapping
    fun create(@RequestBody request: Request): ResponseEntity<Request> {
        val created = service.submit(request)
        return ResponseEntity.ok(created)
    }

    @GetMapping
    fun list(): List<Request> = service.listAll()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<Request> =
        service.findById(id).map { ResponseEntity.ok(it) }.orElse(ResponseEntity.notFound().build())
}
