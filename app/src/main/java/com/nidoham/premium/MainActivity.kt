package com.nidoham.premium

import android.os.Bundle
import androidx.activity.compose.setContent
import com.nidoham.premium.presentation.screen.MainScreen

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThemedContent {
                MainScreen()
            }
        }
    }
}