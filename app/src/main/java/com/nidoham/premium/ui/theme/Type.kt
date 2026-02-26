@file:Suppress("unused")

package com.nidoham.premium.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp

// ╔════════════════════════════════════════════════════════════════════╗
// ║  GLASS MORPHISM TYPOGRAPHY SYSTEM (2026)                           ║
// ║  Clean, geometric, and legible type optimized for translucent UI.  ║
// ╚════════════════════════════════════════════════════════════════════╝

// ── FONT CONFIGURATION ───────────────────────────────────────────────

/**
 * Primary Font Family.
 *
 * Defaults to [FontFamily.SansSerif] (San Francisco on iOS / Roboto on Android)
 * to ensure native "Apple-like" rendering and zero compilation errors.
 */
val GlassFontFamily: FontFamily = FontFamily.SansSerif

/**
 * Ensures text is vertically centered within its line height.
 * Crucial for aligning text perfectly inside glass buttons and chips.
 */
private val CenteredLineHeight: LineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

// ════════════════════════════════════════════════════════════════════
// MATERIAL 3 TYPOGRAPHY HIERARCHY
// ════════════════════════════════════════════════════════════════════

val AppTypography: Typography = Typography(
    // ── Display: Large Hero Text ─────────────────────────────────────
    displayLarge = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
        lineHeightStyle = CenteredLineHeight
    ),
    displayMedium = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = CenteredLineHeight
    ),
    displaySmall = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = CenteredLineHeight
    ),

    // ── Headline: Section Headers ────────────────────────────────────
    headlineLarge = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = CenteredLineHeight
    ),
    headlineMedium = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = CenteredLineHeight
    ),
    headlineSmall = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = CenteredLineHeight
    ),

    // ── Title: Card Titles & Subsections ─────────────────────────────
    titleLarge = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.SemiBold, // Slightly heavier for glass legibility
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = CenteredLineHeight
    ),
    titleMedium = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        lineHeightStyle = CenteredLineHeight
    ),
    titleSmall = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        lineHeightStyle = CenteredLineHeight
    ),

    // ── Body: Long Form Content ──────────────────────────────────────
    bodyLarge = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = CenteredLineHeight
    ),
    bodyMedium = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        lineHeightStyle = CenteredLineHeight
    ),
    bodySmall = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
        lineHeightStyle = CenteredLineHeight
    ),

    // ── Label: Buttons, Chips, Captions ──────────────────────────────
    labelLarge = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        lineHeightStyle = CenteredLineHeight
    ),
    labelMedium = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = CenteredLineHeight
    ),
    labelSmall = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = CenteredLineHeight
    )
)

// ════════════════════════════════════════════════════════════════════
// CUSTOM TYPOGRAPHY TOKENS
// ════════════════════════════════════════════════════════════════════

/**
 * Specialized text styles that exist outside the standard Material 3 hierarchy.
 * Used for unique dashboard elements, stats, and decorative text.
 */
object GlassTypeTokens {

    /**
     * Massive text for Dashboard Statistics (e.g., "85%").
     * Uses tight spacing and heavy weight.
     */
    val StatHero: TextStyle = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 64.sp,
        lineHeight = 64.sp,
        letterSpacing = (-2).sp,
        lineHeightStyle = CenteredLineHeight
    )

    /**
     * Secondary stats (e.g., chart values).
     */
    val StatMedium: TextStyle = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-1).sp,
        lineHeightStyle = CenteredLineHeight
    )

    /**
     * Monospace font for Order IDs, Transaction Hashes, or API keys.
     * Ensures characters like '0' and 'O' are distinguishable.
     */
    val MonoKey: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = CenteredLineHeight
    )

    /**
     * Ultra-wide uppercase for section dividers or subtle background labels.
     * (e.g., "TODAY", "RECENT TRANSACTIONS")
     */
    val SectionHeaderCaps: TextStyle = TextStyle(
        fontFamily = GlassFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.5.sp, // Wide tracking
        lineHeightStyle = CenteredLineHeight
    )
}