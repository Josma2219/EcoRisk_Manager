package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import com.example.ecorisk_manager.data.repository.ProveedorRepository
import com.example.ecorisk_manager.model.ResultadoOperacion
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProveedorViewModel(
    private val proveedorRepository: ProveedorRepository
) : ViewModel() {

    private val _proveedores = MutableLiveData<List<ProveedorEntity>>(emptyList())
    val proveedores: LiveData<List<ProveedorEntity>> = _proveedores

    private val _proveedorSeleccionado = MutableLiveData<ProveedorEntity?>()
    val proveedorSeleccionado: LiveData<ProveedorEntity?> = _proveedorSeleccionado

    private val _resultadoOperacion = MutableLiveData<ResultadoOperacion?>()
    val resultadoOperacion: LiveData<ResultadoOperacion?> = _resultadoOperacion

    private var trabajoLista: Job? = null

    fun cargarProveedores() {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            proveedorRepository.obtenerProveedores().collectLatest { lista ->
                _proveedores.value = lista
            }
        }
    }

    fun buscarProveedores(texto: String) {
        if (texto.isBlank()) {
            cargarProveedores()
            return
        }

        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            proveedorRepository.buscarProveedores(texto.trim()).collectLatest { lista ->
                _proveedores.value = lista
            }
        }
    }

    fun cargarProveedorPorId(idProveedor: Int) {
        viewModelScope.launch {
            val proveedor = proveedorRepository.obtenerProveedorPorId(idProveedor)
            _proveedorSeleccionado.value = proveedor
        }
    }

    fun guardarProveedor(
        idProveedor: Int,
        nombre: String,
        telefono: String,
        correo: String,
        direccion: String,
        contactoPrincipal: String
    ) {
        val resultadoValidacion = validarDatosProveedor(
            nombre = nombre,
            telefono = telefono,
            correo = correo,
            direccion = direccion,
            contactoPrincipal = contactoPrincipal
        )

        if (resultadoValidacion != null) {
            _resultadoOperacion.value = resultadoValidacion
            return
        }

        viewModelScope.launch {
            try {
                val correoLimpio = correo.trim().lowercase()

                val correoRepetido = if (idProveedor == 0) {
                    proveedorRepository.existeCorreoProveedor(correoLimpio)
                } else {
                    proveedorRepository.existeCorreoEnOtroRegistro(
                        correo = correoLimpio,
                        idProveedor = idProveedor
                    )
                }

                if (correoRepetido) {
                    _resultadoOperacion.value = ResultadoOperacion(
                        exitoso = false,
                        mensaje = "Ya existe un proveedor con ese correo"
                    )
                    return@launch
                }

                val proveedor = ProveedorEntity(
                    idProveedor = idProveedor,
                    nombre = nombre.trim(),
                    telefono = telefono.trim(),
                    correo = correoLimpio,
                    direccion = direccion.trim(),
                    contactoPrincipal = contactoPrincipal.trim()
                )

                if (idProveedor == 0) {
                    proveedorRepository.insertarProveedor(proveedor)
                    _resultadoOperacion.value = ResultadoOperacion(
                        exitoso = true,
                        mensaje = "Proveedor registrado correctamente"
                    )
                } else {
                    proveedorRepository.actualizarProveedor(proveedor)
                    _resultadoOperacion.value = ResultadoOperacion(
                        exitoso = true,
                        mensaje = "Proveedor actualizado correctamente"
                    )
                }
            } catch (error: Exception) {
                _resultadoOperacion.value = ResultadoOperacion(
                    exitoso = false,
                    mensaje = "No se pudo guardar el proveedor"
                )
            }
        }
    }

    fun limpiarResultadoOperacion() {
        _resultadoOperacion.value = null
    }

    private fun validarDatosProveedor(
        nombre: String,
        telefono: String,
        correo: String,
        direccion: String,
        contactoPrincipal: String
    ): ResultadoOperacion? {
        if (nombre.isBlank()) {
            return ResultadoOperacion(false, "Digite el nombre del proveedor")
        }

        if (telefono.isBlank()) {
            return ResultadoOperacion(false, "Digite el teléfono del proveedor")
        }

        if (correo.isBlank()) {
            return ResultadoOperacion(false, "Digite el correo del proveedor")
        }

        if (!correo.contains("@") || !correo.contains(".")) {
            return ResultadoOperacion(false, "Digite un correo válido")
        }

        if (direccion.isBlank()) {
            return ResultadoOperacion(false, "Digite la dirección del proveedor")
        }

        if (contactoPrincipal.isBlank()) {
            return ResultadoOperacion(false, "Digite el contacto principal")
        }

        return null
    }
}