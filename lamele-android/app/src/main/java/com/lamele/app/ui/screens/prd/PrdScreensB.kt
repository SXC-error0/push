package com.lamele.app.ui.screens.prd

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lamele.app.domain.FortuneSign
import com.lamele.app.domain.GutPersonalityQuiz
import com.lamele.app.domain.PoetryGenerator
import com.lamele.app.domain.ReportGenerator
import com.lamele.app.domain.SeasonDefinitions
import com.lamele.app.domain.StreakUtils
import com.lamele.app.ui.AppViewModel
import com.lamele.app.ui.FeaturesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(featVm: FeaturesViewModel, onBack: () -> Unit, onPublish: () -> Unit) {
    val posts by featVm.feed.collectAsState()
    PrdScreenScaffold("屎友圈", onBack) { mod ->
        Column(mod.fillMaxSize()) {
            Button(onClick = onPublish, modifier = Modifier.fillMaxWidth()) { Text("发布动态") }
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(posts, key = { it.id }) { p ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            val who = if (p.isAnonymous) "匿名屎友" else "实名"
                            Text(who, style = MaterialTheme.typography.labelMedium)
                            Text(p.content)
                            Text(
                                "冲了 · 懂你 · 马桶已阅",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeedPublishScreen(featVm: FeaturesViewModel, onBack: () -> Unit) {
    var text by remember { mutableStateOf("") }
    var anon by remember { mutableStateOf(true) }
    PrdScreenScaffold("发布动态", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(text, { text = it }, Modifier.fillMaxWidth(), minLines = 3, label = { Text("整点活儿") })
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(anon, { anon = it })
                Text("匿名（推荐）")
            }
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        featVm.postFeed(text, anon, null)
                        onBack()
                    }
                },
                Modifier.fillMaxWidth(),
            ) { Text("发送") }
        }
    }
}

@Composable
fun TreeHoleScreen(featVm: FeaturesViewModel, onBack: () -> Unit, onPublish: () -> Unit) {
    val holes by featVm.treeHoles.collectAsState()
    PrdScreenScaffold("厕所树洞", onBack) { mod ->
        Column(mod.fillMaxSize()) {
            Button(onClick = onPublish, modifier = Modifier.fillMaxWidth()) { Text("投递树洞") }
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(holes, key = { it.id }) { h ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(h.moodTag ?: "未分类", style = MaterialTheme.typography.labelSmall)
                            Text(h.content)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TreePublishScreen(featVm: FeaturesViewModel, onBack: () -> Unit) {
    var text by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf("哲思") }
    val moods = listOf("哲思", "发疯", "破防", "谢谢马桶")
    PrdScreenScaffold("写树洞", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            moods.forEach { m ->
                FilterChip(selected = mood == m, onClick = { mood = m }, label = { Text(m) })
            }
            OutlinedTextField(text, { text = it }, Modifier.fillMaxWidth(), minLines = 4)
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        featVm.postTree(text, mood)
                        onBack()
                    }
                },
                Modifier.fillMaxWidth(),
            ) { Text("匿名投递") }
        }
    }
}

@Composable
fun DanmakuScreen(featVm: FeaturesViewModel, onBack: () -> Unit) {
    val lines by featVm.danmaku.collectAsState()
    var draft by remember { mutableStateOf("") }
    PrdScreenScaffold("拉屎弹幕", onBack) { mod ->
        Column(mod.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(lines, key = { it.id }) { d ->
                    Text("• ${d.text}", Modifier.padding(6.dp))
                }
            }
            OutlinedTextField(draft, { draft = it }, Modifier.fillMaxWidth(), label = { Text("发射弹幕") })
            Button(
                onClick = {
                    if (draft.isNotBlank()) {
                        featVm.shootDanmaku(draft)
                        draft = ""
                    }
                },
                Modifier.fillMaxWidth(),
            ) { Text("发送") }
        }
    }
}

@Composable
fun FortuneScreen(onBack: () -> Unit) {
    PrdScreenScaffold("今日屎运签", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState())) {
            Text(FortuneSign.todayLine(), style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            fortuneExtras().forEach { Text(it, Modifier.padding(4.dp)) }
        }
    }
}

private fun fortuneExtras() = listOf(
    "宜：准时喝水。忌：硬憋开会有风险。",
    "宜：分享战报。忌：上传真实图片。",
    "宜：带薪合规释放。忌：迷信本签文。",
)

@Composable
fun PoetryScreen(appVm: AppViewModel, onBack: () -> Unit) {
    val records by appVm.records.collectAsState()
    val poem = remember(records) { PoetryGenerator.shitPoem(records.maxByOrNull { it.timeMillis }) }
    val context = LocalContext.current
    PrdScreenScaffold("AI 屎诗", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState())) {
            Text(poem, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    val send = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, poem)
                    }
                    context.startActivity(Intent.createChooser(send, "分享屎诗"))
                },
                Modifier.fillMaxWidth(),
            ) { Text("分享") }
        }
    }
}

