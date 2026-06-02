package com.lamele.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.lamele.app.domain.PaidPoopMath
import com.lamele.app.ui.AppViewModel
import com.lamele.app.ui.components.CuteKvp
import com.lamele.app.ui.components.CuteSectionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
) {
    val salaryState by viewModel.monthlySalary.collectAsState()
    val daysState by viewModel.workDays.collectAsState()
    val hoursState by viewModel.workHours.collectAsState()
    val records by viewModel.records.collectAsState()

    var salary by remember { mutableFloatStateOf(salaryState) }
    var days by remember { mutableIntStateOf(daysState) }
    var hours by remember { mutableFloatStateOf(hoursState) }
    var sessionMin by remember { mutableIntStateOf(15) }
    LaunchedEffect(salaryState, daysState, hoursState) {
        salary = salaryState
        days = daysState
        hours = hoursState
    }
    val monthTotal = PaidPoopMath.monthPaidTotalMillis(
        records,
        salary,
        days,
        hours,
    )
    val hourly = PaidPoopMath.hourlyRate(salary, days, hours)
    val session = PaidPoopMath.sessionEarnings(sessionMin, salary, days, hours)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("带薪拉屎计算器") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CuteSectionCard(emoji = "🧡", title = "娱乐提示") {
                Text(
                    "公司欠我的，我从厕所拿回来。以下为娱乐向估算，不构成财务建议。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                )
            }
            OutlinedTextField(
                value = salary.toInt().toString(),
                onValueChange = { it.toFloatOrNull()?.let { v -> salary = v } },
                label = { Text("月薪（元）") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = days.toString(),
                onValueChange = { it.toIntOrNull()?.let { v -> days = v.coerceIn(1, 31) } },
                label = { Text("每月工作天数") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = hours.toString(),
                onValueChange = { it.toFloatOrNull()?.let { v -> hours = v.coerceIn(0.5f, 24f) } },
                label = { Text("每天工作小时") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = sessionMin.toString(),
                onValueChange = { it.toIntOrNull()?.let { v -> sessionMin = v.coerceIn(1, 120) } },
                label = { Text("本次蹲坑分钟") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
            CuteSectionCard(emoji = "✨", title = "估算面板") {
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    CuteKvp(emoji = "🕒", key = "估算时薪", value = "¥${"%.2f".format(hourly)}")
                    CuteKvp(emoji = "🧷", key = "本次薅回", value = "¥${"%.2f".format(session)}")
                    CuteKvp(emoji = "📅", key = "本月带薪累计（已打卡）", value = "¥${"%.2f".format(monthTotal)}")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.setSalary(salary, days, hours)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("保存薪资档案（用于统计）")
            }
        }
    }
}
