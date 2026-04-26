package com.discroom.tv.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.discroom.tv.di.AppContainer
import com.discroom.tv.ui.screens.HomeScreen
import com.discroom.tv.ui.screens.PlayerScreen
import com.discroom.tv.ui.screens.SettingsScreen
import com.discroom.tv.ui.theme.DiscRoomTheme
import com.discroom.tv.viewmodel.HomeViewModel
import com.discroom.tv.viewmodel.HomeViewModelFactory
import com.discroom.tv.viewmodel.PlayerViewModel
import com.discroom.tv.viewmodel.PlayerViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun DiscRoomTvApp(appContext: Context) {
    val navController = rememberNavController()
    val container = remember { AppContainer(appContext) }

    DiscRoomTheme {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                val vm: HomeViewModel = viewModel(factory = HomeViewModelFactory(container))
                HomeScreen(
                    viewModel = vm,
                    openPlayer = { uri ->
                        val encoded = URLEncoder.encode(uri, StandardCharsets.UTF_8.toString())
                        navController.navigate("player/$encoded")
                    },
                    openSettings = { navController.navigate("settings") }
                )
            }
            composable("player/{uri}") { backStackEntry ->
                val encoded = backStackEntry.arguments?.getString("uri").orEmpty()
                val vm: PlayerViewModel = viewModel(
                    factory = PlayerViewModelFactory(container, encoded)
                )
                PlayerScreen(viewModel = vm, onExit = { navController.popBackStack() })
            }
            composable("settings") {
                SettingsScreen(container.settingsRepository) { navController.popBackStack() }
            }
        }
    }
}
