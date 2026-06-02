package com.lamele.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lamele.app.model.AmountLevel
import com.lamele.app.model.ColorType
import com.lamele.app.model.MoodType
import com.lamele.app.model.SceneType
import com.lamele.app.model.ShapeType
import com.lamele.app.model.SmoothLevel
import com.lamele.app.ui.AppViewModel
import com.lamele.app.ui.components.CuteSectionCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CheckInScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onSaved: (Long) -> Unit,
) {
    val draft by viewModel.checkInDraft.collectAsState()
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("今日一拉") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CuteSectionCard(emoji = "⏳", title = "时长（分钟）") {
                Text("${draft.durationMinutes} 分钟", style = MaterialTheme.typography.bodyLarge)
                Slider(
                    value = draft.durationMinutes.toFloat(),
                    onValueChange = {
                        viewModel.checkInDraft.value = draft.copy(durationMinutes = it.toInt().coerceIn(1, 60))
                    },
                    valueRange = 1f..60f,
                )
            }

            CuteSectionCard(emoji = "🗺️", title = "城市（屎迹地图，可空）") {
                OutlinedTextField(
                    value = draft.city,
                    onValueChange = { viewModel.checkInDraft.value = draft.copy(city = it) },
                    placeholder = { Text("如：杭州") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }

            CuteSectionCard(emoji = "🎭", title = "场景") {
                chipRow(SceneType.entries, draft.scene, { viewModel.checkInDraft.value = draft.copy(scene = it) }) { it.label }
            }

            CuteSectionCard(emoji = "⚖️", title = "量级") {
                chipRow(AmountLevel.entries, draft.amount, { viewModel.checkInDraft.value = draft.copy(amount = it) }) { it.label }
            }

            CuteSectionCard(emoji = "🍌", title = "形状") {
                chipRow(ShapeType.entries, draft.shape, { viewModel.checkInDraft.value = draft.copy(shape = it) }) { it.label }
            }

            CuteSectionCard(emoji = "🎨", title = "颜色") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    FilterChip(
                        selected = draft.color == null,
                        onClick = { viewModel.checkInDraft.value = draft.copy(color = null) },
                        label = { Text("不记录") },
                    )
                    ColorType.entries.forEach { c ->
                        FilterChip(
                            selected = draft.color == c,
                            onClick = { viewModel.checkInDraft.value = draft.copy(color = c) },
                            label = { Text(c.label) },
                        )
                    }
                }
            }

            CuteSectionCard(emoji = "💨", title = "顺畅度") {
                chipRow(SmoothLevel.entries, draft.smooth, { viewModel.checkInDraft.value = draft.copy(smooth = it) }) { it.label }
            }

            CuteSectionCard(emoji = "😊", title = "心情") {
                chipRow(MoodType.entries, draft.mood, { viewModel.checkInDraft.value = draft.copy(mood = it) }) { it.label }
            }

            CuteSectionCard(emoji = "✅", title = "选项") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("带薪拉屎")
                        Switch(checked = draft.paid, onCheckedChange = { viewModel.checkInDraft.value = draft.copy(paid = it) })
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("同步屎友圈（仅占位）")
                        Switch(
                            checked = draft.isPublic,
                            onCheckedChange = { viewModel.checkInDraft.value = draft.copy(isPublic = it) },
                        )
                    }
                }
            }

            CuteSectionCard(emoji = "📝", title = "备注") {
                OutlinedTextField(
                    value = draft.note,
                    onValueChange = { viewModel.checkInDraft.value = draft.copy(note = it) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        val id = viewModel.saveCheckIn()
                        onSaved(id)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("完成打卡")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T> chipRow(
    items: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    label: (T) -> String,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items.forEach { item ->
            FilterChip(
                selected = item == selected,
                onClick = { onSelect(item) },
                label = { Text(label(item)) },
            )
        }
    }
}
