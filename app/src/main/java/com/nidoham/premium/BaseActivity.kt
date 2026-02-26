package com.nidoham.premium

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nidoham.premium.ui.theme.PremiumTheme
import com.nidoham.premium.ui.theme.ThemeManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale

/**
 * Base [ComponentActivity] that wires [ThemeManager] into [PremiumTheme].
 *
 * ## Usage
 * ```kotlin
 * class MainActivity : BaseActivity() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         setContent { ThemedContent { AppNavGraph() } }
 *     }
 * }
 * ```
 *
 * ## Language changes
 * Call [ThemeManager.setLanguage] then `recreate()` to apply the new locale.
 * [attachBaseContext] reads the persisted language tag synchronously and
 * wraps the base context before resources are resolved by the system.
 */
abstract class BaseActivity : ComponentActivity() {

    /** Singleton [ThemeManager] initialised in `Application.onCreate()`. */
    protected val themeManager: ThemeManager by lazy { ThemeManager.get() }

    /**
     * Applies the persisted locale before any resource resolution occurs.
     * This is the only reliable hook for locale-wrapping across all API levels.
     */
    override fun attachBaseContext(newBase: Context) {
        // DataStore has no synchronous API; runBlocking here is intentional and
        // bounded — it reads a single cached preferences file from disk.
        val tag = runBlocking {
            newBase.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
                .getString("language", "en") ?: "en"
        }
        super.attachBaseContext(wrapWithLocale(newBase, tag))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }

    /**
     * Reactive theme wrapper. Collects [ThemeManager.settings] as Compose state
     * so any setting change — dark mode, contrast, font scale — recomposes
     * only the affected subtree without requiring [recreate].
     *
     * Language changes are the sole exception: call `recreate()` after
     * [ThemeManager.setLanguage] so [attachBaseContext] can apply the new locale.
     */
    @Composable
    fun ThemedContent(content: @Composable () -> Unit) {
        val settings by themeManager.settings.collectAsState()
        PremiumTheme(settings = settings, content = content)
    }

    // ── Internal ─────────────────────────────────────────────────────

    private fun wrapWithLocale(context: Context, langTag: String): Context {
        val locale = Locale(langTag)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration).also {
            it.setLocale(locale)
        }
        return context.createConfigurationContext(config)
    }
}