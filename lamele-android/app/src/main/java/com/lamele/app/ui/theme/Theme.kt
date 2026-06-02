package com.lamele.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// PRD：米黄、奶油、薄荷绿、橙黄、浅蓝 — 轻松卡通
val Cream = Color(0xFFFFF8F0)
val WarmWhite = Color(0xFFFFFBF5)
val Mint = Color(0xFFA5D6A7)
val MintDark = Color(0xFF66BB6A)
val Apricot = Color(0xFFFFB74D)
val Sky = Color(0xFF81D4FA)
val TextPrimary = Color(0xFF4E342E)
val TextMuted = Color(0xFF8D6E63)

private val LightColors = lightColorScheme(
    primary = MintDark,
    onPrimary = Color.White,
    primaryContainer = Mint,
    secondary = Apricot,
    tertiary = Sky,
    background = Cream,
    surface = WarmWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

private val DarkColors = darkColorScheme(
    primary = Mint,
    onPrimary = TextPrimary,
    secondary = Apricot,
    background = Color(0xFF1E1E1C),
    surface = Color(0xFF2A2826),
    onBackground = WarmWhite,
    onSurface = WarmWhite,
)

@Composable
fun LameleTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = LameleTypography,
        content = content,
    )
}
