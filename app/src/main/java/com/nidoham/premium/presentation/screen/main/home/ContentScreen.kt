package com.nidoham.premium.presentation.screen.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Refresh
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
import com.nidoham.premium.presentation.viewmodel.main.home.ContentUiState
import com.nidoham.premium.presentation.viewmodel.main.home.ContentViewModel
import com.nidoham.premium.ui.theme.GlassTheme
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

// ─────────────────────────────────────────────
// UI Components
// ─────────────────────────────────────────────

@Composable
private fun VideoCard(item: StreamItem) {
    val thumbnail = item.thumbnails.maxByOrNull { it.height }?.url

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Navigate */ }
            .padding(bottom = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)) {
            AsyncImage(
                model = thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().background(GlassTheme.colors.surfaceVariant)
            )

            if (item.duration > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.duration.formatDuration(),
                        style = GlassTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(GlassTheme.colors.primary.copy(alpha = 0.2f))
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = GlassTheme.typography.bodyLarge,
                    color = GlassTheme.colors.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${item.uploaderName} • ${item.viewCount.formatCount()} views",
                    style = GlassTheme.typography.labelMedium,
                    color = GlassTheme.colors.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun ChannelRow(item: StreamItem) {
    val avatar = item.thumbnails.maxByOrNull { it.height }?.url
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = avatar,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(GlassTheme.colors.surfaceVariant),
            contentScale = ContentScale.Crop
        )
        Column {
            Text(item.name, style = GlassTheme.typography.titleMedium, color = GlassTheme.colors.onSurface)
            if (item.subscriberCount > 0) {
                Text(
                    "${item.subscriberCount.formatCount()} subs",
                    style = GlassTheme.typography.bodySmall,
                    color = GlassTheme.colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PlaylistCard(item: StreamItem) {
    val thumbnail = item.thumbnails.maxByOrNull { it.height }?.url
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(160.dp, 90.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(GlassTheme.colors.surfaceVariant)
        ) {
            AsyncImage(model = thumbnail, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Box(Modifier.align(Alignment.BottomEnd).background(Color.Black.copy(0.7f), RoundedCornerShape(4.dp)).padding(4.dp)) {
                Text(text = "Playlist", color = Color.White, style = GlassTheme.typography.labelSmall)
            }
        }
        Column {
            Text(item.name, style = GlassTheme.typography.titleSmall, color = GlassTheme.colors.onSurface)
            Text(item.uploaderName, style = GlassTheme.typography.bodySmall, color = GlassTheme.colors.onSurfaceVariant)
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

// ─────────────────────────────────────────────
// Extensions
// ─────────────────────────────────────────────

private fun Long.formatDuration(): String {
    if (this <= 0) return ""
    val h = this / 3600
    val m = (this % 3600) / 60
    val s = this % 60
    return if (h > 0) String.format(Locale.US, "%d:%02d:%02d", h, m, s)
    else String.format(Locale.US, "%d:%02d", m, s)
}

private fun Long.formatCount(): String = when {
    this >= 1_000_000_000 -> String.format(Locale.US, "%.1fB", this / 1_000_000_000.0)
    this >= 1_000_000     -> String.format(Locale.US, "%.1fM", this / 1_000_000.0)
    this >= 1_000         -> String.format(Locale.US, "%.1fK", this / 1_000.0)
    else                  -> this.toString()
}