@Composable
fun GutTestScreen(onBack: () -> Unit) {
    val q = GutPersonalityQuiz.questions
    var step by remember { mutableIntStateOf(0) }
    val picks = remember { mutableStateListOf<Int>() }
    PrdScreenScaffold("肠道人格", onBack) { mod ->
        if (step >= q.size) {
            val r = GutPersonalityQuiz.result(picks.toList())
            Column(Modifier.padding(8.dp)) {
                Text(r.title, style = MaterialTheme.typography.headlineSmall)
                Text(r.desc)
                Button(onClick = { step = 0; picks.clear() }) { Text("重测") }
            }
        } else {
            val cur = q[step]
            Column(mod.verticalScroll(rememberScrollState())) {
                Text(cur.q, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                cur.options.forEachIndexed { i, opt ->
                    OutlinedButton(
                        onClick = {
                            picks.add(i)
                            step++
                        },
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                    ) { Text(opt) }
                }
            }
        }
    }
}

@Composable
fun CoinShopScreen(appVm: AppViewModel, featVm: FeaturesViewModel, onBack: () -> Unit) {
    val coins by appVm.poopCoins.collectAsState()
    var msg by remember { mutableStateOf("") }
    PrdScreenScaffold("屎币小店", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState())) {
            Text("当前屎币：$coins", style = MaterialTheme.typography.titleMedium)
            if (msg.isNotBlank()) Text(msg, color = MaterialTheme.colorScheme.primary)
            ShopLine("限定头像框·薄荷绿", 40, "frame_mint", featVm) { msg = it }
            ShopLine("称号喷漆·带薪传说", 80, "title_legend", featVm) { msg = it }
        }
    }
}

@Composable
private fun ShopLine(title: String, price: Int, cid: String, featVm: FeaturesViewModel, onMsg: (String) -> Unit) {
    Card(Modifier.padding(vertical = 6.dp).fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(title)
            Text("$price 屎币")
            Button(onClick = {
                featVm.tryPurchaseCosmetic(price, cid) { ok -> onMsg(if (ok) "已解锁 $cid" else "余额不足") }
            }) { Text("购买") }
        }
    }
}

@Composable
fun CosmeticsScreen(featVm: FeaturesViewModel, onBack: () -> Unit) {
    val owned by featVm.game.ownedCosmetics.collectAsState(initial = emptySet())
    PrdScreenScaffold("装扮背包", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState())) {
            Text("已解锁：${owned.joinToString()}", style = MaterialTheme.typography.bodyLarge)
            Text("可在分享或资料中自行展示。")
        }
    }
}

@Composable
fun MiniGameScreen(featVm: FeaturesViewModel, onBack: () -> Unit) {
    var taps by remember { mutableIntStateOf(0) }
    var playing by remember { mutableStateOf(false) }
    val latestTaps by rememberUpdatedState(taps)
    val hi by featVm.game.miniGameHigh.collectAsState(initial = 0)
    LaunchedEffect(playing) {
        if (!playing) return@LaunchedEffect
        repeat(10) { delay(1000) }
        playing = false
        featVm.submitMiniGameScore(latestTaps)
    }
    PrdScreenScaffold("冲水连击", onBack) { mod ->
        Column(mod, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("10 秒连点「冲水」，结束自动提交。最高分：$hi")
            if (playing) {
                Text("连击 $taps", style = MaterialTheme.typography.headlineSmall)
                Button(onClick = { taps++ }, Modifier.fillMaxWidth()) { Text("冲水！！") }
            } else {
                Button(
                    onClick = { taps = 0; playing = true },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("开始") }
            }
        }
    }
}

@Composable
fun PetScreen(featVm: FeaturesViewModel, onBack: () -> Unit) {
    val lv by featVm.game.petLevel.collectAsState(initial = 1)
    val xp by featVm.game.petXp.collectAsState(initial = 0)
    var msg by remember { mutableStateOf("") }
    PrdScreenScaffold("马桶养成", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState())) {
            Text("马桶等级 $lv，经验 $xp", style = MaterialTheme.typography.titleMedium)
            val need = (lv * 20).coerceAtLeast(1)
            LinearProgressIndicator((xp % need) / need.toFloat(), Modifier.fillMaxWidth())
            if (msg.isNotBlank()) Text(msg)
            Button(onClick = { featVm.feedPet { msg = it } }, Modifier.fillMaxWidth()) { Text("花 10 币喂一口") }
        }
    }
}

