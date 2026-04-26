package com.discroom.tv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = Color(0xFFD6AF57),
    secondary = Color(0xFF8DA9C4),
    background = Color(0xFF06080D),
    surface = Color(0xFF111827),
    onPrimary = Color(0xFF1A1A1A),
    onBackground = Color(0xFFF8F8F8)
)

private val LightScheme = lightColorScheme(
    primary = Color(0xFF8D5F00),
    background = Color(0xFFF2F4F8)
)

@Composable
fun DiscRoomTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkScheme else LightScheme,
        content = content
    )
}
