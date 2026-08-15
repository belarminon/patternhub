package br.com.patternhub.service

interface NotificationChannel {
    fun send(to: String, subject: String, body: String)
}
