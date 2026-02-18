package com.nidoham.premium.presentation.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nidoham.premium.ui.theme.GlassTheme

/**
 * Placeholder Library screen.
 */
@Composable
fun LibraryScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Your Library",
            style = GlassTheme.typography.headlineMedium,
            color = GlassTheme.colors.onSurface
        )
    }

}