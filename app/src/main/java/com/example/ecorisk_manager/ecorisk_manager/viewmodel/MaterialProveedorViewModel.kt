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

/**
 * ViewModel encargado de gestionar las relaciones
 * entre materiales peligrosos y proveedores.
 */
class MaterialProveedorViewModel(
    private val materialProveedorRepository: MaterialProveedorRepository
) : ViewModel() {

    private val _relaciones = MutableLiveData<List<MaterialProveedorDetalle>>(emptyList())
    val relaciones: LiveData<List<MaterialProveedorDetalle>> = _relaciones

    private val _resultadoOperacion = MutableLiveData<ResultadoOperacion?>()
    val resultadoOperacion: LiveData<ResultadoOperacion?> = _resultadoOperacion

    // Permite cancelar la consulta anterior cuando se aplica
    // un nuevo filtro sobre las relaciones.
    private var trabajoLista: Job? = null

    /**
     * Obtiene todas las relaciones registradas
     * entre materiales y proveedores.
     */
    fun cargarRelaciones() {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            materialProveedorRepository.obtenerRelacionesDetalle().collectLatest { lista ->
                _relaciones.value = lista
            }
        }
    }

    /**
     * Obtiene únicamente las relaciones
     * correspondientes a un material.
     */
    fun cargarRelacionesPorMaterial(idMaterial: Int) {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            materialProveedorRepository.obtenerRelacionesDetallePorMaterial(idMaterial).collectLatest { lista ->
                _relaciones.value = lista
            }
        }
    }

    /**
     * Obtiene únicamente las relaciones
     * correspondientes a un proveedor.
     */
    fun cargarRelacionesPorProveedor(idProveedor: Int) {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            materialProveedorRepository.obtenerRelacionesDetallePorProveedor(idProveedor).collectLatest { lista ->
                _relaciones.value = lista
            }
        }
    }

    /**
     * Valida la información ingresada y registra
     * una nueva relación entre material y proveedor.
     */
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

                // Evita registrar la misma relación más de una vez.
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

    /**
     * Elimina una relación registrada entre
     * un material y un proveedor.
     */
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

    /**
     * Restablece el resultado de la última operación
     * para evitar que vuelva a procesarse.
     */
    fun limpiarResultadoOperacion() {
        _resultadoOperacion.value = null
    }

    /**
     * Comprueba que la información necesaria para crear
     * una relación sea válida antes de guardarla.
     */
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