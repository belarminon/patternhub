package br.com.patternhub.service

import br.com.patternhub.model.Request
import br.com.patternhub.model.User
import kotlin.test.Test
import kotlin.test.assertTrue
import java.time.Instant

class NormalProcessingStrategyTest {
    @Test
    fun `sends notification via channels`() {
        var called = false
        val channel = object : NotificationChannel {
            override fun send(to: String, subject: String, body: String) {
                called = true
            }
        }
        val strategy = NormalProcessingStrategy(listOf(channel))
        val user = User(id = 1L, name = "Test", email = "a@b.com", createdAt = Instant.now())
        val request = Request(id = 1L, user = user, type = "T", description = "d")
        strategy.process(request)
        assertTrue(called)
    }
}
