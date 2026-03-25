package com.masum.mls.api.util

import com.masum.mls.api.dto.MetaData
import com.masum.mls.api.filter.RequestLoggingFilter
import jakarta.servlet.http.HttpServletRequest

fun buildMeta(request: HttpServletRequest): MetaData {
    val startTime = request.getAttribute(RequestLoggingFilter.START_TIME) as? Long
    val duration = startTime?.let { System.currentTimeMillis() - it }

    return MetaData(
        method = request.method,
        path = request.requestURI,
        query = request.queryString,
        traceId = request.getAttribute(RequestLoggingFilter.TRACE_ID) as? String,
        requestId = request.getAttribute(RequestLoggingFilter.REQUEST_ID) as? String,
        durationMs = duration
    )
}