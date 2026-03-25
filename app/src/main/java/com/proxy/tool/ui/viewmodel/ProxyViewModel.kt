package com.proxy.tool.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proxy.tool.proxy.ProxyServer
import com.proxy.tool.proxy.models.HttpRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProxyViewModel : ViewModel() {
    private val _isProxyEnabled = MutableStateFlow(false)
    val isProxyEnabled: StateFlow<Boolean> = _isProxyEnabled.asStateFlow()
    
    private val _isInterceptEnabled = MutableStateFlow(false)
    val isInterceptEnabled: StateFlow<Boolean> = _isInterceptEnabled.asStateFlow()
    
    private val _interceptedRequests = MutableStateFlow<List<HttpRequest>>(emptyList())
    val interceptedRequests: StateFlow<List<HttpRequest>> = _interceptedRequests.asStateFlow()
    
    val proxyPort = 8080
    
    private val proxyServer = ProxyServer(port = proxyPort)
    
    fun toggleProxy() {
        viewModelScope.launch {
            if (_isProxyEnabled.value) {
                proxyServer.stop()
                _isProxyEnabled.value = false
            } else {
                proxyServer.start()
                _isProxyEnabled.value = true
            }
        }
    }
    
    fun toggleIntercept() {
        _isInterceptEnabled.value = !_isInterceptEnabled.value
        if (_isInterceptEnabled.value) {
            // Enable interception mode
            proxyServer.enableInterception()
        } else {
            proxyServer.disableInterception()
        }
    }
    
    fun forwardRequest(request: HttpRequest) {
        viewModelScope.launch {
            proxyServer.forwardRequest(request)
            _interceptedRequests.value = _interceptedRequests.value.filter { it.id != request.id }
        }
    }
    
    fun dropRequest(request: HttpRequest) {
        viewModelScope.launch {
            proxyServer.dropRequest(request)
            _interceptedRequests.value = _interceptedRequests.value.filter { it.id != request.id }
        }
    }
    
    fun editRequest(request: HttpRequest) {
        viewModelScope.launch {
            // Update request with modifications
            proxyServer.modifyRequest(request)
        }
    }
}