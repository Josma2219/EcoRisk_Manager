package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.HojaSeguridadDao
import com.example.ecorisk_manager.data.entity.HojaSeguridadEntity
import com.example.ecorisk_manager.model.HojaSeguridadDetalle
import kotlinx.coroutines.flow.Flow

class HojaSeguridadRepository(
    private val hojaSeguridadDao: HojaSeguridadDao
) {

    fun obtenerHojasDetalle(): Flow<List<HojaSeguridadDetalle>> {
        return hojaSeguridadDao.obtenerHojasDetalle()
    }

    fun obtenerHojasDetallePorMaterial(idMaterial: Int): Flow<List<HojaSeguridadDetalle>> {
        return hojaSeguridadDao.obtenerHojasDetallePorMaterial(idMaterial)
    }

    suspend fun obtenerHojaPorId(idHoja: Int): HojaSeguridadEntity? {
        return hojaSeguridadDao.obtenerHojaPorId(idHoja)
    }

    suspend fun obtenerHojaDetallePorId(idHoja: Int): HojaSeguridadDetalle? {
        return hojaSeguridadDao.obtenerHojaDetallePorId(idHoja)
    }

    suspend fun insertarHoja(hoja: HojaSeguridadEntity): Long {
        return hojaSeguridadDao.insertarHoja(hoja)
    }

    suspend fun actualizarHoja(hoja: HojaSeguridadEntity) {
        hojaSeguridadDao.actualizarHoja(hoja)
    }

    suspend fun existeVersionParaMaterial(idMaterial: Int, version: String): Boolean {
        return hojaSeguridadDao.contarVersionPorMaterial(
            idMaterial = idMaterial,
            version = version
        ) > 0
    }

    suspend fun existeVersionEnOtraHoja(
        idMaterial: Int,
        version: String,
        idHoja: Int
    ): Boolean {
        return hojaSeguridadDao.contarVersionPorMaterialEnOtraHoja(
            idMaterial = idMaterial,
            version = version,
            idHoja = idHoja
        ) > 0
    }

    suspend fun eliminarHojaPorId(idHoja: Int) {
        hojaSeguridadDao.eliminarHojaPorId(idHoja)
    }
}