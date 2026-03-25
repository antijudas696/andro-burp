package com.proxy.tool.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.proxy.tool.ui.components.GlassPanel
import com.proxy.tool.ui.theme.*
import com.proxy.tool.ui.viewmodel.HttpHistoryViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RepeaterScreen(
    historyViewModel: HttpHistoryViewModel
) {
    var url by remember { mutableStateOf("") }
    var method by remember { mutableStateOf("GET") }
    var requestBody by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Request section
        GlassPanel(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Request",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                
                // Method selector and URL
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Method dropdown
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it }
                        ) {
                            OutlinedTextField(
                                value = method,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .weight(0.3f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBlue,
                                    unfocusedBorderColor = BorderColor,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                listOf("GET", "POST", "PUT", "DELETE", "PATCH").forEach { m ->
                                    DropdownMenuItem(
                                        text = { Text(m) },
                                        onClick = {
                                            method = m
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    OutlinedTextField(
                        value = url,
                        onValueChange = { url = it },
                        placeholder = { Text("https://api.example.com/endpoint") },
                        modifier = Modifier.weight(0.7f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = BorderColor,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedPlaceholderColor = TextSecondary,
                            unfocusedPlaceholderColor = TextSecondary
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        singleLine = true
                    )
                }
                
                // Request body
                if (method in listOf("POST", "PUT", "PATCH")) {
                    OutlinedTextField(
                        value = requestBody,
                        onValueChange = { requestBody = it },
                        placeholder = { Text("Request body (JSON, form data, etc.)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        maxLines = 8,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = BorderColor,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
                
                // Send button
                Button(
                    onClick = {
                        isSending = true
                        // Simulate sending request
                        historyViewModel.sendRequest(url, method, requestBody) { result ->
                            response = result
                            isSending = false
                        }
                    },
                    enabled = !isSending && url.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue
                    )
                ) {
                    if (isSending) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = TextPrimary
                        )
                    } else {
                        Text("Send Request")
                    }
                }
            }
        }
        
        // Response section
        GlassPanel(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Response",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Response tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TabItem(
                        title = "Raw",
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    TabItem(
                        title = "Preview",
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Response content
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        fadeIn() + slideInHorizontally() togetherWith 
                        fadeOut() + slideOutHorizontally()
                    },
                    label = "responseTab"
                ) { tab ->
                    when (tab) {
                        0 -> {
                            LazyColumn {
                                item {
                                    Text(
                                        text = response.ifEmpty { "No response yet" },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }
                        1 -> {
                            // Preview for JSON/HTML would go here
                            Text(
                                text = "Response preview",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}