package com.proxy.tool.proxy.models

data class HttpRequest(
    val id: String,
    val method: String,
    val host: String,
    val path: String,
    val statusCode: Int,
    val responseLength: Int,
    val timestamp: Long,
    val headers: Map<String, String> = emptyMap(),
    val body: String = "",
    val rawRequest: String = "",
    val rawResponse: String = ""
)