package com.example.registrasiperpustakaan.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.registrasiperpustakaan.data.DaftarProgramStudi
import com.example.registrasiperpustakaan.data.FormErrors
import com.example.registrasiperpustakaan.data.FormInput
import com.example.registrasiperpustakaan.data.JenisKeanggotaan
import com.example.registrasiperpustakaan.data.validasiForm

/**
 * Formulir yang dipakai untuk pendaftaran dan edit.
 * [nimTerdaftar] berisi NIM/NIP anggota lain untuk pengecekan duplikat.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    judul: String,
    tombol: String,
    awal: FormInput,
    nimTerdaftar: Set<String>,
    onKembali: () -> Unit,
    onSimpan: (FormInput) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var nama by rememberSaveable { mutableStateOf(awal.nama) }
    var nim by rememberSaveable { mutableStateOf(awal.nim) }
    var prodi by rememberSaveable { mutableStateOf(awal.programStudi) }
    var alamat by rememberSaveable { mutableStateOf(awal.alamat) }
    var telepon by rememberSaveable { mutableStateOf(awal.telepon) }
    var email by rememberSaveable { mutableStateOf(awal.email) }
    var jenis by rememberSaveable { mutableStateOf(awal.jenis) }
    var galat by remember { mutableStateOf(FormErrors()) }
    var menuProdi by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(judul, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onKembali) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Kolom(
                label = "Nama Lengkap *",
                nilai = nama,
                galat = galat.nama,
                onChange = { nama = it; galat = galat.copy(nama = null) },
                keyboard = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Kolom(
                label = "NIM / NIP *",
                nilai = nim,
                galat = galat.nim,
                onChange = { nim = it.filter(Char::isDigit); galat = galat.copy(nim = null) },
                keyboard = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            ExposedDropdownMenuBox(
                expanded = menuProdi,
                onExpandedChange = { menuProdi = it },
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                OutlinedTextField(
                    value = prodi,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Program Studi *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuProdi) },
                    isError = galat.programStudi != null,
                    supportingText = galat.programStudi?.let { { Text(it) } },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = menuProdi, onDismissRequest = { menuProdi = false }) {
                    DaftarProgramStudi.forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p) },
                            onClick = {
                                prodi = p
                                galat = galat.copy(programStudi = null)
                                menuProdi = false
                            }
                        )
                    }
                }
            }

            Kolom(
                label = "Alamat *",
                nilai = alamat,
                galat = galat.alamat,
                onChange = { alamat = it; galat = galat.copy(alamat = null) },
                keyboard = KeyboardOptions(keyboardType = KeyboardType.Text),
                satuBaris = false
            )
            Kolom(
                label = "Nomor Telepon *",
                nilai = telepon,
                galat = galat.telepon,
                onChange = { telepon = it.filter(Char::isDigit); galat = galat.copy(telepon = null) },
                keyboard = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Kolom(
                label = "Email *",
                nilai = email,
                galat = galat.email,
                onChange = { email = it; galat = galat.copy(email = null) },
                keyboard = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Text(
                "Jenis Keanggotaan *",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                JenisKeanggotaan.entries.forEachIndexed { i, j ->
                    SegmentedButton(
                        selected = jenis == j,
                        onClick = { jenis = j },
                        shape = SegmentedButtonDefaults.itemShape(i, JenisKeanggotaan.entries.size)
                    ) { Text(j.label) }
                }
            }

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    val input = FormInput(nama, nim, prodi, alamat, telepon, email, jenis)
                    val hasil = validasiForm(input, nimTerdaftar)
                    galat = hasil
                    if (!hasil.adaError) onSimpan(input)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) { Text(tombol, fontWeight = FontWeight.SemiBold) }
        }
    }
}

@Composable
private fun Kolom(
    label: String,
    nilai: String,
    galat: String?,
    onChange: (String) -> Unit,
    keyboard: KeyboardOptions,
    satuBaris: Boolean = true
) {
    OutlinedTextField(
        value = nilai,
        onValueChange = onChange,
        label = { Text(label) },
        isError = galat != null,
        supportingText = galat?.let { { Text(it) } },
        keyboardOptions = keyboard,
        singleLine = satuBaris,
        minLines = if (satuBaris) 1 else 3,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    )
}
