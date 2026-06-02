package com.lamele.app.ui.screens.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lamele.app.ui.components.CuteHeader

private data class FeatureEntry(val title: String, val subtitle: String, val route: String)

private val allFeatures = listOf(
    FeatureEntry("🏆 排行榜", "附近 / 好友 / 带薪（本地模拟+你的真实数据）", "leaderboard"),
    FeatureEntry("⚔️ 好友 PK", "和谁比谁更通畅（对照打卡数据）", "pk"),
    FeatureEntry("🚽 厕所探索与评分", "档案、详情、雷达维度点评", "toilets"),
    FeatureEntry("💬 屎友圈", "动态流 · 冲了 / 懂你（本地）", "feed"),
    FeatureEntry("🕳️ 厕所树洞", "匿名发疯与哲学", "tree"),
    FeatureEntry("📣 拉屎弹幕", "附近嘴替（持久化 + 系统梗）", "danmaku"),
    FeatureEntry("🔮 今日屎运签", "赛博求签", "fortune"),
    FeatureEntry("🤖 AI 屎诗", "模板诗人 · 可接模型", "poetry"),
    FeatureEntry("🧠 肠道人格测试", "5 题无厘头诊断", "gut_test"),
    FeatureEntry("🪙 屎币小店", "头像框 / 称号兑换", "coin_shop"),
    FeatureEntry("🎒 装扮背包", "已解锁道具", "cosmetics"),
    FeatureEntry("🫧 冲水连击", "10 秒小游戏 · 记榜", "mini_game"),
    FeatureEntry("🐳 马桶养成", "喂食升级你的桶", "pet"),
    FeatureEntry("🌱 肠道农场", "三块 plot · 1 分钟熟", "farm"),
    FeatureEntry("📊 周报 / 月报 / 年报", "文本战报 + 系统分享", "reports"),
    FeatureEntry("👯 好友列表", "内置损友（可拓展真社交）", "friends"),
    FeatureEntry("🥦 饮食与轻健康", "温和提醒 · 非医疗", "diet"),
    FeatureEntry("🏅 赛季活动", "春季通畅杯任务面板", "season"),
    FeatureEntry("🧑‍⚖️ AI 屎评官", "当前规则说明 & 后续接入 API", "ai_officer"),
)

@Composable
fun DiscoverScreen(onOpen: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            CuteHeader(
                emoji = "🧭",
                title = "发现",
                subtitle = "以下为 PRD 全模块本地版：数据存 Room/DataStore，排行榜等为娱乐模拟（但你是真的在努力）。",
            )
        }
        items(allFeatures) { f ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpen(f.route) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(f.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        f.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                    )
                }
            }
        }
    }
}
