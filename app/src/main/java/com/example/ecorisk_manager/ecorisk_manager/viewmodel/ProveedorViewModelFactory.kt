package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.repository.ProveedorRepository

class ProveedorViewModelFactory(
    private val proveedorRepository: ProveedorRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProveedorViewModel::class.java)) {
            return ProveedorViewModel(proveedorRepository) as T
        }

        throw IllegalArgumentException("ViewModel no reconocido")
    }
}