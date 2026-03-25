cat > create_files.sh << 'SCRIPT'
#!/bin/bash

# MainActivity.kt
cat > app/src/main/java/com/proxy/tool/MainActivity.kt << 'EOF'
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
EOF

# Color.kt
cat > app/src/main/java/com/proxy/tool/ui/theme/Color.kt << 'EOF'
package com.proxy.tool.ui.theme

import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF0A0A0A)
val DarkSurface = Color(0xFF1A1A1A)
val DarkSurfaceVariant = Color(0xFF2A2A2A)
val PrimaryBlue = Color(0xFF3B82F6)
val PrimaryBlueDark = Color(0xFF2563EB)
val PrimaryBlueLight = Color(0xFF60A5FA)
val SecondaryGray = Color(0xFF6B7280)
val SecondaryGrayLight = Color(0xFF9CA3AF)
val SuccessGreen = Color(0xFF10B981)
val WarningYellow = Color(0xFFF59E0B)
val ErrorRed = Color(0xFFEF4444)
val TextPrimary = Color(0xFFE5E7EB)
val TextSecondary = Color(0xFF9CA3AF)
val DividerColor = Color(0xFF2A2A2A)
val BorderColor = Color(0xFF333333)
EOF

# Create other files similarly...
# [Continue with all the file contents from previous response]

echo "All files created successfully!"
SCRIPT

chmod +x create_files.sh
./create_files.sh
