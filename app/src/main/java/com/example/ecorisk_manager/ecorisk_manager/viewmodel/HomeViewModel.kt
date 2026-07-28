package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecorisk_manager.data.repository.HomeRepository
import com.example.ecorisk_manager.model.ResumenDashboard
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de obtener y exponer la información
 * resumida mostrada en el dashboard principal de la aplicación.
 */
class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _resumenDashboard = MutableLiveData(ResumenDashboard())
    val resumenDashboard: LiveData<ResumenDashboard> = _resumenDashboard

    /**
     * Obtiene los indicadores principales del sistema y actualiza
     * la información mostrada en el dashboard.
     */
    fun cargarResumenDashboard() {
        viewModelScope.launch {
            try {
                _resumenDashboard.value = homeRepository.obtenerResumenDashboard()
            } catch (error: Exception) {
                // Si algo falla, dejamos el dashboard en cero para que la app no se caiga.
                _resumenDashboard.value = ResumenDashboard()
            }
        }
    }
}