package com.example.ffmpegkittestapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ffmpegkittestapp.Dialog
import com.example.ffmpegkittestapp.VideoPlayer
import com.example.ffmpegkittestapp.logic.VideoLogic

@Composable
fun VideoScreen(logic: VideoLogic) {
    val state by logic.state.collectAsState()

    if(state.isEncoding) {
        Dialog(onDismissRequest = {}) {
            Text("Progress is ${state.progress}")
        }
    }

    Column {
        Button(onClick = logic::encodeVideo) { Text("Encode") }
        Text("Video File is ${state.videoFile}")

        if(state.videoFile != null) {
            VideoPlayer(state.videoFile!!.toString())
        }
    }
}
