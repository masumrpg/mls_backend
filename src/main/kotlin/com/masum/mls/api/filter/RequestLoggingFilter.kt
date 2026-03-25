package com.masum.mls.api.filter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.masum.mls.api.util.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.util.*

@Component
class RequestLoggingFilter : OncePerRequestFilter() {

    private val log = logger()
    private val objectMapper = ObjectMapper()

    companion object {
        const val TRACE_ID = "TRACE_ID"
        const val REQUEST_ID = "REQUEST_ID"
        const val START_TIME = "START_TIME"
        const val MAX_PAYLOAD = 1024 * 1024 // 1MB

        // 🔥 limit biar ga jebol ES
        const val MAX_MDC_FIELDS = 100
        const val MAX_DEPTH = 5
    }

    private val sensitiveFields = setOf(
        "password",
        "token",
        "accessToken",
        "refreshToken",
        "secret"
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if (request.contentType?.contains("multipart") == true) {
            filterChain.doFilter(request, response)
            return
        }

        val wrappedRequest = ContentCachingRequestWrapper(request, MAX_PAYLOAD)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        val traceId = request.getHeader("X-Trace-Id") ?: UUID.randomUUID().toString()
        val requestId = request.getHeader("X-Request-Id") ?: UUID.randomUUID().toString()
        val userId = request.getHeader("X-User-Id") ?: "Anonymous"
        val startTime = System.currentTimeMillis()

        request.setAttribute(TRACE_ID, traceId)
        request.setAttribute(REQUEST_ID, requestId)
        request.setAttribute(START_TIME, startTime)

        // 🔥 MDC base
        val baseMdc = mapOf(
            "trace.id" to traceId,
            "user.id" to userId,
            "http.request.method" to request.method,
            "url.path" to request.requestURI
        )

        putMDC(baseMdc)

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse)
        } finally {

            val duration = System.currentTimeMillis() - startTime

            val requestNode = parseAndMaskNode(wrappedRequest.contentAsByteArray)
            val responseNode = parseAndMaskNode(wrappedResponse.contentAsByteArray)

            // 🔥 flatten JSON ke MDC
            var counter = 0

            requestNode?.let {
                counter += flattenToMDC(
                    prefix = "http.request.body.content",
                    node = it,
                    depth = 0
                )
            }

            responseNode?.let {
                counter += flattenToMDC(
                    prefix = "http.response.body.content",
                    node = it,
                    depth = 0
                )
            }

            // 🔥 ECS tambahan
            putMDC(
                mapOf(
                    "http.response.status_code" to wrappedResponse.status.toString(),
                    "event.duration" to (duration * 1_000_000).toString(),
                    "url.query" to (request.queryString ?: "")
                )
            )

            log.info { "HTTP request completed" }

            wrappedResponse.copyBodyToResponse()
            MDC.clear()
        }
    }

    // =========================
    // 🔐 Parse + Mask
    // =========================
    private fun parseAndMaskNode(bytes: ByteArray): JsonNode? {
        if (bytes.isEmpty()) return null

        return try {
            val node = objectMapper.readTree(bytes)

            when {
                node.isObject -> maskObject(node as ObjectNode)
                node.isArray -> node.forEach {
                    if (it is ObjectNode) maskObject(it)
                }
            }

            node
        } catch (e: Exception) {
            null
        }
    }

    private fun maskObject(node: ObjectNode) {
        val fields = node.fieldNames()

        while (fields.hasNext()) {
            val field = fields.next()
            val value = node.get(field)

            if (sensitiveFields.contains(field)) {
                node.put(field, "***")
            } else {
                when {
                    value.isObject -> maskObject(value as ObjectNode)
                    value.isArray -> value.forEach {
                        if (it is ObjectNode) maskObject(it)
                    }
                }
            }
        }
    }

    // =========================
    // 🔥 Flatten JSON → MDC
    // =========================
    private fun flattenToMDC(
        prefix: String,
        node: JsonNode,
        depth: Int
    ): Int {

        if (depth > MAX_DEPTH) return 0

        var count = 0

        when {
            node.isObject -> {
                node.properties().forEach { (key, value) ->
                    count += flattenToMDC("$prefix.$key", value, depth + 1)
                }
            }

            node.isArray -> {
                node.forEachIndexed { index, value ->
                    count += flattenToMDC("$prefix[$index]", value, depth + 1)
                }
            }

            else -> {
                if (count < MAX_MDC_FIELDS) {
                    MDC.put(prefix, node.asText())
                    count++
                }
            }
        }

        return count
    }

    // =========================
    // 🔥 MDC helper
    // =========================
    private fun putMDC(data: Map<String, String?>) {
        data.forEach { (key, value) ->
            if (!value.isNullOrBlank()) {
                MDC.put(key, value)
            }
        }
    }
}