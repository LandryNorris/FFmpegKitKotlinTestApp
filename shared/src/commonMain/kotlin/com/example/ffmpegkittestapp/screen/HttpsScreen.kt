package com.example.ffmpegkittestapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ffmpegkittestapp.logic.HttpsLogic

@Composable
internal fun HttpsScreen(logic: HttpsLogic) {
    val state by logic.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(state.currentUrl, onValueChange = logic::setUrlText)
        Button(onClick = logic::httpButtonPressed) { Text("Https") }
        Button(onClick = logic::randomButtonPressed) { Text("Random") }
        Button(onClick = logic::failButtonPressed) { Text("Fail") }

        val scrollState = rememberScrollState()
        Text(modifier = Modifier.verticalScroll(scrollState).weight(1f), text = state.output)
    }
}
