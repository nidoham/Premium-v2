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
import androidx.compose.material3.MaterialTheme
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
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val categories = remember { KioskTranslator.entries }
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()

    val categoryBarHeight = 56.dp
    val density = LocalDensity.current
    val categoryBarHeightPx = with(density) { categoryBarHeight.toPx() }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(categoryBarHeightPx) {
        scrollBehavior.state.heightOffsetLimit = -categoryBarHeightPx
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(categoryBarHeight)
                    .offset {
                        IntOffset(x = 0, y = scrollBehavior.state.heightOffset.roundToInt())
                    }
                    .background(MaterialTheme.colorScheme.background)
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
        val topPadding = innerPadding.calculateTopPadding() +
                with(density) { scrollBehavior.state.heightOffset.toDp() }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding),
        ) { pageIndex ->
            ContentScreen(
                kiosk = categories[pageIndex],
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun CategoryBar(
    categories: List<KioskTranslator>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(selectedIndex) {
        listState.animateScrollToItem(index = selectedIndex, scrollOffset = -150)
    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        itemsIndexed(categories) { index, kiosk ->
            val isSelected = index == selectedIndex
            val colors = MaterialTheme.colorScheme

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) colors.primary
                        else colors.surfaceVariant
                    )
                    .clickable { onCategorySelected(index) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = kiosk.displayName,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) colors.onPrimary else colors.onSurfaceVariant,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                )
            }
        }
    }
}