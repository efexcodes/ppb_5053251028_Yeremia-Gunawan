package com.example.marketplacesiswa

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marketplacesiswa.data.ThemeMode
import com.example.marketplacesiswa.ui.MarketApp
import com.example.marketplacesiswa.ui.theme.MarketplaceSiswaTheme
import com.example.marketplacesiswa.viewmodel.MarketViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MarketViewModel = viewModel()
            val state by viewModel.uiState.collectAsState()

            val darkTheme = when (state.themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            // Ikon status bar & navigation bar mengikuti tema aplikasi (bukan tema sistem).
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
                        darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
                    ) { darkTheme }
                )
                onDispose {}
            }

            MarketplaceSiswaTheme(
                darkTheme = darkTheme,
                dynamicColor = state.dynamicColor
            ) {
                MarketApp(viewModel)
            }
        }
    }
}
