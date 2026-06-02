package com.lamele.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lamele.app.ui.HomeUiState
import com.lamele.app.ui.components.CuteHeader
import com.lamele.app.ui.components.CuteKvp
import com.lamele.app.ui.components.CuteSectionCard

@Composable
fun ProfileScreen(
    home: HomeUiState,
    title: String,
    weekCount: Int,
    monthPaid: Float,
    onCalculator: () -> Unit,
    onAchievements: () -> Unit,
    onStats: () -> Unit,
    onPrivacy: () -> Unit,
    onClearData: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        CuteHeader(
            emoji = "🧑‍🚀",
            title = home.nickname.ifBlank { "肠道旅人" },
            subtitle = "称号：$title",
        )

        val monthPaidStr = "%.2f".format(monthPaid)
        CuteSectionCard(emoji = "📦", title = "你的战绩") {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CuteKvp(emoji = "🧷", key = "总打卡", value = "${home.totalCount} 次")
                CuteKvp(emoji = "🔥", key = "连续", value = "${home.streak} 天")
                CuteKvp(emoji = "🗓️", key = "本周", value = "$weekCount 次")
                CuteKvp(
                    emoji = "💸",
                    key = "本月带薪（估）",
                    value = "¥$monthPaidStr",
                )
                CuteKvp(emoji = "🪙", key = "屎币", value = "${home.poopCoins}")
            }
        }

        FilledTonalButton(onClick = onCalculator, modifier = Modifier.fillMaxWidth()) {
            Text("🧮 带薪拉屎计算器")
        }
        OutlinedButton(onClick = onAchievements, modifier = Modifier.fillMaxWidth()) {
            Text("🏆 成就墙")
        }
        OutlinedButton(onClick = onStats, modifier = Modifier.fillMaxWidth()) {
            Text("📊 数据统计")
        }
        OutlinedButton(onClick = onPrivacy, modifier = Modifier.fillMaxWidth()) {
            Text("🔒 隐私设置")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onClearData,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("🧨 清空本地记录（调试）")
        }
    }
}
