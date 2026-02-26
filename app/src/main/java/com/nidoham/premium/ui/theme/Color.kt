package com.nidoham.premium.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ── Primitive Palette ────────────────────────────────────────────────

private object Palette {
    // Blue (Primary)
    val Blue500  = Color(0xFF007AFF)
    val Blue300  = Color(0xFF4DA6FF)
    val Blue100  = Color(0xFFCCE5FF)

    // Indigo (Secondary)
    val Indigo500 = Color(0xFF5856D6)
    val Indigo300 = Color(0xFF857DFF)
    val Indigo100 = Color(0xFFE8E7FF)
    val Indigo900 = Color(0xFF221F70)

    // Cyan (Tertiary)
    val Cyan500  = Color(0xFF32ADE6)
    val Cyan300  = Color(0xFF6DD5FA)
    val Cyan900  = Color(0xFF004466)

    // Dark Neutrals
    val Dark900  = Color(0xFF040C1A)
    val Dark800  = Color(0xFF0A1422)
    val Dark700  = Color(0xFF0F1B30)
    val Dark600  = Color(0xFF162540)
    val Dark500  = Color(0xFF1E3A6E)

    // Light Neutrals
    val White    = Color(0xFFFFFFFF)
    val Gray50   = Color(0xFFF0F5FF)
    val Gray100  = Color(0xFFEBF2FF)
    val Gray200  = Color(0xFFD0E4FF)

    // Signals
    val Red      = Color(0xFFFF3B30)
    val Green    = Color(0xFF34C759)
}

// ── Dark Color Scheme ────────────────────────────────────────────────

val DarkColorScheme: ColorScheme = darkColorScheme(
    primary              = Palette.Blue300,
    onPrimary            = Palette.Dark900,
    primaryContainer     = Palette.Dark500,
    onPrimaryContainer   = Palette.Blue100,

    secondary            = Palette.Indigo300,
    onSecondary          = Palette.Dark900,
    secondaryContainer   = Palette.Indigo900,
    onSecondaryContainer = Palette.Indigo100,

    tertiary             = Palette.Cyan300,
    onTertiary           = Palette.Dark900,
    tertiaryContainer    = Palette.Cyan900,
    onTertiaryContainer  = Palette.Cyan500,

    background           = Palette.Dark700,
    onBackground         = Palette.Gray50,

    surface              = Palette.Dark600,
    onSurface            = Palette.Gray50,
    surfaceVariant       = Palette.Dark500,
    onSurfaceVariant     = Palette.Blue100,

    outline              = Palette.Dark500,
    outlineVariant       = Palette.Dark600,

    error                = Palette.Red,
    onError              = Palette.White,

    scrim                = Palette.Dark900.copy(alpha = 0.80f)
)

// ── Light Color Scheme ───────────────────────────────────────────────

val LightColorScheme: ColorScheme = lightColorScheme(
    primary              = Palette.Blue500,
    onPrimary            = Palette.White,
    primaryContainer     = Palette.Blue100,
    onPrimaryContainer   = Palette.Dark700,

    secondary            = Palette.Indigo500,
    onSecondary          = Palette.White,
    secondaryContainer   = Palette.Indigo100,
    onSecondaryContainer = Palette.Indigo900,

    tertiary             = Palette.Cyan500,
    onTertiary           = Palette.White,
    tertiaryContainer    = Palette.Gray200,
    onTertiaryContainer  = Palette.Cyan900,

    background           = Palette.Gray50,
    onBackground         = Palette.Dark800,

    surface              = Palette.White,
    onSurface            = Palette.Dark800,
    surfaceVariant       = Palette.Gray100,
    onSurfaceVariant     = Palette.Indigo500,

    outline              = Palette.Gray200,
    outlineVariant       = Palette.Blue100,

    error                = Palette.Red,
    onError              = Palette.White,

    scrim                = Palette.Dark800.copy(alpha = 0.38f)
)

// ── High Contrast Overrides ──────────────────────────────────────────
// Increased foreground/background separation for accessibility.

val DarkHighContrastColorScheme: ColorScheme = DarkColorScheme.copy(
    primary            = Color(0xFF82CFFF),
    onPrimary          = Color(0xFF000000),
    primaryContainer   = Color(0xFF004C73),
    onPrimaryContainer = Color(0xFFCCEEFF),
    background         = Color(0xFF000000),
    onBackground       = Color(0xFFFFFFFF),
    surface            = Color(0xFF0A0A0A),
    onSurface          = Color(0xFFFFFFFF),
    outline            = Color(0xFFAAAAAA),
)

val LightHighContrastColorScheme: ColorScheme = LightColorScheme.copy(
    primary            = Color(0xFF003A70),
    onPrimary          = Color(0xFFFFFFFF),
    primaryContainer   = Color(0xFF004C9E),
    onPrimaryContainer = Color(0xFFFFFFFF),
    background         = Color(0xFFFFFFFF),
    onBackground       = Color(0xFF000000),
    surface            = Color(0xFFFFFFFF),
    onSurface          = Color(0xFF000000),
    outline            = Color(0xFF333333),
)