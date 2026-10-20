package com.example.registrasiperpustakaan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.registrasiperpustakaan.data.Anggota
import com.example.registrasiperpustakaan.data.FormInput
import com.example.registrasiperpustakaan.ui.DaftarScreen
import com.example.registrasiperpustakaan.ui.DetailScreen
import com.example.registrasiperpustakaan.ui.FormScreen
import com.example.registrasiperpustakaan.ui.theme.RegistrasiPerpustakaanTheme

class MainActivity : ComponentActivity() {
    private val viewModel: AnggotaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistrasiPerpustakaanTheme {
                LibraryRegApp(viewModel)
            }
        }
    }
}

@Composable
fun LibraryRegApp(vm: AnggotaViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    var akanDihapus by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(vm.pesan) {
        vm.pesan?.let {
            snackbarHostState.showSnackbar(it)
            vm.pesanDitampilkan()
        }
    }

    BackHandler(enabled = vm.layar != Layar.Daftar) { vm.kembali() }

    when (val layar = vm.layar) {
        Layar.Daftar -> DaftarScreen(
            anggota = vm.anggotaTampil,
            totalAnggota = vm.semuaAnggota.size,
            kataKunci = vm.kataKunci,
            onKataKunciChange = { vm.kataKunci = it },
            filter = vm.filterJenis,
            onFilterChange = { vm.filterJenis = it },
            onDaftar = { vm.buka(Layar.Pendaftaran) },
            onBuka = { vm.buka(Layar.Detail(it.nim)) },
            onEdit = { vm.buka(Layar.Edit(it.nim)) },
            onHapus = { akanDihapus = it.nim },
            snackbarHostState = snackbarHostState
        )

        Layar.Pendaftaran -> FormScreen(
            judul = "Pendaftaran Anggota",
            tombol = "Daftar",
            awal = FormInput(),
            nimTerdaftar = vm.nimTerdaftar(),
            onKembali = vm::kembali,
            onSimpan = vm::daftar,
            snackbarHostState = snackbarHostState
        )

        is Layar.Detail -> {
            val anggota = vm.cari(layar.nim)
            if (anggota == null) {
                LaunchedEffect(Unit) { vm.kembali() }
            } else {
                DetailScreen(
                    anggota = anggota,
                    onKembali = vm::kembali,
                    onEdit = { vm.buka(Layar.Edit(anggota.nim)) },
                    onHapus = { akanDihapus = anggota.nim },
                    snackbarHostState = snackbarHostState
                )
            }
        }

        is Layar.Edit -> {
            val anggota = vm.cari(layar.nim)
            if (anggota == null) {
                LaunchedEffect(Unit) { vm.kembali() }
            } else {
                FormScreen(
                    judul = "Edit Anggota",
                    tombol = "Simpan Perubahan",
                    awal = anggota.toFormInput(),
                    nimTerdaftar = vm.nimTerdaftar(kecuali = anggota.nim),
                    onKembali = vm::kembali,
                    onSimpan = { vm.perbarui(anggota.nim, it) },
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }

    akanDihapus?.let { nim ->
        val anggota = vm.cari(nim)
        if (anggota == null) {
            akanDihapus = null
        } else {
            AlertDialog(
                onDismissRequest = { akanDihapus = null },
                title = { Text("Hapus anggota?") },
                text = {
                    Text("Data ${anggota.nama} (${anggota.nim}) akan dihapus dari daftar anggota dan tidak dapat dikembalikan.")
                },
                confirmButton = {
                    TextButton(onClick = {
                        akanDihapus = null
                        vm.hapus(nim)
                    }) { Text("Hapus", color = MaterialTheme.colorScheme.error) }
                },
                dismissButton = {
                    TextButton(onClick = { akanDihapus = null }) { Text("Batal") }
                }
            )
        }
    }
}

private fun Anggota.toFormInput() = FormInput(
    nama = nama,
    nim = nim,
    programStudi = programStudi,
    alamat = alamat,
    telepon = telepon,
    email = email,
    jenis = jenis
)
