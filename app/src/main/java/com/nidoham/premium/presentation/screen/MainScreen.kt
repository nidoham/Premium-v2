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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.nidoham.premium.presentation.component.common.BottomNavigation
import com.nidoham.premium.presentation.component.common.TopBar
import com.nidoham.premium.presentation.screen.main.HomeScreen
import com.nidoham.premium.presentation.screen.main.LibraryScreen
import com.nidoham.premium.presentation.screen.main.SubscriptionsScreen

/** Bottom navigation destination descriptor. */
data class NavDestination(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasBadge: Boolean = false,
)

val navDestinations = listOf(
    NavDestination(
        label         = "Home",
        selectedIcon  = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    NavDestination(
        label          = "Subscriptions",
        selectedIcon   = Icons.Rounded.Subscriptions,
        unselectedIcon = Icons.Outlined.Subscriptions,
        hasBadge       = true,
    ),
    NavDestination(
        label          = "Library",
        selectedIcon   = Icons.Rounded.LibraryMusic,
        unselectedIcon = Icons.Outlined.LibraryMusic,
    ),
)

/**
 * Root screen that hosts the [TopBar], [BottomNavigation], and the three main
 * tabs: Home, Subscriptions, and Library.
 *
 * @param onSearchClick Forwarded to [TopBar]'s search action.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onSearchClick: () -> Unit = {}) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopBar(
                title         = navDestinations[selectedTab].label,
                onSearchClick = onSearchClick,
            )
        },
        bottomBar = {
            BottomNavigation(
                selectedTab   = selectedTab,
                onTabSelected = { selectedTab = it },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        MainContent(
            selectedTab   = selectedTab,
            paddingValues = paddingValues,
        )
    }
}

@Composable
private fun MainContent(
    selectedTab: Int,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background),
    ) {
        AnimatedContent(
            targetState    = selectedTab,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
            },
            label = "MainScreenTransition",
        ) { tab ->
            when (tab) {
                0 -> HomeScreen()
                1 -> SubscriptionsScreen()
                2 -> LibraryScreen()
            }
        }
    }
}