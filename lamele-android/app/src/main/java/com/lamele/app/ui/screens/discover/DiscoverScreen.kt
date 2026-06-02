package com.lamele.app.ui.screens.discover

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamele.app.ui.components.LameleTopBar
import com.lamele.app.ui.components.StickerCard
import com.lamele.app.ui.components.hardShadow

private data class FeatureEntry(
    val title: String,
    val subtitle: String,
    val route: String,
    val emoji: String,
    val highlight: Boolean = false,
    val badge: String? = null,
)

private val primaryFeatures = listOf(
    FeatureEntry("排行榜", "看看谁是今天的"最速传说"", "leaderboard", "🏆"),
    FeatureEntry("厕所树洞", "秘密吐槽，悄悄释放", "tree", "🕳️"),
    FeatureEntry("AI 屎诗", "为你的每一次伟大战役赋诗一首", "poetry", "🤖", highlight = true, badge = "NEW"),
    FeatureEntry("肠道人格测试", "解锁你的身体隐藏性格", "gut_test", "🧠"),
    FeatureEntry("屎币小店", "头像框 / 称号兑换", "coin_shop", "🪙"),
)

private val moreFeatures = listOf(
    FeatureEntry("屎友圈", "同步动态，分享这一刻", "feed", "💬"),
    FeatureEntry("好友 PK", "和谁比谁更通畅", "pk", "⚔️"),
    FeatureEntry("厕所探索", "档案、详情、雷达维度", "toilets", "🚽"),
    FeatureEntry("拉屎弹幕", "附近嘴替（持久化+系统梗）", "danmaku", "📣"),
    FeatureEntry("今日屎运签", "赛博求签", "fortune", "🔮"),
    FeatureEntry("冲水连击", "10 秒小游戏 · 记榜", "mini_game", "🫧"),
    FeatureEntry("马桶养成", "喂食升级你的桶", "pet", "🐳"),
    FeatureEntry("肠道农场", "三块 plot · 1 分钟熟", "farm", "🌱"),
    FeatureEntry("周报 / 月报", "文本战报 + 系统分享", "reports", "📊"),
    FeatureEntry("好友列表", "内置损友（可拓展真社交）", "friends", "👯"),
    FeatureEntry("饮食与轻健康", "温和提醒 · 非医疗", "diet", "🥦"),
    FeatureEntry("赛季活动", "春季通畅杯任务面板", "season", "🏅"),
    FeatureEntry("AI 屎评官", "当前规则说明 & 后续接入", "ai_officer", "🧑‍⚖️"),
    FeatureEntry("装扮背包", "已解锁道具", "cosmetics", "🎒"),
)

@Composable
fun DiscoverScreen(onOpen: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        LameleTopBar()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { Spacer(Modifier.height(12.dp)) }

            // Section header
            item {
                Column {
                    Text(
                        "🧭 发现",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        "探索全功能模块",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Featured leaderboard card (full width)
            item {
                FeaturedCard(
                    entry = primaryFeatures[0],
                    onOpen = onOpen,
                )
            }

            // 2-column row: 树洞 + AI诗 fallback row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    SmallFeatureCard(
                        entry = FeatureEntry("屎友圈", "同步动态，分享这一刻", "feed", "💬"),
                        onOpen = onOpen,
                        modifier = Modifier.weight(1f),
                    )
                    SmallFeatureCard(
                        entry = primaryFeatures[1], // 树洞
                        onOpen = onOpen,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            // AI 屎诗 - highlighted with teal border
            item {
                HighlightedFeatureCard(
                    entry = primaryFeatures[2], // AI 屎诗
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.15f),
                    borderColor = MaterialTheme.colorScheme.tertiaryContainer,
                    onOpen = onOpen,
                )
            }

            // 肠道人格测试 - amber border
            item {
                HighlightedFeatureCard(
                    entry = primaryFeatures[3],
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.15f),
                    borderColor = MaterialTheme.colorScheme.secondaryContainer,
                    onOpen = onOpen,
                )
            }

            // 屎币小店
            item {
                FeaturedCard(entry = primaryFeatures[4], onOpen = onOpen)
            }

            // More features section
            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    "更多功能",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            val moreRows = moreFeatures.chunked(2)
            items(moreRows) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    row.forEach { feature ->
                        SmallFeatureCard(
                            entry = feature,
                            onOpen = onOpen,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (row.size == 1) {
                        androidx.compose.foundation.layout.Box(Modifier.weight(1f))
                    }
                }
            }

            // Decorative sticker
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Transparent,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
                ) {
                    Text(
                        "🧻 RELAX MOMENT",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun FeaturedCard(entry: FeatureEntry, onOpen: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .hardShadow(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
            .clickable { onOpen(entry.route) },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(entry.emoji, fontSize = 32.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    entry.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    entry.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outlineVariant,
            )
        }
    }
}

@Composable
private fun SmallFeatureCard(
    entry: FeatureEntry,
    onOpen: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clickable { onOpen(entry.route) },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(entry.emoji, fontSize = 28.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                entry.title,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                entry.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun HighlightedFeatureCard(
    entry: FeatureEntry,
    containerColor: Color,
    borderColor: Color,
    onOpen: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen(entry.route) },
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        border = BorderStroke(2.dp, borderColor),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(entry.emoji, fontSize = 32.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    entry.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    entry.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (entry.badge != null) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = borderColor,
                ) {
                    Text(
                        entry.badge,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}
