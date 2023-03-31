package com.example.ffmpegkittestapp.android

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import com.example.ffmpegkittestapp.Encoding
import com.example.ffmpegkittestapp.MainApp
import okio.FileSystem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Encoding.context = this
        Encoding.fs = FileSystem.SYSTEM

        val baseDirectory = ContextCompat.getExternalFilesDirs(this,
            Environment.DIRECTORY_MOVIES).first {
            it.exists()
        }

        setContent {
            MainApp(baseDirectory = baseDirectory.absolutePath)
        }
    }
}
