package com.example.registrasiperpustakaan.data

data class FormInput(
    val nama: String = "",
    val nim: String = "",
    val programStudi: String = "",
    val alamat: String = "",
    val telepon: String = "",
    val email: String = "",
    val jenis: JenisKeanggotaan = JenisKeanggotaan.MAHASISWA
)

data class FormErrors(
    val nama: String? = null,
    val nim: String? = null,
    val programStudi: String? = null,
    val alamat: String? = null,
    val telepon: String? = null,
    val email: String? = null
) {
    val adaError: Boolean
        get() = listOf(nama, nim, programStudi, alamat, telepon, email).any { it != null }
}

private val EmailRegex = Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*\.[A-Za-z]{2,}$""")

/**
 * Memeriksa isian formulir.
 * [nimTerdaftar] adalah NIM/NIP milik anggota lain yang sudah ada (untuk cek duplikat).
 */
fun validasiForm(input: FormInput, nimTerdaftar: Set<String>): FormErrors {
    val nama = input.nama.trim()
    val nim = input.nim.trim()
    val telepon = input.telepon.trim()
    val email = input.email.trim()

    return FormErrors(
        nama = if (nama.isEmpty()) "Nama lengkap wajib diisi" else null,
        nim = when {
            nim.isEmpty() -> "NIM/NIP wajib diisi"
            !nim.all { it.isDigit() } || nim.length !in 8..15 -> "NIM/NIP harus 8–15 digit angka"
            nim in nimTerdaftar -> "NIM/NIP sudah terdaftar"
            else -> null
        },
        programStudi = if (input.programStudi.isBlank()) "Program studi wajib dipilih" else null,
        alamat = if (input.alamat.isBlank()) "Alamat wajib diisi" else null,
        telepon = when {
            telepon.isEmpty() -> "Nomor telepon wajib diisi"
            !telepon.all { it.isDigit() } || telepon.length !in 10..13 -> "Nomor telepon harus 10–13 digit"
            else -> null
        },
        email = when {
            email.isEmpty() -> "Email wajib diisi"
            !EmailRegex.matches(email) -> "Format email tidak valid"
            else -> null
        }
    )
}
