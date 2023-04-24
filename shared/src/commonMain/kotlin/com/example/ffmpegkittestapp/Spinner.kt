package com.example.ffmpegkittestapp

import androidx.compose.runtime.Composable

@Composable
internal expect fun Spinner(expanded: Boolean, onExpandedChange: (Boolean) -> Unit,
                            options: List<String>, onOptionChosen: (String) -> Unit, current: String)
