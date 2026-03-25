package com.proxy.tool.proxy

import com.proxy.tool.proxy.models.HttpRequest

class Interceptor {
    private val interceptedRequests = mutableListOf<HttpRequest>()
    
    fun interceptRequest(request: HttpRequest) {
        interceptedRequests.add(request)
        // Notify UI about intercepted request
    }
    
    fun modifyRequest(request: HttpRequest, modifications: Map<String, String>): HttpRequest {
        // Apply modifications to request
        return request.copy(
            headers = request.headers + modifications
        )
    }
    
    fun getInterceptedRequests(): List<HttpRequest> {
        return interceptedRequests.toList()
    }
}