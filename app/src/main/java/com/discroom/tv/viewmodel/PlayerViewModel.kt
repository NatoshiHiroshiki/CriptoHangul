package com.discroom.tv.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.discroom.tv.di.AppContainer
import com.discroom.tv.dvdsession.DvdSessionManager
import com.discroom.tv.player.PlayerEngine
import com.discroom.tv.player.PlayerState
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class PlayerViewModel(
    private val uriRaw: String,
    val playerEngine: PlayerEngine
) : ViewModel() {
    val playerState = playerEngine.state
    private val session = DvdSessionManager(playerEngine)

    fun start() {
        val decoded = URLDecoder.decode(uriRaw, StandardCharsets.UTF_8.toString())
        session.start(Uri.parse(decoded))
    }

    fun onPlayPause() = playerEngine.playPause()
    fun onStop() = session.stop()
    fun onNextChapter() = session.nextChapter()
    fun onPrevChapter() = session.previousChapter()
    fun onMainMenu() = session.showMainMenu()
    fun onTitleMenu() = session.showTitleMenu()

    override fun onCleared() {
        playerEngine.release()
        super.onCleared()
    }
}

class PlayerViewModelFactory(
    private val container: AppContainer,
    private val encodedUri: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PlayerViewModel(encodedUri, container.createPlayerEngine()) as T
}
