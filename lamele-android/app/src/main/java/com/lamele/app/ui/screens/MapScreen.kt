package com.lamele.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.lamele.app.data.local.PoopRecordEntity
import com.lamele.app.data.local.ToiletEntity
import com.lamele.app.model.SceneType
import com.lamele.app.ui.components.LameleTopBar
import com.lamele.app.ui.components.StickerCard
import com.lamele.app.ui.components.StatPill
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(
    records: List<PoopRecordEntity>,
    toilets: List<ToiletEntity>,
) {
    val cityGroups = records
        .filter { !it.city.isNullOrBlank() }
        .groupBy { it.city!!.trim() }
        .mapValues { (_, v) -> v.size }
        .toList()
        .sortedByDescending { it.second }

    val sceneCounts = records.groupingBy { it.sceneType }.eachCount().mapKeys { (k, _) ->
        runCatching { enumValueOf<SceneType>(k) }.getOrNull()?.label ?: k
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LameleTopBar()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            // Stats pills
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    StatPill(emoji = "🗺️", label = "已点亮 ${cityGroups.size} 城市", modifier = Modifier.weight(1f))
                    StatPill(emoji = "📍", label = "打卡 ${records.size} 次", modifier = Modifier.weight(1f))
                }
            }

            // Map view
            item {
                AndroidView(
                    factory = { ctx ->
                        MapView(ctx).apply {
                            setDestroyMode(false)
                            setTileSource(TileSourceFactory.MAPNIK)
                            setMultiTouchControls(true)
                            controller.setZoom(5.5)
                            controller.setCenter(GeoPoint(33.5, 106.0))
                        }
                    },
                    update = { map ->
                        map.onResume()
                        map.overlays.clear()
                        toilets.filter { it.latitude != null && it.longitude != null }.forEach { t ->
                            val mk = Marker(map).apply {
                                position = GeoPoint(t.latitude!!, t.longitude!!)
                                title = "🚾 ${t.name}"
                                snippet = listOfNotNull(t.city, t.alias).joinToString(" · ")
                            }
                            map.overlays.add(mk)
                        }
                        cityGroups.forEach { (city, n) ->
                            val mk = Marker(map).apply {
                                position = approxGeoForCity(city)
                                title = "📍 $city"
                                snippet = "约 $n 次记录"
                            }
                            map.overlays.add(mk)
                        }
                        map.invalidate()
                    },
                    onRelease = { map ->
                        map.onPause()
                        map.onDetach()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                )
            }

            // Footer pull card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                    ) {
                        // Drag handle
                        Surface(
                            modifier = Modifier
                                .height(4.dp)
                                .fillMaxWidth(0.3f)
                                .align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.outlineVariant,
                        ) {}
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "足迹历程",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                        )
                    }
                }
            }

            if (cityGroups.isEmpty() && toilets.none { it.latitude != null }) {
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp),
                    ) {
                        StickerCard(withShadow = false) {
                            Text(
                                "🗺️ 还没有可标定的城市或带坐标的厕所，先去打卡或新增厕所档案～",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            } else {
                items(cityGroups) { (city, n) ->
                    LocationItem(
                        emoji = cityEmoji(city),
                        title = city,
                        subtitle = "打卡 $n 次",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    )
                }
            }

            if (sceneCounts.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "场景分布",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
                items(sceneCounts.entries.toList()) { (scene, count) ->
                    LocationItem(
                        emoji = "🎭",
                        title = scene,
                        subtitle = "$count 次",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    )
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun LocationItem(
    emoji: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
            ) {
                Text(emoji, modifier = Modifier.padding(10.dp), fontSize = 20.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private fun cityEmoji(city: String): String {
    return when {
        city.contains("上海") -> "🌆"
        city.contains("北京") -> "🏛️"
        city.contains("广州") || city.contains("深圳") -> "🌴"
        city.contains("杭州") -> "🍵"
        city.contains("成都") -> "🐼"
        else -> "🏙️"
    }
}

private fun approxGeoForCity(city: String): GeoPoint {
    val h = city.hashCode().toLong()
    val lat = 26.0 + (h and 0x3FFL) / 1024.0 * 16.0
    val lon = 98.0 + ((h shr 10) and 0x3FFL) / 1024.0 * 22.0
    return GeoPoint(lat, lon)
}
