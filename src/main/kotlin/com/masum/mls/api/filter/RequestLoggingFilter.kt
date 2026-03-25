package com.masum.mls.api.filter

import com.masum.mls.api.util.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class RequestLoggingFilter : OncePerRequestFilter() {

    private val log = logger()

    companion object {
        const val TRACE_ID = "TRACE_ID"
        const val REQUEST_ID = "REQUEST_ID"
        const val START_TIME = "START_TIME"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        // 🔥 1. traceId (ambil dari header / generate)
        val traceId = request.getHeader("X-Trace-Id") ?: UUID.randomUUID().toString()

        // 🔥 2. requestId
        val requestId = request.getHeader("X-Request-Id") ?: UUID.randomUUID().toString()

        // 🔥 3. start time
        val startTime = System.currentTimeMillis()

        val userId = request.getHeader("X-User-Id") ?: "Anonymous"

        // simpan ke request (biar bisa diambil di handler)
        request.setAttribute(TRACE_ID, traceId)
        request.setAttribute(REQUEST_ID, requestId)
        request.setAttribute(START_TIME, startTime)

        // Simpan ke MDC
        MDC.put("traceId", traceId)
        MDC.put("userId", userId)
        MDC.put("path", request.requestURI)
        MDC.put("method", request.method)

        try {
            log.info { "Request started: ${request.method} ${request.requestURI}" }
            filterChain.doFilter(request, response)
        } finally {
            val duration = System.currentTimeMillis() - startTime
            log.info { "[traceId=$traceId] method=${request.method} path=${request.requestURI} duration=${duration}ms" }
            MDC.clear()
        }
    }
}