package com.dormirbien.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.ds: DataStore<Preferences> by preferencesDataStore("alarm_prefs")

data class AlarmState(
    val isSet:     Boolean = false,
    val h1: Int = 7, val m1: Int = 0,
    val h2: Int = 7, val m2: Int = 5,
    val cycles:    Int    = 0,
    val hoursText: String = "",
)

data class UserSettings(
    val wakeHour:     Int    = 7,
    val wakeMinute:   Int    = 0,
    val onsetMinutes: Int    = 14,
    val soundUri:     String = "",
)

@Singleton
class AlarmPreferences @Inject constructor(
    @ApplicationContext private val ctx: Context,
) {
    private object K {
        val SET      = booleanPreferencesKey("alarm_set")
        val H1       = intPreferencesKey("h1"); val M1 = intPreferencesKey("m1")
        val H2       = intPreferencesKey("h2"); val M2 = intPreferencesKey("m2")
        val CYCLES   = intPreferencesKey("cycles")
        val HOURS    = stringPreferencesKey("hours_txt")
        val WAKE_H   = intPreferencesKey("wake_hour")
        val WAKE_M   = intPreferencesKey("wake_min")
        val ONSET    = intPreferencesKey("onset")
        val SOUND    = stringPreferencesKey("sound_uri")
    }

    val alarmState: Flow<AlarmState> = ctx.ds.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { p ->
            AlarmState(
                isSet     = p[K.SET]    ?: false,
                h1        = p[K.H1]     ?: 7,  m1 = p[K.M1] ?: 0,
                h2        = p[K.H2]     ?: 7,  m2 = p[K.M2] ?: 5,
                cycles    = p[K.CYCLES] ?: 0,
                hoursText = p[K.HOURS]  ?: "",
            )
        }

    val userSettings: Flow<UserSettings> = ctx.ds.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { p ->
            UserSettings(
                wakeHour     = p[K.WAKE_H] ?: 7,
                wakeMinute   = p[K.WAKE_M] ?: 0,
                onsetMinutes = p[K.ONSET]  ?: 14,
                soundUri     = p[K.SOUND]  ?: "",
            )
        }

    suspend fun saveAlarm(h1: Int, m1: Int, h2: Int, m2: Int, cycles: Int, hoursText: String) {
        ctx.ds.edit { p ->
            p[K.SET] = true
            p[K.H1] = h1; p[K.M1] = m1
            p[K.H2] = h2; p[K.M2] = m2
            p[K.CYCLES] = cycles; p[K.HOURS] = hoursText
        }
        // Mirror to SharedPrefs for sync access from Service/Receiver (no coroutines)
        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putBoolean("set", true)
            .putInt("h1", h1).putInt("m1", m1)
            .putInt("h2", h2).putInt("m2", m2)
            .putInt("cycles", cycles).putString("hours", hoursText)
            .apply()
    }

    suspend fun clearAlarm() {
        ctx.ds.edit { it[K.SET] = false }
        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putBoolean("set", false).apply()
    }

    suspend fun saveSettings(wH: Int, wM: Int, onset: Int) {
        ctx.ds.edit { p -> p[K.WAKE_H] = wH; p[K.WAKE_M] = wM; p[K.ONSET] = onset }
    }

    suspend fun saveSoundUri(uri: String) {
        ctx.ds.edit { it[K.SOUND] = uri }
        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putString("sound_uri", uri).apply()
    }

    // Sync helpers (called from onResume — no suspend needed)
    fun isPendingReview(): Boolean =
        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE)
            .getBoolean("pending_review", false)

    fun clearPendingReview() =
        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE).edit()
            .putBoolean("pending_review", false).putBoolean("set", false).apply()

    fun getSoundUriSync(): String? =
        ctx.getSharedPreferences("db_sync", Context.MODE_PRIVATE)
            .getString("sound_uri", null)
}
