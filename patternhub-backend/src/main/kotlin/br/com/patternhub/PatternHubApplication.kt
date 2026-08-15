package br.com.patternhub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PatternHubApplication

fun main(args: Array<String>) {
    runApplication<PatternHubApplication>(*args)
}
