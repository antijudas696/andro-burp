package com.proxy.tool.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun PullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val refreshThreshold = with(LocalDensity.current) { 80.dp.toPx() }
    var pullDistance by remember { mutableStateOf(0f) }
    var refreshing by remember { mutableStateOf(isRefreshing) }
    
    LaunchedEffect(isRefreshing) {
        refreshing = isRefreshing
        if (!isRefreshing && pullDistance > 0) {
            pullDistance = 0f
        }
    }
    
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (refreshing) return Offset.Zero
                
                val delta = available.y
                if (delta < 0 && pullDistance > 0) {
                    pullDistance = (pullDistance + delta).coerceAtLeast(0f)
                    return Offset(0f, delta)
                }
                return Offset.Zero
            }
            
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (refreshing) return Offset.Zero
                
                val delta = available.y
                if (delta > 0 && pullDistance < refreshThreshold) {
                    pullDistance = (pullDistance + delta).coerceAtMost(refreshThreshold)
                    return Offset(0f, delta)
                }
                return Offset.Zero
            }
            
            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (refreshing) return Velocity.Zero
                
                if (pullDistance >= refreshThreshold && !refreshing) {
                    refreshing = true
                    onRefresh()
                    delay(1000)
                    refreshing = false
                    pullDistance = 0f
                } else {
                    pullDistance = 0f
                }
                return Velocity.Zero
            }
        }
    }
    
    Column(
        modifier = modifier.nestedScroll(nestedScrollConnection)
    ) {
        if (pullDistance > 0 || refreshing) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(LocalDensity.current) { pullDistance.toDp() })
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                val rotation by animateFloatAsState(
                    targetValue = if (pullDistance >= refreshThreshold) 180f else 0f,
                    animationSpec = tween(durationMillis = 200),
                    label = "refreshRotation"
                )
                
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation)
                )
            }
        }
        
        content()
    }
}