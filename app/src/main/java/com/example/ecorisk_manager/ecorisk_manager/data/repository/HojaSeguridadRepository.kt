package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.HojaSeguridadDao
import com.example.ecorisk_manager.data.entity.HojaSeguridadEntity
import com.example.ecorisk_manager.model.HojaSeguridadDetalle
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de gestionar las operaciones relacionadas
 * con las hojas de seguridad.
 */
class HojaSeguridadRepository(
    private val hojaSeguridadDao: HojaSeguridadDao
) {

    /**
     * Obtiene el detalle de todas las hojas de seguridad registradas.
     */
    fun obtenerHojasDetalle(): Flow<List<HojaSeguridadDetalle>> {
        return hojaSeguridadDao.obtenerHojasDetalle()
    }

    /**
     * Obtiene las hojas de seguridad asociadas a un material.
     */
    fun obtenerHojasDetallePorMaterial(idMaterial: Int): Flow<List<HojaSeguridadDetalle>> {
        return hojaSeguridadDao.obtenerHojasDetallePorMaterial(idMaterial)
    }

    /**
     * Busca una hoja de seguridad utilizando su identificador.
     */
    suspend fun obtenerHojaPorId(idHoja: Int): HojaSeguridadEntity? {
        return hojaSeguridadDao.obtenerHojaPorId(idHoja)
    }

    /**
     * Obtiene el detalle completo de una hoja de seguridad.
     */
    suspend fun obtenerHojaDetallePorId(idHoja: Int): HojaSeguridadDetalle? {
        return hojaSeguridadDao.obtenerHojaDetallePorId(idHoja)
    }

    /**
     * Registra una nueva hoja de seguridad.
     */
    suspend fun insertarHoja(hoja: HojaSeguridadEntity): Long {
        return hojaSeguridadDao.insertarHoja(hoja)
    }

    /**
     * Actualiza la información de una hoja de seguridad.
     */
    suspend fun actualizarHoja(hoja: HojaSeguridadEntity) {
        hojaSeguridadDao.actualizarHoja(hoja)
    }

    /**
     * Verifica si un material ya tiene registrada la versión indicada.
     */
    suspend fun existeVersionParaMaterial(idMaterial: Int, version: String): Boolean {
        return hojaSeguridadDao.contarVersionPorMaterial(
            idMaterial = idMaterial,
            version = version
        ) > 0
    }

    /**
     * Verifica si otra hoja del mismo material ya utiliza la versión indicada.
     */
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

    /**
     * Elimina una hoja de seguridad utilizando su identificador.
     */
    suspend fun eliminarHojaPorId(idHoja: Int) {
        hojaSeguridadDao.eliminarHojaPorId(idHoja)
    }
}