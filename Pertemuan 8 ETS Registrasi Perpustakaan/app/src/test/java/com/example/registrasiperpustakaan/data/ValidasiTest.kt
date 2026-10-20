package com.example.registrasiperpustakaan.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidasiTest {
    private val valid = FormInput(
        nama = "Andi Pratama",
        nim = "2210512001",
        programStudi = "Informatika",
        alamat = "Jl. Mawar 12",
        telepon = "081234567890",
        email = "andi@student.ac.id"
    )

    @Test fun dataValid_tanpaError() {
        assertFalse(validasiForm(valid, emptySet()).adaError)
    }

    @Test fun kolomKosong_wajibDiisi() {
        val e = validasiForm(FormInput(), emptySet())
        assertNotNull(e.nama); assertNotNull(e.nim); assertNotNull(e.programStudi)
        assertNotNull(e.alamat); assertNotNull(e.telepon); assertNotNull(e.email)
    }

    @Test fun formatEmail() {
        assertNotNull(validasiForm(valid.copy(email = "andi@"), emptySet()).email)
        assertNotNull(validasiForm(valid.copy(email = "andi.student.ac.id"), emptySet()).email)
        assertNull(validasiForm(valid.copy(email = "a.b@kampus.ac.id"), emptySet()).email)
    }

    @Test fun panjangNim() {
        assertNotNull(validasiForm(valid.copy(nim = "1234567"), emptySet()).nim)
        assertNull(validasiForm(valid.copy(nim = "12345678"), emptySet()).nim)
        assertNull(validasiForm(valid.copy(nim = "123456789012345"), emptySet()).nim)
        assertNotNull(validasiForm(valid.copy(nim = "1234567890123456"), emptySet()).nim)
        assertNotNull(validasiForm(valid.copy(nim = "22A10512"), emptySet()).nim)
    }

    @Test fun nimDuplikat() {
        assertTrue(validasiForm(valid, setOf("2210512001")).nim!!.contains("sudah terdaftar"))
    }

    @Test fun panjangTelepon() {
        assertNotNull(validasiForm(valid.copy(telepon = "0812"), emptySet()).telepon)
        assertNull(validasiForm(valid.copy(telepon = "0812345678"), emptySet()).telepon)
        assertNotNull(validasiForm(valid.copy(telepon = "08123456789012"), emptySet()).telepon)
    }
}
