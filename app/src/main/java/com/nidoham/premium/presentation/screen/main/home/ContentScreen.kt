package com.nidoham.premium.presentation.screen.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nidoham.extractor.stream.StreamItem
import com.nidoham.extractor.util.KioskTranslator
import com.nidoham.premium.items.stream.ChannelRow
import com.nidoham.premium.items.stream.PlaylistCard
import com.nidoham.premium.items.stream.VideoCard
import com.nidoham.premium.presentation.viewmodel.main.home.ContentUiState
import com.nidoham.premium.presentation.viewmodel.main.home.ContentViewModel

/**
 * Displays the content feed for a single [kiosk] tab.
 *
 * Keying the [ContentViewModel] by [KioskTranslator.kioskId] ensures each tab
 * owns an independent ViewModel and fetch lifecycle.
 */
@Composable
fun ContentScreen(
    kiosk: KioskTranslator,
    modifier: Modifier = Modifier,
    viewModel: ContentViewModel = viewModel(key = kiosk.kioskId),
) {
    LaunchedEffect(kiosk) { viewModel.fetchKiosk(kiosk) }

    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is ContentUiState.Idle,
            ContentUiState.Loading -> ContentLoadingIndicator()

            is ContentUiState.Error -> ContentErrorView(
                message = state.message,
                onRetry = { viewModel.retry() },
            )

            is ContentUiState.Success -> {
                if (state.items.isEmpty()) {
                    ContentEmptyView(
                        categoryName = kiosk.displayName,
                        onRetry = { viewModel.fetchKiosk(kiosk, forceRefresh = true) },
                    )
                } else {
                    ContentList(
                        items = state.items,
                        isPaginating = state.isPaginating,
                        endReached = state.endReached,
                        onLoadMore = { viewModel.loadNextPage() },
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
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()

    // Trigger next page when within 2 items of the bottom.
    val shouldLoadMore by remember {
        derivedStateOf {
            val total = listState.layoutInfo.totalItemsCount
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            !isPaginating && !endReached && total > 0 && lastVisible >= total - 2
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            items = items,
            // Composite key guards against duplicate URLs from the API.
            key = { "${it.url}_${it.name.hashCode()}" },
        ) { item ->
            when (item.type) {
                StreamItem.ItemType.VIDEO    -> VideoCard(item)
                StreamItem.ItemType.CHANNEL  -> ChannelRow(item)
                StreamItem.ItemType.PLAYLIST -> PlaylistCard(item)
                else                         -> Unit
            }
        }

        if (isPaginating) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentLoadingIndicator() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ContentErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Oops!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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