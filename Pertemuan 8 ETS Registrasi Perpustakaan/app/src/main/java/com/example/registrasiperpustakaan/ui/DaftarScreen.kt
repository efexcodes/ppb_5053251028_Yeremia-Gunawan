package com.example.registrasiperpustakaan.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasiperpustakaan.data.Anggota
import com.example.registrasiperpustakaan.data.JenisKeanggotaan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarScreen(
    anggota: List<Anggota>,
    totalAnggota: Int,
    kataKunci: String,
    onKataKunciChange: (String) -> Unit,
    filter: JenisKeanggotaan?,
    onFilterChange: (JenisKeanggotaan?) -> Unit,
    onDaftar: () -> Unit,
    onBuka: (Anggota) -> Unit,
    onEdit: (Anggota) -> Unit,
    onHapus: (Anggota) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("LibraryReg", fontWeight = FontWeight.SemiBold)
                        Text(
                            "$totalAnggota anggota terdaftar",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onDaftar,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Daftar", fontWeight = FontWeight.SemiBold) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = kataKunci,
                onValueChange = onKataKunciChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cari nama atau NIM/NIP") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (kataKunci.isNotEmpty()) {
                        IconButton(onClick = { onKataKunciChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Hapus kata kunci")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp)
            )
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filter == null,
                    onClick = { onFilterChange(null) },
                    label = { Text("Semua") }
                )
                JenisKeanggotaan.entries.forEach { jenis ->
                    FilterChip(
                        selected = filter == jenis,
                        onClick = { onFilterChange(jenis) },
                        label = { Text(jenis.label) }
                    )
                }
            }

            if (anggota.isEmpty()) {
                KeadaanKosong(adaPencarian = kataKunci.isNotBlank() || filter != null)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(anggota, key = { it.nim }) { a ->
                        KartuAnggota(a, kataKunci.trim(), { onBuka(a) }, { onEdit(a) }, { onHapus(a) })
                    }
                }
            }
        }
    }
}

@Composable
private fun KartuAnggota(
    anggota: Anggota,
    kataKunci: String,
    onBuka: () -> Unit,
    onEdit: () -> Unit,
    onHapus: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onBuka),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarInisial(anggota.nama)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(sorot(anggota.nama, kataKunci), fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(
                    sorot("${anggota.nim} · ${anggota.programStudi}", kataKunci),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LabelJenis(anggota.jenis, Modifier.padding(top = 5.dp))
            }
            Column {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit ${anggota.nama}",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onHapus) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Hapus ${anggota.nama}",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/** Menyorot bagian teks yang cocok dengan kata kunci. */
private fun sorot(teks: String, kataKunci: String): AnnotatedString = buildAnnotatedString {
    val awal = if (kataKunci.isEmpty()) -1 else teks.indexOf(kataKunci, ignoreCase = true)
    if (awal < 0) {
        append(teks)
    } else {
        append(teks.substring(0, awal))
        withStyle(SpanStyle(background = Color(0xFFFDE68A))) {
            append(teks.substring(awal, awal + kataKunci.length))
        }
        append(teks.substring(awal + kataKunci.length))
    }
}

@Composable
private fun KeadaanKosong(adaPencarian: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.height(56.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Text(
            if (adaPencarian) "Anggota tidak ditemukan" else "Belum ada anggota",
            fontWeight = FontWeight.SemiBold
        )
        Text(
            if (adaPencarian) "Coba kata kunci lain atau periksa penulisan nama / NIM."
            else "Ketuk tombol Daftar untuk menambah anggota.",
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
