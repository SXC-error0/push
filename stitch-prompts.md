# 《拉了么》Stitch AI 原型生成提示词集

> 使用方法：打开 https://stitch.withgoogle.com → 粘贴英文提示词 → 生成页面
> 技巧：每次只生成一个页面；不满意点 "Regenerate"；生成后用 "Connect Screens" 串联原型

---

## Stitch 使用准备

### 第一步：设置 DESIGN.md 风格基准

在 Stitch 中先粘贴这段设计约束，让 AI 记住全局风格：

```
GLOBAL DESIGN CONSTRAINTS for all screens:
- App name: "拉了么" (Lamele) — a quirky Chinese poop-tracking & stress-relief app
- Visual style: Cute cartoon sticker aesthetic with playful emoji-driven UI
- Color palette: Cream background #FFF8F0, Warm white cards #FFFBF5, Mint green primary #66BB6A, Light mint #A5D6A7, Apricot accent #FFB74D, Sky blue #81D4FA
- Text: Primary #4E342E (warm dark brown), Secondary #8D6E63 (light brown)
- Border radius: Cards 16px, Buttons 24px, Chips 20px
- Page padding: 20px on both sides
- Card padding: 14px inside all cards
- Card gap: 12-16px between cards
- Buttons: Full-width, 52px height, rounded 24px, mint green with green glow shadow
- Typography: Headings bold, body text 16px, secondary text 14px muted
- Tone: Funny, lighthearted, stress-relieving, anti-work humor, youthful
- CRITICAL: No realistic toilet/bathroom imagery. Use emoji icons only.
- CRITICAL: Never medical or gross. Always fun and encouraging.
- Target: Chinese young professionals and college students
- Language: UI text in Chinese, labels in Chinese
- All screens are mobile app screens (375-414px wide)
```

### 第二步：逐个生成页面

按照以下顺序生成 MVP 的 8 个核心页面。

---

## 页面 1：首页 (Home Screen)

```
Design a mobile app home screen for "拉了么" (Lamele), a quirky poop-tracking app. Cute cartoon sticker style with cream background #FFF8F0. Layout top to bottom:

1. GREETING HEADER: emoji "👋" + "嘿，{nickname}" title. Subtitle: if no check-ins today show "今天你释放了吗？要不要来一拉？" (Haven't released today? Want to go?), if has check-ins show "今日已释放 X 次，继续保持" (Released X times today, keep it up)

2. FORTUNE CARD: In a white rounded card (16px radius, 14px padding) with "🔮 今日屎运签" header. Show a funny fortune line like "今日屎运：大吉，宜带薪，忌憋。" in 16px body text

3. BATTLE REPORT CARD: White card with "🧨 今日战报" header. Inside three key-value rows with emoji prefixes:
   - "🔥 连续打卡：12 天"
   - "📦 累计释放：438 次"
   - "🪙 屎币：260（打卡 +5）"

4. PRIMARY BUTTON: Full-width, 52px tall, 24px radius, mint green #66BB6A background, white bold text "今日一拉", with a subtle green glow shadow

5. SECONDARY BUTTON: Full-width, 48px tall, 24px radius, light mint background, mint green text, with money bag emoji "💰  带薪拉屎计算器"

6. TWO OUTLINED BUTTONS side by side: "⭐ 成就" and "📊 统计", outlined with mint green border, 20px radius

Make it feel fun and inviting. The user should smile and want to tap "今日一拉".
```

---

## 页面 2：打卡页面 (Check-in Screen)

