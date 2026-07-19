package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecorisk_manager.data.entity.HojaSeguridadEntity
import com.example.ecorisk_manager.data.repository.HojaSeguridadRepository
import com.example.ecorisk_manager.model.HojaSeguridadDetalle
import com.example.ecorisk_manager.model.ResultadoOperacion
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HojaSeguridadViewModel(
    private val hojaSeguridadRepository: HojaSeguridadRepository
) : ViewModel() {

    private val _hojas = MutableLiveData<List<HojaSeguridadDetalle>>(emptyList())
    val hojas: LiveData<List<HojaSeguridadDetalle>> = _hojas

    private val _hojaSeleccionada = MutableLiveData<HojaSeguridadEntity?>()
    val hojaSeleccionada: LiveData<HojaSeguridadEntity?> = _hojaSeleccionada

    private val _hojaDetalleSeleccionada = MutableLiveData<HojaSeguridadDetalle?>()
    val hojaDetalleSeleccionada: LiveData<HojaSeguridadDetalle?> = _hojaDetalleSeleccionada

    private val _resultadoOperacion = MutableLiveData<ResultadoOperacion?>()
    val resultadoOperacion: LiveData<ResultadoOperacion?> = _resultadoOperacion

    private var trabajoLista: Job? = null

    fun cargarHojas() {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            hojaSeguridadRepository.obtenerHojasDetalle().collectLatest { lista ->
                _hojas.value = lista
            }
        }
    }

    fun cargarHojasPorMaterial(idMaterial: Int) {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            hojaSeguridadRepository.obtenerHojasDetallePorMaterial(idMaterial).collectLatest { lista ->
                _hojas.value = lista
            }
        }
    }

    fun cargarHojaPorId(idHoja: Int) {
        viewModelScope.launch {
            val hoja = hojaSeguridadRepository.obtenerHojaPorId(idHoja)
            _hojaSeleccionada.value = hoja
        }
    }

    fun cargarHojaDetallePorId(idHoja: Int) {
        viewModelScope.launch {
            val hoja = hojaSeguridadRepository.obtenerHojaDetallePorId(idHoja)
            _hojaDetalleSeleccionada.value = hoja
        }
    }

    fun guardarHoja(
        idHoja: Int,
        idMaterial: Int,
        version: String,
        fechaEmision: String,
        archivoPdf: String,
        observaciones: String
    ) {
        val resultadoValidacion = validarDatosHoja(
            idMaterial = idMaterial,
            version = version,
            fechaEmision = fechaEmision,
            archivoPdf = archivoPdf
        )

        if (resultadoValidacion != null) {
            _resultadoOperacion.value = resultadoValidacion
            return
        }

        viewModelScope.launch {
            try {
                val versionLimpia = version.trim()

                val versionRepetida = if (idHoja == 0) {
                    hojaSeguridadRepository.existeVersionParaMaterial(
                        idMaterial = idMaterial,
                        version = versionLimpia
                    )
                } else {
                    hojaSeguridadRepository.existeVersionEnOtraHoja(
                        idMaterial = idMaterial,
                        version = versionLimpia,
                        idHoja = idHoja
                    )
                }

                if (versionRepetida) {
                    _resultadoOperacion.value = ResultadoOperacion(
                        exitoso = false,
                        mensaje = "Ese material ya tiene una hoja con esa versión"
                    )
                    return@launch
                }

                val hoja = HojaSeguridadEntity(
                    idHoja = idHoja,
                    version = versionLimpia,
                    fechaEmision = fechaEmision.trim(),
                    archivoPdf = archivoPdf.trim(),
                    observaciones = observaciones.trim(),
                    idMaterial = idMaterial
                )

                if (idHoja == 0) {
                    hojaSeguridadRepository.insertarHoja(hoja)
                    _resultadoOperacion.value = ResultadoOperacion(
                        exitoso = true,
                        mensaje = "Hoja de seguridad registrada correctamente"
                    )
                } else {
                    hojaSeguridadRepository.actualizarHoja(hoja)
                    _resultadoOperacion.value = ResultadoOperacion(
                        exitoso = true,
                        mensaje = "Hoja de seguridad actualizada correctamente"
                    )
                }
            } catch (error: Exception) {
                _resultadoOperacion.value = ResultadoOperacion(
                    exitoso = false,
                    mensaje = "No se pudo guardar la hoja de seguridad"
                )
            }
        }
    }

    fun eliminarHoja(idHoja: Int) {
        viewModelScope.launch {
            try {
                hojaSeguridadRepository.eliminarHojaPorId(idHoja)
                _resultadoOperacion.value = ResultadoOperacion(
                    exitoso = true,
                    mensaje = "Hoja eliminada correctamente"
                )
            } catch (error: Exception) {
                _resultadoOperacion.value = ResultadoOperacion(
                    exitoso = false,
                    mensaje = "No se pudo eliminar la hoja"
                )
            }
        }
    }

    fun limpiarResultadoOperacion() {
        _resultadoOperacion.value = null
    }

    private fun validarDatosHoja(
        idMaterial: Int,
        version: String,
        fechaEmision: String,
        archivoPdf: String
    ): ResultadoOperacion? {
        if (idMaterial == 0) {
            return ResultadoOperacion(false, "Seleccione un material")
        }

        if (version.isBlank()) {
            return ResultadoOperacion(false, "Digite la versión de la hoja")
        }

        if (fechaEmision.isBlank()) {
            return ResultadoOperacion(false, "Digite la fecha de emisión")
        }

        if (archivoPdf.isBlank()) {
            return ResultadoOperacion(false, "Digite el nombre del archivo PDF")
        }

        return null
    }
}