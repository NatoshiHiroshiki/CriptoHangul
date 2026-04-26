package com.discroom.tv.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.discroom.tv.model.AspectMode
import com.discroom.tv.model.SettingsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsStore by preferencesDataStore(name = "discroom_settings")

class SettingsRepository(private val context: Context) {
    private val audioKey = stringPreferencesKey("audio")
    private val subtitleKey = stringPreferencesKey("subtitle")
    private val aspectKey = stringPreferencesKey("aspect")
    private val resumeKey = booleanPreferencesKey("auto_resume")
    private val perfKey = booleanPreferencesKey("performance")

    val state: Flow<SettingsState> = context.settingsStore.data.map { prefs ->
        SettingsState(
            preferredAudio = prefs[audioKey] ?: "pt",
            preferredSubtitle = prefs[subtitleKey] ?: "pt",
            aspectMode = runCatching { AspectMode.valueOf(prefs[aspectKey] ?: "FIT") }.getOrDefault(AspectMode.FIT),
            autoResume = prefs[resumeKey] ?: true,
            performanceMode = prefs[perfKey] ?: true
        )
    }

    suspend fun save(newState: SettingsState) {
        context.settingsStore.edit { prefs ->
            prefs[audioKey] = newState.preferredAudio
            prefs[subtitleKey] = newState.preferredSubtitle
            prefs[aspectKey] = newState.aspectMode.name
            prefs[resumeKey] = newState.autoResume
            prefs[perfKey] = newState.performanceMode
        }
    }
}
