package com.example.registrasiperpustakaan.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BiruUtama,
    onPrimary = Color.White,
    primaryContainer = BiruMuda,
    onPrimaryContainer = Color(0xFF1B1F2A),
    secondary = Oranye,
    onSecondary = Color(0xFF1B1F2A),
    background = LatarAplikasi,
    surface = Color.White,
    error = Merah,
    errorContainer = MerahMuda,
    onErrorContainer = Merah
)

@Composable
fun RegistrasiPerpustakaanTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
