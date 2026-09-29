package com.example.studentmanager

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.studentmanager.data.SampleStudents
import com.example.studentmanager.data.Student
import com.example.studentmanager.data.filterStudents

sealed interface Screen {
    data object Splash : Screen
    data object List : Screen
    data object Add : Screen
    data class Edit(val studentId: Long) : Screen
}

class StudentViewModel : ViewModel() {
    var screen: Screen by mutableStateOf(Screen.Splash)
        private set

    var query by mutableStateOf("")
        private set

    /** Pesan singkat (snackbar) yang menunggu ditampilkan oleh layar daftar. */
    var message: String? by mutableStateOf(null)
        private set

    private val allStudents = mutableStateListOf<Student>().apply { addAll(SampleStudents) }
    private var nextId = (SampleStudents.maxOfOrNull { it.id } ?: 0L) + 1

    val students: List<Student> get() = allStudents

    val filteredStudents by derivedStateOf { filterStudents(allStudents, query) }

    fun studentById(id: Long): Student? = allStudents.firstOrNull { it.id == id }

    fun finishSplash() {
        if (screen == Screen.Splash) screen = Screen.List
    }

    fun openAdd() {
        screen = Screen.Add
    }

    fun openEdit(student: Student) {
        screen = Screen.Edit(student.id)
    }

    fun openList() {
        screen = Screen.List
    }

    fun updateQuery(value: String) {
        query = value
    }

    fun refresh() {
        query = ""
        message = "Daftar diperbarui"
    }

    fun addStudent(nim: String, name: String, programStudi: String) {
        allStudents.add(Student(nextId++, nim.trim(), name.trim(), programStudi))
        message = "Mahasiswa berhasil ditambahkan"
        screen = Screen.List
    }

    fun updateStudent(id: Long, nim: String, name: String, programStudi: String) {
        val index = allStudents.indexOfFirst { it.id == id }
        if (index >= 0) {
            allStudents[index] = Student(id, nim.trim(), name.trim(), programStudi)
            message = "Data mahasiswa berhasil diubah"
        }
        screen = Screen.List
    }

    fun deleteStudent(student: Student) {
        allStudents.removeAll { it.id == student.id }
        message = "${student.name} telah dihapus"
    }

    fun consumeMessage() {
        message = null
    }
}
