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
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamele.app.domain.FortuneSign
import com.lamele.app.ui.HomeUiState
import com.lamele.app.ui.components.LameleTopBar
import com.lamele.app.ui.components.StickerCard
import com.lamele.app.ui.components.StatPill
import com.lamele.app.ui.components.hardShadow

@Composable
fun HomeScreen(
    state: HomeUiState,
    onCheckIn: () -> Unit,
    onCalculator: () -> Unit,
    onAchievements: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LameleTopBar(onSettings = onSettings)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Greeting header
            Column {
                Text(
                    text = if (state.nickname.isBlank()) "嘿，肠道旅人 👋" else "嘿，${state.nickname} 👋",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = if (state.todayCount == 0) "今天你释放了吗？要不要来一拉？"
                    else "今日已释放 ${state.todayCount} 次，继续保持 💪",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Stats pills row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StatPill(emoji = "🔥", label = "${state.streak} 天连击", modifier = Modifier.weight(1f))
                StatPill(emoji = "📦", label = "${state.totalCount} 次累计", modifier = Modifier.weight(1f))
                StatPill(emoji = "🪙", label = "${state.poopCoins} 屎币", modifier = Modifier.weight(1f))
            }

            // Fortune banner
            StickerCard(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f),
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text("🔮", fontSize = 28.sp)
                    Column {
                        Text(
                            "今日屎运签",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            FortuneSign.todayLine(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = FontStyle.Italic,
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }

            // Today's battle report
            StickerCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            "🧨 今日战报",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.height(4.dp))
                        if (state.todayCount == 0) {
                            Text(
                                "今天还没有战报，出发！",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        } else {
                            Text(
                                "已成功释放 ${state.todayCount} 次",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                    ) {
                        Text(
                            if (state.todayCount == 0) "还没拉" else "已打卡",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // Main CTA button
            Button(
                onClick = onCheckIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .hardShadow(MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Text(
                    "💩  今日一拉",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    fontSize = 18.sp,
                )
            }

            // Secondary actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onCalculator,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
                ) {
                    Text("💰 计算器", style = MaterialTheme.typography.labelLarge)
                }
                OutlinedButton(
                    onClick = onAchievements,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
                ) {
                    Text("🏆 成就墙", style = MaterialTheme.typography.labelLarge)
                }
            }

            // History button
            OutlinedButton(
                onClick = onHistory,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
            ) {
                Icon(Icons.Default.History, contentDescription = null)
                Text(
                    "  历史记录",
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            // Decorative footer quote
            Text(
                "\"上班可以忍，屎不能憋。\n公司欠你的，从厕所拿回来。\"",
                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )
        }
    }
}
