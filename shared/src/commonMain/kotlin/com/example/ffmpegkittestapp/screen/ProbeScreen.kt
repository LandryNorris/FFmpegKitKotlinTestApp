package com.example.ffmpegkittestapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ffmpegkittestapp.logic.ProbeLogic

@Composable
internal fun ProbeScreen(logic: ProbeLogic) {
    val state by logic.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(modifier = Modifier.weight(1f),
                value = state.urlText, onValueChange = logic::setUrlText)
            Button(onClick = logic::execute) { Text("Execute") }
        }

        val scrollState = rememberScrollState()
        Text(modifier = Modifier.verticalScroll(scrollState).weight(1f), text = state.output)
    }
}
