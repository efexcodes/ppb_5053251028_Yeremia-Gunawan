package com.example.datadiri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.datadiri.ui.theme.DataDiriTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataDiriTheme {
                ProfileCardScreen(
                    modifier = Modifier.fillMaxSize(),
                    logoPainter = painterResource(id = R.drawable.logo_its),
                    photoPainter = painterResource(id = R.drawable.foto_profil)
                )
            }
        }
    }
}
