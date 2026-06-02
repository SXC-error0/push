package com.lamele.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 4px offset hard shadow — matches the CSS sticker design system */
fun Modifier.hardShadow(
    shadowColor: Color,
    cornerRadius: Dp = 12.dp,
    offsetX: Dp = 4.dp,
    offsetY: Dp = 4.dp,
): Modifier = this
    .padding(end = offsetX, bottom = offsetY)
    .drawBehind {
        drawRoundRect(
            color = shadowColor,
            topLeft = Offset(offsetX.toPx(), offsetY.toPx()),
            size = size,
            cornerRadius = CornerRadius(cornerRadius.toPx()),
        )
    }

/** Sticker-border card with optional hard shadow */
@Composable
fun StickerCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    withShadow: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shadowColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    val outlineVariant = MaterialTheme.colorScheme.outlineVariant
    val baseModifier = if (withShadow) {
        modifier.fillMaxWidth().hardShadow(shadowColor)
    } else {
        modifier.fillMaxWidth()
    }
    Surface(
        modifier = baseModifier,
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        border = BorderStroke(2.dp, outlineVariant),
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

/** Shared top app bar used on all main tab screens */
@Composable
fun LameleTopBar(
    modifier: Modifier = Modifier,
    onSettings: () -> Unit = {},
    trailingContent: @Composable RowScope.() -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("💩", fontSize = 20.sp)
                    }
                }
                Text(
                    "拉了么",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp,
                    ),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                trailingContent()
                IconButton(onClick = onSettings) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "设置",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}

/** Small stat pill used in HomeScreen header area */
@Composable
fun StatPill(
    emoji: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(emoji, fontSize = 16.sp)
            Text(
                label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** 2-column mini stat card */
@Composable
fun MiniStatCard(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
) {
    StickerCard(modifier = modifier, containerColor = containerColor, withShadow = false) {
        Text(emoji, fontSize = 20.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

// ───── Legacy components (kept for compatibility) ─────

@Composable
fun CuteHeader(
    emoji: String,
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "$emoji  $title",
                style = MaterialTheme.typography.titleLarge,
            )
        }
        subtitle?.let {
            Spacer(Modifier.height(6.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
            )
        }
    }
}

@Composable
fun CuteSectionCard(
    emoji: String,
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CuteHeader(emoji = emoji, title = title, subtitle = subtitle)
        Spacer(modifier = Modifier.height(10.dp))
        StickerCard(withShadow = false) { content() }
    }
}

@Composable
fun CuteStatCard(
    emoji: String,
    title: String,
    value: String,
    hint: String? = null,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.widthIn(min = 0.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
        ),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "$emoji  $title",
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
            )
            hint?.let {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
        }
    }
}

@Composable
fun CuteKvp(
    emoji: String,
    key: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "$emoji  $key：",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(end = 6.dp),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
    }
}
