package br.com.patternhub.service

import br.com.patternhub.model.Request
import org.springframework.stereotype.Component

@Component("priorityStrategy")
class PriorityProcessingStrategy(private val notificationChannels: List<NotificationChannel>) : ProcessingStrategy {
    override fun process(request: Request) {
        // apply priority rules
        notificationChannels.forEach { it.send(request.user.email, "Request processed (PRIORITY)", "Request ${request.id} processed with priority") }
    }
}
