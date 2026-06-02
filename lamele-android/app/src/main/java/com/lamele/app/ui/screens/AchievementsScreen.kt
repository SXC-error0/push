package com.lamele.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamele.app.domain.Achievement
import com.lamele.app.domain.AchievementChecker
import com.lamele.app.ui.components.StickerCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    achievements: List<Achievement>,
    onBack: () -> Unit,
) {
    val all = AchievementChecker.allDefinitions()
    val unlockedCount = achievements.size
    val totalCount = all.size
    val progress = if (totalCount > 0) unlockedCount.toFloat() / totalCount else 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("成就墙") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Progress header
            item {
                Spacer(Modifier.height(8.dp))
                StickerCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            "🏅 成就墙",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        )
                        Text(
                            "已解锁 $unlockedCount/$totalCount",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            // Achievement grid - 2 per row
            val rows = all.chunked(2)
            items(rows) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    row.forEach { def ->
                        AchievementCell(
                            id = def.id,
                            title = def.title,
                            description = def.description,
                            unlocked = achievements.any { it.id == def.id },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    // Fill empty space if odd count in last row
                    if (row.size == 1) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }

            // Motivational footer
            item {
                if (unlockedCount < totalCount) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondaryContainer),
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text("💪", fontSize = 22.sp)
                            Text(
                                "继续加油，骚年！还有 ${totalCount - unlockedCount} 个成就等着你带回家~",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

private fun achievementEmoji(id: String): String = when (id) {
    "morning" -> "🌅"
    "night" -> "🌙"
    "fast" -> "⚡"
    "meditate" -> "🧘"
    "paid" -> "💰"
    "home" -> "🏠"
    "smooth" -> "🌿"
    "mud" -> "💧"
    "banana" -> "🍌"
    "30d" -> "🏆"
    else -> "🌟"
}

@Composable
private fun AchievementCell(
    id: String,
    title: String,
    description: String,
    unlocked: Boolean,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (unlocked) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    }
    val borderColor = if (unlocked) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }
    val textColor = if (unlocked) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        border = BorderStroke(2.dp, borderColor),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            if (unlocked) {
                Text(achievementEmoji(id), fontSize = 28.sp)
            } else {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (unlocked) FontWeight.Bold else FontWeight.Normal,
                ),
                color = textColor,
            )
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = textColor.copy(alpha = 0.75f),
            )
            if (unlocked) {
                Spacer(Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                ) {
                    Text(
                        "已达成",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                    )
                }
            }
        }
    }
}
