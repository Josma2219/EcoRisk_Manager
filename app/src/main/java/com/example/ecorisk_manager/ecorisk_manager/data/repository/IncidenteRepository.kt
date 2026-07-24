package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.IncidenteDao
import com.example.ecorisk_manager.data.entity.IncidenteEntity
import com.example.ecorisk_manager.model.IncidenteDetalle
import kotlinx.coroutines.flow.Flow

class IncidenteRepository(
    private val incidenteDao: IncidenteDao
) {

    fun obtenerIncidentesDetalle(): Flow<List<IncidenteDetalle>> {
        return incidenteDao.obtenerIncidentesDetalle()
    }

    fun obtenerIncidentesDetallePorMaterial(idMaterial: Int): Flow<List<IncidenteDetalle>> {
        return incidenteDao.obtenerIncidentesDetallePorMaterial(idMaterial)
    }

    fun obtenerIncidentesDetallePorEstado(estado: String): Flow<List<IncidenteDetalle>> {
        return incidenteDao.obtenerIncidentesDetallePorEstado(estado)
    }

    fun obtenerIncidentesDetallePorSeveridad(nivelSeveridad: String): Flow<List<IncidenteDetalle>> {
        return incidenteDao.obtenerIncidentesDetallePorSeveridad(nivelSeveridad)
    }

    suspend fun obtenerIncidentePorId(idIncidente: Int): IncidenteEntity? {
        return incidenteDao.obtenerIncidentePorId(idIncidente)
    }

    suspend fun obtenerIncidenteDetallePorId(idIncidente: Int): IncidenteDetalle? {
        return incidenteDao.obtenerIncidenteDetallePorId(idIncidente)
    }

    suspend fun insertarIncidente(incidente: IncidenteEntity): Long {
        return incidenteDao.insertarIncidente(incidente)
    }

    suspend fun actualizarIncidente(incidente: IncidenteEntity) {
        incidenteDao.actualizarIncidente(incidente)
    }

    suspend fun eliminarIncidentePorId(idIncidente: Int) {
        incidenteDao.eliminarIncidentePorId(idIncidente)
    }
}