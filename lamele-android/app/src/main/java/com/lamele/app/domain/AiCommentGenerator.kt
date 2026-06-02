package com.lamele.app.domain

import com.lamele.app.model.AmountLevel
import com.lamele.app.model.MoodType
import com.lamele.app.model.ShapeType
import com.lamele.app.model.SmoothLevel
import kotlin.random.Random

/**
 * 本地规则模拟「AI 屎评」，后续可替换为云端大模型。
 */
object AiCommentGenerator {
    private val generic = listOf(
        "今日一拉，世界少了一份压力。",
        "恭喜你，成功卸下人生负担。",
        "马桶已接收你的情绪垃圾。",
        "你不是在拉屎，你是在重启系统。",
        "肠道已完成版本更新。",
    )

    fun generate(
        amount: AmountLevel,
        shape: ShapeType,
        smooth: SmoothLevel,
        mood: MoodType,
        paid: Boolean,
    ): String {
        val parts = mutableListOf<String>()
        parts += when (amount) {
            AmountLevel.SMALL, AmountLevel.MEDIUM -> "这次释放相当克制，"
            AmountLevel.BIG, AmountLevel.HUGE -> "货量感人，"
            AmountLevel.NUKE, AmountLevel.GRAD -> "史诗级排量，"
        }
        parts += when (shape) {
            ShapeType.BANANA -> "形态堪称香蕉王者。"
            ShapeType.MUDSLIDE -> "泥石流型选手，注意补水。"
            ShapeType.RABBIT -> "兔子屎出没，今天纤维够吗？"
            ShapeType.BOBA -> "奶茶珍珠风，肠道在玩消消乐。"
            ShapeType.PASTA, ShapeType.CHAOS, ShapeType.ABSTRACT -> "抽象派大作，只允许自己欣赏。"
        }
        parts += when (smooth) {
            SmoothLevel.SMOOTH -> "顺畅得像德芙，建议写进周报。"
            SmoothLevel.OK -> "中规中矩，马桶表示还能接住。"
            SmoothLevel.HARD, SmoothLevel.DISASTER -> "历程略坎坷，下次对自己温柔点。"
        }
        parts += when (mood) {
            MoodType.RELIEF, MoodType.CLEAR, MoodType.REBORN -> "心情通透，今日人设：已卸载缓存。"
            MoodType.REGRET, MoodType.DOUBT -> "情绪复杂，但至少带宽腾出来了。"
            MoodType.THANKS -> "感恩马桶承重，功德+1。"
        }
        if (paid) {
            parts += " 带薪释放，公司正在为这次重启买单。"
        }
        val core = parts.joinToString("")
        val tail = generic[Random.nextInt(generic.size)]
        return "$core $tail"
    }
}
