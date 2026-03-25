package com.proxy.tool.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.proxy.tool.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    proxyEnabled: Boolean,
    port: Int,
    onToggleProxy: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEnabled by remember { mutableStateOf(proxyEnabled) }
    
    LaunchedEffect(proxyEnabled) {
        isEnabled = proxyEnabled
    }
    
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.NetworkCheck,
                    contentDescription = "Proxy",
                    tint = if (isEnabled) PrimaryBlue else SecondaryGray
                )
                Text(
                    text = "Andro-Burp",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
            }
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    color = DarkSurfaceVariant,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.height(32.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "Port:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = port.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isEnabled) PrimaryBlue else TextPrimary
                        )
                    }
                }
                
                Switch(
                    checked = isEnabled,
                    onCheckedChange = {
                        isEnabled = it
                        onToggleProxy()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = PrimaryBlue,
                        checkedTrackColor = PrimaryBlue.copy(alpha = 0.5f),
                        uncheckedThumbColor = SecondaryGray,
                        uncheckedTrackColor = SecondaryGray.copy(alpha = 0.3f)
                    )
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkSurface,
            scrolledContainerColor = DarkSurface,
            titleContentColor = TextPrimary
        ),
        modifier = modifier.shadow(
            elevation = 2.dp,
            shape = MaterialTheme.shapes.medium,
            clip = false
        )
    )
}