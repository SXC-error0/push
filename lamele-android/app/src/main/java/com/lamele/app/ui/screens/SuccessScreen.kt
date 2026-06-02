package com.lamele.app.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lamele.app.data.local.PoopRecordEntity
import com.lamele.app.domain.PaidPoopMath
import com.lamele.app.model.AmountLevel
import com.lamele.app.model.ColorType
import com.lamele.app.model.MoodType
import com.lamele.app.model.SceneType
import com.lamele.app.model.ShapeType
import com.lamele.app.model.SmoothLevel
import com.lamele.app.ui.AppViewModel
import com.lamele.app.ui.components.CuteKvp
import com.lamele.app.ui.components.CuteSectionCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val e = entity
    if (e == null) {
        Text("加载中…", modifier = Modifier.padding(24.dp))
        return
    }
    val salary = viewModel.monthlySalary.value
    val days = viewModel.workDays.value
    val hours = viewModel.workHours.value
    val earn = if (e.isPaidPoop) {
        PaidPoopMath.sessionEarnings(e.durationMinutes, salary, days, hours)
    } else {
        0f
    }
    val successLine = remember {
        listOf(
            "今日一拉，世界少了一份压力。",
            "马桶已接收你的情绪垃圾。",
            "你不是在拉屎，你是在重启系统。",
        ).random()
    }
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("打卡成功", style = MaterialTheme.typography.headlineLarge)
        Text(
            successLine,
            style = MaterialTheme.typography.bodyLarge,
        )
        CuteSectionCard(emoji = "🤖", title = "AI 屎评") {
            Text(e.aiComment ?: "", style = MaterialTheme.typography.bodyLarge)
        }

        CuteSectionCard(emoji = "🧾", title = "本次记录（简版面板）") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val fmt = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA) }

                CuteKvp(emoji = "⏰", key = "时间", value = fmt.format(Date(e.timeMillis)))
                CuteKvp(emoji = "⏳", key = "时长", value = "${e.durationMinutes} 分钟")
                e.city?.let { city ->
                    CuteKvp(emoji = "🗺️", key = "城市", value = city)
                }
                CuteKvp(
                    emoji = "🎭",
                    key = "场景/量级/形状",
                    value = "${enumLabel<SceneType>(e.sceneType) { it.label }} · " +
                        "${enumLabel<AmountLevel>(e.amountLevel) { it.label }} · " +
                        "${enumLabel<ShapeType>(e.shapeType) { it.label }}"
                )
                CuteKvp(
                    emoji = "💸",
                    key = "带薪",
                    value = if (e.isPaidPoop) "是（薅回走起）" else "否（纯享）",
                )

                if (expanded) {
                    e.colorType?.let { c ->
                        CuteKvp(
                            emoji = "🎨",
                            key = "颜色",
                            value = enumLabel<ColorType>(c) { it.label },
                        )
                    }
                    CuteKvp(
                        emoji = "💨",
                        key = "顺畅",
                        value = enumLabel<SmoothLevel>(e.smoothLevel) { it.label },
                    )
                    CuteKvp(
                        emoji = "😊",
                        key = "心情",
                        value = enumLabel<MoodType>(e.mood) { it.label },
                    )
                    if (e.isPaidPoop) {
                        CuteKvp(
                            emoji = "🧧",
                            key = "本次薅回（娱乐）",
                            value = "¥${"%.2f".format(earn)}",
                        )
                    }
                    e.note?.let { note ->
                        CuteKvp(emoji = "📝", key = "备注", value = note)
                    }
                }

                OutlinedButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(if (expanded) "收起明细" else "展开明细")
                }
            }
        }
        Button(
            onClick = {
                val share = buildShareText(e, earn)
                val send = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, share)
                }
                context.startActivity(Intent.createChooser(send, "分享今日一拉"))
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Default.Share, contentDescription = null)
            Text(" 分享卡片（文本）", modifier = Modifier.padding(start = 8.dp))
        }
        FilledTonalButton(onClick = onHome, modifier = Modifier.fillMaxWidth()) {
            Text("回首页")
        }
        OutlinedButton(onClick = onAgain, modifier = Modifier.fillMaxWidth()) {
            Text("再记一笔")
        }
    }
}

private inline fun <reified T : Enum<T>> enumLabel(name: String, label: (T) -> String): String {
    return try {
        label(enumValueOf<T>(name))
    } catch (_: Exception) {
        name
    }
}

private fun buildShareText(e: PoopRecordEntity, earn: Float): String = buildString {
    appendLine("【拉了么】今日一拉战报")
    appendLine(enumLabel<ShapeType>(e.shapeType) { it.label })
    appendLine(e.aiComment ?: "")
    if (e.isPaidPoop) appendLine("本次带薪释放估算：¥${"%.2f".format(earn)}")
    appendLine("—— 表面搞怪，内核解压。")
}