```
Design a mobile app check-in recording screen for "拉了么" app. Cream background #FFF8F0. Top app bar with back arrow and centered title "今日一拉". Scrollable form in sections, each a white rounded card with emoji header:

SECTION 1 — "⏳ 时长（分钟）": Show current duration value "5 分钟" in large text, and a green slider from 1 to 60

SECTION 2 — "🗺️ 城市（屎迹地图，可空）": A text input field with placeholder "如：杭州", rounded 12px corners, mint border

SECTION 3 — "🎭 场景": Row of filter chips: 家里厕所 / 公司厕所 / 学校厕所 / 商场厕所 / 高铁厕所 / 飞机厕所 / 服务区厕所 / 景区厕所 / 朋友家厕所 / 对象家厕所 / 陌生城市厕所 / 其他. Selected chip is mint green filled. Unselected chips are outlined. 20px radius chips in a flow-row wrap layout

SECTION 4 — "⚖️ 量级": Chips: 小试牛刀 / 略有成果 / 一泻千里 / 山崩地裂 / 核弹级释放 / 今日肠道毕业设计

SECTION 5 — "🍌 形状": Chips: 兔子屎 / 香蕉王者 / 泥石流 / 奶茶珍珠 / 意大利面 / 灵魂散装 / 见不得人的抽象派

SECTION 6 — "🎨 颜色": Chips: 不记录 / 棕色 / 黄色 / 绿色 / 黑色 / 红色 / 其他

SECTION 7 — "💨 顺畅度": Chips: 顺畅 / 一般 / 困难 / 灾难

SECTION 8 — "😊 心情": Chips: 释怀 / 悔恨 / 通透 / 重获新生 / 怀疑人生 / 感谢马桶接住了我

SECTION 9 — "✅ 选项": Two toggle switches side by side with labels "带薪拉屎" and "同步屎友圈（仅占位）"

SECTION 10 — "📝 备注": Multi-line text input, rounded 12px, mint border

BOTTOM: Full-width "完成打卡" primary button, 52px height, 24px radius, mint green #66BB6A with green glow shadow
```

---

## 页面 3：打卡成功页 (Success Screen)

```
Design a mobile app celebration success screen for "拉了么" after completing a check-in. Cream background #FFF8F0. This is the reward moment — make it joyful with bounce-worthy design.

1. TOP: Large celebration emoji "🎉" centered, with bold congratulatory text below randomly showing one of:
   "肠道已完成版本更新" or "恭喜你，成功卸下人生负担" or "马桶已接收你的情绪垃圾" or "你不是在拉屎，你是在重启系统"

2. RECORD SUMMARY CARD: White rounded card (16px radius) showing this check-in's details in key-value rows:
   - 时长: 5 分钟
   - 量级: 略有成果
   - 形状: 香蕉王者
   - 顺畅度: 顺畅
   - 心情: 释怀

3. AI COMMENT CARD: A slightly different card with highlight border in mint green, "🤖 AI 屎评官" header. Show a funny AI-generated comment like:
   "今日肠道表现稳定，形态接近"王者香蕉"，释放过程顺滑，说明你和身体达成了短暂和平。建议奖励自己一杯温水，不建议奖励麻辣烫。"

4. REWARD SECTION: A highlighted area showing "+5 屎币 🪙" with coin gain animation (just show the number prominently)

5. BOTTOM BUTTONS: Two buttons side by side:
   - "返回首页" (outlined, 20px radius)
   - "再来一拉" (filled mint green, 24px radius)

Make the whole screen feel like a warm, funny, rewarding moment. The user should feel proud and amused.
```

---

## 页面 4：带薪拉屎计算器 (Paid Poop Calculator)

```
Design a mobile app calculator screen for "拉了么" that calculates how much money you "earned back" from your employer by using the bathroom on company time. The vibe is "funny anti-work humor" but lighthearted. Cream background #FFF8F0.

1. TITLE: "💰 带薪拉屎计算器" with money bag emoji

2. INPUT SECTION (white card):
   - "月薪（元）": number input, default 12000
   - "每月工作天数": number input, default 22
   - "每天工作小时": number input, default 8
   - "本次拉屎时长（分钟）": number input, default 5

3. CALCULATION RESULT CARD (highlighted, with green accent border):
   - "时薪：25.00 元/小时"
   - "本次收益：2.08 元" in large bold text

4. MONTHLY SUMMARY CARD (white card):
   - "📊 本月带薪拉屎收益"
   - Large number: "44.58 元" in bold 28px
   - Smaller text: "累计带薪蹲坑 1 小时 47 分钟"
   - "带薪次数：8 次"

5. SHARE BUTTON: Full-width secondary button "生成分享卡片" with share icon, to generate a shareable image

6. FUNNY TAGLINE at bottom in small text: "上班可以忍，屎不能憋。公司欠你的，从厕所拿回来。"

The calculator should feel like a fun tool you'd screenshot and send to coworkers.
```

---

## 页面 5：成就墙 (Achievements Wall)

