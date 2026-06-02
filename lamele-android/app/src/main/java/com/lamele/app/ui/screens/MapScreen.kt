package com.lamele.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.lamele.app.data.local.PoopRecordEntity
import com.lamele.app.data.local.ToiletEntity
import com.lamele.app.model.SceneType
import com.lamele.app.ui.components.CuteHeader
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            CuteHeader(
                emoji = "🗺️",
                title = "屎迹地图 · OpenStreetMap",
                subtitle = "城市为近似锚点；厕所需在档案中填写经纬度以精确定位（OSM 开源开放数据）。",
            )
        }
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
                            snippet = "约 $n 次记录（城市近似位）"
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
        if (cityGroups.isEmpty() && toilets.none { it.latitude != null }) {
            item { Text("还没有可标定的城市或带坐标的厕所，先去打卡或新增厕所档案～") }
        } else {
            items(cityGroups) { (city, n) ->
                Card {
                    Column(Modifier.padding(16.dp)) {
                        Text(city, style = MaterialTheme.typography.titleMedium)
                        Text("打卡 $n 次")
                    }
                }
            }
        }
        item {
            Text("场景分布", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
        }
        items(sceneCounts.entries.toList()) { (scene, count) ->
            Text("$scene：$count 次")
        }
    }
}

/** 同城多次聚合到稳定但非真实的地图坐标，仅供娱乐展示 */
private fun approxGeoForCity(city: String): GeoPoint {
    val h = city.hashCode().toLong()
    val lat = 26.0 + (h and 0x3FFL) / 1024.0 * 16.0
    val lon = 98.0 + ((h shr 10) and 0x3FFL) / 1024.0 * 22.0
    return GeoPoint(lat, lon)
}
