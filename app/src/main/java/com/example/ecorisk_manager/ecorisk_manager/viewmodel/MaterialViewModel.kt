package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.data.repository.MaterialRepository
import com.example.ecorisk_manager.model.ResultadoOperacion
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar el registro, consulta,
 * búsqueda y actualización de materiales peligrosos.
 */
class MaterialViewModel(
    private val materialRepository: MaterialRepository
) : ViewModel() {

    private val _materiales = MutableLiveData<List<MaterialPeligrosoEntity>>(emptyList())
    val materiales: LiveData<List<MaterialPeligrosoEntity>> = _materiales

    private val _materialSeleccionado = MutableLiveData<MaterialPeligrosoEntity?>()
    val materialSeleccionado: LiveData<MaterialPeligrosoEntity?> = _materialSeleccionado

    private val _resultadoOperacion = MutableLiveData<ResultadoOperacion?>()
    val resultadoOperacion: LiveData<ResultadoOperacion?> = _resultadoOperacion

    // Permite cancelar la consulta anterior cuando se realiza una
    // nueva búsqueda o filtrado, evitando observadores duplicados.
    private var trabajoLista: Job? = null

    /**
     * Obtiene el listado completo de materiales registrados.
     */
    fun cargarMateriales() {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            materialRepository.obtenerMateriales().collectLatest { lista ->
                _materiales.value = lista
            }
        }
    }

    /**
     * Busca materiales utilizando el código o el nombre comercial.
     */
    fun buscarMateriales(texto: String) {
        if (texto.isBlank()) {
            cargarMateriales()
            return
        }

        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            materialRepository.buscarMateriales(texto.trim()).collectLatest { lista ->
                _materiales.value = lista
            }
        }
    }

    /**
     * Filtra los materiales según su clasificación de riesgo.
     */
    fun filtrarPorRiesgo(clasificacionRiesgo: String) {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            materialRepository.obtenerMaterialesPorRiesgo(clasificacionRiesgo).collectLatest { lista ->
                _materiales.value = lista
            }
        }
    }

    /**
     * Recupera la información de un material para visualizarla
     * o editarla posteriormente.
     */
    fun cargarMaterialPorId(idMaterial: Int) {
        viewModelScope.launch {
            val material = materialRepository.obtenerMaterialPorId(idMaterial)
            _materialSeleccionado.value = material
        }
    }

    /**
     * Valida la información ingresada y registra o actualiza
     * el material según corresponda.
     */
    fun guardarMaterial(
        idMaterial: Int,
        codigoMaterial: String,
        nombreComercial: String,
        descripcion: String,
        clasificacionRiesgo: String,
        unidadMedida: String,
        fechaRegistro: String,
        estado: String
    ) {
        val resultadoValidacion = validarDatosMaterial(
            codigoMaterial = codigoMaterial,
            nombreComercial = nombreComercial,
            descripcion = descripcion,
            clasificacionRiesgo = clasificacionRiesgo,
            unidadMedida = unidadMedida,
            fechaRegistro = fechaRegistro,
            estado = estado
        )

        if (resultadoValidacion != null) {
            _resultadoOperacion.value = resultadoValidacion
            return
        }

        viewModelScope.launch {
            try {
                val codigoLimpio = codigoMaterial.trim()

                // Verificar que el código del material sea único
                // antes de registrar o actualizar el registro.
                val codigoRepetido = if (idMaterial == 0) {
                    materialRepository.existeCodigoMaterial(codigoLimpio)
                } else {
                    materialRepository.existeCodigoEnOtroRegistro(
                        codigoMaterial = codigoLimpio,
                        idMaterial = idMaterial
                    )
                }

                if (codigoRepetido) {
                    _resultadoOperacion.value = ResultadoOperacion(
                        exitoso = false,
                        mensaje = "Ya existe un material con ese código"
                    )
                    return@launch
                }

                val material = MaterialPeligrosoEntity(
                    idMaterial = idMaterial,
                    codigoMaterial = codigoLimpio,
                    nombreComercial = nombreComercial.trim(),
                    descripcion = descripcion.trim(),
                    clasificacionRiesgo = clasificacionRiesgo,
                    unidadMedida = unidadMedida,
                    fechaRegistro = fechaRegistro.trim(),
                    estado = estado
                )

                if (idMaterial == 0) {
                    materialRepository.insertarMaterial(material)
                    _resultadoOperacion.value = ResultadoOperacion(
                        exitoso = true,
                        mensaje = "Material registrado correctamente"
                    )
                } else {
                    materialRepository.actualizarMaterial(material)
                    _resultadoOperacion.value = ResultadoOperacion(
                        exitoso = true,
                        mensaje = "Material actualizado correctamente"
                    )
                }
            } catch (error: Exception) {
                _resultadoOperacion.value = ResultadoOperacion(
                    exitoso = false,
                    mensaje = "No se pudo guardar el material"
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
     * Comprueba que todos los datos requeridos del material
     * hayan sido ingresados correctamente antes de guardarlo.
     */
    private fun validarDatosMaterial(
        codigoMaterial: String,
        nombreComercial: String,
        descripcion: String,
        clasificacionRiesgo: String,
        unidadMedida: String,
        fechaRegistro: String,
        estado: String
    ): ResultadoOperacion? {
        if (codigoMaterial.isBlank()) {
            return ResultadoOperacion(false, "Digite el código del material")
        }

        if (nombreComercial.isBlank()) {
            return ResultadoOperacion(false, "Digite el nombre comercial")
        }

        if (descripcion.isBlank()) {
            return ResultadoOperacion(false, "Digite la descripción del material")
        }

        if (clasificacionRiesgo.startsWith("Seleccione")) {
            return ResultadoOperacion(false, "Seleccione una clasificación de riesgo")
        }

        if (unidadMedida.startsWith("Seleccione")) {
            return ResultadoOperacion(false, "Seleccione una unidad de medida")
        }

        if (fechaRegistro.isBlank()) {
            return ResultadoOperacion(false, "Digite la fecha de registro")
        }

        if (estado.isBlank()) {
            return ResultadoOperacion(false, "Seleccione el estado")
        }

        return null
    }
}