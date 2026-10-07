/*package com.example.mobile_cue_things.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val DarkNavy = Color(0xFF041628)
val DeepTeal = Color(0xFF072B40)
val SurfaceBlue = Color(0xFF0E3A52)
val OnSurfaceLight = Color(0xFFE6F7FF)
val OnSurfaceMuted = Color(0xFF9FC7D8)

val AccentCyan = Color(0xFF00E5FF)
val AccentMagenta = Color(0xFFB53BFF)
val AccentPurple = Color(0xFF6C56FF)
val AccentGold = Color(0xFFFFB86B)
val DisabledDark = Color(0xFF1B3B4D)

val DarkColors = darkColorScheme(
    primary = AccentCyan,
    secondary = AccentMagenta,
    tertiary = AccentPurple,
    background = DarkNavy,
    surface = SurfaceBlue,
    onPrimary = DarkNavy,
    onBackground = OnSurfaceLight,
    onSurface = OnSurfaceLight,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MobilecuethingsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

 */

package com.example.mobile_cue_things.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// -------------------------------------------------
// Palette
// -------------------------------------------------

val Dark = Color(0x000000)
val DarkNavy = Color(0xFF041628)
val DeepTeal = Color(0xFF072B40)
val SurfaceBlue = Color(0xFF0E3A52)
val OnSurfaceLight = Color(0xFFE6F7FF)
val OnSurfaceMuted = Color(0xFF9FC7D8)

val AccentCyan = Color(0xFF00E5FF)
val AccentMagenta = Color(0xFFB53BFF)
val AccentPurple = Color(0xFF6C56FF)
val AccentGold = Color(0xFFFFB86B)
val DisabledDark = Color(0xFF1B3B4D)
val blueIcons = Color(0x0044d0)
// -------------------------------------------------
// Dark / Light ColorSchemes (Material3)
// -------------------------------------------------
private val DarkColors = darkColorScheme(
    primary = Dark,
   // onPrimary = DarkNavy,
    onPrimary = Color.White,
    secondary = AccentMagenta,
    tertiary = AccentPurple,
    onSecondary = Color.White,
    background = DarkNavy,
    onBackground = OnSurfaceLight,
    surface = SurfaceBlue,
    onSurface = OnSurfaceLight,
    error = Color(0xFFEF5350),
    onError = Color.White
)

private val LightColors = lightColorScheme(
    primary = Color.White,
    onPrimary = DarkNavy,
    secondary = Dark,
    tertiary = AccentPurple,
    background = Color.White,

    // very light cyan-ish background for light mode
    onBackground = Color(0xFF0F1720),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F1720),
    error = Color(0xFFB00020),
    onError = Color.White
)

// -------------------------------------------------
// Theme Composable
// -------------------------------------------------
@Composable
fun MobilecuethingsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    // Make the status bar match the theme background and set icon color accordingly
    if (!view.isInEditMode) {
        SideEffect {
            val window = (context as Activity).window
            // set status bar color to a darker background from the color scheme
            window.statusBarColor = colorScheme.background.toArgb()
            // control status bar content (icons) contrast: true -> dark icons; false -> light icons
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // usa tu Typography definido en el módulo
        content = content
    )
}
