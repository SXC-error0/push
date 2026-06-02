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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamele.app.data.local.PoopRecordEntity
import com.lamele.app.domain.PaidPoopMath
import com.lamele.app.model.SmoothLevel
import com.lamele.app.ui.components.StickerCard

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
    val paidRecords = records.count { it.isPaidPoop }
    val avgDuration = if (records.isEmpty()) 0f else records.sumOf { it.durationMinutes }.toFloat() / records.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("数据统计", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Overview stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MiniStatItem("📌", "总记录", "${records.size} 次", modifier = Modifier.weight(1f))
                MiniStatItem("💨", "顺畅率", "${"%.0f".format(smoothRate)}%", modifier = Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MiniStatItem("⏱️", "平均时长", "${"%.1f".format(avgDuration)} 分", modifier = Modifier.weight(1f))
                MiniStatItem("💸", "带薪次数", "$paidRecords 次", modifier = Modifier.weight(1f))
            }

            // Smoothness bar
            StickerCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("🌿", fontSize = 18.sp)
                        Text(
                            "顺畅率",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        )
                    }
                    Text(
                        "${"%.0f".format(smoothRate)}%",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { (smoothRate / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            // Paid earnings
            StickerCard(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.15f),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("💸", fontSize = 18.sp)
                            Text(
                                "本月带薪收益（估）",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                        Text(
                            "¥${"%.2f".format(paidTotal)}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                            ),
                        )
                    }
                    Text("💰", fontSize = 32.sp)
                }
            }

            // Placeholder for future charts
            StickerCard(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                withShadow = false,
            ) {
                Text(
                    "📈 趋势图、形状分布等图表即将上线~\n先用文字把你哄住，开发中 🚧",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
        }
    }
}

@Composable
private fun MiniStatItem(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(emoji, fontSize = 20.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