```
Design a mobile app achievements/trophies screen for "拉了么". Game-like collection feel but cute and funny. Cream background #FFF8F0.

1. HEADER: "🏆 成就墙" with subtitle "10 个成就等你解锁"

2. PROGRESS BAR at top: "已解锁 4/10" with a green progress bar

3. ACHIEVEMENT GRID (2 columns, scrollable) of cards, each showing:
   - Large emoji icon
   - Achievement name in Chinese (bold, 14px)
   - Unlock condition in small text (12px, muted)
   - If unlocked: full color card with checkmark
   - If locked: grayed out / slightly transparent

Achievements to show:
   ✅ 晨间第一炮 — 早上8点前打卡 (unlocked, bright)
   ✅ 带薪修仙 — 记录过带薪释放 (unlocked, bright)
   ✅ 顺滑如德芙 — 连续7天顺畅 (unlocked, bright)
   ✅ 王者香蕉 — 记录过香蕉王者形态 (unlocked, bright)
   🔒 三分钟真男人 — 3分钟内解决 (locked, dimmed)
   🔒 厕所冥想家 — 单次≥20分钟 (locked, dimmed)
   🔒 故乡守护者 — 连续7天在家打卡 (locked, dimmed)
   🔒 一泻千里 — 记录过泥石流型 (locked, dimmed)
   🔒 深夜释放者 — 凌晨打卡 (locked, dimmed)
   🔒 我和马桶和解了 — 连续打卡30天 (locked, dimmed, with progress "12/30")

4. Each card should have 16px radius, soft shadow, and padding 12px. Unlocked cards have a subtle green glow.

Make it feel like collecting Pokémon badges — fun, motivating, and shareable.
```

---

## 页面 6：个人统计页 (Stats Dashboard — Cyber Mode)

```
Design a mobile app statistics dashboard for "拉了么". THIS SCREEN USES THE CYBER/NEON MODE as a fun contrast — dark background #1A1A2E with neon green #00FF88 data highlights. This is the "screenshot and share" screen.

1. HEADER in neon green: "📊 肠道系统监控" (Gut System Monitor) with a subtle scan-line effect

2. STATS GRID (2x2) of dark surface cards (#16213E background, 12px radius):
   - "总释放次数" → "438" in large neon green numbers
   - "连续打卡" → "12 天" in neon green
   - "本周次数" → "9 次" in neon green
   - "本月带薪收益" → "44.58 元" in neon green

3. CHARTS SECTION:
   - "📈 每日打卡趋势" — a simple bar chart in neon green bars on dark background
   - "🍌 形状分布" — donut/pie chart showing shape breakdown with neon color segments
   - "💨 顺畅率" — "87%" with a circular progress indicator around it

4. FUN METRICS at bottom in smaller neon-muted text:
   - "最长一次: 31 分钟（冥想级）"
   - "最常去: 公司3楼"
   - "最佳状态月份: 5月"

5. Small note at very bottom: "数据仅供娱乐，不构成医疗建议" in muted #7B8D9E

The visual contrast between this dark neon dashboard and the warm cute app is intentional — it should feel like accessing a secret "power user" mode. Think: Strava for bathroom habits.
```

---

## 页面 7：屎迹地图 (Poop Map)

```
Design a mobile app map screen for "拉了么" showing check-in locations. Cute cartoon style overlay on a simplified map. Cream background sections above and below the map.

1. MAP HEADER STATS (above the map, in a white card):
   - "🗺️ 已点亮 8 个城市"
   - "📍 打卡 23 个地点"
   - "🏆 最近新增：杭州 · 公司3楼"

2. MAP VIEW (center, ~300px height): Show a simplified map with cute custom pin markers (use toilet emoji 🚽 pins or star pins, NOT realistic markers). Different colored pins for different scene types:
   - 🏠 Green pin = Home
   - 🏢 Blue pin = Office
   - 🏫 Yellow pin = School
   - 🛒 Orange pin = Mall
   - ✈️ Purple pin = Travel

3. LOCATION LIST (below map, white card):
   - Each row shows: emoji pin + location name + date
   - "🏠 家 · 今天 09:30"
   - "🏢 公司3楼 · 昨天 14:20"
   - "🏫 图书馆 · 3天前"
   - "🛒 万达商场 · 5天前"

4. PRIVACY BADGE at top-right corner: "🔒 精确位置仅自己可见" in small text

5. ADD LOCATION button: "➕ 添加新地点" secondary button

The map should feel like a fun travel diary, not a serious GPS tracker. Keep it light and playful.
```

