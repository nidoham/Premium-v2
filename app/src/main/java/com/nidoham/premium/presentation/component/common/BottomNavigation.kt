package com.nidoham.premium.presentation.component.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.nidoham.premium.presentation.screen.navDestinations
import com.nidoham.premium.ui.theme.GlassAlpha
import com.nidoham.premium.ui.theme.GlassTheme

/**
 * Clean bottom navigation bar with glass morphism styling (no blur).
 */
@Composable
fun BottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = GlassTheme.colors.background.luminance() < 0.5f

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
        containerColor = if (isDark) {
            GlassTheme.colors.surface.copy(alpha = GlassAlpha.REGULAR)
        } else {
            GlassTheme.colors.surface.copy(alpha = GlassAlpha.THICK)
        },
        tonalElevation = 0.dp
    ) {
        navDestinations.forEachIndexed { index, destination ->
            val isSelected = selectedTab == index

            NavigationBarItem(
                icon = {
                    if (destination.hasBadge && !isSelected) {
                        BadgedBox(
                            badge = { Badge(containerColor = GlassTheme.colors.error) }
                        ) {
                            Icon(
                                imageVector = if (isSelected) destination.selectedIcon
                                else destination.unselectedIcon,
                                contentDescription = destination.label
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) destination.selectedIcon
                            else destination.unselectedIcon,
                            contentDescription = destination.label
                        )
                    }
                },
                label = {
                    Text(
                        text = destination.label,
                        style = GlassTheme.typography.labelMedium
                    )
                },
                selected = isSelected,
                onClick = { onTabSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GlassTheme.colors.primary,
                    selectedTextColor = GlassTheme.colors.primary,
                    unselectedIconColor = GlassTheme.colors.onSurfaceVariant,
                    unselectedTextColor = GlassTheme.colors.onSurfaceVariant,
                    indicatorColor = GlassTheme.colors.primaryContainer.copy(
                        alpha = GlassAlpha.REGULAR
                    )
                ),
                alwaysShowLabel = true
            )
        }
    }
}