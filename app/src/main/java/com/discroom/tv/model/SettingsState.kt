package com.discroom.tv.model

enum class AspectMode { ORIGINAL, FIT, FILL }

data class SettingsState(
    val preferredAudio: String = "pt",
    val preferredSubtitle: String = "pt",
    val aspectMode: AspectMode = AspectMode.FIT,
    val autoResume: Boolean = true,
    val performanceMode: Boolean = true
)
