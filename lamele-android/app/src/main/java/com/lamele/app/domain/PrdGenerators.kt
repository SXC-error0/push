package com.lamele.app.domain

import com.lamele.app.data.local.PoopRecordEntity
import com.lamele.app.data.local.ToiletReviewEntity
import com.lamele.app.model.ShapeType
import com.lamele.app.model.SmoothLevel
import java.util.Calendar
import kotlin.math.roundToInt

object ToiletGrade {
    fun fromReviews(reviews: List<ToiletReviewEntity>): String {
        if (reviews.isEmpty()) return "待评分的神秘角落"
        val avgs = listOf(
            reviews.map { it.cleanliness },
            reviews.map { it.privacy },
            reviews.map { it.paper },
            reviews.map { it.flushPower },
            reviews.map { it.comfort },
        ).map { row -> row.average() }
        val avg = avgs.average()
        return label(avg)
    }

    private fun label(avg: Double): String = when {
        avg >= 4.8 -> "神之马桶"
        avg >= 4.2 -> "肠道避难所"
        avg >= 3.6 -> "史诗级厕所"
        avg >= 3.0 -> "钻石厕所"
        avg >= 2.2 -> "黄金厕所"
        else -> "青铜厕所"
    }
}

object ReportGenerator {
    enum class Period { WEEK, MONTH, YEAR }

    fun build(records: List<PoopRecordEntity>, period: Period): String {
        val cal = Calendar.getInstance()
        val end = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        when (period) {
            Period.WEEK -> cal.add(Calendar.DAY_OF_MONTH, -7)
            Period.MONTH -> cal.add(Calendar.MONTH, -1)
            Period.YEAR -> cal.add(Calendar.YEAR, -1)
        }
        val start = cal.timeInMillis
        val slice = records.filter { it.timeMillis in start..end }
        if (slice.isEmpty()) {
            return "本${period.zh()}没有任何释放记录——你在憋大招吗？"
        }
        val totalMin = slice.sumOf { it.durationMinutes }
        val paid = slice.count { it.isPaidPoop }
        val smoothPct = slice.count { it.smoothLevel == SmoothLevel.SMOOTH.name } * 100f / slice.size
        val topShape = slice.groupingBy { it.shapeType }.eachCount().maxByOrNull { it.value }?.key
        val shapeZh = topShape?.let {
            runCatching { enumValueOf<ShapeType>(it).label }.getOrNull()
        } ?: "混搭"
        return buildString {
            appendLine("【拉了么 · ${period.zh()}战报】")
            appendLine("释放次数：${slice.size} 次")
            appendLine("总蹲坑时长：约 $totalMin 分钟")
            appendLine("带薪次数：$paid")
            appendLine("顺畅率：${smoothPct.roundToInt()}%")
            appendLine("主打形态：$shapeZh")
            appendLine()
            appendLine(
                when (period) {
                    Period.WEEK -> "保持节奏，下周继续占领马桶高地。"
                    Period.MONTH -> "月度肠道 KPI 已归档，记得截图给损友。"
                    Period.YEAR -> "这一年，你认真对待了每一次释放。"
                },
            )
        }
    }

    private fun Period.zh() = when (this) {
        Period.WEEK -> "周"
        Period.MONTH -> "月"
        Period.YEAR -> "年"
    }
}

object PoetryGenerator {
    fun shitPoem(last: PoopRecordEntity?): String {
        if (last == null) {
            return "尚未有诗，先去今日一拉。\n—— 拉了么伪莎士比亚"
        }
        val shape = runCatching { enumValueOf<ShapeType>(last.shapeType).label }.getOrNull() ?: "未知"
        return """
            《释放三章》
            马桶承我意，沉默亦温柔。
            今日成$shape，明日更自由。
            —— AI 屎诗生成器（本地打油版）
        """.trimIndent()
    }
}

data class GutQuestion(val q: String, val options: List<String>)
data class GutResult(val title: String, val desc: String)

object GutPersonalityQuiz {
    val questions = listOf(
        GutQuestion("蹲坑时你更常？", listOf("刷短视频", "思考人生", "赶工作消息")),
        GutQuestion("通畅程度对你意味着？", listOf("今日运势晴雨表", "饮水 KPI 反馈", "纯随机")),
        GutQuestion("公司厕所对你来说？", listOf("第二工位", "紧急避难所", "尽量不去")),
        GutQuestion("出差释放首选？", listOf("酒店干净第一", "商场就近即可", "高铁极限操作")),
        GutQuestion("带薪拉屎态度？", listOf("合规薅羊毛", "偶尔为之", "我热爱工作")),
    )

    fun result(choices: List<Int>): GutResult {
        val sum = choices.sum()
        return when {
            sum <= 5 -> GutResult("宿便型哲学家", "你擅长把压力转成蹲坑冥想，建议保持纤维摄入。")
            sum <= 9 -> GutResult("职场游侠型肠道", "你在公司与厕所之间走位灵活，带薪与效能平衡大师。")
            else -> GutResult("紧绷型战士", "你更习惯快节奏，记得别硬憋，对身体诚实一点。")
        }
    }
}

object LeaderboardMock {
    data class Row(val name: String, val score: Int, val isYou: Boolean = false)

    fun nearbyYou(userWeekly: Int): List<Row> {
        val base = listOf(
            Row("匿名勇士073", 12),
            Row("隔壁工位神秘人", 9),
            Row("楼下咖啡续命族", 11),
            Row("地铁闯关王", 7),
        ).shuffled().take(4)
        return (base + Row("你", userWeekly, true)).sortedByDescending { it.score }
    }

    fun paidRanking(userMin: Int): List<Row> {
        val rows = listOf(
            Row("匿名氪肠人", 55),
            Row("隔壁大厅", 41),
            Row("高铁站魂", 38),
            Row("你", userMin, true),
        )
        return rows.sortedByDescending { it.score }
    }
}

object SeasonDefinitions {
    const val TITLE = "四月：春季通畅杯（本地赛季）"

    fun tasks(streak: Int, totalRecords: Int, toiletCount: Int): List<Pair<String, Boolean>> {
        return listOf(
            "连续打卡 7 天" to (streak >= 7),
            "完成 10 次今日一拉" to (totalRecords >= 10),
            "收藏 3 个厕所档案" to (toiletCount >= 3),
            "使用发现页任意功能" to (streak >= 1),
            "把周报分享给好友（心证即可）" to (totalRecords >= 3),
        )
    }
}
