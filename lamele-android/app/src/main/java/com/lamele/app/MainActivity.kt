package com.lamele.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lamele.app.domain.AchievementChecker
import com.lamele.app.ui.AppViewModel
import com.lamele.app.ui.FeaturesViewModel
import com.lamele.app.ui.screens.AchievementsScreen
import com.lamele.app.ui.screens.CalculatorScreen
import com.lamele.app.ui.screens.CheckInScreen
import com.lamele.app.ui.screens.HistoryScreen
import com.lamele.app.ui.screens.HomeScreen
import com.lamele.app.ui.screens.LoginScreen
import com.lamele.app.ui.screens.MapScreen
import com.lamele.app.ui.screens.PrivacyScreen
import com.lamele.app.ui.screens.ProfileScreen
import com.lamele.app.ui.screens.StatsScreen
import com.lamele.app.ui.screens.SuccessScreen
import com.lamele.app.ui.screens.discover.DiscoverScreen
import com.lamele.app.ui.screens.prd.AiOfficerScreen
import com.lamele.app.ui.screens.prd.CoinShopScreen
import com.lamele.app.ui.screens.prd.CosmeticsScreen
import com.lamele.app.ui.screens.prd.DanmakuScreen
import com.lamele.app.ui.screens.prd.DietHealthScreen
import com.lamele.app.ui.screens.prd.FarmScreen
import com.lamele.app.ui.screens.prd.FeedPublishScreen
import com.lamele.app.ui.screens.prd.FeedScreen
import com.lamele.app.ui.screens.prd.FortuneScreen
import com.lamele.app.ui.screens.prd.FriendsListScreen
import com.lamele.app.ui.screens.prd.GutTestScreen
import com.lamele.app.ui.screens.prd.LeaderboardScreen
import com.lamele.app.ui.screens.prd.MiniGameScreen
import com.lamele.app.ui.screens.prd.PetScreen
import com.lamele.app.ui.screens.prd.PkScreen
import com.lamele.app.ui.screens.prd.PoetryScreen
import com.lamele.app.ui.screens.prd.ReportsScreen
import com.lamele.app.ui.screens.prd.SeasonScreen
import com.lamele.app.ui.screens.prd.ToiletAddScreen
import com.lamele.app.ui.screens.prd.ToiletDetailScreen
import com.lamele.app.ui.screens.prd.ToiletListScreen
import com.lamele.app.ui.screens.prd.ToiletRateScreen
import com.lamele.app.ui.screens.prd.TreeHoleScreen
import com.lamele.app.ui.screens.prd.TreePublishScreen
import com.lamele.app.ui.theme.LameleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LameleTheme {
                val vm: AppViewModel = viewModel()
                val loggedIn by vm.loggedIn.collectAsState(false)
                Crossfade(targetState = loggedIn, label = "auth") { inApp ->
                    if (!inApp) {
                        LoginScreen { vm.setNickname(it) }
                    } else {
                        LameleNav(vm)
                    }
                }
            }
        }
    }
}

private enum class MainTab(val label: String) {
    HOME("首页"),
    STATS("统计"),
    MAP("地图"),
    DISCOVER("发现"),
}

