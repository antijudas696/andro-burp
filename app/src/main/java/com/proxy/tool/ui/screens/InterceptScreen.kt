package com.proxy.tool.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proxy.tool.ui.components.GlassPanel
import com.proxy.tool.ui.theme.*
import com.proxy.tool.ui.viewmodel.HttpHistoryViewModel
import com.proxy.tool.ui.viewmodel.ProxyViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun InterceptScreen(
    proxyViewModel: ProxyViewModel,
    historyViewModel: HttpHistoryViewModel
) {
    val isInterceptEnabled by proxyViewModel.isInterceptEnabled.collectAsState()
    val interceptedRequests by proxyViewModel.interceptedRequests.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Intercept toggle section
        GlassPanel(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Intercept",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    Text(
                        text = if (isInterceptEnabled) 
                            "Requests will be paused for inspection" 
                        else 
                            "Enable to intercept requests",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
                
                Switch(
                    checked = isInterceptEnabled,
                    onCheckedChange = { proxyViewModel.toggleIntercept() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = PrimaryBlue,
                        checkedTrackColor = PrimaryBlue.copy(alpha = 0.5f),
                        uncheckedThumbColor = SecondaryGray,
                        uncheckedTrackColor = SecondaryGray.copy(alpha = 0.3f)
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Intercepted requests list
        if (isInterceptEnabled) {
            AnimatedContent(
                targetState = interceptedRequests.isEmpty(),
                transitionSpec = {
                    fadeIn() + slideInVertically() togetherWith 
                    fadeOut() + slideOutVertically()
                },
                label = "interceptContent"
            ) { isEmpty ->
                if (isEmpty) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No intercepted requests",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(interceptedRequests) { request ->
                            InterceptedRequestItem(
                                request = request,
                                onForward = { proxyViewModel.forwardRequest(request) },
                                onDrop = { proxyViewModel.dropRequest(request) },
                                onEdit = { proxyViewModel.editRequest(request) }
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Intercept is disabled",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun InterceptedRequestItem(
    request: HttpRequest,
    onForward: () -> Unit,
    onDrop: () -> Unit,
    onEdit: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    GlassPanel(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${request.method} ${request.path}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryBlue
                    )
                    Text(
                        text = request.host,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onEdit,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkSurfaceVariant
                        ),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Edit", style = MaterialTheme.typography.labelLarge)
                    }
                    
                    Button(
                        onClick = onForward,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SuccessGreen
                        ),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Forward", style = MaterialTheme.typography.labelLarge)
                    }
                    
                    Button(
                        onClick = onDrop,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ErrorRed
                        ),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Drop", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
            
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Divider(color = DividerColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = request.rawRequest.take(200),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 5
                    )
                }
            }
            
            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = if (expanded) "Show less" else "Show details",
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryBlue
                )
            }
        }
    }
}