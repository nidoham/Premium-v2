package com.nidoham.premium.presentation.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nidoham.premium.ui.theme.GlassAlpha
import com.nidoham.premium.ui.theme.GlassTheme

/**
 * Clean glass morphism top app bar without blur color artifacts.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = GlassTheme.colors.background.luminance() < 0.5f

    // Clean semi-transparent surface without gradient blur
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = GlassTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = GlassTheme.colors.onBackground
                )
            },
            actions = {
                // Search icon button with subtle background
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            color = GlassTheme.colors.background.copy(alpha = GlassAlpha.THIN)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = GlassTheme.colors.primary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}