package com.nidoham.premium.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.nidoham.premium.presentation.component.common.BottomNavigation
import com.nidoham.premium.presentation.component.common.TopBar
import com.nidoham.premium.presentation.screen.main.HomeScreen
import com.nidoham.premium.presentation.screen.main.LibraryScreen
import com.nidoham.premium.presentation.screen.main.SubscriptionsScreen
import com.nidoham.premium.ui.theme.GlassTheme
import com.nidoham.premium.ui.theme.NavigationBarTheme

/**
 * Navigation destination configuration for bottom navigation items.
 */
data class NavDestination(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasBadge: Boolean = false
)

val navDestinations = listOf(
    NavDestination(
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    NavDestination(
        label = "Subscriptions",
        selectedIcon = Icons.Rounded.Subscriptions,
        unselectedIcon = Icons.Outlined.Subscriptions,
        hasBadge = true // Example: show badge for new content
    ),
    NavDestination(
        label = "Library",
        selectedIcon = Icons.Rounded.LibraryMusic,
        unselectedIcon = Icons.Outlined.LibraryMusic
    )
)

/**
 * Main application screen with glass morphism top bar and bottom navigation.
 * Features Home, Subscriptions, and Library tabs with animated content transitions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSearchClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // Apply navigation bar glass physics to bottom nav
    NavigationBarTheme {
        Scaffold(
            topBar = {
                TopBar(
                    title = navDestinations[selectedTab].label,
                    onSearchClick = onSearchClick
                )
            },
            bottomBar = {
                BottomNavigation(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            },
            containerColor = Color.Transparent // Let glass background show through
        ) { paddingValues ->
            MainContent(
                selectedTab = selectedTab,
                paddingValues = paddingValues
            )
        }
    }
}

/**
 * Animated content container for switching between main screens.
 */
@Composable
private fun MainContent(
    selectedTab: Int,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(GlassTheme.colors.background)
    ) {
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                        fadeOut(animationSpec = tween(300))
            },
            label = "ScreenTransition"
        ) { tab ->
            when (tab) {
                0 -> HomeScreen()
                1 -> SubscriptionsScreen()
                2 -> LibraryScreen()
            }
        }
    }
}