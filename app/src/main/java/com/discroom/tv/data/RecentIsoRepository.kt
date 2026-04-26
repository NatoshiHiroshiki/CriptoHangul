package com.discroom.tv.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.discroom.tv.model.DiscIso
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "recent_isos")

class RecentIsoRepository(private val context: Context) {
    private val key = stringSetPreferencesKey("recent_iso_entries")

    fun observeRecent(): Flow<List<DiscIso>> = context.dataStore.data.map { prefs ->
        prefs[key].orEmpty().mapNotNull(::decode).sortedByDescending { it.lastOpenedAt }
    }

    suspend fun push(item: DiscIso) {
        context.dataStore.edit { prefs ->
            val current = prefs[key].orEmpty().mapNotNull(::decode)
            val updated = (listOf(item) + current.filterNot { it.uri == item.uri })
                .take(20)
                .map(::encode)
                .toSet()
            prefs[key] = updated
        }
    }

    private fun encode(item: DiscIso): String = listOf(item.uri, item.displayName, item.lastOpenedAt).joinToString("|")

    private fun decode(raw: String): DiscIso? {
        val parts = raw.split("|")
        if (parts.size != 3) return null
        return parts[2].toLongOrNull()?.let { DiscIso(parts[0], parts[1], it) }
    }
}
