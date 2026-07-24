package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.repository.RespaldoRepository

class RespaldoViewModelFactory(
    private val respaldoRepository: RespaldoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RespaldoViewModel::class.java)) {
            return RespaldoViewModel(respaldoRepository) as T
        }

        throw IllegalArgumentException("ViewModel no reconocido")
    }
}