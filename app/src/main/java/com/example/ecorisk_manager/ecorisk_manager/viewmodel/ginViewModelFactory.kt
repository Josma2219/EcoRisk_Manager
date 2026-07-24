package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.repository.UsuarioRepository

class LoginViewModelFactory(
    private val usuarioRepository: UsuarioRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(usuarioRepository) as T
        }

        throw IllegalArgumentException("ViewModel no reconocido")
    }
}