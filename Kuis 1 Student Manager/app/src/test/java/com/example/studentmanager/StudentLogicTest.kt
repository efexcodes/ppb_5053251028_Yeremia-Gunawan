package com.example.studentmanager

import com.example.studentmanager.data.SampleStudents
import com.example.studentmanager.data.filterStudents
import com.example.studentmanager.data.validateStudent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StudentLogicTest {
    @Test
    fun filter_blankQueryReturnsAll() {
        assertEquals(SampleStudents, filterStudents(SampleStudents, "  "))
    }

    @Test
    fun filter_matchesNameNimAndProdi() {
        assertEquals(listOf("Budi Santoso"), filterStudents(SampleStudents, "budi").map { it.name })
        assertEquals(listOf("Siti Aminah"), filterStudents(SampleStudents, "2301002").map { it.name })
        assertEquals(listOf("Andi Wijaya"), filterStudents(SampleStudents, "komputer").map { it.name })
        assertTrue(filterStudents(SampleStudents, "zzz").isEmpty())
    }

    @Test
    fun validate_rejectsEmptyFields() {
        val errors = validateStudent("", "", "", SampleStudents)
        assertNotNull(errors.nim)
        assertNotNull(errors.name)
        assertNotNull(errors.programStudi)
        assertTrue(errors.hasError)
    }

    @Test
    fun validate_rejectsDuplicateNimButAllowsSameStudentOnEdit() {
        assertNotNull(validateStudent("2301001", "X", "Informatika", SampleStudents).nim)
        assertNull(validateStudent("2301001", "Budi", "Informatika", SampleStudents, editingId = 1).nim)
    }

    @Test
    fun validate_acceptsValidInput() {
        assertFalse(validateStudent("2301099", "Dewi", "Akuntansi", SampleStudents).hasError)
    }

    @Test
    fun viewModel_addEditDelete() {
        val vm = StudentViewModel()
        vm.finishSplash()
        assertEquals(Screen.List, vm.screen)
        assertEquals(3, vm.students.size)

        vm.addStudent("2301004", " Dewi ", "Akuntansi")
        assertEquals(4, vm.students.size)
        assertEquals("Dewi", vm.students.last().name)

        val added = vm.students.last()
        vm.updateStudent(added.id, "2301004", "Dewi Lestari", "Manajemen")
        assertEquals("Dewi Lestari", vm.studentById(added.id)?.name)

        vm.updateQuery("dewi")
        assertEquals(1, vm.filteredStudents.size)

        vm.deleteStudent(added)
        assertEquals(3, vm.students.size)
        assertNull(vm.studentById(added.id))
    }
}
