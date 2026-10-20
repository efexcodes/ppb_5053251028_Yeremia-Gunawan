package com.example.registrasiperpustakaan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.registrasiperpustakaan.data.Anggota
import com.example.registrasiperpustakaan.data.FormInput
import com.example.registrasiperpustakaan.data.JenisKeanggotaan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed interface Layar {
    data object Daftar : Layar
    data object Pendaftaran : Layar
    data class Detail(val nim: String) : Layar
    data class Edit(val nim: String) : Layar
}

class AnggotaViewModel : ViewModel() {
    val semuaAnggota = mutableStateListOf(
        Anggota("2210512001", "Andi Pratama", "Informatika", "Jl. Mawar No. 12, Surabaya", "081234567890", "andi.pratama@student.ac.id", JenisKeanggotaan.MAHASISWA, "20 Oktober 2026"),
        Anggota("198503122010", "Budi Santoso", "Sistem Informasi", "Jl. Melati No. 5, Sidoarjo", "082134567811", "budi.santoso@kampus.ac.id", JenisKeanggotaan.DOSEN, "20 Oktober 2026"),
        Anggota("2210512045", "Citra Lestari", "Teknik Industri", "Jl. Anggrek No. 8, Surabaya", "085712345678", "citra.lestari@student.ac.id", JenisKeanggotaan.MAHASISWA, "20 Oktober 2026"),
        Anggota("197812052005", "Dewi Anggraini", "Perpustakaan", "Jl. Kenanga No. 3, Gresik", "081398765432", "dewi.anggraini@kampus.ac.id", JenisKeanggotaan.STAF, "20 Oktober 2026")
    )

    var layar by mutableStateOf<Layar>(Layar.Daftar)
        private set
    var kataKunci by mutableStateOf("")
    var filterJenis by mutableStateOf<JenisKeanggotaan?>(null)
    var pesan by mutableStateOf<String?>(null)

    val anggotaTampil: List<Anggota>
        get() {
            val q = kataKunci.trim()
            return semuaAnggota.filter { a ->
                (filterJenis == null || a.jenis == filterJenis) &&
                    (q.isEmpty() || a.nama.contains(q, ignoreCase = true) || a.nim.contains(q))
            }
        }

    fun cari(nim: String): Anggota? = semuaAnggota.firstOrNull { it.nim == nim }

    /** NIM/NIP anggota lain, tidak termasuk [kecuali] (dipakai saat edit). */
    fun nimTerdaftar(kecuali: String? = null): Set<String> =
        semuaAnggota.map { it.nim }.filter { it != kecuali }.toSet()

    fun buka(tujuan: Layar) { layar = tujuan }

    fun kembali() {
        layar = when (val l = layar) {
            is Layar.Edit -> Layar.Detail(l.nim)
            else -> Layar.Daftar
        }
    }

    fun daftar(input: FormInput) {
        semuaAnggota.add(
            input.toAnggota(tanggal = SimpleDateFormat("d MMMM yyyy", Locale.forLanguageTag("id-ID")).format(Date()))
        )
        pesan = "Anggota berhasil didaftarkan"
        layar = Layar.Daftar
    }

    fun perbarui(nimLama: String, input: FormInput) {
        val index = semuaAnggota.indexOfFirst { it.nim == nimLama }
        if (index < 0) return
        val baru = input.toAnggota(tanggal = semuaAnggota[index].tanggalDaftar)
        semuaAnggota[index] = baru
        pesan = "Data anggota berhasil diperbarui"
        layar = Layar.Detail(baru.nim)
    }

    fun hapus(nim: String) {
        semuaAnggota.removeAll { it.nim == nim }
        pesan = "Anggota berhasil dihapus"
        layar = Layar.Daftar
    }

    fun pesanDitampilkan() { pesan = null }

    private fun FormInput.toAnggota(tanggal: String) = Anggota(
        nim = nim.trim(),
        nama = nama.trim(),
        programStudi = programStudi,
        alamat = alamat.trim(),
        telepon = telepon.trim(),
        email = email.trim(),
        jenis = jenis,
        tanggalDaftar = tanggal
    )
}
