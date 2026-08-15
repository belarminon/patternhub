package br.com.patternhub.integration

import br.com.patternhub.model.User
import br.com.patternhub.model.Request
import br.com.patternhub.repository.RequestRepository
import br.com.patternhub.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Instant
import kotlin.test.assertEquals

@SpringBootTest
@Testcontainers
class RequestRepositoryIntegrationTest {
    companion object {
        @Container
        val postgresContainer = PostgreSQLContainer<Nothing>("postgres:15").apply {
            withDatabaseName("test")
            withUsername("postgres")
            withPassword("postgres")
        }
    }

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var requestRepository: RequestRepository

    @Test
    fun `save and load request`() {
        val user = userRepository.save(User(name = "Integration", email = "i@local", createdAt = Instant.now()))
        val req = Request(user = user, type = "T", description = "d")
        val saved = requestRepository.save(req)
        val loaded = requestRepository.findById(saved.id!!).get()
        assertEquals(saved.description, loaded.description)
    }
}
