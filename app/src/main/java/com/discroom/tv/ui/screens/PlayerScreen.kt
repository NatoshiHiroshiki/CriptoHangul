package com.discroom.tv.ui.screens

import android.view.KeyEvent
import android.view.SurfaceView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.tv.material3.Button
import androidx.tv.material3.Card
import androidx.tv.material3.Text
import com.discroom.tv.viewmodel.PlayerViewModel

@Composable
fun PlayerScreen(viewModel: PlayerViewModel, onExit: () -> Unit) {
    val state by viewModel.playerState.collectAsState()

    LaunchedEffect(Unit) { viewModel.start() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onKeyEvent { event ->
                if (event.nativeKeyEvent.action != KeyEvent.ACTION_DOWN) return@onKeyEvent false
                when (event.nativeKeyEvent.keyCode) {
                    KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_DPAD_CENTER -> {
                        viewModel.onPlayPause(); true
                    }
                    KeyEvent.KEYCODE_MEDIA_STOP -> { viewModel.onStop(); true }
                    KeyEvent.KEYCODE_MEDIA_NEXT -> { viewModel.onNextChapter(); true }
                    KeyEvent.KEYCODE_MEDIA_PREVIOUS -> { viewModel.onPrevChapter(); true }
                    KeyEvent.KEYCODE_MENU -> { viewModel.onMainMenu(); true }
                    KeyEvent.KEYCODE_BACK -> { onExit(); true }
                    else -> false
                }
            }
    ) {
        AndroidView(
            factory = { context ->
                SurfaceView(context).also { viewModel.playerEngine.attachVideoSurface(it) }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (state.isLoading || state.errorMessage != null) {
            Card(modifier = Modifier.align(Alignment.Center)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(state.errorMessage ?: "Lendo disco...")
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = viewModel::onPlayPause) { Text(if (state.isPlaying) "Pause" else "Play") }
            Button(onClick = viewModel::onPrevChapter) { Text("Capítulo -") }
            Button(onClick = viewModel::onNextChapter) { Text("Capítulo +") }
            Button(onClick = viewModel::onMainMenu) { Text("Menu DVD") }
            Button(onClick = onExit) { Text("Sair") }
        }
    }
}
