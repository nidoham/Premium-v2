package com.nidoham.premium.presentation.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nidoham.premium.ui.theme.GlassTheme

/**
 * Placeholder Subscriptions screen.
 */
@Composable
fun SubscriptionsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Subscriptions",
            style = GlassTheme.typography.headlineMedium,
            color = GlassTheme.colors.onSurface
        )

    }
}