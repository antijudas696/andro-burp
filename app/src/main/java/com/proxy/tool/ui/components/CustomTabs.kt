package com.proxy.tool.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.proxy.tool.ui.theme.*

@Composable
fun CustomTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Intercept", "HTTP History", "Repeater")
    val indicatorColor = PrimaryBlue
    
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(horizontal = 16.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                TabItem(
                    title = title,
                    isSelected = selectedTab == index,
                    onClick = { onTabSelected(index) }
                )
            }
        }
        
        // Animated indicator
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(DarkSurface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f / tabs.size)
                        .height(2.dp)
                        .offset(x = (selectedTab * LocalDensity.current.run { 1.dp.roundToPx() }).dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(PrimaryBlue, PrimaryBlueLight)
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tabAnimation"
    )
    
    Surface(
        modifier = Modifier
            .weight(1f)
            .padding(vertical = 12.dp),
        color = Color.Transparent,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) PrimaryBlue else TextSecondary,
                modifier = Modifier.alpha(0.9f + animationProgress * 0.1f)
            )
        }
    }
}