package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.repository.MaterialProveedorRepository

class MaterialProveedorViewModelFactory(
    private val materialProveedorRepository: MaterialProveedorRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MaterialProveedorViewModel::class.java)) {
            return MaterialProveedorViewModel(materialProveedorRepository) as T
        }

        throw IllegalArgumentException("ViewModel no reconocido")
    }
}