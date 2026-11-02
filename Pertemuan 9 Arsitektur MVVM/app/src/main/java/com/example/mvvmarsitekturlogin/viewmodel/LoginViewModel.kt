package com.example.mvvmarsitekturlogin.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mvvmarsitekturlogin.data.local.database.AppDatabase
import com.example.mvvmarsitekturlogin.data.local.entity.User
import com.example.mvvmarsitekturlogin.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val message: String? = null,
    val isSuccess: Boolean = false
)

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        insertDummyUser()
    }

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, message = null, isSuccess = false) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, message = null, isSuccess = false) }
    }

    fun login() {
        val username = _uiState.value.username.trim()
        val password = _uiState.value.password

        if (username.isEmpty() || password.isEmpty()) {
            _uiState.update {
                it.copy(message = "Username dan password tidak boleh kosong", isSuccess = false)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            val user = repository.login(username, password)
            _uiState.update {
                if (user != null) {
                    it.copy(isLoading = false, message = "Login Berhasil, selamat datang ${user.username}!", isSuccess = true)
                } else {
                    it.copy(isLoading = false, message = "Username atau Password Salah", isSuccess = false)
                }
            }
        }
    }

    private fun insertDummyUser() {
        viewModelScope.launch {
            repository.insert(User(username = "admin", password = "12345"))
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                val dao = AppDatabase.getDatabase(app).userDao()
                LoginViewModel(UserRepository(dao))
            }
        }
    }
}
