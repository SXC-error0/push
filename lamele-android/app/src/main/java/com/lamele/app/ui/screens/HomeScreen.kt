package com.lamele.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lamele.app.domain.FortuneSign
import com.lamele.app.ui.HomeUiState
import com.lamele.app.ui.components.CuteHeader
import com.lamele.app.ui.components.CuteKvp
import com.lamele.app.ui.components.CuteSectionCard

@Composable
fun HomeScreen(
    state: HomeUiState,
    onCheckIn: () -> Unit,
    onCalculator: () -> Unit,
    onAchievements: () -> Unit,
    onStats: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CuteHeader(
            emoji = "👋",
            title = "嘿，${state.nickname.ifBlank { "肠道旅人" }}",
            subtitle = if (state.todayCount == 0) "今天你释放了吗？要不要来一拉？" else "今日已释放 ${state.todayCount} 次，继续保持。",
        )

        CuteSectionCard(emoji = "🔮", title = "今日屎运签") {
            Text(FortuneSign.todayLine(), style = MaterialTheme.typography.bodyLarge)
        }

        CuteSectionCard(emoji = "🧨", title = "今日战报") {
            Column(Modifier.fillMaxWidth()) {
                CuteKvp(emoji = "🔥", key = "连续打卡", value = "${state.streak} 天")
                CuteKvp(emoji = "📦", key = "累计释放", value = "${state.totalCount} 次")
                CuteKvp(emoji = "🪙", key = "屎币", value = "${state.poopCoins}（打卡 +5）")
            }
        }

        Button(
            onClick = onCheckIn,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("今日一拉")
        }
        FilledTonalButton(
            onClick = onCalculator,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Default.AttachMoney, contentDescription = null)
            Text(" 带薪拉屎计算器", modifier = Modifier.padding(start = 8.dp))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedButton(
                onClick = onAchievements,
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Default.Star, contentDescription = null)
                Text(" 成就")
            }
            OutlinedButton(
                onClick = onStats,
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.Default.Map, contentDescription = null)
                Text(" 统计")
            }
        }
    }
}
