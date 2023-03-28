package com.example.ffmpegkittestapp

import androidx.compose.runtime.Composable
import com.example.ffmpegkittestapp.screen.RootScreen

//We need all Composable methods in commonMain to be internal for iOS, but Android MainActivity
//needs to access the root.
@Composable
fun MainApp(baseDirectory: String) = RootScreen(baseDirectory)