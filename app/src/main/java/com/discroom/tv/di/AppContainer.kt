package com.discroom.tv.di

import android.content.ContentResolver
import android.content.Context
import com.discroom.tv.data.RecentIsoRepository
import com.discroom.tv.player.VlcPlayerEngine
import com.discroom.tv.settings.SettingsRepository

class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    val contentResolver: ContentResolver = appContext.contentResolver

    val recentIsoRepository = RecentIsoRepository(appContext)
    val settingsRepository = SettingsRepository(appContext)

    fun createPlayerEngine() = VlcPlayerEngine(appContext)
}
