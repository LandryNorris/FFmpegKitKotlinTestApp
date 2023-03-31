package com.example.ffmpegkittestapp

import dev.icerock.moko.resources.ImageResource
import okio.Path

expect fun ImageResource.toFile(context: Any, file: Path)
