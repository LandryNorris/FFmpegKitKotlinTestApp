package com.example.ffmpegkittestapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform