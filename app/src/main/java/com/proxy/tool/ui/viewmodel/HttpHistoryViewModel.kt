package com.proxy.tool.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proxy.tool.proxy.models.HttpRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HttpHistoryViewModel : ViewModel() {
    private val _requests = MutableStateFlow<List<HttpRequest>>(emptyList())
    val requests: StateFlow<List<HttpRequest>> = _requests.asStateFlow()
    
    init {
        // Load mock data for demo
        loadMockData()
    }
    
    fun refreshHistory() {
        viewModelScope.launch {
            // Simulate network delay
            delay(1000)
            loadMockData()
        }
    }
    
    fun sendRequest(url: String, method: String, body: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            // Simulate sending request
            delay(1000)
            val response = """
                HTTP/1.1 200 OK
                Content-Type: application/json
                
                {
                  "status": "success",
                  "message": "Request sent to $url",
                  "method": "$method"
                }
            """.trimIndent()
            
            // Add to history
            val newRequest = HttpRequest(
                id = System.currentTimeMillis().toString(),
                method = method,
                host = url.split("/")[2],
                path = "/" + url.split("/").drop(3).joinToString("/"),
                statusCode = 200,
                responseLength = response.length,
                timestamp = System.currentTimeMillis(),
                rawRequest = "$method $url\n\n$body",
                rawResponse = response
            )
            _requests.value = listOf(newRequest) + _requests.value
            
            onResult(response)
        }
    }
    
    private fun loadMockData() {
        val mockRequests = listOf(
            HttpRequest(
                id = "1",
                method = "GET",
                host = "api.example.com",
                path = "/users",
                statusCode = 200,
                responseLength = 1024,
                timestamp = System.currentTimeMillis(),
                rawRequest = "GET /users HTTP/1.1\nHost: api.example.com",
                rawResponse = "HTTP/1.1 200 OK\nContent-Length: 1024"
            ),
            HttpRequest(
                id = "2",
                method = "POST",
                host = "api.example.com",
                path = "/users",
                statusCode = 201,
                responseLength = 512,
                timestamp = System.currentTimeMillis() - 60000,
                rawRequest = "POST /users HTTP/1.1\nHost: api.example.com\n\n{\"name\":\"John\"}",
                rawResponse = "HTTP/1.1 201 Created"
            ),
            HttpRequest(
                id = "3",
                method = "GET",
                host = "jsonplaceholder.typicode.com",
                path = "/posts/1",
                statusCode = 200,
                responseLength = 2048,
                timestamp = System.currentTimeMillis() - 120000,
                rawRequest = "GET /posts/1 HTTP/1.1",
                rawResponse = "HTTP/1.1 200 OK\n\n{\"id\":1,\"title\":\"Sample\"}"
            )
        )
        _requests.value = mockRequests
    }
}