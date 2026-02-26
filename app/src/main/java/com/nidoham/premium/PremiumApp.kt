package com.nidoham.premium

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.nidoham.extractor.Downloader
import com.nidoham.premium.ui.theme.ThemeManager
import timber.log.Timber

class PremiumApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        ThemeManager.init(this)
        Downloader.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                // Prefix every log tag so logs are easy to filter in Logcat.
                override fun createStackElementTag(element: StackTraceElement): String =
                    "GlassApp:${super.createStackElementTag(element)}"
            })
        }
    }

    /**
     * Provides a single app-wide [ImageLoader] instance for all Coil requests.
     *
     * - `crossfade(300)` — smooth 300 ms fade-in instead of an abrupt pop.
     * - `respectCacheHeaders(false)` — always serves from Coil's disk cache,
     *   ignoring `Cache-Control` / `Expires` headers from the server. Remove
     *   this if your image URLs are versioned and you want server-driven expiry.
     */
    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .crossfade(300)
            .respectCacheHeaders(false)
            .build()
}