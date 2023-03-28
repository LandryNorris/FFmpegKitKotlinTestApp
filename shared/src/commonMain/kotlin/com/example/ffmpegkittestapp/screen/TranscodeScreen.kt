package com.example.ffmpegkittestapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ffmpegkit.sessions.FFmpegSession
import com.example.ffmpegkittestapp.logic.TranscodeLogic
import com.example.ffmpegkittestapp.theme.MyApplicationTheme

@Composable
internal fun TranscodeScreen(logic: TranscodeLogic) {
    var isDone by remember { mutableStateOf(false) }
    var session by remember { mutableStateOf<FFmpegSession?>(null) }
    LaunchedEffect(Unit) {
        session = logic.downloadFile()
        isDone = true
    }
    if(isDone) {
        Column {
            Text("File is ready!")
            session?.let { SessionView(it) }
        }
    } else {
        Text("Downloading file...")
    }
}

@Composable
internal fun SessionView(session: FFmpegSession) {
    Text("Id: ${session.sessionId}")
    Text("State: ${session.state}")
    Text("Result: ${session.returnCode}")
    Text("Start Time: ${session.startTime}")

    Text("Output: ${session.output}")
}
