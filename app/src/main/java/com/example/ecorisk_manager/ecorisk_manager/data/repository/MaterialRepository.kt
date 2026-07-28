package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.MaterialPeligrosoDao
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de gestionar las operaciones
 * relacionadas con los materiales peligrosos.
 */
class MaterialRepository(
    private val materialPeligrosoDao: MaterialPeligrosoDao
) {

    /**
     * Obtiene todos los materiales registrados.
     */
    fun obtenerMateriales(): Flow<List<MaterialPeligrosoEntity>> {
        return materialPeligrosoDao.obtenerMateriales()
    }

    /**
     * Busca materiales por nombre o código.
     */
    fun buscarMateriales(texto: String): Flow<List<MaterialPeligrosoEntity>> {
        return materialPeligrosoDao.buscarMateriales(texto)
    }

    /**
     * Obtiene los materiales filtrados por clasificación de riesgo.
     */
    fun obtenerMaterialesPorRiesgo(clasificacionRiesgo: String): Flow<List<MaterialPeligrosoEntity>> {
        return materialPeligrosoDao.obtenerMaterialesPorRiesgo(clasificacionRiesgo)
    }

    /**
     * Busca un material utilizando su identificador.
     */
    suspend fun obtenerMaterialPorId(idMaterial: Int): MaterialPeligrosoEntity? {
        return materialPeligrosoDao.obtenerMaterialPorId(idMaterial)
    }

    /**
     * Registra un nuevo material peligroso.
     */
    suspend fun insertarMaterial(material: MaterialPeligrosoEntity): Long {
        return materialPeligrosoDao.insertarMaterial(material)
    }

    /**
     * Actualiza la información de un material existente.
     */
    suspend fun actualizarMaterial(material: MaterialPeligrosoEntity) {
        materialPeligrosoDao.actualizarMaterial(material)
    }

    /**
     * Verifica si ya existe un material con el mismo código.
     */
    suspend fun existeCodigoMaterial(codigoMaterial: String): Boolean {
        return materialPeligrosoDao.contarMaterialPorCodigo(codigoMaterial) > 0
    }

    /**
     * Verifica si otro material ya utiliza el mismo código.
     */
    suspend fun existeCodigoEnOtroRegistro(codigoMaterial: String, idMaterial: Int): Boolean {
        return materialPeligrosoDao.contarMaterialPorCodigoEnOtroRegistro(
            codigoMaterial = codigoMaterial,
            idMaterial = idMaterial
        ) > 0
    }
}