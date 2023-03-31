package com.example.ffmpegkittestapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dev.icerock.moko.resources.ImageResource
import okio.Path
import java.io.FileOutputStream

actual fun ImageResource.toFile(context: Any, file: Path) {
    val androidContext = context as? Context ?: error("$context is invalid for Android")
    val bitmap = BitmapFactory.decodeResource(androidContext.resources, drawableResId)

    val outputStream = FileOutputStream(file.toFile())
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
}