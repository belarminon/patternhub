package br.com.patternhub.service

import org.springframework.stereotype.Component

@Component
class EmailNotificationChannel : NotificationChannel {
    override fun send(to: String, subject: String, body: String) {
        // placeholder: integrate with email provider
        println("[EMAIL] to=$to subject=$subject body=$body")
    }
}
