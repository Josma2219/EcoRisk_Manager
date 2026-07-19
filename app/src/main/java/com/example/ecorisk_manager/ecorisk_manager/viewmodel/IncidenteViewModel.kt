package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecorisk_manager.data.entity.IncidenteEntity
import com.example.ecorisk_manager.data.repository.IncidenteRepository
import com.example.ecorisk_manager.model.IncidenteDetalle
import com.example.ecorisk_manager.model.ResultadoOperacion
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class IncidenteViewModel(
    private val incidenteRepository: IncidenteRepository
) : ViewModel() {

    private val _incidentes = MutableLiveData<List<IncidenteDetalle>>(emptyList())
    val incidentes: LiveData<List<IncidenteDetalle>> = _incidentes

    private val _incidenteSeleccionado = MutableLiveData<IncidenteEntity?>()
    val incidenteSeleccionado: LiveData<IncidenteEntity?> = _incidenteSeleccionado

    private val _incidenteDetalleSeleccionado = MutableLiveData<IncidenteDetalle?>()
    val incidenteDetalleSeleccionado: LiveData<IncidenteDetalle?> = _incidenteDetalleSeleccionado

    private val _resultadoOperacion = MutableLiveData<ResultadoOperacion?>()
    val resultadoOperacion: LiveData<ResultadoOperacion?> = _resultadoOperacion

    private var trabajoLista: Job? = null

    fun cargarIncidentes() {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            incidenteRepository.obtenerIncidentesDetalle().collectLatest { lista ->
                _incidentes.value = lista
            }
        }
    }

    fun cargarIncidentesPorMaterial(idMaterial: Int) {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            incidenteRepository.obtenerIncidentesDetallePorMaterial(idMaterial).collectLatest { lista ->
                _incidentes.value = lista
            }
        }
    }

    fun cargarIncidentesPorEstado(estado: String) {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            incidenteRepository.obtenerIncidentesDetallePorEstado(estado).collectLatest { lista ->
                _incidentes.value = lista
            }
        }
    }

    fun cargarIncidentesPorSeveridad(nivelSeveridad: String) {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            incidenteRepository.obtenerIncidentesDetallePorSeveridad(nivelSeveridad).collectLatest { lista ->
                _incidentes.value = lista
            }
        }
    }

    fun cargarIncidentePorId(idIncidente: Int) {
        viewModelScope.launch {
            val incidente = incidenteRepository.obtenerIncidentePorId(idIncidente)
            _incidenteSeleccionado.value = incidente
        }
    }

    fun cargarIncidenteDetallePorId(idIncidente: Int) {
        viewModelScope.launch {
            val incidente = incidenteRepository.obtenerIncidenteDetallePorId(idIncidente)
            _incidenteDetalleSeleccionado.value = incidente
        }
    }

    fun guardarIncidente(
        idIncidente: Int,
        fechaIncidente: String,
        tipoIncidente: String,
        descripcion: String,
        nivelSeveridad: String,
        accionesCorrectivas: String,
        estado: String,
        idMaterial: Int
    ) {
        val resultadoValidacion = validarDatosIncidente(
            fechaIncidente = fechaIncidente,
            tipoIncidente = tipoIncidente,
            descripcion = descripcion,
            nivelSeveridad = nivelSeveridad,
            estado = estado,
            idMaterial = idMaterial
        )

        if (resultadoValidacion != null) {
            _resultadoOperacion.value = resultadoValidacion
            return
        }

        viewModelScope.launch {
            try {
                val incidente = IncidenteEntity(
                    idIncidente = idIncidente,
                    fechaIncidente = fechaIncidente.trim(),
                    tipoIncidente = tipoIncidente,
                    descripcion = descripcion.trim(),
                    nivelSeveridad = nivelSeveridad,
                    accionesCorrectivas = accionesCorrectivas.trim(),
                    estado = estado,
                    idMaterial = idMaterial
                )

                if (idIncidente == 0) {
                    incidenteRepository.insertarIncidente(incidente)
                    _resultadoOperacion.value = ResultadoOperacion(
                        exitoso = true,
                        mensaje = "Incidente registrado correctamente"
                    )
                } else {
                    incidenteRepository.actualizarIncidente(incidente)
                    _resultadoOperacion.value = ResultadoOperacion(
                        exitoso = true,
                        mensaje = "Incidente actualizado correctamente"
                    )
                }
            } catch (error: Exception) {
                _resultadoOperacion.value = ResultadoOperacion(
                    exitoso = false,
                    mensaje = "No se pudo guardar el incidente"
                )
            }
        }
    }

    fun eliminarIncidente(idIncidente: Int) {
        viewModelScope.launch {
            try {
                incidenteRepository.eliminarIncidentePorId(idIncidente)

                _resultadoOperacion.value = ResultadoOperacion(
                    exitoso = true,
                    mensaje = "Incidente eliminado correctamente"
                )
            } catch (error: Exception) {
                _resultadoOperacion.value = ResultadoOperacion(
                    exitoso = false,
                    mensaje = "No se pudo eliminar el incidente"
                )
            }
        }
    }

    fun limpiarResultadoOperacion() {
        _resultadoOperacion.value = null
    }

    private fun validarDatosIncidente(
        fechaIncidente: String,
        tipoIncidente: String,
        descripcion: String,
        nivelSeveridad: String,
        estado: String,
        idMaterial: Int
    ): ResultadoOperacion? {
        if (idMaterial == 0) {
            return ResultadoOperacion(false, "Seleccione un material")
        }

        if (fechaIncidente.isBlank()) {
            return ResultadoOperacion(false, "Digite la fecha del incidente")
        }

        if (tipoIncidente.startsWith("Seleccione")) {
            return ResultadoOperacion(false, "Seleccione el tipo de incidente")
        }

        if (descripcion.isBlank()) {
            return ResultadoOperacion(false, "Digite la descripción del incidente")
        }

        if (nivelSeveridad.startsWith("Seleccione")) {
            return ResultadoOperacion(false, "Seleccione el nivel de severidad")
        }

        if (estado.startsWith("Seleccione")) {
            return ResultadoOperacion(false, "Seleccione el estado del incidente")
        }

        return null
    }
}