---

## 页面 8：发现页 (Discover Hub)

```
Design a mobile app discover/exploration hub screen for "拉了么" showing all extra features. Cream background #FFF8F0. This is a feature gateway — make each card tappable and inviting.

1. HEADER: "🧭 发现" with subtitle "以下为全功能模块——休闲娱乐，轻度解压"

2. SCROLLABLE LIST of feature entry cards. Each card is a white rounded rectangle (16px radius) with:
   - Emoji + feature name as title (bold)
   - Short description in muted text (12px)
   - The card should feel tappable with subtle shadow

Feature list (in order):
   🏆 排行榜 — 附近 / 好友 / 带薪排名
   ⚔️ 好友 PK — 和损友比谁更通畅
   🚽 厕所探索与评分 — 找好厕所+雷达图点评
   💬 屎友圈 — 动态流 · 冲了/懂你 互动
   🕳️ 厕所树洞 — 匿名发疯与人生哲学
   📣 拉屎弹幕 — 附近匿名弹幕陪你蹲
   🔮 今日屎运签 — 赛博求签搞怪预测
   🤖 AI 屎诗 — 打油诗生成器·多风格
   🧠 肠道人格测试 — 5题无厘头诊断
   🪙 屎币小店 — 头像框/称号兑换
   🎒 装扮背包 — 已解锁道具查看
   🫧 冲水连击 — 10秒小游戏
   🐳 马桶养成 — 喂食升级你的桶
   🌱 肠道农场 — 种菜收菜
   📊 周报/月报/年报 — 文本战报+分享
   👯 好友列表 — 内置损友可扩展
   🥦 饮食与轻健康 — 温和提醒非医疗
   🏅 赛季活动 — 春季通畅杯任务
   🧑‍⚖️ AI 屎评官 — 规则说明

3. Each card has a subtle right arrow ">" indicating it's tappable

Make the hub feel like opening a treasure chest of fun mini-features. The visual density should invite exploration.
```

---

## Stitch 操作步骤

### 第 1 步：设置全局风格
1. 打开 stitch.withgoogle.com，登录 Google 账号
2. 在 Stitch 的 prompt 输入框中粘贴"GLOBAL DESIGN CONSTRAINTS"
3. 等待 AI 理解（它会记住这些约束用于后续生成）

### 第 2 步：逐个生成页面
1. 粘贴"页面 1：首页"的提示词 → 点击 Generate
2. 不满意就点 Regenerate，或用语音说 "make the button bigger"
3. 满意后点 "Save" 保存
4. 重复以上步骤生成全部 8 个页面

### 第 3 步：连接页面制作交互原型
1. 点击 Stitch 的 "Play" 按钮进入交互模式
2. 选中"今日一拉"按钮 → 链接到"打卡页面"
3. 选中打卡页的"完成打卡" → 链接到"打卡成功页"
4. 选中成功页的"返回首页" → 链接回"首页"
5. 首页的计算器/成就/统计按钮 → 链接到对应页面
6. 重复完成所有页面跳转

### 第 4 步：预览与分享
1. 点击 "Preview" 预览完整原型
2. 点击 "Share" 生成分享链接
3. 在手机上打开链接，体验真实手感
4. 分享给朋友/用户收集反馈

### 第 5 步：导出
1. 导出到 Figma：自动保留图层和 Auto Layout
2. 或导出 HTML/CSS/React 代码
3. 将 DESIGN.md 中的 tokens 导入 Figma 保证一致

---

## Stitch 提示词优化技巧

1. **每次只描述一个页面** — 不要在一个 prompt 里要多个页面
2. **用英文写提示词** — Stitch 底层 Gemini 对英文理解更精确，但 UI 文字保持中文
3. **指定具体色号** — 用 #FFF8F0 而非 "cream"，确保颜色准确
4. **分层描述** — 用 NUMBERED LIST 按从上到下描述页面结构
5. **给出文字内容** — 直接给 UI 文案，AI 不需要猜写什么
6. **追加微调** — 如果某处不对，直接发补充指令如 "Make the primary button 52px height instead"
7. **用 DESIGN.md 跨页面同步** — 第一个页面满意后，导出 DESIGN.md，后续页面引用它
