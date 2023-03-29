package com.example.ffmpegkittestapp.logic

import com.example.ffmpegkit.FFmpegKit
import com.example.ffmpegkit.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommandLogic {
    val state = MutableStateFlow(CommandState())
    private val context = CoroutineScope(Dispatchers.Default)

    fun setCommandText(newText: String) {
        state.update { it.copy(commandText = newText.lowercase()) }
    }

    fun execute() {
        state.update { it.copy(output = "") }
        val currentState = state.value
        context.launch {
            val session = FFmpegKit.executeBlocking(currentState.commandText, logCallback = { log ->
                appendText(log.message)
                println("Got log: ${log.message}")
            }, statisticsCallback = {
                println("Got statistics: $it")
            })

            if(session.returnCode == null || !session.returnCode!!.isSuccess) {
                state.update { it.copy(output = "Command failed with error " +
                        "${session.returnCode?.value}\n") }
            }
        }
    }

    private fun appendText(text: String) {
        state.update { it.copy(output = it.output + text) }
    }
}

data class CommandState(val output: String = "", val commandText: String = "")
