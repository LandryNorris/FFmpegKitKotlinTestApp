package com.example.ffmpegkittestapp.logic

import com.example.ffmpegkit.FFmpegKit
import com.example.ffmpegkit.sessions.FFmpegSession
import kotlin.math.log

class TranscodeLogic(private val baseDirectory: String) {
    suspend fun downloadFile(): FFmpegSession {
        val url = "http://distribution.bbb3d.renderfarming.net/video/mp4/bbb_sunflower_1080p_30fps_normal.mp4"
        val file = baseDirectory.trimEnd('/') + "/bigbuckbunny.mp4"
        println("Grabbing file from URL")
        val session = FFmpegKit.execute("-i $url $file", logCallback = {
            println("Got log: $it")
        }, statisticsCallback = {
            println("Got statistics: $it")
        })
        println("Done grabbing file from URL")
        return session
    }
}