@Composable
fun FarmScreen(appVm: AppViewModel, featVm: FeaturesViewModel, onBack: () -> Unit) {
    val t0 by featVm.game.farmPlot0.collectAsState(initial = 0L)
    val t1 by featVm.game.farmPlot1.collectAsState(initial = 0L)
    val t2 by featVm.game.farmPlot2.collectAsState(initial = 0L)
    val scope = rememberCoroutineScope()
    PrdScreenScaffold("肠道农场", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("三块纤维田：种植约 1 分钟可收获，入账屎币。")
            FarmRow("田 A", t0, 0, appVm, featVm, scope)
            FarmRow("田 B", t1, 1, appVm, featVm, scope)
            FarmRow("田 C", t2, 2, appVm, featVm, scope)
        }
    }
}

@Composable
private fun FarmRow(
    label: String,
    planted: Long,
    idx: Int,
    appVm: AppViewModel,
    featVm: FeaturesViewModel,
    scope: kotlinx.coroutines.CoroutineScope,
) {
    Column(Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.titleSmall)
        val status = when {
            planted == 0L -> "空置"
            System.currentTimeMillis() - planted > 60_000 -> "可收获"
            else -> "生长中…"
        }
        Text(status)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { scope.launch { featVm.game.plantPlot(idx) } }) { Text("种植") }
            Button(onClick = {
                scope.launch {
                    val r = featVm.game.harvestPlot(idx)
                    if (r > 0) appVm.rewardCoins(r)
                }
            }) { Text("收获") }
        }
    }
}

@Composable
fun ReportsScreen(appVm: AppViewModel, onBack: () -> Unit) {
    val records by appVm.records.collectAsState()
    var tab by remember { mutableIntStateOf(0) }
    val period = when (tab) {
        0 -> ReportGenerator.Period.WEEK
        1 -> ReportGenerator.Period.MONTH
        else -> ReportGenerator.Period.YEAR
    }
    val text = remember(records, tab) { ReportGenerator.build(records, period) }
    val context = LocalContext.current
    PrdScreenScaffold("周报 / 月报 / 年报", onBack) { mod ->
        Column(mod.fillMaxSize()) {
            TabRow(selectedTabIndex = tab) {
                Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("周报") })
                Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("月报") })
                Tab(selected = tab == 2, onClick = { tab = 2 }, text = { Text("年报") })
            }
            Text(text, Modifier.padding(8.dp))
            Button(
                onClick = {
                    val send = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, text)
                    }
                    context.startActivity(Intent.createChooser(send, "分享战报"))
                },
                Modifier.fillMaxWidth(),
            ) { Text("系统分享") }
        }
    }
}

@Composable
fun FriendsListScreen(featVm: FeaturesViewModel, onBack: () -> Unit) {
    val fs by featVm.friends.collectAsState()
    PrdScreenScaffold("好友（演示）", onBack) { mod ->
        LazyColumn {
            items(fs, key = { it.id }) { f ->
                Card(Modifier.padding(8.dp).fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(f.nickname, style = MaterialTheme.typography.titleSmall)
                        Text("周次数：${f.mockWeeklyCount}；带薪分：${f.mockPaidMinutes}")
                    }
                }
            }
        }
    }
}

@Composable
fun DietHealthScreen(onBack: () -> Unit) {
    PrdScreenScaffold("饮食 · 轻健康", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "温和提醒，不替代医疗。黑便、血便、剧痛请就医。",
                color = MaterialTheme.colorScheme.error,
            )
            Text("饮水、纤维、睡眠均影响顺畅度。后续可接饮食标签与系统提醒。")
        }
    }
}

@Composable
fun SeasonScreen(appVm: AppViewModel, featVm: FeaturesViewModel, onBack: () -> Unit) {
    val records by appVm.records.collectAsState()
    val streak = StreakUtils.currentStreak(records)
    val toilets by featVm.toilets.collectAsState()
    val pts by featVm.game.seasonPoints.collectAsState(initial = 0)
    val tasks = remember(records, toilets, streak) {
        SeasonDefinitions.tasks(streak, records.size, toilets.size)
    }
    PrdScreenScaffold("赛季", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState())) {
            Text(SeasonDefinitions.TITLE, style = MaterialTheme.typography.titleMedium)
            Text("赛季积分（本地）：$pts")
            Spacer(Modifier.height(8.dp))
            tasks.forEach { (name, done) ->
                Row(Modifier.padding(4.dp)) {
                    Text(if (done) "✓" else "○")
                    Text(name, Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

@Composable
fun AiOfficerScreen(onBack: () -> Unit) {
    PrdScreenScaffold("AI 屎评官", onBack) { mod ->
        Column(mod.verticalScroll(rememberScrollState())) {
            Text("本地规则生成点评；后续可接大模型、内容安全与会员风格包。")
        }
    }
}
