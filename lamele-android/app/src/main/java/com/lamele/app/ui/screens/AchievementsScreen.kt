package com.lamele.app.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lamele.app.domain.Achievement
import com.lamele.app.domain.AchievementChecker
import com.lamele.app.ui.components.CuteStatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    achievements: List<Achievement>,
    onBack: () -> Unit,
) {
    val all = AchievementChecker.allDefinitions()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("成就墙") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                CuteStatCard(
                    emoji = "🏅",
                    title = "已解锁",
                    value = "${achievements.size} / ${all.size}",
                )
            }
            items(all) { def ->
                val unlocked = achievements.any { it.id == def.id }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (unlocked) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        },
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                def.title,
                                fontWeight = FontWeight.SemiBold,
                                color = if (unlocked) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            )
                            Text(
                                def.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (unlocked) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                            )
                        }
                        Text(
                            if (unlocked) "已解锁" else "未解锁",
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
        }
    }
}
