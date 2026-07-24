package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.repository.ReporteRepository

class ReporteViewModelFactory(
    private val reporteRepository: ReporteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReporteViewModel::class.java)) {
            return ReporteViewModel(reporteRepository) as T
        }

        throw IllegalArgumentException("ViewModel no reconocido")
    }
}