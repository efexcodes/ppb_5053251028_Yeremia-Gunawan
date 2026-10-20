package com.example.registrasiperpustakaan.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasiperpustakaan.data.Anggota

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    anggota: Anggota,
    onKembali: () -> Unit,
    onEdit: () -> Unit,
    onHapus: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Anggota", fontWeight = FontWeight.SemiBold) },
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AvatarInisial(anggota.nama, ukuran = 80.dp, teks = 30.sp)
                Text(
                    anggota.nama,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(anggota.nim, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LabelJenis(anggota.jenis, Modifier.padding(top = 6.dp))
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                    BarisInfo(Icons.Default.Person, "Program Studi", anggota.programStudi)
                    HorizontalDivider()
                    BarisInfo(Icons.Default.Home, "Alamat", anggota.alamat)
                    HorizontalDivider()
                    BarisInfo(Icons.Default.Phone, "Nomor Telepon", anggota.telepon)
                    HorizontalDivider()
                    BarisInfo(Icons.Default.Email, "Email", anggota.email)
                    HorizontalDivider()
                    BarisInfo(Icons.Default.Info, "Tanggal Daftar", anggota.tanggalDaftar)
                }
            }

            Button(
                onClick = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Text("  Edit Data", fontWeight = FontWeight.SemiBold)
            }
            Button(
                onClick = onHapus,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Text("  Hapus Anggota", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun BarisInfo(ikon: ImageVector, label: String, nilai: String) {
    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(ikon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Column {
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(nilai, fontSize = 14.sp)
        }
    }
}
