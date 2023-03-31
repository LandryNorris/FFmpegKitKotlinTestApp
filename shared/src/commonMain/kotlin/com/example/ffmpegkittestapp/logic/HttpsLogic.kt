package com.example.ffmpegkittestapp.logic

import com.example.ffmpegkit.FFmpegKitConfig.enableLogCallback
import com.example.ffmpegkit.FFmpegKitConfig.enableStatisticsCallback
import com.example.ffmpegkit.FFprobeKit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject

const val DEFAULT_HTTPS_STRING = "https://download.blender.org/peach/trailer/trailer_1080p.ogg"
const val FAIL_HTTPS_STRING = "https://downloadfail.blender.org/peach/trailer/trailer_1080p.ogg"
const val HTTPS_STRING_1 = "https://filesamples.com/samples/video/mov/sample_640x360.mov"
const val HTTPS_STRING_2 = "https://filesamples.com/samples/audio/mp3/sample3.mp3"
const val HTTPS_STRING_3 = "https://filesamples.com/samples/image/webp/sample1.webp"

class HttpsLogic {
    val state = MutableStateFlow(HttpsState())

    fun setUrlText(newText: String) {
        state.update { it.copy(currentUrl = newText) }
    }

    fun handleLogCallback() {
        enableLogCallback(null)
        enableStatisticsCallback(null)
    }

    fun httpButtonPressed() {
        val url = DEFAULT_HTTPS_STRING

        CoroutineScope(Dispatchers.Default).launch {
            getMediaInformation(url)
        }
    }

    fun randomButtonPressed() {
        val url = listOf(HTTPS_STRING_1, HTTPS_STRING_2, HTTPS_STRING_3).random()

        CoroutineScope(Dispatchers.Default).launch {
            getMediaInformation(url)
        }
    }

    fun failButtonPressed() {
        val url = FAIL_HTTPS_STRING

        clearOutput()
        CoroutineScope(Dispatchers.Default).launch {
            getMediaInformation(url)
        }
    }

    private suspend fun getMediaInformation(url: String) {
        val session = FFprobeKit.getMediaInformation(path = url, timeout = 60000)

        val output = buildString {
            val info = session.mediaInformation
            if(info == null) {
                appendLine("GetMediaInformationFailed")
                appendLine("State: ${session.state}")
                appendLine("Duration: ${session.duration}")
                appendLine("ReturnCode: ${session.returnCode}")
                appendLine("Output: ${session.output}")
            } else {
                appendLine("Media information for ${info.filename}")
                if (info.format != null) appendLine("Format: ${info.format}")
                if (info.bitrate != null) appendLine("Bitrate: ${info.bitrate}")
                if (info.duration != null) appendLine("Duration: ${info.duration}")
                if (info.startTime != null) appendLine("Start time: ${info.startTime}")

                val tags = info.tags as? JsonObject
                tags?.forEach {
                    appendLine("Tag: ${it.key}: ${it.value}")
                }

                info.streams.forEach {
                    appendLine("Stream index: ${it.index}")
                    appendLine("Stream type: ${it.codecType}")
                    if(it.codecName != null) appendLine("Stream codec: ${it.codecName}")
                    if(it.pixelFormat != null) appendLine("Stream format: ${it.pixelFormat}")
                    if(it.width != null) appendLine("Stream width: ${it.width}")
                    if(it.height != null) appendLine("Stream height: ${it.height}")
                    if(it.bitrate != null) appendLine("Stream Bitrate: ${it.bitrate}")
                    if(it.sampleRate != null)
                        appendLine("Stream Sample Rate: ${it.sampleRate}")
                    if(it.sampleFormat != null)
                        appendLine("Stream Sample Format: ${it.sampleFormat}")
                    if(it.sampleAspectRatio != null)
                        appendLine("Stream Sample Aspect Ratio: ${it.sampleAspectRatio}")
                    if(it.displayAspectRatio != null)
                        appendLine("Stream Display Aspect Ratio: ${it.displayAspectRatio}")
                    if(it.averageFrameRate != null)
                        appendLine("Stream Average Frame Rate: ${it.averageFrameRate}")
                    if(it.rFrameRate != null)
                        appendLine("Stream Real Frame Rate: ${it.rFrameRate}")
                    if(it.timeBase != null)
                        appendLine("Stream Time Base: ${it.timeBase}")

                    val streamTags = it.tags as? JsonObject
                    streamTags?.forEach { tag ->
                        appendLine("Stream Tag: ${tag.key}: ${tag.value}")
                    }
                }
            }
        }

        state.update { it.copy(output = it.output + output) }
    }

    fun clearOutput() {
        state.update { it.copy(output = "") }
    }
}

data class HttpsState(val currentUrl: String = "", val output: String = "")
