package com.proxy.tool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proxy.tool.ui.components.CustomTabs
import com.proxy.tool.ui.components.TopBar
import com.proxy.tool.ui.screens.HttpHistoryScreen
import com.proxy.tool.ui.screens.InterceptScreen
import com.proxy.tool.ui.screens.RepeaterScreen
import com.proxy.tool.ui.theme.AndroBurpTheme
import com.proxy.tool.ui.viewmodel.HttpHistoryViewModel
import com.proxy.tool.ui.viewmodel.ProxyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroBurpTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AndroBurpApp()
                }
            }
        }
    }
}

@Composable
fun AndroBurpApp() {
    val proxyViewModel: ProxyViewModel = viewModel()
    val historyViewModel: HttpHistoryViewModel = viewModel()
    var selectedTab by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(
            proxyEnabled = proxyViewModel.isProxyEnabled,
            port = proxyViewModel.proxyPort,
            onToggleProxy = { proxyViewModel.toggleProxy() }
        )
        
        CustomTabs(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
        
        when (selectedTab) {
            0 -> InterceptScreen(
                proxyViewModel = proxyViewModel,
                historyViewModel = historyViewModel
            )
            1 -> HttpHistoryScreen(
                historyViewModel = historyViewModel,
                onRequestSelected = { request ->
                    // Navigate to request viewer
                }
            )
            2 -> RepeaterScreen(historyViewModel = historyViewModel)
        }
    }
}
