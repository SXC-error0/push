package com.lamele.app.ui.screens.prd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lamele.app.data.local.FriendEntity
import com.lamele.app.data.local.ToiletReviewEntity
import com.lamele.app.domain.LeaderboardMock
import com.lamele.app.domain.ToiletGrade
import com.lamele.app.domain.StreakUtils
import com.lamele.app.ui.AppViewModel
import com.lamele.app.ui.FeaturesViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrdScreenScaffold(title: String, onBack: () -> Unit, content: @Composable (Modifier) -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) { padding ->
        content(Modifier.padding(padding).padding(16.dp))
    }
}

@Composable
fun LeaderboardScreen(appVm: AppViewModel, onBack: () -> Unit) {
    val records by appVm.records.collectAsState()
    var tab by remember { mutableIntStateOf(0) }
    val week = StreakUtils.weekCount(records)
    val paidMin = records.filter { it.isPaidPoop }.sumOf { it.durationMinutes }
    PrdScreenScaffold("排行榜（本地）", onBack) { mod ->
        Column(mod.fillMaxSize()) {
            TabRow(selectedTabIndex = tab) {
                Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("附近次数") })
                Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("带薪时长") })
            }
            Spacer(Modifier.height(12.dp))
            when (tab) {
                0 -> {
                    LeaderboardMock.nearbyYou(week).forEach { row ->
                        Text(
                            "${if (row.isYou) "★ " else ""}${row.name} … ${row.score} 次 / 本周估",
                            Modifier.padding(8.dp),
                        )
                        LinearProgressIndicator(
                            progress = (row.score / 15f).coerceIn(0.05f, 1f),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
                else -> {
                    LeaderboardMock.paidRanking(paidMin).forEach { row ->
                        Text(
                            "${if (row.isYou) "★ " else ""}${row.name} … ${row.score} 分钟估",
                            Modifier.padding(8.dp),
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "说明：无服务端，榜单为娱乐模拟 + 你的统计穿插。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
fun PkScreen(appVm: AppViewModel, featVm: FeaturesViewModel, onBack: () -> Unit) {
    val records by appVm.records.collectAsState()
    val friends by featVm.friends.collectAsState()
    var pick by remember { mutableStateOf<FriendEntity?>(null) }
    LaunchedEffect(friends) {
        if (pick == null && friends.isNotEmpty()) pick = friends.first()
    }
    val my = StreakUtils.weekCount(records)
    PrdScreenScaffold("好友 PK", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("选一位内置好友对抗本周次数")
            friends.forEach { f ->
                FilterChip(
                    selected = f.id == pick?.id,
                    onClick = { pick = f },
                    label = { Text(f.nickname) },
                )
            }
            val them = pick?.mockWeeklyCount ?: 0
            Card {
                Column(Modifier.padding(12.dp)) {
                    Text("你：$my 次 / 周估")
                    Text("${pick?.nickname ?: "待定"}：$them 次（演示）")
                    Text(
                        when {
                            pick == null -> "先选一个对手。"
                            my > them -> "你赢了，肠道今天的 MVP 是你。"
                            my < them -> "惜败，对方摸鱼合法化程度略高。"
                            else -> "平局，建议再加赛一局带薪局。"
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ToiletListScreen(featVm: FeaturesViewModel, onBack: () -> Unit, onDetail: (Long) -> Unit, onAdd: () -> Unit) {
    val list by featVm.toilets.collectAsState()
    PrdScreenScaffold("厕所探索", onBack) { mod ->
        Column(mod.fillMaxSize()) {
            Button(onClick = onAdd, modifier = Modifier.fillMaxWidth()) { Text("新增厕所档案") }
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(list, key = { it.id }) { t ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(t.name, style = MaterialTheme.typography.titleSmall)
                            Text(listOfNotNull(t.city, t.alias).joinToString(" · "))
                            Button(onClick = { onDetail(t.id) }) { Text("详情 / 评分") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ToiletAddScreen(featVm: FeaturesViewModel, onBack: () -> Unit, onSaved: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }
    PrdScreenScaffold("新增厕所", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("名称") })
            OutlinedTextField(city, { city = it }, Modifier.fillMaxWidth(), label = { Text("城市") })
            OutlinedTextField(alias, { alias = it }, Modifier.fillMaxWidth(), label = { Text("别名（可选）") })
            Button(
                onClick = {
                    featVm.addToilet(
                        com.lamele.app.data.local.ToiletEntity(
                            name = name.ifBlank { "未命名圣地" },
                            city = city.ifBlank { null },
                            alias = alias.ifBlank { null },
                            latitude = null,
                            longitude = null,
                        ),
                    ) { onSaved() }
                },
                Modifier.fillMaxWidth(),
                enabled = name.isNotBlank(),
            ) { Text("保存") }
        }
    }
}

@Composable
fun ToiletDetailScreen(toiletId: Long, featVm: FeaturesViewModel, onBack: () -> Unit, onRate: () -> Unit) {
    val reviews by featVm.reviewsFor(toiletId).collectAsState(emptyList())
    val toilets by featVm.toilets.collectAsState()
    val t = toilets.find { it.id == toiletId }
    PrdScreenScaffold(t?.name ?: "厕所详情", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState())) {
            Text("评级：${ToiletGrade.fromReviews(reviews)}", style = MaterialTheme.typography.titleMedium)
            Text(listOfNotNull(t?.city, t?.alias).joinToString(" · "))
            Button(onClick = onRate, modifier = Modifier.fillMaxWidth()) { Text("写一条雷达评") }
            Spacer(Modifier.height(10.dp))
            reviews.take(8).forEach { r ->
                Text(
                    "干净${r.cleanliness} 隐私${r.privacy} … ${r.comment ?: ""}",
                    Modifier.padding(vertical = 4.dp),
                )
            }
        }
    }
}

private val rateLabels = listOf(
    "干净度", "隐私度", "纸巾", "冲水", "味道攻击(1最好)", "舒适度", "带薪适合度", "信号", "灵魂回声",
)

@Composable
fun ToiletRateScreen(toiletId: Long, featVm: FeaturesViewModel, onBack: () -> Unit) {
    var dims by remember { mutableStateOf(List(9) { 4 }) }
    var comment by remember { mutableStateOf("") }
    PrdScreenScaffold("厕所雷达评分", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            rateLabels.forEachIndexed { i, label ->
                Text(label)
                Slider(
                    value = dims[i].toFloat(),
                    onValueChange = { v ->
                        dims = dims.toMutableList().apply {
                            this[i] = v.roundToInt().coerceIn(1, 5)
                        }
                    },
                    valueRange = 1f..5f,
                    steps = 3,
                )
            }
            OutlinedTextField(comment, { comment = it }, Modifier.fillMaxWidth(), label = { Text("短评") })
            Button(
                onClick = {
                    featVm.addReview(
                        ToiletReviewEntity(
                            toiletId = toiletId,
                            cleanliness = dims[0],
                            privacy = dims[1],
                            paper = dims[2],
                            flushPower = dims[3],
                            smellAttack = dims[4],
                            comfort = dims[5],
                            paidPoopSuitability = dims[6],
                            signal = dims[7],
                            soulEcho = dims[8],
                            comment = comment.ifBlank { null },
                        ),
                    ) { onBack() }
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("提交评分") }
        }
    }
}
