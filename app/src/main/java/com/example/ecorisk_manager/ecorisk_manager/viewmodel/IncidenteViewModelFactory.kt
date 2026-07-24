package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.repository.IncidenteRepository

class IncidenteViewModelFactory(
    private val incidenteRepository: IncidenteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncidenteViewModel::class.java)) {
            return IncidenteViewModel(incidenteRepository) as T
        }

        throw IllegalArgumentException("ViewModel no reconocido")
    }
}