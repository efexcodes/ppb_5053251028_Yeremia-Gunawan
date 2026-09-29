package com.example.studentmanager.data

data class Student(
    val id: Long,
    val nim: String,
    val name: String,
    val programStudi: String,
)

val ProgramStudiOptions = listOf(
    "Informatika",
    "Sistem Informasi",
    "Teknik Komputer",
    "Desain Komunikasi Visual",
    "Manajemen",
    "Akuntansi",
)

const val MaxNimLength = 15

val SampleStudents = listOf(
    Student(1, "2301001", "Budi Santoso", "Informatika"),
    Student(2, "2301002", "Siti Aminah", "Sistem Informasi"),
    Student(3, "2301003", "Andi Wijaya", "Teknik Komputer"),
)

/** Filter mahasiswa berdasarkan nama, NIM, atau program studi (tanpa membedakan huruf besar/kecil). */
fun filterStudents(students: List<Student>, query: String): List<Student> {
    val keyword = query.trim()
    if (keyword.isEmpty()) return students
    return students.filter {
        it.name.contains(keyword, ignoreCase = true) ||
            it.nim.contains(keyword, ignoreCase = true) ||
            it.programStudi.contains(keyword, ignoreCase = true)
    }
}

data class FormErrors(
    val nim: String? = null,
    val name: String? = null,
    val programStudi: String? = null,
) {
    val hasError: Boolean get() = nim != null || name != null || programStudi != null
}

/**
 * Validasi input form tambah/edit.
 * [editingId] diisi saat mode edit agar NIM milik mahasiswa itu sendiri tidak dianggap duplikat.
 */
fun validateStudent(
    nim: String,
    name: String,
    programStudi: String,
    existing: List<Student>,
    editingId: Long? = null,
): FormErrors {
    val nimError = when {
        nim.isBlank() -> "NIM tidak boleh kosong"
        !nim.all { it.isDigit() } -> "NIM hanya boleh berisi angka"
        existing.any { it.nim == nim.trim() && it.id != editingId } -> "NIM sudah terdaftar"
        else -> null
    }
    val nameError = if (name.isBlank()) "Nama tidak boleh kosong" else null
    val prodiError = if (programStudi.isBlank()) "Program studi harus dipilih" else null
    return FormErrors(nimError, nameError, prodiError)
}
