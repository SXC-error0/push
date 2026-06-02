package com.lamele.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lamele.app.LameleApplication
import com.lamele.app.data.local.PoopRecordEntity
import com.lamele.app.domain.AiCommentGenerator
import com.lamele.app.domain.PaidPoopMath
import com.lamele.app.domain.StreakUtils
import com.lamele.app.model.AmountLevel
import com.lamele.app.model.ColorType
import com.lamele.app.model.MoodType
import com.lamele.app.model.SceneType
import com.lamele.app.model.ShapeType
import com.lamele.app.model.SmoothLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val nickname: String,
    val todayCount: Int,
    val streak: Int,
    val totalCount: Int,
    val poopCoins: Int,
)

data class CheckInDraft(
    val timeMillis: Long = System.currentTimeMillis(),
    val durationMinutes: Int = 5,
    val city: String = "",
    val scene: SceneType = SceneType.HOME,
    val amount: AmountLevel = AmountLevel.MEDIUM,
    val shape: ShapeType = ShapeType.BANANA,
    val color: ColorType? = ColorType.BROWN,
    val smooth: SmoothLevel = SmoothLevel.SMOOTH,
    val mood: MoodType = MoodType.RELIEF,
    val paid: Boolean = false,
    val isPublic: Boolean = false,
    val note: String = "",
)

class AppViewModel(app: Application) : AndroidViewModel(app) {
    private val lamele = app as LameleApplication
    private val poopRepo = lamele.poopRepository
    private val prefs = lamele.userPreferencesRepository

    val records = poopRepo.records.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val nickname = prefs.nickname.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val loggedIn = prefs.loggedIn.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val monthlySalary = prefs.monthlySalary.stateIn(viewModelScope, SharingStarted.Eagerly, 12_000f)
    val workDays = prefs.workDaysPerMonth.stateIn(viewModelScope, SharingStarted.Eagerly, 22)
    val workHours = prefs.workHoursPerDay.stateIn(viewModelScope, SharingStarted.Eagerly, 8f)
    val hideLocation = prefs.hideExactLocation.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val poopCoins = prefs.poopCoins.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    private val cal = java.util.Calendar.getInstance()

    val homeState: StateFlow<HomeUiState> = combine(records, nickname, poopCoins) { r, n, c ->
        cal.timeInMillis = System.currentTimeMillis()
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.add(java.util.Calendar.DAY_OF_MONTH, 1)
        val end = cal.timeInMillis
        val today = r.count { it.timeMillis in start until end }
        HomeUiState(
            nickname = n,
            todayCount = today,
            streak = StreakUtils.currentStreak(r.toList()),
            totalCount = r.size,
            poopCoins = c,
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeUiState("", 0, 0, 0, 0))

    val checkInDraft = MutableStateFlow(CheckInDraft())

    fun setNickname(name: String) {
        viewModelScope.launch { prefs.setNickname(name) }
    }

    fun setSalary(salary: Float, days: Int, hours: Float) {
        viewModelScope.launch { prefs.setSalaryProfile(salary, days, hours) }
    }

    fun setHideLocation(hide: Boolean) {
        viewModelScope.launch { prefs.setHideExactLocation(hide) }
    }

    suspend fun saveCheckIn(): Long {
        val d = checkInDraft.value
        val local = AiCommentGenerator.generate(d.amount, d.shape, d.smooth, d.mood, d.paid)
        val prompt = buildShitPrompt(d)
        val remote = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            lamele.mimoClient?.completeShitReview(prompt)
        }
        val comment = remote?.takeIf { it.isNotBlank() } ?: local
        val entity = PoopRecordEntity(
            timeMillis = d.timeMillis,
            durationMinutes = d.durationMinutes,
            city = d.city.ifBlank { null },
            sceneType = d.scene.name,
            amountLevel = d.amount.name,
            shapeType = d.shape.name,
            colorType = d.color?.name,
            smoothLevel = d.smooth.name,
            mood = d.mood.name,
            isPaidPoop = d.paid,
            isPublic = d.isPublic,
            note = d.note.ifBlank { null },
            aiComment = comment,
        )
        val id = poopRepo.insert(entity)
        prefs.addPoopCoins(5)
        checkInDraft.value = CheckInDraft()
        return id
    }

    fun userTitle(): String {
        val r = records.value
        val s = StreakUtils.currentStreak(r)
        val t = r.size
        return when {
            s >= 30 -> "屎界传说"
            s >= 14 -> "马桶征服者"
            s >= 7 -> "资深蹲将"
            t >= 50 -> "厕所游侠"
            else -> "初级拉手"
        }
    }

    fun monthPaidTotal(): Float = PaidPoopMath.monthPaidTotalMillis(
        records.value,
        monthlySalary.value,
        workDays.value,
        workHours.value,
    )

    fun weeklyCount() = StreakUtils.weekCount(records.value)

    fun rewardCoins(amount: Int) {
        viewModelScope.launch { prefs.addPoopCoins(amount) }
    }

    suspend fun fetchRecord(id: Long) = poopRepo.getById(id)

    fun clearAllData() {
        viewModelScope.launch {
            poopRepo.deleteAll()
            lamele.extrasRepository.wipeAllExtras()
            lamele.gameStateRepository.resetAll()
            prefs.wipeAllPrefs()
            lamele.extrasRepository.seedIfNeeded()
        }
    }
}

private fun buildShitPrompt(d: CheckInDraft): String = buildString {
    appendLine("本次打卡信息（仅供幽默点评）：")
    appendLine("量级：${d.amount.label}")
    appendLine("形状：${d.shape.label}")
    appendLine("顺畅：${d.smooth.label}")
    appendLine("心情：${d.mood.label}")
    appendLine("带薪：${if (d.paid) "是" else "否"}")
    d.color?.let { appendLine("颜色：${it.label}") }
    d.city.takeIf { it.isNotBlank() }?.let { appendLine("城市/地点词：$it") }
    appendLine("场景：${d.scene.label}")
    d.note.takeIf { it.isNotBlank() }?.let { appendLine("备注：${it.take(80)}") }
}
