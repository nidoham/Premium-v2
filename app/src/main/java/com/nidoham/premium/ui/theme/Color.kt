
@file:Suppress("unused")
package com.nidoham.premium.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import kotlin.math.pow

// ╔════════════════════════════════════════════════════════════════════╗
// ║  GLASS MORPHISM COLOR SYSTEM (2026)                                ║
// ║  A high-fidelity, translucent color system optimized for OLED,     ║
// ║  Pro Display XDR, and Super Retina panels.                         ║
// ╚════════════════════════════════════════════════════════════════════╝

/**
 * **Base Primitive Palette**
 *
 * These are the raw, absolute color values used to generate the semantic theme.
 * Unlike Material 2, we do not use "Primary/Secondary" naming here.
 * Instead, we use literal names (Azure, Indigo, Midnight) with numeric tonal values.
 */
private object BasePalette {
    // ── Brand Colors (Electric Azure) ────────────────────────────────
    /** Standard System Blue. Used for primary actions in Light Mode. */
    val Azure500 = Color(0xFF007AFF)

    /** Vibrant Electric Blue. Used for gradients and accents. */
    val Azure400 = Color(0xFF2979FF)

    /** High-Contrast Sky Blue. Optimized for visibility against Dark Mode backgrounds. */
    val Azure300 = Color(0xFF4DA6FF)

    /** Soft Pastel Blue. Used for containers and tinting. */
    val Azure100 = Color(0xFFCCE5FF)

    // ── Secondary Colors (Deep Indigo) ───────────────────────────────
    /** Standard Indigo. Used for secondary actions/FABs. */
    val Indigo500 = Color(0xFF5856D6)

    /** Bright Violet. Used for text links in dark mode. */
    val Indigo300 = Color(0xFF857DFF)

    /** Pale Lavender. Used for secondary containers. */
    val Indigo100 = Color(0xFFE8E7FF)

    /** Deep Navy Purple. Used for text on light backgrounds. */
    val Indigo900 = Color(0xFF221F70)

    // ── Accent Colors (Cyan/Teal) ────────────────────────────────────
    val Cyan500 = Color(0xFF32ADE6)
    val Cyan300 = Color(0xFF6DD5FA)
    val Cyan900 = Color(0xFF004466)

    // ── Neutral System (Dark Mode / Night) ───────────────────────────
    /** Scrim / Modal Background. Pitch black with slight blue tint. */
    val Midnight900 = Color(0xFF040C1A)

    /** App Backdrop. The deepest layer of the app. */
    val Midnight800 = Color(0xFF0A1422)

    /** Main Background. Standard dark mode background (Navy, not Black). */
    val Midnight700 = Color(0xFF0F1B30)

    /** Surface Layer. Used for Cards and Sheets. */
    val Midnight600 = Color(0xFF162540)

    /** Glass Tint. Used to tint frosted glass layers. */
    val Midnight500 = Color(0xFF1E3A6E)

    // ── Neutral System (Light Mode / Day) ────────────────────────────
    /** Pure White. Surface layer. */
    val Frost100 = Color(0xFFFFFFFF)

    /** Off-White Blue. Main background. */
    val Frost200 = Color(0xFFF0F5FF)

    /** Pale Gray-Blue. Secondary background. */
    val Frost300 = Color(0xFFEBF2FF)

    /** Crisp Blue-White. High contrast surface. */
    val Frost400 = Color(0xFFD0E4FF)

    // ── Functional Signals ───────────────────────────────────────────
    val SignalRed    = Color(0xFFFF3B30)
    val SignalOrange = Color(0xFFFF9500)
    val SignalGreen  = Color(0xFF34C759)
}

/**
 * **Glass Alpha Constants**
 *
 * Standardized opacity values for creating depth without relying solely on shadows.
 * Use these values when defining `Color.copy(alpha = ...)` for custom components.
 */
object GlassAlpha {
    /** 7% - Barely visible. Used for subtle borders or inactive states. */
    const val ULTRA_THIN = 0.07f

    /** 15% - Watermark level. Used for disabled text or decorative patterns. */
    const val THIN       = 0.15f

    /** 26% - Standard Glass. Used for toolbars and floating buttons. */
    const val REGULAR    = 0.26f

    /** 44% - Frosted Glass. Used for Bottom Sheets and Modals. */
    const val THICK      = 0.44f

    /** 66% - Heavy Glass. Used for high-contrast overlays over busy backgrounds. */
    const val ULTRA_THICK = 0.66f
}

// ════════════════════════════════════════════════════════════════════
// SEMANTIC COLOR SCHEMES (MATERIAL 3)
// ════════════════════════════════════════════════════════════════════

/**
 * **Dark Theme Configuration**
 *
 * Targeted for OLED displays.
 * - **Backgrounds**: Deep Navy (not pure black) to reduce eye strain and smearing.
 * - **Primary**: Bright Azure (300) for maximum legibility.
 */
val GlassDarkScheme: ColorScheme = darkColorScheme(
    primary             = BasePalette.Azure300,
    onPrimary           = BasePalette.Midnight900,
    primaryContainer    = BasePalette.Azure500.copy(alpha = 0.30f),
    onPrimaryContainer  = BasePalette.Azure100,

    secondary           = BasePalette.Indigo300,
    onSecondary         = BasePalette.Midnight900,
    secondaryContainer  = BasePalette.Indigo900,
    onSecondaryContainer = BasePalette.Indigo100,

    tertiary            = BasePalette.Cyan300,
    onTertiary          = BasePalette.Midnight900,
    tertiaryContainer   = BasePalette.Cyan900,
    onTertiaryContainer = BasePalette.Cyan500,

    background          = BasePalette.Midnight700,
    onBackground        = BasePalette.Frost200,

    surface             = BasePalette.Midnight600,
    onSurface           = BasePalette.Frost200,
    surfaceVariant      = BasePalette.Midnight500.copy(alpha = 0.5f),
    onSurfaceVariant    = BasePalette.Azure100,

    outline             = BasePalette.Azure300.copy(alpha = GlassAlpha.REGULAR),
    outlineVariant      = BasePalette.Midnight500,

    error               = BasePalette.SignalRed.copy(alpha = 0.9f),
    onError             = Color.White,

    scrim               = BasePalette.Midnight900.copy(alpha = 0.80f)
)

