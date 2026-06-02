package com.lamele.app.domain

import com.lamele.app.data.local.PoopRecordEntity
import java.util.Calendar

object StreakUtils {
    private val cal = Calendar.getInstance()

    fun currentStreak(records: List<PoopRecordEntity>): Int {
        if (records.isEmpty()) return 0
        val days = records.map { dayStart(it.timeMillis) }.toSet()
        cal.timeInMillis = System.currentTimeMillis()
        zeroTime()
        var cursor = cal.timeInMillis
        if (cursor !in days) cursor -= DAY_MS
        if (cursor !in days) return 0
        var n = 0
        while (cursor in days) {
            n++
            cursor -= DAY_MS
        }
        return n
    }

    fun weekCount(records: List<PoopRecordEntity>): Int {
        cal.timeInMillis = System.currentTimeMillis()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        zeroTime()
        val start = cal.timeInMillis
        val now = System.currentTimeMillis()
        return records.count { it.timeMillis in start..now }
    }

    private fun zeroTime() {
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
    }

    private fun dayStart(millis: Long): Long {
        cal.timeInMillis = millis
        zeroTime()
        return cal.timeInMillis
    }

    private const val DAY_MS = 24L * 60 * 60 * 1000
}

object FortuneSign {
    private val lines = listOf(
        "今日屎运：大吉，宜带薪，忌憋。",
        "今日梗运：开会前释放，智商在线。",
        "肠道星象：水星顺行，通畅加成。",
        "宜：点赞自己。忌：低估马桶。",
        "今日人设：带薪哲学家。",
    )

    fun todayLine(): String {
        val day = System.currentTimeMillis() / DAY_MS
        return lines[(day % lines.size).toInt()]
    }

    private const val DAY_MS = 24L * 60 * 60 * 1000
}
