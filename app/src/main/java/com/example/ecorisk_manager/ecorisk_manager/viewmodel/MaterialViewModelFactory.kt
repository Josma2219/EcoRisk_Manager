package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.repository.MaterialRepository

class MaterialViewModelFactory(
    private val materialRepository: MaterialRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MaterialViewModel::class.java)) {
            return MaterialViewModel(materialRepository) as T
        }

        throw IllegalArgumentException("ViewModel no reconocido")
    }
}