package br.com.patternhub.service

import br.com.patternhub.model.Request
import org.springframework.stereotype.Component

@Component("normalStrategy")
class NormalProcessingStrategy(private val notificationChannels: List<NotificationChannel>) : ProcessingStrategy {
    override fun process(request: Request) {
        // validation, persistence assumed done earlier
        notificationChannels.forEach { it.send(request.user.email, "Request processed", "Request ${request.id} processed normally") }
    }
}
