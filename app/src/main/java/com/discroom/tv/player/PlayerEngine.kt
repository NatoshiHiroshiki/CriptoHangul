package com.discroom.tv.player

import android.net.Uri
import android.view.SurfaceView
import kotlinx.coroutines.flow.StateFlow

interface PlayerEngine {
    val state: StateFlow<PlayerState>
    fun attachVideoSurface(surfaceView: SurfaceView)
    fun loadIso(uri: Uri)
    fun playPause()
    fun stop()
    fun nextChapter()
    fun previousChapter()
    fun openTitleMenu()
    fun openMainMenu()
    fun setAudioTrack(trackId: Int)
    fun setSubtitleTrack(trackId: Int)
    fun release()
}

data class PlayerState(
    val isLoading: Boolean = true,
    val isPlaying: Boolean = false,
    val errorMessage: String? = null,
    val title: String = "Lendo disco..."
)
