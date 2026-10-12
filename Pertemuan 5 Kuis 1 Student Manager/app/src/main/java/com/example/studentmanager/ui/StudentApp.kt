package com.example.studentmanager.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.studentmanager.Screen
import com.example.studentmanager.StudentViewModel

@Composable
fun StudentApp(viewModel: StudentViewModel, onExit: () -> Unit) {
    val screen = viewModel.screen

    BackHandler(enabled = screen is Screen.Add || screen is Screen.Edit) {
        viewModel.openList()
    }

    Crossfade(targetState = screen, label = "screen") { current ->
        when (current) {
            Screen.Splash -> SplashScreen(onFinished = viewModel::finishSplash)

            Screen.List -> StudentListScreen(
                students = viewModel.filteredStudents,
                totalCount = viewModel.students.size,
                query = viewModel.query,
                message = viewModel.message,
                onQueryChange = viewModel::updateQuery,
                onAddClick = viewModel::openAdd,
                onEditClick = viewModel::openEdit,
                onDeleteConfirmed = viewModel::deleteStudent,
                onRefresh = viewModel::refresh,
                onMessageShown = viewModel::consumeMessage,
                onExit = onExit,
            )

            Screen.Add -> StudentFormScreen(
                student = null,
                existing = viewModel.students,
                onSave = viewModel::addStudent,
                onCancel = viewModel::openList,
            )

            is Screen.Edit -> {
                val student = viewModel.studentById(current.studentId)
                if (student == null) {
                    LaunchedEffect(Unit) { viewModel.openList() }
                } else {
                    StudentFormScreen(
                        student = student,
                        existing = viewModel.students,
                        onSave = { nim, name, prodi ->
                            viewModel.updateStudent(student.id, nim, name, prodi)
                        },
                        onCancel = viewModel::openList,
                    )
                }
            }
        }
    }
}
