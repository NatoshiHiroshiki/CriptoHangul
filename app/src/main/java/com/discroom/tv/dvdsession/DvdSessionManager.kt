package com.discroom.tv.dvdsession

import android.net.Uri
import com.discroom.tv.player.PlayerEngine

class DvdSessionManager(private val playerEngine: PlayerEngine) {
    fun start(uri: Uri) = playerEngine.loadIso(uri)
    fun stop() = playerEngine.stop()
    fun nextChapter() = playerEngine.nextChapter()
    fun previousChapter() = playerEngine.previousChapter()
    fun showTitleMenu() = playerEngine.openTitleMenu()
    fun showMainMenu() = playerEngine.openMainMenu()
}
