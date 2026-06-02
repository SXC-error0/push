package com.lamele.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.gameStore by preferencesDataStore("lamele_game")

class GameStateRepository(private val context: Context) {
    private val ds = context.gameStore

    val petLevel: Flow<Int> = ds.data.map { it[PET_LEVEL] ?: 1 }
    val petXp: Flow<Int> = ds.data.map { it[PET_XP] ?: 0 }
    val farmPlot0: Flow<Long> = ds.data.map { it[FARM_0] ?: 0L }
    val farmPlot1: Flow<Long> = ds.data.map { it[FARM_1] ?: 0L }
    val farmPlot2: Flow<Long> = ds.data.map { it[FARM_2] ?: 0L }
    val miniGameHigh: Flow<Int> = ds.data.map { it[MINI_HI] ?: 0 }
    val seasonPoints: Flow<Int> = ds.data.map { it[SEASON_PTS] ?: 0 }
    val ownedCosmetics: Flow<Set<String>> = ds.data.map { prefs ->
        prefs[COSMETICS]?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
    }

    suspend fun addPetXp(amount: Int) {
        if (amount <= 0) return
        ds.edit { p ->
            var lv = p[PET_LEVEL] ?: 1
            var xp = (p[PET_XP] ?: 0) + amount
            while (xp >= lv * 20) {
                xp -= lv * 20
                lv += 1
            }
            p[PET_LEVEL] = lv
            p[PET_XP] = xp
        }
    }

    suspend fun plantPlot(index: Int) {
        val key = plotKey(index)
        ds.edit { it[key] = System.currentTimeMillis() }
    }

    /** 返回收获的「纤维币」奖励，0 表示未成熟 */
    suspend fun harvestPlot(index: Int): Int {
        val key = plotKey(index)
        var reward = 0
        ds.edit { p ->
            val t = p[key] ?: 0L
            if (t > 0 && System.currentTimeMillis() - t > 60_000L) {
                p[key] = 0L
                reward = 8
            }
        }
        return reward
    }

    suspend fun updateMiniGameScore(score: Int) {
        ds.edit { p ->
            val cur = p[MINI_HI] ?: 0
            if (score > cur) p[MINI_HI] = score
        }
    }

    suspend fun addSeasonPoints(delta: Int) {
        ds.edit { p ->
            p[SEASON_PTS] = (p[SEASON_PTS] ?: 0) + delta
        }
    }

    suspend fun unlockCosmetic(id: String) {
        ds.edit { p ->
            val cur = p[COSMETICS]?.split(",")?.filter { it.isNotBlank() }?.toMutableSet() ?: mutableSetOf()
            cur.add(id)
            p[COSMETICS] = cur.joinToString(",")
        }
    }

    suspend fun resetAll() {
        ds.edit { it.clear() }
    }

    private fun plotKey(index: Int) = when (index) {
        0 -> FARM_0
        1 -> FARM_1
        else -> FARM_2
    }

    companion object {
        private val PET_LEVEL = intPreferencesKey("pet_level")
        private val PET_XP = intPreferencesKey("pet_xp")
        private val FARM_0 = longPreferencesKey("farm_0")
        private val FARM_1 = longPreferencesKey("farm_1")
        private val FARM_2 = longPreferencesKey("farm_2")
        private val MINI_HI = intPreferencesKey("mini_hi")
        private val SEASON_PTS = intPreferencesKey("season_pts")
        private val COSMETICS = stringPreferencesKey("cosmetics")
    }
}