/**
 * **Light Theme Configuration**
 *
 * Clean, clinical, and airy.
 * - **Tint**: Cool blue tint (no warm yellows) to feel modern and crisp.
 * - **Primary**: Standard System Blue (Azure 500).
 */
val GlassLightScheme: ColorScheme = lightColorScheme(
    primary             = BasePalette.Azure500,
    onPrimary           = Color.White,
    primaryContainer    = BasePalette.Azure100,
    onPrimaryContainer  = BasePalette.Midnight700,

    secondary           = BasePalette.Indigo500,
    onSecondary         = Color.White,
    secondaryContainer  = BasePalette.Indigo100,
    onSecondaryContainer = BasePalette.Indigo900,

    tertiary            = BasePalette.Cyan500,
    onTertiary          = Color.White,
    tertiaryContainer   = BasePalette.Frost400,
    onTertiaryContainer = BasePalette.Cyan900,

    background          = BasePalette.Frost200,
    onBackground        = BasePalette.Midnight800,

    surface             = BasePalette.Frost100,
    onSurface           = BasePalette.Midnight800,
    surfaceVariant      = BasePalette.Frost300,
    onSurfaceVariant    = BasePalette.Indigo500,

    outline             = BasePalette.Azure500.copy(alpha = GlassAlpha.REGULAR),
    outlineVariant      = BasePalette.Azure100,

    error               = BasePalette.SignalRed,
    onError             = Color.White,

    scrim               = BasePalette.Midnight800.copy(alpha = 0.38f)
)

// ════════════════════════════════════════════════════════════════════
// CUSTOM GLASS TOKENS & UTILITIES
// ════════════════════════════════════════════════════════════════════

/**
 * **Glass Specification Data Class**
 *
 * Holds rendering configuration for specific glass surfaces.
 */
data class GlassSpec(
    val blurRadius: Float,
    val noiseIntensity: Float,
    val saturationBoost: Float
)

/**
 * **Glass Presets**
 *
 * Pre-defined configurations for common UI components.
 */
object GlassPresets {
    /** Used for the bottom navigation bar or tab row. */
    val NavigationBar = GlassSpec(blurRadius = 32f, noiseIntensity = 0.01f, saturationBoost = 1.15f)

    /** Used for modal bottom sheets. High blur to separate content. */
    val BottomSheet   = GlassSpec(blurRadius = 48f, noiseIntensity = 0.02f, saturationBoost = 1.20f)

    /** Used for standard dialogs/popups. */
    val Dialog        = GlassSpec(blurRadius = 24f, noiseIntensity = 0.01f, saturationBoost = 1.10f)

    /** Used for floating cards or widgets. */
    val Widget        = GlassSpec(blurRadius = 20f, noiseIntensity = 0.01f, saturationBoost = 1.05f)
}

/**
 * **Glass Tokens**
 *
 * Semantic color tokens specifically for glassmorphism effects that don't fit
 * into the standard Material 3 slots.
 */
object GlassTokens {
    /**
     * The specific color for the Top App Bar background.
     * Uses a highly transparent, blurred version of the surface color.
     */
    val HeaderSurfaceDark = BasePalette.Midnight800.copy(alpha = 0.85f)
    val HeaderSurfaceLight = BasePalette.Frost100.copy(alpha = 0.85f)

    object Border {
        val Subtle = BasePalette.Azure500.copy(alpha = 0.15f)
        val Highlight = BasePalette.Azure300.copy(alpha = 0.40f)
    }
}

/**
 * **Display Profile Enumeration**
 *
 * Used to adjust color calculations based on the theoretical display technology.
 */
enum class DisplayProfile(val gamma: Float) {
    StandardLCD(2.2f),
    /** Optimized for infinite contrast ratios. */
    OLED(2.4f),
    /** Optimized for high dynamic range peak brightness. */
    ProXDR(2.2f)
}

// ════════════════════════════════════════════════════════════════════
// KOTLIN EXTENSIONS
// ════════════════════════════════════════════════════════════════════

/**
 * Applies gamma correction to a color to simulate how it renders on specific hardware.
 */
fun Color.applyDisplayProfile(profile: DisplayProfile): Color {
    val correction = 1f / profile.gamma
    return Color(
        red   = red.pow(correction).coerceIn(0f, 1f),
        green = green.pow(correction).coerceIn(0f, 1f),
        blue  = blue.pow(correction).coerceIn(0f, 1f),
        alpha = alpha
    )
}

/**
 * Calculates the resulting color when this color (transparent) is placed over a background.
 * Essential for predicting contrast ratios on glass surfaces.
 *
 * @param elevation Conceptual elevation (0-100) which increases opacity.
 * @param isDark Whether the underlying theme is dark mode.
 */
fun Color.compositeOnSurface(elevation: Float = 0f, isDark: Boolean = true): Color {
    val alphaStep = (elevation / 100f).coerceIn(0f, 0.25f)
    val tint = if (isDark) BasePalette.Azure300 else BasePalette.Frost100

    // Composite tint over base color, then tint+base over background (simulated)
    return tint.copy(alpha = alphaStep).compositeOver(this)
}