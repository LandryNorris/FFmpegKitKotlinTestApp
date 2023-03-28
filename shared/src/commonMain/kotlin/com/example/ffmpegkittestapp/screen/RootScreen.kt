package com.example.ffmpegkittestapp.screen

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import com.example.ffmpegkittestapp.logic.CommandLogic
import com.example.ffmpegkittestapp.logic.TranscodeLogic

@Composable
fun RootScreen(baseDirectory: String) {
    val transcodeLogic = remember { TranscodeLogic(baseDirectory) }
    val commandLogic = remember { CommandLogic() }

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(bottomBar = { BottomNav { selectedIndex = it } }) {
        when(selectedIndex) {
            0 -> CommandScreen(commandLogic)
            1 -> TranscodeScreen(transcodeLogic)
        }
    }
}

@Composable
fun BottomNav(onItemSelected: (Int) -> Unit) {
    BottomNavigation {
        (0 until 2).forEach {
            BottomNavigationItem(label = { Text("Item $it") },
                onClick = { onItemSelected(it) },
                icon = {},
                selected = false)
        }
    }
}
