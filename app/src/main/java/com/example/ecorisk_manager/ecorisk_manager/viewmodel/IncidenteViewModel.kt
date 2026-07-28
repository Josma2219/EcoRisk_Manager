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

/**
 * ViewModel encargado de gestionar el registro, consulta,
 * actualización y eliminación de incidentes.
 */
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

    // Permite cancelar la consulta anterior cuando se aplica
    // un nuevo filtro sobre la lista de incidentes.
    private var trabajoLista: Job? = null

    /**
     * Obtiene el listado completo de incidentes.
     */
    fun cargarIncidentes() {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            incidenteRepository.obtenerIncidentesDetalle().collectLatest { lista ->
                _incidentes.value = lista
            }
        }
    }

    /**
     * Obtiene únicamente los incidentes asociados
     * al material seleccionado.
     */
    fun cargarIncidentesPorMaterial(idMaterial: Int) {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            incidenteRepository.obtenerIncidentesDetallePorMaterial(idMaterial).collectLatest { lista ->
                _incidentes.value = lista
            }
        }
    }

    /**
     * Filtra los incidentes según su estado.
     */
    fun cargarIncidentesPorEstado(estado: String) {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            incidenteRepository.obtenerIncidentesDetallePorEstado(estado).collectLatest { lista ->
                _incidentes.value = lista
            }
        }
    }

    /**
     * Filtra los incidentes según su nivel de severidad.
     */
    fun cargarIncidentesPorSeveridad(nivelSeveridad: String) {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            incidenteRepository.obtenerIncidentesDetallePorSeveridad(nivelSeveridad).collectLatest { lista ->
                _incidentes.value = lista
            }
        }
    }

    /**
     * Recupera la información de un incidente para editarlo.
     */
    fun cargarIncidentePorId(idIncidente: Int) {
        viewModelScope.launch {
            val incidente = incidenteRepository.obtenerIncidentePorId(idIncidente)
            _incidenteSeleccionado.value = incidente
        }
    }

    /**
     * Recupera el detalle completo de un incidente
     * para mostrarlo en pantalla.
     */
    fun cargarIncidenteDetallePorId(idIncidente: Int) {
        viewModelScope.launch {
            val incidente = incidenteRepository.obtenerIncidenteDetallePorId(idIncidente)
            _incidenteDetalleSeleccionado.value = incidente
        }
    }

    /**
     * Valida la información ingresada y registra o actualiza
     * el incidente según corresponda.
     */
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

    /**
     * Elimina un incidente registrado.
     */
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

    /**
     * Restablece el resultado de la última operación para evitar
     * que vuelva a procesarse después de un cambio de configuración.
     */
    fun limpiarResultadoOperacion() {
        _resultadoOperacion.value = null
    }

    /**
     * Comprueba que todos los datos obligatorios del incidente
     * hayan sido ingresados antes de guardarlo.
     */
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