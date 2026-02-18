package com.nidoham.premium

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.nidoham.extractor.Downloader
import timber.log.Timber

// ╔════════════════════════════════════════════════════════════════════╗
// ║  PREMIUM APPLICATION ENTRY POINT                                   ║
// ║  Standard Application class (No DI Framework).                     ║
// ╚════════════════════════════════════════════════════════════════════╝

class PremiumApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        Downloader.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    super.log(priority, "GlassApp:$tag", message, t)
                }
            })
        }
    }

    /**
     * **Global Coil Configuration**
     *
     * Configures the default ImageLoader for the entire app.
     * This is crucial for Glassmorphism to ensure images fade in smoothly
     * behind the glass layers rather than popping in harshly.
     */
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            // Enable crossfade for smooth UI transitions
            .crossfade(true)
            .crossfade(300)
            // Caching strategy (respects OS memory limits)
            .respectCacheHeaders(false)
            .build()
    }
}