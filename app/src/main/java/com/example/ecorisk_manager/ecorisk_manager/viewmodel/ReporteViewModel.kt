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

/**
 * ViewModel encargado de gestionar la información utilizada
 * para la generación de reportes del sistema.
 */
class ReporteViewModel(
    private val reporteRepository: ReporteRepository
) : ViewModel() {

    private val _materialesReporte = MutableLiveData<List<MaterialPeligrosoEntity>>(emptyList())
    val materialesReporte: LiveData<List<MaterialPeligrosoEntity>> = _materialesReporte

    private val _incidentesReporte = MutableLiveData<List<IncidenteDetalle>>(emptyList())
    val incidentesReporte: LiveData<List<IncidenteDetalle>> = _incidentesReporte

    // Se utilizan trabajos independientes para permitir cancelar
    // consultas anteriores cuando se aplican nuevos filtros.
    private var trabajoMateriales: Job? = null
    private var trabajoIncidentes: Job? = null

    /**
     * Obtiene todos los materiales registrados para el reporte.
     */
    fun cargarTodosLosMateriales() {
        trabajoMateriales?.cancel()

        trabajoMateriales = viewModelScope.launch {
            reporteRepository.obtenerTodosLosMateriales().collectLatest { lista ->
                _materialesReporte.value = lista
            }
        }
    }

    /**
     * Obtiene los materiales que pertenecen a una
     * clasificación de riesgo específica.
     */
    fun cargarMaterialesPorRiesgo(clasificacionRiesgo: String) {
        trabajoMateriales?.cancel()

        trabajoMateriales = viewModelScope.launch {
            reporteRepository.obtenerMaterialesPorRiesgo(clasificacionRiesgo).collectLatest { lista ->
                _materialesReporte.value = lista
            }
        }
    }

    /**
     * Obtiene el historial completo de incidentes.
     */
    fun cargarHistorialIncidentes() {
        trabajoIncidentes?.cancel()

        trabajoIncidentes = viewModelScope.launch {
            reporteRepository.obtenerHistorialIncidentes().collectLatest { lista ->
                _incidentesReporte.value = lista
            }
        }
    }

    /**
     * Filtra los incidentes según su estado.
     */
    fun cargarIncidentesPorEstado(estado: String) {
        trabajoIncidentes?.cancel()

        trabajoIncidentes = viewModelScope.launch {
            reporteRepository.obtenerIncidentesPorEstado(estado).collectLatest { lista ->
                _incidentesReporte.value = lista
            }
        }
    }

    /**
     * Filtra los incidentes según su nivel de severidad.
     */
    fun cargarIncidentesPorSeveridad(nivelSeveridad: String) {
        trabajoIncidentes?.cancel()

        trabajoIncidentes = viewModelScope.launch {
            reporteRepository.obtenerIncidentesPorSeveridad(nivelSeveridad).collectLatest { lista ->
                _incidentesReporte.value = lista
            }
        }
    }
}