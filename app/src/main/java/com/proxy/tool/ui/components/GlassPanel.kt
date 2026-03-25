package com.proxy.tool.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.proxy.tool.ui.theme.*

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    elevation: Int = 4,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false
            ),
        shape = RoundedCornerShape(12.dp),
        color = DarkSurface.copy(alpha = 0.85f),
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.05f),
                            Color.White.copy(alpha = 0f)
                        )
                    )
                )
                .clip(RoundedCornerShape(12.dp))
        ) {
            content()
        }
    }
}

@Composable
fun FrostedPanel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .blur(4.dp),
        color = DarkSurfaceVariant.copy(alpha = 0.7f)
    ) {
        content()
    }
}