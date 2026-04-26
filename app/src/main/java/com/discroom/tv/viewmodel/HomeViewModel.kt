package com.discroom.tv.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.discroom.tv.data.RecentIsoRepository
import com.discroom.tv.di.AppContainer
import com.discroom.tv.model.DiscIso
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val recentRepo: RecentIsoRepository,
    private val contentResolver: ContentResolver
) : ViewModel() {
    val recent: StateFlow<List<DiscIso>> = recentRepo.observeRecent().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun remember(uri: Uri) {
        viewModelScope.launch {
            val name = queryDisplayName(uri) ?: "DVD ISO"
            recentRepo.push(DiscIso(uri.toString(), name, System.currentTimeMillis()))
        }
    }

    private fun queryDisplayName(uri: Uri): String? {
        val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
        return contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (!cursor.moveToFirst()) return@use null
            cursor.getString(0)
        }
    }
}

class HomeViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        HomeViewModel(container.recentIsoRepository, container.contentResolver) as T
}
