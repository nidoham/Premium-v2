@file:Suppress("unused")
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

// ╔════════════════════════════════════════════════════════════════════╗
// ║  GLASS MORPHISM THEME ENGINE (2026)                                ║
// ║  Orchestrates Colors, Type, and Glass Physics across the UI.       ║
// ╚════════════════════════════════════════════════════════════════════╝

// ════════════════════════════════════════════════════════════════════
// COMPOSITION LOCALS
// ════════════════════════════════════════════════════════════════════

/**
 * Provides the current rendering specification for Glass surfaces.
 * This allows nested components (like a card inside a sidebar) to know
 * how much blur or noise they should apply.
 */
val LocalGlassSpec = staticCompositionLocalOf { GlassPresets.Widget }

/**
 * Provides the display calibration profile.
 * Used by low-level graphics components to adjust gamma for OLED/LCD.
 */
val LocalDisplayProfile = staticCompositionLocalOf { DisplayProfile.StandardLCD }

// ════════════════════════════════════════════════════════════════════
// SHAPES SYSTEM
// ════════════════════════════════════════════════════════════════════

/**
 * Modern "Squircle-like" rounding.
 * In 2026, interfaces use generous corner radii to feel organic and fluid.
 */
val GlassShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),    // Tags, tiny badges
    small      = RoundedCornerShape(12.dp),   // Buttons, inputs
    medium     = RoundedCornerShape(20.dp),   // Cards, small dialogs
    large      = RoundedCornerShape(32.dp),   // Bottom sheets, large modals
    extraLarge = RoundedCornerShape(48.dp)    // Full screen surfaces
)

// ════════════════════════════════════════════════════════════════════
// THEME ENTRY POINT
// ════════════════════════════════════════════════════════════════════

/**
 * **Admin Glass Theme**
 *
 * The root theme composable for the application.
 *
 * @param darkTheme Whether to use the Dark (OLED) or Light (Clean) scheme.
 * @param dynamicColor Whether to use Android 12+ Wallpaper extraction (Material You).
 *                     Defaults to false to preserve the custom Glass brand identity.
 * @param displayProfile Hardware calibration profile (default: OLED for modern phones).
 */
@Composable
fun PremiumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // specific brand identity preferred over dynamic
    displayProfile: DisplayProfile = if (darkTheme) DisplayProfile.OLED else DisplayProfile.StandardLCD,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current

    // 1. Resolve Color Scheme
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> GlassDarkScheme
        else -> GlassLightScheme
    }

    // 2. Handle System Bars (Edge-to-Edge Glass)
    if (!view.isInEditMode) {
        SideEffect {
            val window = (context as? Activity)?.window ?: return@SideEffect

            // Set bars to transparent so glass backgrounds show through
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            WindowCompat.getInsetsController(window, view).apply {
                // Dark Content (Icons) on Light Theme, Light Content on Dark Theme
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    // 3. Provide Theme & Custom Locals
    CompositionLocalProvider(
        LocalDisplayProfile provides displayProfile,
        LocalGlassSpec provides GlassPresets.Widget // Default spec
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = GlassTypography,
            shapes = GlassShapes,
            content = content
        )
    }
}

// ════════════════════════════════════════════════════════════════════
// THEME ACCESSOR OBJECT
// ════════════════════════════════════════════════════════════════════

/**
 * Convenient shorthand to access theme properties.
 * Usage: `GlassTheme.colors.primary` or `GlassTheme.spec.blurRadius`
 */
object GlassTheme {
    val colors: ColorScheme
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable @ReadOnlyComposable get() = MaterialTheme.shapes

    val spec: GlassSpec
        @Composable @ReadOnlyComposable get() = LocalGlassSpec.current

    val display: DisplayProfile
        @Composable @ReadOnlyComposable get() = LocalDisplayProfile.current
}

// ════════════════════════════════════════════════════════════════════
// SPECIALIZED SURFACE WRAPPERS
// ════════════════════════════════════════════════════════════════════

/**
 * Applies the specific visual physics for a **Side Navigation Drawer**.
 * Uses a medium blur and darker tint to separate from main content.
 */
@Composable
fun SidebarTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalGlassSpec provides GlassPresets.Widget.copy(blurRadius = 36f, saturationBoost = 1.22f)
    ) {
        content()
    }
}

/**
 * Applies the specific visual physics for a **Bottom Navigation Bar**.
 * High blur, low noise to ensure icon legibility.
 */
@Composable
fun NavigationBarTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalGlassSpec provides GlassPresets.NavigationBar
    ) {
        content()
    }
}

/**
 * Applies the specific visual physics for a **Modal/Dialog**.
 * Maximum blur to focus user attention on the foreground.
 */
@Composable
fun ModalTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalGlassSpec provides GlassPresets.Dialog
    ) {
        content()
    }
}