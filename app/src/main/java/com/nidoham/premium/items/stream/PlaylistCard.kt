package com.nidoham.premium.items.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nidoham.extractor.stream.StreamItem
import com.nidoham.premium.ui.theme.GlassTheme


@Composable
fun PlaylistCard(item: StreamItem) {
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