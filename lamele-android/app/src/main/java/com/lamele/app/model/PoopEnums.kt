package com.lamele.app.model

/** 拉屎量级 */
enum class AmountLevel(val label: String) {
    SMALL("小试牛刀"),
    MEDIUM("略有成果"),
    BIG("一泻千里"),
    HUGE("山崩地裂"),
    NUKE("核弹级释放"),
    GRAD("今日肠道毕业设计"),
}

/** 搞怪布里斯托 */
enum class ShapeType(val label: String, val emoji: String) {
    RABBIT("兔子探", "🫘"),
    BANANA("香蕉王者", "🍌"),
    MUDSLIDE("泥石流", "💧"),
    BOBA("奶茶珍珠", "🧋"),
    PASTA("意大利面", "🍝"),
    CHAOS("灵魂散装", "✨"),
    ABSTRACT("抽象派", "🎨"),
}

enum class ColorType(val label: String) {
    BROWN("棕色"),
    YELLOW("黄色"),
    GREEN("绿色"),
    BLACK("黑色"),
    RED("红色"),
    OTHER("其他"),
}

enum class SmoothLevel(val label: String, val emoji: String) {
    SMOOTH("顺畅", "🌿"),
    OK("一般", "☁️"),
    HARD("困难", "⚡"),
    DISASTER("灾难", "🌋"),
}

enum class MoodType(val label: String, val emoji: String) {
    RELIEF("释怀", "🕊️"),
    REGRET("悔恨", "😅"),
    CLEAR("通透", "💎"),
    REBORN("重获新生", "☀️"),
    DOUBT("怀疑人生", "🤔"),
    THANKS("感谢马桶", "🙏"),
}

enum class SceneType(val label: String) {
    HOME("家里"),
    COMPANY("公司"),
    SCHOOL("学校"),
    MALL("商场"),
    TRAIN("高铁"),
    PLANE("飞机"),
    SERVICE("服务区"),
    SCENIC("景区"),
    FRIEND("朋友家"),
    PARTNER("对象家"),
    STRANGE_CITY("陌生城市"),
    OTHER("其他"),
}
