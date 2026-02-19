package com.nidoham.premium.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.nidoham.extractor.util.KioskTranslator
import com.nidoham.premium.presentation.screen.main.home.ContentScreen
import com.nidoham.premium.ui.theme.GlassTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val categories = remember { KioskTranslator.entries }
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()

    // 1. Define exact height for the Category Bar
    val categoryBarHeight = 56.dp
    val density = LocalDensity.current
    val categoryBarHeightPx = with(density) { categoryBarHeight.toPx() }

    // 2. Setup Scroll Behavior (EnterAlways = Hides on scroll down, shows on scroll up)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // 3. IMPORTANT: Tell the behavior strictly how tall the item to hide is.
    // This ensures the scroll stops exactly when the bar is hidden.
    LaunchedEffect(categoryBarHeightPx) {
        scrollBehavior.state.heightOffsetLimit = -categoryBarHeightPx
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            // This connects the scroll gestures from the list to the top bar
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            // 4. The Category Bar Container
            // We apply the offset here. When scrolling down, heightOffset goes from 0 to -56dp
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(categoryBarHeight)
                    .offset {
                        IntOffset(x = 0, y = scrollBehavior.state.heightOffset.roundToInt())
                    }
                    .background(GlassTheme.colors.surface) // Matches your app background
            ) {
                CategoryBar(
                    categories = categories,
                    selectedIndex = pagerState.currentPage,
                    onCategorySelected = { index ->
                        scope.launch { pagerState.animateScrollToPage(index) }
                    }
                )
            }
        }
    ) { innerPadding ->
        // 5. The Content Pager
        // We calculate the top padding dynamically.
        // innerPadding.calculateTopPadding() gives us 56dp.
        // scrollBehavior.state.heightOffset gives us (0 to -56dp).
        // Result: When hidden, topPadding becomes 0dp, and the list fills the screen.
        val topPadding = innerPadding.calculateTopPadding() +
                with(density) { scrollBehavior.state.heightOffset.toDp() }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding), // Dynamic padding creates the "Big Screen" effect
            userScrollEnabled = true
        ) { pageIndex ->
            ContentScreen(
                kiosk = categories[pageIndex],
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun CategoryBar(
    categories: List<KioskTranslator>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit
) {
    val listState = rememberLazyListState()

    // Auto-scroll logic
    LaunchedEffect(selectedIndex) {
        listState.animateScrollToItem(
            index = selectedIndex,
            scrollOffset = -150
        )
    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp), // Spacing between chips
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(categories) { index, kiosk ->
            val isSelected = index == selectedIndex

            // Design matches your screenshot: Rounded Rectangle Chips
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) GlassTheme.colors.onSurface
                        else GlassTheme.colors.surfaceVariant.copy(alpha = 0.3f) // Darker gray for unselected
                    )
                    .clickable { onCategorySelected(index) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = kiosk.displayName,
                    style = GlassTheme.typography.labelLarge,
                    color = if (isSelected) GlassTheme.colors.surface else GlassTheme.colors.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}