package com.nidoham.premium.presentation.screen.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nidoham.extractor.stream.StreamItem
import com.nidoham.extractor.util.KioskTranslator
import com.nidoham.premium.items.stream.ChannelRow
import com.nidoham.premium.items.stream.PlaylistCard
import com.nidoham.premium.items.stream.VideoCard
import com.nidoham.premium.presentation.viewmodel.main.home.ContentUiState
import com.nidoham.premium.presentation.viewmodel.main.home.ContentViewModel
import com.nidoham.premium.ui.theme.GlassTheme
import com.nidoham.premium.util.TimeUtil.Companion.formatCount
import java.util.Locale

@Composable
fun ContentScreen(
    kiosk: KioskTranslator,
    modifier: Modifier = Modifier,
    // Keying by kioskId ensures we get a fresh ViewModel for each tab
    viewModel: ContentViewModel = viewModel(key = kiosk.kioskId)
) {
    // Initial fetch
    LaunchedEffect(kiosk) {
        viewModel.fetchKiosk(kiosk)
    }

    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is ContentUiState.Idle, ContentUiState.Loading -> {
                ContentLoadingIndicator()
            }

            is ContentUiState.Error -> {
                ContentErrorView(
                    message = state.message,
                    onRetry = { viewModel.retry() }
                )
            }

            is ContentUiState.Success -> {
                if (state.items.isEmpty()) {
                    ContentEmptyView(
                        categoryName = kiosk.displayName,
                        onRetry = { viewModel.fetchKiosk(kiosk, forceRefresh = true) }
                    )
                } else {
                    ContentList(
                        items = state.items,
                        isPaginating = state.isPaginating,
                        endReached = state.endReached,
                        onLoadMore = { viewModel.loadNextPage() }
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentList(
    items: List<StreamItem>,
    isPaginating: Boolean,
    endReached: Boolean,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    // Infinite Scroll Logic
    // We trigger load when we are 2 items away from the bottom
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // Criteria: Not currently loading, not reached end, list has content, user scrolled near bottom
            !isPaginating && !endReached && totalItems > 0 && (lastVisibleItemIndex >= totalItems - 2)
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        // Bottom padding ensures the last item isn't hidden behind the nav bar
        contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = items,
            // Use composite key to avoid crashes if API returns duplicates
            key = { "${it.url}_${it.name.hashCode()}" }
        ) { item ->
            when (item.type) {
                StreamItem.ItemType.VIDEO -> VideoCard(item)
                StreamItem.ItemType.CHANNEL -> ChannelRow(item)
                StreamItem.ItemType.PLAYLIST -> PlaylistCard(item)
                else -> Unit
            }
        }

        // Pagination Loader at the bottom
        if (isPaginating) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = GlassTheme.colors.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentLoadingIndicator() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = GlassTheme.colors.primary)
    }
}

@Composable
private fun ContentErrorView(message: String, onRetry: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Oops!", style = GlassTheme.typography.titleMedium, color = GlassTheme.colors.error)
        Text(message, style = GlassTheme.typography.bodyMedium, color = GlassTheme.colors.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@Composable
private fun ContentEmptyView(categoryName: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No content found in $categoryName")
            Button(onClick = onRetry) { Text("Refresh") }
        }
    }
}