package com.example.studentmanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studentmanager.data.FormErrors
import com.example.studentmanager.data.MaxNimLength
import com.example.studentmanager.data.ProgramStudiOptions
import com.example.studentmanager.data.Student
import com.example.studentmanager.data.validateStudent

private fun errorText(message: String?): (@Composable () -> Unit)? =
    message?.let { { Text(it) } }

/**
 * Form untuk tambah ([student] == null) maupun edit mahasiswa.
 * [onSave] menerima NIM, nama, dan program studi yang sudah divalidasi.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFormScreen(
    student: Student?,
    existing: List<Student>,
    onSave: (nim: String, name: String, programStudi: String) -> Unit,
    onCancel: () -> Unit,
) {
    var nim by rememberSaveable { mutableStateOf(student?.nim.orEmpty()) }
    var name by rememberSaveable { mutableStateOf(student?.name.orEmpty()) }
    var programStudi by rememberSaveable { mutableStateOf(student?.programStudi.orEmpty()) }
    var dropdownExpanded by rememberSaveable { mutableStateOf(false) }
    // Error baru ditampilkan setelah pengguna menekan Simpan; kemudian diperbarui saat mengetik.
    var submitted by rememberSaveable { mutableStateOf(false) }

    val errors = if (submitted) {
        validateStudent(nim, name, programStudi, existing, student?.id)
    } else {
        FormErrors()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (student == null) "Tambah Mahasiswa" else "Edit Mahasiswa") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = nim,
                    onValueChange = { input ->
                        nim = input.filter { it.isDigit() }.take(MaxNimLength)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("NIM") },
                    placeholder = { Text("Masukkan NIM") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errors.nim != null,
                    supportingText = errorText(errors.nim),
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nama") },
                    placeholder = { Text("Masukkan nama mahasiswa") },
                    singleLine = true,
                    isError = errors.name != null,
                    supportingText = errorText(errors.name),
                )
                // Dropdown digambar inline (bukan jendela popup) agar selalu bisa disentuh.
                Box {
                    OutlinedTextField(
                        value = programStudi,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Program Studi") },
                        placeholder = { Text("Pilih program studi") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.rotate(if (dropdownExpanded) 180f else 0f),
                            )
                        },
                        isError = errors.programStudi != null,
                        supportingText = errorText(errors.programStudi),
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { dropdownExpanded = !dropdownExpanded },
                    )
                }
                if (dropdownExpanded) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        ProgramStudiOptions.forEach { option ->
                            val selected = option == programStudi
                            DropdownMenuItem(
                                text = { Text(option) },
                                modifier = Modifier.background(
                                    if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                    else Color.Transparent,
                                ),
                                onClick = {
                                    programStudi = option
                                    dropdownExpanded = false
                                },
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    shape = ButtonShape,
                ) { Text("Batal") }
                Button(
                    shape = ButtonShape,
                    onClick = {
                        submitted = true
                        if (!validateStudent(nim, name, programStudi, existing, student?.id).hasError) {
                            onSave(nim, name, programStudi)
                        }
                    },
                    modifier = Modifier.weight(1f),
                ) { Text("Simpan") }
            }
        }
    }
}
