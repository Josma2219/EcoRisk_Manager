package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.data.repository.ReporteRepository
import com.example.ecorisk_manager.model.IncidenteDetalle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReporteViewModel(
    private val reporteRepository: ReporteRepository
) : ViewModel() {

    private val _materialesReporte = MutableLiveData<List<MaterialPeligrosoEntity>>(emptyList())
    val materialesReporte: LiveData<List<MaterialPeligrosoEntity>> = _materialesReporte

    private val _incidentesReporte = MutableLiveData<List<IncidenteDetalle>>(emptyList())
    val incidentesReporte: LiveData<List<IncidenteDetalle>> = _incidentesReporte

    private var trabajoMateriales: Job? = null
    private var trabajoIncidentes: Job? = null

    fun cargarTodosLosMateriales() {
        trabajoMateriales?.cancel()

        trabajoMateriales = viewModelScope.launch {
            reporteRepository.obtenerTodosLosMateriales().collectLatest { lista ->
                _materialesReporte.value = lista
            }
        }
    }

    fun cargarMaterialesPorRiesgo(clasificacionRiesgo: String) {
        trabajoMateriales?.cancel()

        trabajoMateriales = viewModelScope.launch {
            reporteRepository.obtenerMaterialesPorRiesgo(clasificacionRiesgo).collectLatest { lista ->
                _materialesReporte.value = lista
            }
        }
    }

    fun cargarHistorialIncidentes() {
        trabajoIncidentes?.cancel()

        trabajoIncidentes = viewModelScope.launch {
            reporteRepository.obtenerHistorialIncidentes().collectLatest { lista ->
                _incidentesReporte.value = lista
            }
        }
    }

    fun cargarIncidentesPorEstado(estado: String) {
        trabajoIncidentes?.cancel()

        trabajoIncidentes = viewModelScope.launch {
            reporteRepository.obtenerIncidentesPorEstado(estado).collectLatest { lista ->
                _incidentesReporte.value = lista
            }
        }
    }

    fun cargarIncidentesPorSeveridad(nivelSeveridad: String) {
        trabajoIncidentes?.cancel()

        trabajoIncidentes = viewModelScope.launch {
            reporteRepository.obtenerIncidentesPorSeveridad(nivelSeveridad).collectLatest { lista ->
                _incidentesReporte.value = lista
            }
        }
    }
}