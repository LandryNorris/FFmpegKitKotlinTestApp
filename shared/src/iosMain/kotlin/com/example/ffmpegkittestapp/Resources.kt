package com.example.ffmpegkittestapp

import dev.icerock.moko.resources.ImageResource
import okio.FileSystem
import okio.Path
import platform.Foundation.writeToFile
import platform.UIKit.UIImageJPEGRepresentation

actual fun ImageResource.toFile(context: Any, file: Path) {
    val image = toUIImage() ?: error("Unable to create UIImage for $assetImageName")
    val jpeg = UIImageJPEGRepresentation(image, 1.0)
        ?: error("Unable to create jpeg for $assetImageName")
    val success = jpeg.writeToFile(file.toString(), true)
    if(!success) error("Unable to write jpeg to $file")
}
