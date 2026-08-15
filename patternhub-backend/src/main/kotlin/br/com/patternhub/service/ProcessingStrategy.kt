package br.com.patternhub.service

import br.com.patternhub.model.Request

interface ProcessingStrategy {
    fun process(request: Request)
}
