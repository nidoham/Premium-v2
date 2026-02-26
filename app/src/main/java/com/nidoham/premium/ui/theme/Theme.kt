package com.nidoham.premium.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

// ── Shapes ───────────────────────────────────────────────────────────

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small      = RoundedCornerShape(10.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

// ── CompositionLocal ──────────────────────────────────────────────────

/**
 * Provides the full [AppSettings] snapshot to the entire composition tree.
 *
 * Usage anywhere in the tree:
 * ```kotlin
 * val settings = LocalAppSettings.current
 * if (settings.reducedMotion) { ... }
 * ```
 */
val LocalAppSettings = staticCompositionLocalOf { AppSettings() }

// ── Theme Entry Point ─────────────────────────────────────────────────

/**
 * Root theme composable. Wire it to [ThemeManager] in your Activity:
 *
 * ```kotlin
 * val settings by themeManager.settings.collectAsStateWithLifecycle()
 * PremiumTheme(settings = settings) { AppNavGraph() }
 * ```
 *
 * @param settings     Full settings snapshot from [ThemeManager].
 * @param systemIsDark Pass [isSystemInDarkTheme()] (default handles most cases).
 */
@Composable
fun PremiumTheme(
    settings: AppSettings = AppSettings(),
    systemIsDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view    = LocalView.current
    val isDark  = settings.resolveIsDark(systemIsDark)

    val colorScheme: ColorScheme = when {
        settings.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        isDark -> if (settings.useHighContrast) DarkHighContrastColorScheme  else DarkColorScheme
        else   -> if (settings.useHighContrast) LightHighContrastColorScheme else LightColorScheme
    }

    val typography = if (settings.fontScale == FontScale.NORMAL) {
        AppTypography
    } else {
        AppTypography.scale(settings.fontScale.multiplier)
    }

    if (!view.isInEditMode) {
        SideEffect {
            val window = (context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor     = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars     = !isDark
                isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    CompositionLocalProvider(LocalAppSettings provides settings) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = typography,
            shapes      = AppShapes,
            content     = content
        )
    }
}

// ── AppTheme Accessor ────────────────────────────────────────────────

/**
 * Shorthand for accessing theme tokens and app settings inside composables.
 *
 * ```kotlin
 * Text(color = AppTheme.colors.primary)
 * val isCompact = AppTheme.settings.compactLayout
 * ```
 */
object AppTheme {
    val colors: ColorScheme
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable @ReadOnlyComposable get() = MaterialTheme.shapes

    val settings: AppSettings
        @Composable @ReadOnlyComposable get() = LocalAppSettings.current

    /** `true` when the effective theme is dark (luminance-based). */
    val isDark: Boolean
        @Composable @ReadOnlyComposable get() =
            MaterialTheme.colorScheme.background.luminance() < 0.5f
}

// ── Typography Scaling ────────────────────────────────────────────────

/**
 * Returns a copy of this [Typography] with every `fontSize` and `lineHeight`
 * multiplied by [factor], honouring the user's [FontScale] preference.
 */
private fun Typography.scale(factor: Float): Typography = copy(
    displayLarge   = displayLarge.copy(
        fontSize   = displayLarge.fontSize   * factor,
        lineHeight = displayLarge.lineHeight * factor),
    displayMedium  = displayMedium.copy(
        fontSize   = displayMedium.fontSize  * factor,
        lineHeight = displayMedium.lineHeight * factor),
    displaySmall   = displaySmall.copy(
        fontSize   = displaySmall.fontSize   * factor,
        lineHeight = displaySmall.lineHeight * factor),
    headlineLarge  = headlineLarge.copy(
        fontSize   = headlineLarge.fontSize  * factor,
        lineHeight = headlineLarge.lineHeight * factor),
    headlineMedium = headlineMedium.copy(
        fontSize   = headlineMedium.fontSize * factor,
        lineHeight = headlineMedium.lineHeight * factor),
    headlineSmall  = headlineSmall.copy(
        fontSize   = headlineSmall.fontSize  * factor,
        lineHeight = headlineSmall.lineHeight * factor),
    titleLarge     = titleLarge.copy(
        fontSize   = titleLarge.fontSize     * factor,
        lineHeight = titleLarge.lineHeight   * factor),
    titleMedium    = titleMedium.copy(
        fontSize   = titleMedium.fontSize    * factor,
        lineHeight = titleMedium.lineHeight  * factor),
    titleSmall     = titleSmall.copy(
        fontSize   = titleSmall.fontSize     * factor,
        lineHeight = titleSmall.lineHeight   * factor),
    bodyLarge      = bodyLarge.copy(
        fontSize   = bodyLarge.fontSize      * factor,
        lineHeight = bodyLarge.lineHeight    * factor),
    bodyMedium     = bodyMedium.copy(
        fontSize   = bodyMedium.fontSize     * factor,
        lineHeight = bodyMedium.lineHeight   * factor),
    bodySmall      = bodySmall.copy(
        fontSize   = bodySmall.fontSize      * factor,
        lineHeight = bodySmall.lineHeight    * factor),
    labelLarge     = labelLarge.copy(
        fontSize   = labelLarge.fontSize     * factor,
        lineHeight = labelLarge.lineHeight   * factor),
    labelMedium    = labelMedium.copy(
        fontSize   = labelMedium.fontSize    * factor,
        lineHeight = labelMedium.lineHeight  * factor),
    labelSmall     = labelSmall.copy(
        fontSize   = labelSmall.fontSize     * factor,
        lineHeight = labelSmall.lineHeight   * factor),
)

// ── Luminance helper ──────────────────────────────────────────────────

private fun Color.luminance(): Float =
    (0.2126 * red + 0.7152 * green + 0.0722 * blue).toFloat()