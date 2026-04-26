package com.discroom.tv.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.Text
import com.discroom.tv.file.IsoPicker
import com.discroom.tv.ui.components.PrimaryButton
import com.discroom.tv.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    openPlayer: (String) -> Unit,
    openSettings: () -> Unit
) {
    val recent by viewModel.recent.collectAsState()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        IsoPicker.takePersistableReadPermission(context, uri)
        viewModel.remember(uri)
        openPlayer(uri.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.surface)))
            .padding(36.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("DiscRoom TV", style = androidx.tv.material3.MaterialTheme.typography.displaySmall)
        Text("Seu DVD virtual para Android TV e Google TV")

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PrimaryButton(text = "Abrir ISO", onClick = { launcher.launch(arrayOf("*/*")) }, modifier = Modifier.weight(1f))
            PrimaryButton(text = "Configurações", onClick = openSettings, modifier = Modifier.weight(1f))
        }

        Text("Recentes")
        LazyColumn(contentPadding = PaddingValues(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(recent, key = { it.uri }) { item ->
                Card(onClick = { openPlayer(item.uri) }, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(item.displayName)
                        Text(item.uri, style = androidx.tv.material3.MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
