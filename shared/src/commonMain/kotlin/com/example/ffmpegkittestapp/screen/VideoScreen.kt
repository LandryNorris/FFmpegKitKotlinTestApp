package com.example.ffmpegkittestapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ffmpegkittestapp.Dialog
import com.example.ffmpegkittestapp.VideoPlayer
import com.example.ffmpegkittestapp.logic.VideoLogic

@Composable
internal fun VideoScreen(logic: VideoLogic) {
    val state by logic.state.collectAsState()

    if(state.isEncoding) {
        Dialog(onDismissRequest = {}) {
            Text("Progress is ${state.progress}")
        }
    }

    Column {
        Button(onClick = logic::encodeVideo) { Text("Encode") }

        if(state.videoFile != null) {
            VideoPlayer(Modifier.fillMaxWidth().weight(1f).background(Color.Red),
                state.videoFile!!.toString())
        }
    }
}
