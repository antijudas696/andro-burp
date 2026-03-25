package com.proxy.tool.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proxy.tool.ui.components.GlassPanel
import com.proxy.tool.ui.theme.*

@Composable
fun RequestViewerScreen(
    request: HttpRequest,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top bar with back button
        TopAppBar(
            title = {
                Text(
                    text = "${request.method} ${request.path}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DarkSurface
            )
        )
        
        // Request and Response split view
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Request panel
            GlassPanel(
                modifier = Modifier.weight(1f)
            ) {
                RequestResponsePanel(
                    title = "Request",
                    request = request.rawRequest,
                    headers = request.headers,
                    body = request.body,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    isEditable = true
                )
            }
            
            // Response panel
            GlassPanel(
                modifier = Modifier.weight(1f)
            ) {
                RequestResponsePanel(
                    title = "Response",
                    request = request.rawResponse,
                    headers = emptyMap(),
                    body = "",
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    isEditable = false
                )
            }
        }
    }
}

@Composable
fun RequestResponsePanel(
    title: String,
    request: String,
    headers: Map<String, String>,
    body: String,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    isEditable: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TabItem(
                title = "Headers",
                isSelected = selectedTab == 0,
                onClick = { onTabSelected(0) }
            )
            TabItem(
                title = "Body",
                isSelected = selectedTab == 1,
                onClick = { onTabSelected(1) }
            )
            TabItem(
                title = "Raw",
                isSelected = selectedTab == 2,
                onClick = { onTabSelected(2) }
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Content
        when (selectedTab) {
            0 -> {
                LazyColumn {
                    items(headers.toList()) { (key, value) ->
                        Text(
                            text = "$key: $value",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
            1 -> {
                OutlinedTextField(
                    value = body,
                    onValueChange = {},
                    readOnly = !isEditable,
                    modifier = Modifier.fillMaxSize(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }
            2 -> {
                OutlinedTextField(
                    value = request,
                    onValueChange = {},
                    readOnly = !isEditable,
                    modifier = Modifier.fillMaxSize(),
                    minLines = 10,
                    maxLines = 20,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }
        }
    }
}