package com.example.ffmpegkittestapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal actual fun Spinner(expanded: Boolean, onExpandedChange: (Boolean) -> Unit,
                            options: List<String>, onOptionChosen: (String) -> Unit, current: String) {
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
        Text(current, modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(5.dp)).background(Color.LightGray),
            textAlign = TextAlign.Center)
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            options.forEach {
                DropdownMenuItem(onClick = { onOptionChosen(it) }) {
                    Text(it)
                }
            }
        }
    }
}
