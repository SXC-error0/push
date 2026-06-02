package com.lamele.app.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamele.app.data.local.PoopRecordEntity
import com.lamele.app.domain.PaidPoopMath
import com.lamele.app.model.AmountLevel
import com.lamele.app.model.ShapeType
import com.lamele.app.model.SmoothLevel
import com.lamele.app.model.SceneType
import com.lamele.app.ui.AppViewModel
import com.lamele.app.ui.components.StickerCard
import com.lamele.app.ui.components.hardShadow

@Composable
fun SuccessScreen(
    recordId: Long,
    viewModel: AppViewModel,
    onHome: () -> Unit,
    onAgain: () -> Unit,
) {
    val context = LocalContext.current
    var entity by remember { mutableStateOf<PoopRecordEntity?>(null) }
    LaunchedEffect(recordId) {
        entity = viewModel.fetchRecord(recordId)
    }
    val e = entity ?: run {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("加载中…", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    val earn = if (e.isPaidPoop) {
        PaidPoopMath.sessionEarnings(
            e.durationMinutes,
            viewModel.monthlySalary.value,
            viewModel.workDays.value,
            viewModel.workHours.value,
        )
    } else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 40.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // Celebration emoji
        Text("🎉", fontSize = 64.sp)

        // Headline
        Text(
            "恭喜你，成功卸下人生负担",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )

        // Stats 2x2 grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StickerCard(withShadow = false) {
                    Text(
                        "时长",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        "${e.durationMinutes} 分钟",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                StickerCard(withShadow = false) {
                    Text(
                        "形状",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        enumLabelSuccess<ShapeType>(e.shapeType) { it.label },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StickerCard(withShadow = false) {
                    Text(
                        "量级",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        enumLabelSuccess<AmountLevel>(e.amountLevel) { it.label },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                StickerCard(withShadow = false) {
                    Text(
                        "顺畅度",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    val smoothEmoji = runCatching { enumValueOf<SmoothLevel>(e.smoothLevel).emoji }.getOrDefault("🌿")
                    Text(
                        "${enumLabelSuccess<SmoothLevel>(e.smoothLevel) { it.label }} $smoothEmoji",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        // AI comment card
        StickerCard(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.15f),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Text("🤖", fontSize = 20.sp)
                Column {
                    Text(
                        "AI 屎评官",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        e.aiComment ?: "今日表现稳定，继续保持！",
                        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        // Coins earned
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.secondaryContainer,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("🪙", fontSize = 22.sp)
                Text(
                    "+5 屎币",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }

        if (e.isPaidPoop && earn > 0f) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            ) {
                Text(
                    "💸 本次带薪收益：¥${"%.2f".format(earn)}",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        // Action buttons
        Button(
            onClick = onAgain,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .hardShadow(MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        ) {
            Text(
                "再来一拉 ↺",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                fontSize = 16.sp,
            )
        }

        OutlinedButton(
            onClick = {
                val shareText = buildSuccessShareText(e, earn)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                context.startActivity(Intent.createChooser(intent, "分享今日战报"))
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(50),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
        ) {
            Text("📤 生成分享卡片", style = MaterialTheme.typography.labelLarge)
        }

        OutlinedButton(
            onClick = onHome,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(50),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
        ) {
            Text("🏠 返回首页", style = MaterialTheme.typography.labelLarge)
        }
    }
}

private inline fun <reified T : Enum<T>> enumLabelSuccess(name: String, label: (T) -> String): String {
    return try { label(enumValueOf(name)) } catch (_: Exception) { name }
}

private fun buildSuccessShareText(e: PoopRecordEntity, earn: Float): String = buildString {
    appendLine("【拉了么】今日一拉战报 🎉")
    appendLine("量级：${enumLabelSuccess<AmountLevel>(e.amountLevel) { it.label }}")
    appendLine("形状：${enumLabelSuccess<ShapeType>(e.shapeType) { it.label }}")
    appendLine("时长：${e.durationMinutes} 分钟")
    e.aiComment?.let { appendLine("AI 评：$it") }
    if (e.isPaidPoop && earn > 0f) appendLine("带薪收益：¥${"%.2f".format(earn)}")
    appendLine("—— 表面搞怪，内核解压。")
}
