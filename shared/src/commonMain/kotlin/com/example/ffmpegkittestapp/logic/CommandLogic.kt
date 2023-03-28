package com.example.ffmpegkittestapp.logic

import com.example.ffmpegkit.FFmpegKit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommandLogic {
    val state = MutableStateFlow(CommandState())
    private val context = CoroutineScope(Dispatchers.Default)

    fun setCommandText(newText: String) {
        state.update { it.copy(commandText = newText) }
    }

    fun execute() {
        val currentState = state.value
        context.launch {
            val session = FFmpegKit.execute(currentState.commandText)
            state.update { it.copy(output = session.output) }
        }
    }
}

data class CommandState(val output: String = "", val commandText: String = "")
