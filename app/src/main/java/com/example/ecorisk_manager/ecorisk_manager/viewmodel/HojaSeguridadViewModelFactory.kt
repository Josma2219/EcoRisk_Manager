package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.repository.HojaSeguridadRepository

class HojaSeguridadViewModelFactory(
    private val hojaSeguridadRepository: HojaSeguridadRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HojaSeguridadViewModel::class.java)) {
            return HojaSeguridadViewModel(hojaSeguridadRepository) as T
        }

        throw IllegalArgumentException("ViewModel no reconocido")
    }
}