package com.lamele.app.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamele.app.domain.PaidPoopMath
import com.lamele.app.ui.AppViewModel
import com.lamele.app.ui.components.LameleTopBar
import com.lamele.app.ui.components.StickerCard
import com.lamele.app.ui.components.hardShadow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    showBackButton: Boolean = true,
) {
    val context = LocalContext.current
    val salaryState by viewModel.monthlySalary.collectAsState()
    val daysState by viewModel.workDays.collectAsState()
    val hoursState by viewModel.workHours.collectAsState()
    val records by viewModel.records.collectAsState()

    var salary by remember { mutableFloatStateOf(salaryState) }
    var days by remember { mutableIntStateOf(daysState) }
    var hours by remember { mutableFloatStateOf(hoursState) }
    var sessionMin by remember { mutableIntStateOf(5) }

    LaunchedEffect(salaryState, daysState, hoursState) {
        salary = salaryState; days = daysState; hours = hoursState
    }

    val monthTotal = PaidPoopMath.monthPaidTotalMillis(records, salary, days, hours)
    val hourly = PaidPoopMath.hourlyRate(salary, days, hours)
    val session = PaidPoopMath.sessionEarnings(sessionMin, salary, days, hours)
    val progressRatio = (sessionMin / 60f).coerceIn(0f, 1f)

    if (showBackButton) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("带薪拉屎计算器") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                )
            },
        ) { padding ->
            CalculatorBody(
                modifier = Modifier.padding(padding),
                salary = salary, days = days, hours = hours, sessionMin = sessionMin,
                monthTotal = monthTotal, hourly = hourly, session = session,
                progressRatio = progressRatio, context = context,
                onSalaryChange = { salary = it },
                onDaysChange = { days = it },
                onHoursChange = { hours = it },
                onSessionChange = { sessionMin = it },
                onSave = { viewModel.setSalary(salary, days, hours) },
            )
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            LameleTopBar()
            CalculatorBody(
                modifier = Modifier,
                salary = salary, days = days, hours = hours, sessionMin = sessionMin,
                monthTotal = monthTotal, hourly = hourly, session = session,
                progressRatio = progressRatio, context = context,
                onSalaryChange = { salary = it },
                onDaysChange = { days = it },
                onHoursChange = { hours = it },
                onSessionChange = { sessionMin = it },
                onSave = { viewModel.setSalary(salary, days, hours) },
            )
        }
    }
}

@Composable
private fun CalculatorBody(
    modifier: Modifier,
    salary: Float,
    days: Int,
    hours: Float,
    sessionMin: Int,
    monthTotal: Float,
    hourly: Float,
    session: Float,
    progressRatio: Float,
    context: Context,
    onSalaryChange: (Float) -> Unit,
    onDaysChange: (Int) -> Unit,
    onHoursChange: (Float) -> Unit,
    onSessionChange: (Int) -> Unit,
    onSave: () -> Unit,
) {
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Page header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                "💰 带薪拉屎计算器",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
            )
            Text(
                "每一秒都是对自由的追求",
                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        // Input section - 2x2 grid
        StickerCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("⚡", fontSize = 20.sp)
                Text(
                    "薪酬参数",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = salary.toInt().toString(),
                        onValueChange = { it.toFloatOrNull()?.let(onSalaryChange) },
                        label = { Text("月薪（元）", style = MaterialTheme.typography.labelSmall) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors,
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = hours.toString(),
                        onValueChange = { it.toFloatOrNull()?.let { v -> onHoursChange(v.coerceIn(0.5f, 24f)) } },
                        label = { Text("每日小时", style = MaterialTheme.typography.labelSmall) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors,
                        singleLine = true,
                    )
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = days.toString(),
                        onValueChange = { it.toIntOrNull()?.let { v -> onDaysChange(v.coerceIn(1, 31)) } },
                        label = { Text("工作天数", style = MaterialTheme.typography.labelSmall) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors,
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = sessionMin.toString(),
                        onValueChange = { it.toIntOrNull()?.let { v -> onSessionChange(v.coerceIn(1, 120)) } },
                        label = { Text("拉屎时长（分）", style = MaterialTheme.typography.labelSmall) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors,
                        singleLine = true,
                    )
                }
            }
        }

        // Result card
        StickerCard(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f),
        ) {
            Text(
                "✅ 时薪：${"%.2f".format(hourly)} 元/小时",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "本次收益",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                "${"%.2f".format(session)} 元",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                ),
            )
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { progressRatio },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }

        // Monthly card
        StickerCard(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.12f),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("📊", fontSize = 18.sp)
                        Text(
                            "本月收益",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                    Text(
                        "${"%.2f".format(monthTotal)} 元",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "累计时长",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    val totalMin = if (hourly > 0) (monthTotal / hourly * 60).toInt() else 0
                    Text(
                        "${totalMin / 60}h ${totalMin % 60}m",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        // Save button
        Button(
            onClick = onSave,
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
                "💾 保存薪资档案",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                fontSize = 16.sp,
            )
        }

        // Share button
        OutlinedButton(
            onClick = {
                val shareText = "【拉了么】带薪拉屎报告\n" +
                    "本次收益：¥${"%.2f".format(session)}\n" +
                    "本月累计：¥${"%.2f".format(monthTotal)}\n" +
                    "——公司欠你的，从厕所拿回来。"
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                context.startActivity(Intent.createChooser(intent, "分享带薪计算"))
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(50),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
        ) {
            Text("📤 生成分享卡片", style = MaterialTheme.typography.labelLarge)
        }

        // Footer
        Text(
            ""上班可以忍，屎不能憋。\n公司欠你的，从厕所拿回来。"",
            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        )
    }
}
