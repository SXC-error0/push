package com.lamele.app.domain

import com.lamele.app.data.local.PoopRecordEntity
import com.lamele.app.model.SceneType
import com.lamele.app.model.ShapeType
import com.lamele.app.model.SmoothLevel
import java.util.Calendar

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
)

object AchievementChecker {
    fun allDefinitions(): List<Achievement> = listOf(
        Achievement("morning", "晨间第一炮", "早上 8 点前完成释放"),
        Achievement("night", "深夜释放者", "凌晨时段完成打卡"),
        Achievement("fast", "三分钟真男人", "3 分钟内解决战斗"),
        Achievement("meditate", "厕所冥想家", "单次蹲坑 ≥20 分钟"),
        Achievement("paid", "带薪修仙", "记录过一次带薪释放"),
        Achievement("home", "故乡守护者", "连续 7 天在家打卡"),
        Achievement("smooth", "顺滑如德芙", "连续 7 天记录为顺畅"),
        Achievement("mud", "一泻千里", "记录过泥石流型"),
        Achievement("banana", "王者香蕉", "记录过香蕉王者形态"),
        Achievement("30d", "我和马桶和解了", "连续打卡满 30 天"),
    )

    private val cal = Calendar.getInstance()

    private fun hourOfDay(millis: Long): Int {
        cal.timeInMillis = millis
        return cal.get(Calendar.HOUR_OF_DAY)
    }

    fun evaluate(records: List<PoopRecordEntity>): List<Achievement> {
        if (records.isEmpty()) return emptyList()
        val unlocked = mutableListOf<Achievement>()
        val byDay = records.groupBy { dayStart(it.timeMillis) }
        val days = byDay.keys.toSet()

        if (records.any { hourOfDay(it.timeMillis) < 8 }) {
            unlocked += Achievement("morning", "晨间第一炮", "早上 8 点前完成释放")
        }
        if (records.any {
            val h = hourOfDay(it.timeMillis)
            h >= 23 || h < 5
        }) {
            unlocked += Achievement("night", "深夜释放者", "凌晨时段完成打卡")
        }
        if (records.any { it.durationMinutes in 1..3 }) {
            unlocked += Achievement("fast", "三分钟真男人", "3 分钟内解决战斗")
        }
        if (records.any { it.durationMinutes >= 20 }) {
            unlocked += Achievement("meditate", "厕所冥想家", "单次蹲坑 ≥20 分钟")
        }
        if (records.any { it.isPaidPoop }) {
            unlocked += Achievement("paid", "带薪修仙", "记录过一次带薪释放")
        }
        if (longestStreakSameScene(records, SceneType.HOME.name) >= 7) {
            unlocked += Achievement("home", "故乡守护者", "连续 7 天在家打卡")
        }
        if (longestStreakSmooth(records) >= 7) {
            unlocked += Achievement("smooth", "顺滑如德芙", "连续 7 天记录为顺畅")
        }
        if (records.any { it.shapeType == ShapeType.MUDSLIDE.name }) {
            unlocked += Achievement("mud", "一泻千里", "记录过泥石流型")
        }
        if (records.any { it.shapeType == ShapeType.BANANA.name }) {
            unlocked += Achievement("banana", "王者香蕉", "记录过香蕉王者形态")
        }
        if (computeStreakDays(days) >= 30) {
            unlocked += Achievement("30d", "我和马桶和解了", "连续打卡满 30 天")
        }
        return unlocked.distinctBy { it.id }
    }

    private fun dayStart(millis: Long): Long {
        cal.timeInMillis = millis
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun computeStreakDays(daySet: Set<Long>): Int {
        if (daySet.isEmpty()) return 0
        cal.timeInMillis = System.currentTimeMillis()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        var cursor = cal.timeInMillis
        if (cursor !in daySet) cursor -= DAY_MS
        if (cursor !in daySet) return 0
        var n = 0
        while (cursor in daySet) {
            n++
            cursor -= DAY_MS
        }
        return n
    }

    private fun longestStreakSameScene(records: List<PoopRecordEntity>, scene: String): Int {
        val sortedDays = records
            .filter { it.sceneType == scene }
            .map { dayStart(it.timeMillis) }
            .distinct()
            .sorted()
        return longestConsecutive(sortedDays)
    }

    private fun longestStreakSmooth(records: List<PoopRecordEntity>): Int {
        val sortedDays = records
            .filter { it.smoothLevel == SmoothLevel.SMOOTH.name }
            .map { dayStart(it.timeMillis) }
            .distinct()
            .sorted()
        return longestConsecutive(sortedDays)
    }

    private fun longestConsecutive(sortedDays: List<Long>): Int {
        if (sortedDays.isEmpty()) return 0
        var best = 1
        var cur = 1
        for (i in 1 until sortedDays.size) {
            if (sortedDays[i] == sortedDays[i - 1] + DAY_MS) {
                cur++
                best = maxOf(best, cur)
            } else {
                cur = 1
            }
        }
        return best
    }

    private const val DAY_MS = 24L * 60 * 60 * 1000
}
