package com.proxy.tool.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proxy.tool.ui.components.GlassPanel
import com.proxy.tool.ui.components.PullToRefresh
import com.proxy.tool.ui.theme.*
import com.proxy.tool.ui.viewmodel.HttpHistoryViewModel

@Composable
fun HttpHistoryScreen(
    historyViewModel: HttpHistoryViewModel,
    onRequestSelected: (HttpRequest) -> Unit
) {
    val requests by historyViewModel.requests.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    
    PullToRefresh(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            historyViewModel.refreshHistory()
            isRefreshing = false
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Method", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Text("Host", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Text("Path", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Text("Status", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Text("Size", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }
            
            items(requests) { request ->
                RequestItem(
                    request = request,
                    onClick = { onRequestSelected(request) }
                )
            }
        }
    }
}

@Composable
fun RequestItem(
    request: HttpRequest,
    onClick: () -> Unit
) {
    GlassPanel(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = request.method,
                style = MaterialTheme.typography.bodyMedium,
                color = when (request.method) {
                    "GET" -> SuccessGreen
                    "POST" -> PrimaryBlue
                    "PUT" -> WarningYellow
                    "DELETE" -> ErrorRed
                    else -> TextSecondary
                }
            )
            Text(
                text = request.host.take(20),
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                maxLines = 1
            )
            Text(
                text = request.path.take(30),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                maxLines = 1
            )
            Text(
                text = request.statusCode.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (request.statusCode in 200..299) SuccessGreen
                else if (request.statusCode in 400..599) ErrorRed
                else TextSecondary
            )
            Text(
                text = formatSize(request.responseLength),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

private fun formatSize(bytes: Int): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> "${bytes / (1024 * 1024)} MB"
    }
}

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