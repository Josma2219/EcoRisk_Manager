package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecorisk_manager.data.entity.MaterialProveedorEntity
import com.example.ecorisk_manager.data.repository.MaterialProveedorRepository
import com.example.ecorisk_manager.model.MaterialProveedorDetalle
import com.example.ecorisk_manager.model.ResultadoOperacion
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MaterialProveedorViewModel(
    private val materialProveedorRepository: MaterialProveedorRepository
) : ViewModel() {

    private val _relaciones = MutableLiveData<List<MaterialProveedorDetalle>>(emptyList())
    val relaciones: LiveData<List<MaterialProveedorDetalle>> = _relaciones

    private val _resultadoOperacion = MutableLiveData<ResultadoOperacion?>()
    val resultadoOperacion: LiveData<ResultadoOperacion?> = _resultadoOperacion

    private var trabajoLista: Job? = null

    fun cargarRelaciones() {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            materialProveedorRepository.obtenerRelacionesDetalle().collectLatest { lista ->
                _relaciones.value = lista
            }
        }
    }

    fun cargarRelacionesPorMaterial(idMaterial: Int) {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            materialProveedorRepository.obtenerRelacionesDetallePorMaterial(idMaterial).collectLatest { lista ->
                _relaciones.value = lista
            }
        }
    }

    fun cargarRelacionesPorProveedor(idProveedor: Int) {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            materialProveedorRepository.obtenerRelacionesDetallePorProveedor(idProveedor).collectLatest { lista ->
                _relaciones.value = lista
            }
        }
    }

    fun guardarRelacion(
        idMaterial: Int,
        idProveedor: Int,
        precioReferenciaTexto: String
    ) {
        val resultadoValidacion = validarDatosRelacion(
            idMaterial = idMaterial,
            idProveedor = idProveedor,
            precioReferenciaTexto = precioReferenciaTexto
        )

        if (resultadoValidacion != null) {
            _resultadoOperacion.value = resultadoValidacion
            return
        }

        viewModelScope.launch {
            try {
                val relacionExiste = materialProveedorRepository.existeRelacion(
                    idMaterial = idMaterial,
                    idProveedor = idProveedor
                )

                if (relacionExiste) {
                    _resultadoOperacion.value = ResultadoOperacion(
                        exitoso = false,
                        mensaje = "Ese proveedor ya está asociado a este material"
                    )
                    return@launch
                }

                val relacion = MaterialProveedorEntity(
                    idMaterial = idMaterial,
                    idProveedor = idProveedor,
                    precioReferencia = precioReferenciaTexto.trim().toDouble()
                )

                materialProveedorRepository.insertarRelacion(relacion)

                _resultadoOperacion.value = ResultadoOperacion(
                    exitoso = true,
                    mensaje = "Relación registrada correctamente"
                )
            } catch (error: Exception) {
                _resultadoOperacion.value = ResultadoOperacion(
                    exitoso = false,
                    mensaje = "No se pudo registrar la relación"
                )
            }
        }
    }

    fun eliminarRelacion(idRelacion: Int) {
        viewModelScope.launch {
            try {
                materialProveedorRepository.eliminarRelacionPorId(idRelacion)

                _resultadoOperacion.value = ResultadoOperacion(
                    exitoso = true,
                    mensaje = "Relación eliminada correctamente"
                )
            } catch (error: Exception) {
                _resultadoOperacion.value = ResultadoOperacion(
                    exitoso = false,
                    mensaje = "No se pudo eliminar la relación"
                )
            }
        }
    }

    fun limpiarResultadoOperacion() {
        _resultadoOperacion.value = null
    }

    private fun validarDatosRelacion(
        idMaterial: Int,
        idProveedor: Int,
        precioReferenciaTexto: String
    ): ResultadoOperacion? {
        if (idMaterial == 0) {
            return ResultadoOperacion(false, "Seleccione un material")
        }

        if (idProveedor == 0) {
            return ResultadoOperacion(false, "Seleccione un proveedor")
        }

        if (precioReferenciaTexto.isBlank()) {
            return ResultadoOperacion(false, "Digite el precio de referencia")
        }

        val precio = precioReferenciaTexto.toDoubleOrNull()

        if (precio == null) {
            return ResultadoOperacion(false, "Digite un precio válido")
        }

        if (precio <= 0) {
            return ResultadoOperacion(false, "El precio debe ser mayor a cero")
        }

        return null
    }
}