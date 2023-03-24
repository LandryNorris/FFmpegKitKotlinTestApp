package com.example.ffmpegkittestapp.logic

import com.example.ffmpegkit.FFmpegKit
import com.example.ffmpegkit.sessions.FFmpegSession

class TranscodeLogic(private val baseDirectory: String) {
    suspend fun downloadFile(): FFmpegSession {
        val url = "http://distribution.bbb3d.renderfarming.net/video/mp4/bbb_sunflower_1080p_30fps_normal.mp4"
        val file = baseDirectory.trimEnd('/') + "/bigbuckbunny.mp4"
        val session = FFmpegKit.execute("-i $url $file")
        println("Done grabbing file from URL")
        return session
    }
}
