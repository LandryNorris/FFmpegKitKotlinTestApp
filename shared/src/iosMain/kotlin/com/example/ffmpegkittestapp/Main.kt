package com.example.ffmpegkittestapp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.example.ffmpegkittestapp.screen.RootScreen
import okio.FileSystem
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun MainViewController() = ComposeUIViewController {
    val baseDir = remember { NSFileManager.defaultManager.URLForDirectory(NSDocumentDirectory,
        NSUserDomainMask, null, true, null)?.absoluteString
        ?: error("Unable to get directory") }

    Encoding.fs = FileSystem.SYSTEM

    RootScreen(baseDir)
}
