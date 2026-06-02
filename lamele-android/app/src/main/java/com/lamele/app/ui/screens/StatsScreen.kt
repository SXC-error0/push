package com.lamele.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lamele.app.data.local.PoopRecordEntity
import com.lamele.app.domain.PaidPoopMath
import com.lamele.app.model.SmoothLevel
import com.lamele.app.ui.components.CuteKvp
import com.lamele.app.ui.components.CuteSectionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    records: List<PoopRecordEntity>,
    monthlySalary: Float,
    workDays: Int,
    workHours: Float,
    onBack: () -> Unit,
) {
    val smoothRate = if (records.isEmpty()) 0f else {
        records.count { it.smoothLevel == SmoothLevel.SMOOTH.name } * 100f / records.size
    }
    val paidTotal = PaidPoopMath.monthPaidTotalMillis(records, monthlySalary, workDays, workHours)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("数据统计") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CuteSectionCard(emoji = "📊", title = "概览") {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    CuteKvp(emoji = "📌", key = "总次数", value = "${records.size}")
                    CuteKvp(emoji = "💨", key = "顺畅率", value = "${"%.0f".format(smoothRate)}%")
                    CuteKvp(emoji = "💸", key = "本月带薪收益（估）", value = "¥${"%.2f".format(paidTotal)}")
                    Text(
                        "后续可接趋势图、形状分布等图表（先用文字把你哄住）。",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    )
                }
            }
        }
    }
}