@Composable
private fun LameleNav(vm: AppViewModel) {
    val featVm: FeaturesViewModel = viewModel()
    val navController = rememberNavController()
    val navBackStack by navController.currentBackStackEntryAsState()
    val route = navBackStack?.destination?.route
    val hideBottom = route != null && route != "main"
    var tab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            if (!hideBottom) {
                NavigationBar {
                    MainTab.entries.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = tab == index,
                            onClick = { tab = index },
                            icon = {
                                Icon(
                                    imageVector = when (item) {
                                        MainTab.HOME -> Icons.Default.Home
                                        MainTab.STATS -> Icons.Default.ShowChart
                                        MainTab.MAP -> Icons.Default.Map
                                        MainTab.DISCOVER -> Icons.Default.Explore
                                    },
                                    contentDescription = item.label,
                                )
                            },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = androidx.compose.ui.graphics.Color(0xFF126D27),
                                selectedTextColor = androidx.compose.ui.graphics.Color(0xFF126D27),
                                indicatorColor = androidx.compose.ui.graphics.Color(0xFF66BB6A),
                            ),
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(padding),
        ) {
            composable("main") {
                val home by vm.homeState.collectAsState()
                val records by vm.records.collectAsState()
                val toilets by featVm.toilets.collectAsState()
                when (tab) {
                    MainTab.HOME.ordinal -> HomeScreen(
                        state = home,
                        onCheckIn = { navController.navigate("checkin") },
                        onCalculator = { navController.navigate("calculator") },
                        onAchievements = { navController.navigate("achievements") },
                        onHistory = { navController.navigate("history") },
                        onSettings = { navController.navigate("profile") },
                    )
                    MainTab.STATS.ordinal -> CalculatorScreen(
                        viewModel = vm,
                        onBack = {},
                        showBackButton = false,
                    )
                    MainTab.MAP.ordinal -> MapScreen(records = records, toilets = toilets)
                    MainTab.DISCOVER.ordinal -> DiscoverScreen(
                        onOpen = { r -> navController.navigate(r) },
                    )
                }
            }
            composable("checkin") {
                CheckInScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onSaved = { id ->
                        navController.navigate("success/$id") {
                            popUpTo("checkin") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                )
            }
            composable(
                route = "success/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType }),
            ) { entry ->
                val id = entry.arguments?.getLong("id") ?: return@composable
                SuccessScreen(
                    recordId = id,
                    viewModel = vm,
                    onHome = { navController.popBackStack("main", inclusive = false) },
                    onAgain = {
                        navController.popBackStack("main", inclusive = false)
                        navController.navigate("checkin")
                    },
                )
            }
            composable("calculator") {
                CalculatorScreen(viewModel = vm, onBack = { navController.popBackStack() })
            }
            composable("achievements") {
                val records by vm.records.collectAsState()
                val achievements = remember(records) { AchievementChecker.evaluate(records) }
                AchievementsScreen(achievements = achievements, onBack = { navController.popBackStack() })
            }
            composable("stats") {
                val records by vm.records.collectAsState()
                StatsScreen(
                    records = records,
                    monthlySalary = vm.monthlySalary.value,
                    workDays = vm.workDays.value,
                    workHours = vm.workHours.value,
                    onBack = { navController.popBackStack() },
                )
            }
            composable("history") {
                val records by vm.records.collectAsState()
                HistoryScreen(
                    records = records,
                    onOpen = { id -> navController.navigate("success/$id") },
                )
            }
            composable("profile") {
                val home by vm.homeState.collectAsState()
                ProfileScreen(
                    home = home,
                    title = vm.userTitle(),
                    weekCount = vm.weeklyCount(),
                    monthPaid = vm.monthPaidTotal(),
                    onCalculator = { navController.navigate("calculator") },
                    onAchievements = { navController.navigate("achievements") },
                    onStats = { navController.navigate("stats") },
                    onPrivacy = { navController.navigate("privacy") },
                    onClearData = { vm.clearAllData() },
                )
            }
            composable("privacy") {
                PrivacyScreen(viewModel = vm, onBack = { navController.popBackStack() })
            }
            composable("leaderboard") {
                LeaderboardScreen(vm, onBack = { navController.popBackStack() })
            }
            composable("pk") {
                PkScreen(vm, featVm, onBack = { navController.popBackStack() })
            }
            composable("toilets") {
                ToiletListScreen(
                    featVm,
                    onBack = { navController.popBackStack() },
                    onDetail = { id -> navController.navigate("toilet_detail/$id") },
                    onAdd = { navController.navigate("toilet_add") },
                )
            }
            composable("toilet_add") {
                ToiletAddScreen(
                    featVm,
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() },
                )
            }
            composable(
                "toilet_detail/{id}",
                listOf(navArgument("id") { type = NavType.LongType }),
            ) { entry ->
                val id = entry.arguments?.getLong("id") ?: return@composable
                ToiletDetailScreen(
                    toiletId = id,
                    featVm = featVm,
                    onBack = { navController.popBackStack() },
                    onRate = { navController.navigate("toilet_rate/$id") },
                )
            }
            composable(
                "toilet_rate/{id}",
                listOf(navArgument("id") { type = NavType.LongType }),
            ) { entry ->
                val id = entry.arguments?.getLong("id") ?: return@composable
                ToiletRateScreen(id, featVm, onBack = { navController.popBackStack() })
            }
            composable("feed") {
                FeedScreen(
                    featVm,
                    onBack = { navController.popBackStack() },
                    onPublish = { navController.navigate("feed_publish") },
                )
            }
            composable("feed_publish") {
                FeedPublishScreen(featVm, onBack = { navController.popBackStack() })
            }
            composable("tree") {
                TreeHoleScreen(
                    featVm,
                    onBack = { navController.popBackStack() },
                    onPublish = { navController.navigate("tree_publish") },
                )
            }
            composable("tree_publish") {
                TreePublishScreen(featVm, onBack = { navController.popBackStack() })
            }
            composable("danmaku") {
                DanmakuScreen(featVm, onBack = { navController.popBackStack() })
            }
            composable("fortune") {
                FortuneScreen(onBack = { navController.popBackStack() })
            }
            composable("poetry") {
                PoetryScreen(vm, onBack = { navController.popBackStack() })
            }
            composable("gut_test") {
                GutTestScreen(onBack = { navController.popBackStack() })
            }
            composable("coin_shop") {
                CoinShopScreen(vm, featVm, onBack = { navController.popBackStack() })
            }
            composable("cosmetics") {
                CosmeticsScreen(featVm, onBack = { navController.popBackStack() })
            }
            composable("mini_game") {
                MiniGameScreen(featVm, onBack = { navController.popBackStack() })
            }
            composable("pet") {
                PetScreen(featVm, onBack = { navController.popBackStack() })
            }
            composable("farm") {
                FarmScreen(vm, featVm, onBack = { navController.popBackStack() })
            }
            composable("reports") {
                ReportsScreen(vm, onBack = { navController.popBackStack() })
            }
            composable("friends") {
                FriendsListScreen(featVm, onBack = { navController.popBackStack() })
            }
            composable("diet") {
                DietHealthScreen(onBack = { navController.popBackStack() })
            }
            composable("season") {
                SeasonScreen(vm, featVm, onBack = { navController.popBackStack() })
            }
            composable("ai_officer") {
                AiOfficerScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
