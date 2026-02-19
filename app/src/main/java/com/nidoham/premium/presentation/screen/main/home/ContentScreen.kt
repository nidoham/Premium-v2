package com.nidoham.premium.presentation.screen.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    // Key ensures a specific ViewModel instance for this Kiosk ID
    viewModel: ContentViewModel = viewModel(key = kiosk.kioskId)
) {
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
                    ContentList(items = state.items)
                }
            }
        }
    }
}

@Composable
private fun ContentList(items: List<StreamItem>) {
    LazyColumn(
        // Add bottom padding for Navigation Bar / FAB safety
        contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = items,
            key = { "${it.url}_${it.name.hashCode()}" }
        ) { item ->
            when (item.type) {
                StreamItem.ItemType.VIDEO -> VideoCard(item)
                else -> Unit
            }
        }
    }
}

// ... (Rest of the Card Components: VideoCard, ChannelRow, PlaylistCard - Same as previous best version)

// ─────────────────────────────────────────────
// VIDEO Card (Included for completeness of fix)
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

// ─────────────────────────────────────────────
// Helper Views
// ─────────────────────────────────────────────

@Composable
private fun ContentLoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = GlassTheme.colors.primary)
    }
}

@Composable
private fun ContentErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Oops!", style = GlassTheme.typography.titleMedium)
        Text(message, style = GlassTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@Composable
private fun ContentEmptyView(categoryName: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No content in $categoryName")
        Button(onClick = onRetry) { Text("Refresh") }
    }
}

// Helper Extensions
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