package com.example.ffmpegkittestapp.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ffmpegkittestapp.logic.CommandLogic
import com.example.ffmpegkittestapp.logic.ProbeLogic
import com.example.ffmpegkittestapp.logic.TranscodeLogic
import com.example.ffmpegkittestapp.theme.MyApplicationTheme

@Composable
internal fun RootScreen(baseDirectory: String) {
    val transcodeLogic = remember { TranscodeLogic(baseDirectory) }
    val commandLogic = remember { CommandLogic() }
    val probeLogic = remember { ProbeLogic() }

    var selectedIndex by remember { mutableStateOf(0) }

    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f)) {
                        when(selectedIndex) {
                            0 -> CommandScreen(commandLogic)
                            1 -> TranscodeScreen(transcodeLogic)
                            2 -> ProbeScreen(probeLogic)
                        }
                    }
                    BottomNav { selectedIndex = it }
                }
            }
        }
    }
}

@Composable
internal fun BottomNav(onItemSelected: (Int) -> Unit) {
    BottomNavigation {
        (0 until 3).forEach {
            BottomNavigationItem(label = { Text("Item $it") },
                onClick = { onItemSelected(it) },
                icon = {},
                selected = false,
            )
        }
    }
}
