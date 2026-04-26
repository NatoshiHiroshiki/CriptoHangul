package com.discroom.tv.player

import android.content.Context
import android.net.Uri
import android.view.SurfaceView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer

class VlcPlayerEngine(context: Context) : PlayerEngine {
    private val libVlc = LibVLC(context, arrayListOf("--avcodec-hw=any", "--network-caching=150"))
    private val mediaPlayer = MediaPlayer(libVlc)
    private val _state = MutableStateFlow(PlayerState())
    override val state: StateFlow<PlayerState> = _state

    init {
        mediaPlayer.setEventListener { event ->
            when (event.type) {
                MediaPlayer.Event.Opening -> _state.value = _state.value.copy(isLoading = true, errorMessage = null)
                MediaPlayer.Event.Playing -> _state.value = _state.value.copy(isLoading = false, isPlaying = true)
                MediaPlayer.Event.Paused -> _state.value = _state.value.copy(isPlaying = false)
                MediaPlayer.Event.Stopped, MediaPlayer.Event.EndReached -> _state.value = _state.value.copy(isPlaying = false)
                MediaPlayer.Event.EncounteredError -> _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Não foi possível reproduzir este ISO neste dispositivo."
                )
            }
        }
    }

    override fun attachVideoSurface(surfaceView: SurfaceView) {
        mediaPlayer.attachViews(surfaceView, null, false, false)
    }

    override fun loadIso(uri: Uri) {
        _state.value = _state.value.copy(isLoading = true, title = "Lendo disco...")
        val media = Media(libVlc, uri).apply {
            setHWDecoderEnabled(true, false)
            addOption(":input-slave=none")
        }
        mediaPlayer.media = media
        media.release()
        mediaPlayer.play()
    }

    override fun playPause() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause() else mediaPlayer.play()
    }

    override fun stop() = mediaPlayer.stop()

    override fun nextChapter() {
        mediaPlayer.nextChapter()
    }

    override fun previousChapter() {
        mediaPlayer.previousChapter()
    }

    override fun openTitleMenu() {
        mediaPlayer.navigate(MediaPlayer.NavigateMode.Activate)
    }

    override fun openMainMenu() {
        mediaPlayer.navigate(MediaPlayer.NavigateMode.Activate)
    }

    override fun setAudioTrack(trackId: Int) {
        mediaPlayer.audioTrack = trackId
    }

    override fun setSubtitleTrack(trackId: Int) {
        mediaPlayer.spuTrack = trackId
    }

    override fun release() {
        mediaPlayer.detachViews()
        mediaPlayer.release()
        libVlc.release()
    }
}
