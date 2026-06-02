package com.lamele.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Design system: deep forest green primary, warm cream background, amber secondary
val Primary = Color(0xFF126D27)
val PrimaryContainer = Color(0xFF66BB6A)
val PrimaryFixed = Color(0xFF9FF79F)
val OnPrimary = Color.White
val OnPrimaryContainer = Color(0xFF004814)

val Secondary = Color(0xFF835500)
val SecondaryContainer = Color(0xFFFEB64C)
val OnSecondary = Color.White
val OnSecondaryContainer = Color(0xFF704800)

val Tertiary = Color(0xFF006685)
val TertiaryContainer = Color(0xFF5EB3D8)
val OnTertiary = Color.White
val OnTertiaryContainer = Color(0xFF004358)

val Background = Color(0xFFFFF8F1)
val Surface = Color(0xFFFFF8F1)
val SurfaceContainerLow = Color(0xFFF9F3EB)
val SurfaceContainerHigh = Color(0xFFEEE7DF)
val SurfaceDim = Color(0xFFDFD9D1)

val OnBackground = Color(0xFF1E1B17)
val OnSurface = Color(0xFF1E1B17)
val OnSurfaceVariant = Color(0xFF40493E)
val Outline = Color(0xFF707A6D)
val OutlineVariant = Color(0xFFBFCABA)

val Error = Color(0xFFBA1A1A)
val ErrorContainer = Color(0xFFFFDAD6)

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainerHigh,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    error = Error,
    errorContainer = ErrorContainer,
    inversePrimary = Color(0xFF83DA85),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF83DA85),
    onPrimary = Color(0xFF002105),
    primaryContainer = Color(0xFF005318),
    onPrimaryContainer = Color(0xFF9FF79F),
    secondary = Color(0xFFFFB954),
    onSecondary = Color(0xFF452B00),
    secondaryContainer = Color(0xFF633F00),
    onSecondaryContainer = Color(0xFFFFDDB4),
    tertiary = Color(0xFF7ED1F7),
    background = Color(0xFF1A1C18),
    surface = Color(0xFF1A1C18),
    onBackground = Color(0xFFE3E3DC),
    onSurface = Color(0xFFE3E3DC),
    outline = Color(0xFF8A9387),
    outlineVariant = Color(0xFF40493E),
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
