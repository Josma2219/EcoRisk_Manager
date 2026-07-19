package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.MaterialPeligrosoDao
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import kotlinx.coroutines.flow.Flow

class MaterialRepository(
    private val materialPeligrosoDao: MaterialPeligrosoDao
) {

    fun obtenerMateriales(): Flow<List<MaterialPeligrosoEntity>> {
        return materialPeligrosoDao.obtenerMateriales()
    }

    fun buscarMateriales(texto: String): Flow<List<MaterialPeligrosoEntity>> {
        return materialPeligrosoDao.buscarMateriales(texto)
    }

    fun obtenerMaterialesPorRiesgo(clasificacionRiesgo: String): Flow<List<MaterialPeligrosoEntity>> {
        return materialPeligrosoDao.obtenerMaterialesPorRiesgo(clasificacionRiesgo)
    }

    suspend fun obtenerMaterialPorId(idMaterial: Int): MaterialPeligrosoEntity? {
        return materialPeligrosoDao.obtenerMaterialPorId(idMaterial)
    }

    suspend fun insertarMaterial(material: MaterialPeligrosoEntity): Long {
        return materialPeligrosoDao.insertarMaterial(material)
    }

    suspend fun actualizarMaterial(material: MaterialPeligrosoEntity) {
        materialPeligrosoDao.actualizarMaterial(material)
    }

    suspend fun existeCodigoMaterial(codigoMaterial: String): Boolean {
        return materialPeligrosoDao.contarMaterialPorCodigo(codigoMaterial) > 0
    }

    suspend fun existeCodigoEnOtroRegistro(codigoMaterial: String, idMaterial: Int): Boolean {
        return materialPeligrosoDao.contarMaterialPorCodigoEnOtroRegistro(
            codigoMaterial = codigoMaterial,
            idMaterial = idMaterial
        ) > 0
    }
}