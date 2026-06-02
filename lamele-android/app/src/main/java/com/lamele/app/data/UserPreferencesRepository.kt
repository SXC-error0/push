package com.lamele.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("lamele_prefs")

class UserPreferencesRepository(private val context: Context) {
    private val ds = context.dataStore

    val nickname: Flow<String> = ds.data.map { it[NICKNAME] ?: "" }
    val loggedIn: Flow<Boolean> = ds.data.map { !(it[NICKNAME].isNullOrBlank()) }
    val monthlySalary: Flow<Float> = ds.data.map { it[SALARY] ?: 12_000f }
    val workDaysPerMonth: Flow<Int> = ds.data.map { it[WORK_DAYS] ?: 22 }
    val workHoursPerDay: Flow<Float> = ds.data.map { it[WORK_HOURS] ?: 8f }
    val hideExactLocation: Flow<Boolean> = ds.data.map { it[HIDE_LOC] ?: true }
    val poopCoins: Flow<Int> = ds.data.map { it[COINS] ?: 0 }

    suspend fun setNickname(name: String) {
        ds.edit { it[NICKNAME] = name.trim() }
    }

    suspend fun setSalaryProfile(salary: Float, days: Int, hours: Float) {
        ds.edit {
            it[SALARY] = salary
            it[WORK_DAYS] = days
            it[WORK_HOURS] = hours
        }
    }

    suspend fun setHideExactLocation(hide: Boolean) {
        ds.edit { it[HIDE_LOC] = hide }
    }

    suspend fun addPoopCoins(delta: Int) {
        ds.edit { prefs ->
            val cur = prefs[COINS] ?: 0
            prefs[COINS] = maxOf(0, cur + delta)
        }
    }

    suspend fun wipeAllPrefs() {
        ds.edit { it.clear() }
    }

    suspend fun spendPoopCoins(amount: Int): Boolean {
        if (amount <= 0) return true
        var ok = false
        ds.edit { p ->
            val cur = p[COINS] ?: 0
            if (cur >= amount) {
                p[COINS] = cur - amount
                ok = true
            }
        }
        return ok
    }

    companion object {
        private val NICKNAME = stringPreferencesKey("nickname")
        private val SALARY = floatPreferencesKey("monthly_salary")
        private val WORK_DAYS = intPreferencesKey("work_days")
        private val WORK_HOURS = floatPreferencesKey("work_hours")
        private val HIDE_LOC = booleanPreferencesKey("hide_exact_location")
        private val COINS = intPreferencesKey("poop_coins")
    }
}
