package core.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }

@Composable
fun ChefAiTheme(content: @Composable () -> Unit) {
    val systemIsDark = isSystemInDarkTheme()
    val isDarkState = remember { mutableStateOf(systemIsDark) }
    CompositionLocalProvider(LocalThemeIsDark provides isDarkState) {
        val isDark by isDarkState
        SystemAppearance(!isDark)
        content()
    }
}

@Composable
internal expect fun SystemAppearance(isDark: Boolean)