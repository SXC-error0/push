package com.lamele.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lamele.app.data.local.PoopRecordEntity
import com.lamele.app.model.AmountLevel
import com.lamele.app.model.ShapeType
import com.lamele.app.ui.components.CuteHeader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    records: List<PoopRecordEntity>,
    onOpen: (Long) -> Unit,
) {
    val fmt = remember { SimpleDateFormat("MM-dd HH:mm", Locale.CHINA) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            CuteHeader(
                emoji = "🧾",
                title = "历史记录",
                subtitle = "每一次释放都带编号：点卡片看屎评。"
            )
        }
        items(records, key = { it.id }) { r ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpen(r.id) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text(fmt.format(Date(r.timeMillis)), style = MaterialTheme.typography.titleSmall)
                    Text(
                        "${enumOrRaw<AmountLevel>(r.amountLevel) { it.label }} · ${enumOrRaw<ShapeType>(r.shapeType) { it.label }}${if (r.isPaidPoop) " · 💸带薪" else ""}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

private inline fun <reified T : Enum<T>> enumOrRaw(name: String, label: (T) -> String): String {
    return try {        
        label(enumValueOf<T>(name))
    } catch (_: Exception) {
        name
    }
}
