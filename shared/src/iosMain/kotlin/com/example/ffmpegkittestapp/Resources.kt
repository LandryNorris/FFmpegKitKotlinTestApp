package com.example.ffmpegkittestapp

import dev.icerock.moko.resources.ImageResource
import okio.Path
import platform.Foundation.NSFileManager

actual fun ImageResource.toFile(context: Any, file: Path) {
    val resourcesFolder = bundle.resourcePath ?: error("No bundle path")
    val name = assetImageName
    val resourcePath = resourcesFolder.trimEnd('/') + name + ".jpg"

    NSFileManager.defaultManager.copyItemAtPath(resourcePath, file.toString(), null)
}
