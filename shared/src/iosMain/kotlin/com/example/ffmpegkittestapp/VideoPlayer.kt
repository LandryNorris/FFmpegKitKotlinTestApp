package com.example.ffmpegkittestapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.COpaquePointer
import observer.ObserverProtocol
import platform.AVFoundation.*
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSURL
import platform.Foundation.addObserver
import platform.UIKit.UIColor
import platform.UIKit.UIColor.Companion.blackColor
import platform.UIKit.UIView
import platform.UIKit.backgroundColor
import platform.darwin.NSObject

val observer = StatusObserver()
var onReadyToPlay: () -> Unit = {}
private var playerView: UIView? = null
private var playerLayer: AVPlayerLayer? = null

@Composable
internal actual fun VideoPlayer(modifier: Modifier, url: String) {
    val player = remember(url) {
        println("Setting up AVPlayer for $url")
        val nsurl = NSURL.URLWithString("file://$url")!!
        val item = AVPlayerItem(AVAsset.assetWithURL(nsurl))
        item.addObserver(observer, forKeyPath = "status",
            options = NSKeyValueObservingOptionNew, null)
        AVPlayer(item).also {
            onReadyToPlay = {
                playerLayer?.setFrame(playerView!!.frame)
                it.play()
            }
        }
    }

    UIKitView(modifier = modifier, factory = {
        playerView = UIView().apply {
            backgroundColor = blackColor
        }
        playerLayer = AVPlayerLayer.playerLayerWithPlayer(player)
        playerLayer?.backgroundColor = UIColor.blackColor.CGColor
        playerView?.layer?.addSublayer(playerLayer!!)

        playerView!!
    }, onRelease = { playerView = null })
}

class StatusObserver: NSObject(), ObserverProtocol {
    override fun observeValueForKeyPath(
        keyPath: String?,
        ofObject: Any?,
        change: Map<Any?, *>?,
        context: COpaquePointer?
    ) {
        val value = ofObject as? AVPlayerItem ?: return
        println("Status: ${value.status}")
        println("Error: ${value.error}")
        if(value.status == AVPlayerStatusReadyToPlay) onReadyToPlay()
    }
}
