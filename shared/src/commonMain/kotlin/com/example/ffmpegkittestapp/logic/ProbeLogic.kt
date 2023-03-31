package com.example.ffmpegkittestapp.logic

import com.example.ffmpegkit.FFprobeKit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProbeLogic {
    val state = MutableStateFlow(ProbeState())

    fun setUrlText(newText: String) {
        state.update { it.copy(urlText = newText.lowercase()) } //workaround iOS compose bug
    }

    fun execute() {
        val url = state.value.urlText

        CoroutineScope(Dispatchers.Default).launch {
            println("Probing $url")
            val info = FFprobeKit.getMediaInformation(path = url, timeout = 60000)
            println("Info is $info")
        }
    }
}

data class ProbeState(
    val urlText: String = "https://download.blender.org/peach/bigbuckbunny_movies" +
            "/big_buck_bunny_720p_h264.mov",
    val output: String = "")
