package com.example.ffmpegkittestapp.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ffmpegkittestapp.logic.CommandLogic
import com.example.ffmpegkittestapp.logic.HttpsLogic
import com.example.ffmpegkittestapp.logic.TranscodeLogic
import com.example.ffmpegkittestapp.logic.VideoLogic
import com.example.ffmpegkittestapp.theme.MyApplicationTheme
import okio.Path.Companion.toPath

@Composable
internal fun RootScreen(baseDirectory: String) {
    val transcodeLogic = remember { TranscodeLogic(baseDirectory) }
    val commandLogic = remember { CommandLogic() }
    val httpsLogic = remember { HttpsLogic() }
    val videoLogic = remember { VideoLogic(baseDirectory.toPath()) }

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
                            1 -> VideoScreen(videoLogic)
                            2 -> HttpsScreen(httpsLogic)
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
