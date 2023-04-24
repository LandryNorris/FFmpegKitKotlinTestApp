package com.example.ffmpegkittestapp.logic

import com.example.ffmpegkittestapp.Encoding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.Path

class VideoLogic(val baseDirectory: Path) {
    val state = MutableStateFlow(VideoState())

    fun codecChanged(newCodec: String) {
        state.update { it.copy(codec = newCodec) }
    }

    fun encodeVideo() {
        val current = state.value
        val output = baseDirectory.resolve("encoded.${extension(current.codec)}")

        state.update { it.copy(isEncoding = true, videoFile = null) }
        CoroutineScope(Dispatchers.Default).launch {
            Encoding.encodeFiles(baseDirectory, output, current.codec,
                pixelFormat(current.codec), customOptions(current.codec)) { stats ->
                val videoDuration = 9000 //hardcoded
                val progress = stats.time*100.0/videoDuration
                state.update { it.copy(progress = progress) }
            }
            println("Done encoding $output")
            state.update { it.copy(videoFile = output, isEncoding = false) }
        }
    }

    private fun customOptions(codec: String) = when {
        "x265" in codec -> "-crf 28 -preset fast "
        "vp9" in codec -> "-b:v 2M "
        "vp" in codec -> "-b:v 1M -crf 10 "
        "aom" in codec -> "-crf 30 -strict experimental "
        "theora" in codec -> "-qscale:v 7 "
        "hap" in codec -> "-format hap_q "
        else -> ""
    }

    private fun pixelFormat(codec: String) = if("x265" in codec) "yuv420p10le" else "yuv420p"

    private fun extension(codec: String) = when {
        "vp" in codec -> "webm"
        "aom" in codec -> "mkv"
        "theora" in codec -> "ogv"
        "hap" in codec -> "mov"
        else -> "mp4"
    }
}

private val allCodecs = listOf("libx264", "libopenh264", "libx265", "libxvid", "libvpx",
    "libvpx-vp9", "libaom-av1", "libkvazaar", "libtheora", "hap")

data class VideoState(val isPlaying: Boolean = false, val codec: String = "libx264",
                      val isEncoding: Boolean = false, val progress: Double = 0.0,
                      val codecs: List<String> = allCodecs,
                      val videoFile: Path? = null)
