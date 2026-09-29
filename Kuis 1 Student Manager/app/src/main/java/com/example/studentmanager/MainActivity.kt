package com.example.studentmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.studentmanager.ui.StudentApp
import com.example.studentmanager.ui.theme.StudentManagerTheme

class MainActivity : ComponentActivity() {
    private val viewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentManagerTheme {
                StudentApp(viewModel = viewModel, onExit = { finish() })
            }
        }
    }
}
