package com.lamele.app

import android.app.Application
import androidx.preference.PreferenceManager
import com.lamele.app.data.ExtrasRepository
import com.lamele.app.data.GameStateRepository
import com.lamele.app.data.PoopRepository
import com.lamele.app.data.UserPreferencesRepository
import com.lamele.app.data.local.AppDatabase
import com.lamele.app.data.remote.MimoClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration

class LameleApplication : Application() {
    lateinit var poopRepository: PoopRepository
        private set
    lateinit var userPreferencesRepository: UserPreferencesRepository
        private set
    lateinit var extrasRepository: ExtrasRepository
        private set
    lateinit var gameStateRepository: GameStateRepository
        private set

    /** 未配置 mimo.api.key 时为 null */
    val mimoClient: MimoClient?
        get() {
            if (BuildConfig.MIMO_API_KEY.isBlank()) return null
            return MimoClient(
                apiKey = BuildConfig.MIMO_API_KEY,
                model = BuildConfig.MIMO_MODEL,
                baseUrl = BuildConfig.MIMO_BASE_URL.trimEnd('/'),
            )
        }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance().userAgentValue = packageName
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        val db = AppDatabase.get(this)
        poopRepository = PoopRepository(db.poopDao())
        userPreferencesRepository = UserPreferencesRepository(this)
        extrasRepository = ExtrasRepository(db.toiletDao(), db.socialDao())
        gameStateRepository = GameStateRepository(this)
        appScope.launch {
            extrasRepository.seedIfNeeded()
        }
    }
}
