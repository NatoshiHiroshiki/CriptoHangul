package com.discroom.tv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Card
import androidx.tv.material3.Text
import com.discroom.tv.model.AspectMode
import com.discroom.tv.model.SettingsState
import com.discroom.tv.settings.SettingsRepository
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(repo: SettingsRepository, onBack: () -> Unit) {
    val state by repo.state.collectAsState(initial = SettingsState())
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Configurações")

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Áudio preferencial: ${state.preferredAudio}")
                Text("Legenda preferencial: ${state.preferredSubtitle}")
                Text("Proporção: ${state.aspectMode}")
                Text("Retomada automática: ${if (state.autoResume) "Ativada" else "Desativada"}")
                Text("Modo desempenho: ${if (state.performanceMode) "Ativado" else "Desativado"}")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                scope.launch {
                    repo.save(state.copy(aspectMode = nextAspect(state.aspectMode)))
                }
            }) { Text("Trocar proporção") }

            Button(onClick = {
                scope.launch {
                    repo.save(state.copy(autoResume = !state.autoResume))
                }
            }) { Text("Retomada") }

            Button(onClick = onBack) { Text("Voltar") }
        }
    }
}

private fun nextAspect(current: AspectMode): AspectMode = when (current) {
    AspectMode.ORIGINAL -> AspectMode.FIT
    AspectMode.FIT -> AspectMode.FILL
    AspectMode.FILL -> AspectMode.ORIGINAL
}
