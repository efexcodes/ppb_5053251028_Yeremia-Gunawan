package com.example.kalkulatorsederhana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kalkulatorsederhana.ui.theme.KalkulatorSederhanaTheme

private val OrangeButton = Color(0xFFFF7043)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KalkulatorSederhanaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KalkulatorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun KalkulatorScreen(modifier: Modifier = Modifier) {
    var angka1 by rememberSaveable { mutableStateOf("") }
    var angka2 by rememberSaveable { mutableStateOf("") }
    var hasil by rememberSaveable { mutableStateOf("") }

    fun hitung(operasi: (Double, Double) -> Double?) {
        val a = angka1.toDoubleOrNull()
        val b = angka2.toDoubleOrNull()
        hasil = if (a == null || b == null) {
            "Masukkan angka yang valid"
        } else {
            operasi(a, b)?.let(::formatAngka) ?: "Tidak bisa dibagi nol"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Kalkulator Sederhana",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        OutlinedTextField(
            value = angka1,
            onValueChange = { angka1 = it },
            label = { Text("Angka 1") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = angka2,
            onValueChange = { angka2 = it },
            label = { Text("Angka 2") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = hasil,
            onValueChange = {},
            readOnly = true,
            label = { Text("Result") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OperasiButton("+", Modifier.weight(1f)) { hitung { a, b -> a + b } }
            OperasiButton("-", Modifier.weight(1f)) { hitung { a, b -> a - b } }
            OperasiButton("×", Modifier.weight(1f)) { hitung { a, b -> a * b } }
            OperasiButton("/", Modifier.weight(1f)) { hitung { a, b -> if (b == 0.0) null else a / b } }
        }

        Button(
            onClick = {
                angka1 = ""
                angka2 = ""
                hasil = ""
            },
            colors = ButtonDefaults.buttonColors(containerColor = OrangeButton),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear", fontSize = 18.sp)
        }
    }
}

@Composable
private fun OperasiButton(label: String, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = OrangeButton),
        modifier = modifier
    ) {
        Text(label, fontSize = 20.sp)
    }
}

// 7.0 -> "7", 7.5 -> "7.5"
private fun formatAngka(value: Double): String =
    if (value % 1.0 == 0.0 && kotlin.math.abs(value) < 1e15) value.toLong().toString() else value.toString()

@Preview(showBackground = true)
@Composable
fun KalkulatorPreview() {
    KalkulatorSederhanaTheme {
        KalkulatorScreen()
    }
}
