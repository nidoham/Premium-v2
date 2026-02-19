package com.nidoham.premium.items.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nidoham.extractor.stream.StreamItem
import com.nidoham.premium.ui.theme.GlassTheme
import com.nidoham.premium.util.TimeUtil.Companion.formatCount
import com.nidoham.premium.util.TimeUtil.Companion.formatDuration


@Composable
fun VideoCard(item: StreamItem) {
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