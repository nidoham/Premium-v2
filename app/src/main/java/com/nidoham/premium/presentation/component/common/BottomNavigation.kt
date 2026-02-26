package com.nidoham.premium.presentation.component.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nidoham.premium.presentation.screen.navDestinations

/**
 * Bottom navigation bar styled with [MaterialTheme] color tokens.
 *
 * @param selectedTab   Index of the currently active destination.
 * @param onTabSelected Callback invoked with the tapped destination index.
 */
@Composable
fun BottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    ) {
        navDestinations.forEachIndexed { index, destination ->
            val isSelected = selectedTab == index

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    if (destination.hasBadge && !isSelected) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = MaterialTheme.colorScheme.error)
                            }
                        ) {
                            Icon(
                                imageVector = destination.unselectedIcon,
                                contentDescription = destination.label,
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) destination.selectedIcon
                            else destination.unselectedIcon,
                            contentDescription = destination.label,
                        )
                    }
                },
                label = {
                    Text(
                        text = destination.label,
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = MaterialTheme.colorScheme.primary,
                    selectedTextColor   = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor      = MaterialTheme.colorScheme.primaryContainer,
                ),
                alwaysShowLabel = true,
            )
        }
    }
}