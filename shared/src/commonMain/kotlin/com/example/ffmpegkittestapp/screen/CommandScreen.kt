package com.example.ffmpegkittestapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.ffmpegkittestapp.logic.CommandLogic

@Composable
internal fun CommandScreen(logic: CommandLogic) {
    val state by logic.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            OutlinedTextField(state.commandText, onValueChange = logic::setCommandText)
            Button(onClick = logic::execute) { Text("Execute") }
        }

        val scrollState = rememberScrollState()
        Text(modifier = Modifier.verticalScroll(scrollState).weight(1f), text = state.output)
    }
}
