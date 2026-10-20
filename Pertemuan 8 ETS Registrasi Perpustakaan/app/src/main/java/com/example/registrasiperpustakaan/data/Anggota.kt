package com.example.registrasiperpustakaan.data

enum class JenisKeanggotaan(val label: String) {
    MAHASISWA("Mahasiswa"),
    DOSEN("Dosen"),
    STAF("Staf")
}

data class Anggota(
    val nim: String,
    val nama: String,
    val programStudi: String,
    val alamat: String,
    val telepon: String,
    val email: String,
    val jenis: JenisKeanggotaan,
    val tanggalDaftar: String
)

val DaftarProgramStudi = listOf(
    "Informatika",
    "Sistem Informasi",
    "Teknik Industri",
    "Teknik Elektro",
    "Manajemen",
    "Akuntansi",
    "Perpustakaan"
)
