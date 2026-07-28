package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.IncidenteDao
import com.example.ecorisk_manager.data.entity.IncidenteEntity
import com.example.ecorisk_manager.model.IncidenteDetalle
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de gestionar las operaciones relacionadas
 * con los incidentes.
 */
class IncidenteRepository(
    private val incidenteDao: IncidenteDao
) {

    /**
     * Obtiene el detalle de todos los incidentes registrados.
     */
    fun obtenerIncidentesDetalle(): Flow<List<IncidenteDetalle>> {
        return incidenteDao.obtenerIncidentesDetalle()
    }

    /**
     * Obtiene los incidentes asociados a un material.
     */
    fun obtenerIncidentesDetallePorMaterial(idMaterial: Int): Flow<List<IncidenteDetalle>> {
        return incidenteDao.obtenerIncidentesDetallePorMaterial(idMaterial)
    }

    /**
     * Obtiene los incidentes filtrados por estado.
     */
    fun obtenerIncidentesDetallePorEstado(estado: String): Flow<List<IncidenteDetalle>> {
        return incidenteDao.obtenerIncidentesDetallePorEstado(estado)
    }

    /**
     * Obtiene los incidentes filtrados por nivel de severidad.
     */
    fun obtenerIncidentesDetallePorSeveridad(nivelSeveridad: String): Flow<List<IncidenteDetalle>> {
        return incidenteDao.obtenerIncidentesDetallePorSeveridad(nivelSeveridad)
    }

    /**
     * Busca un incidente utilizando su identificador.
     */
    suspend fun obtenerIncidentePorId(idIncidente: Int): IncidenteEntity? {
        return incidenteDao.obtenerIncidentePorId(idIncidente)
    }

    /**
     * Obtiene el detalle completo de un incidente.
     */
    suspend fun obtenerIncidenteDetallePorId(idIncidente: Int): IncidenteDetalle? {
        return incidenteDao.obtenerIncidenteDetallePorId(idIncidente)
    }

    /**
     * Registra un nuevo incidente.
     */
    suspend fun insertarIncidente(incidente: IncidenteEntity): Long {
        return incidenteDao.insertarIncidente(incidente)
    }

    /**
     * Actualiza la información de un incidente.
     */
    suspend fun actualizarIncidente(incidente: IncidenteEntity) {
        incidenteDao.actualizarIncidente(incidente)
    }

    /**
     * Elimina un incidente utilizando su identificador.
     */
    suspend fun eliminarIncidentePorId(idIncidente: Int) {
        incidenteDao.eliminarIncidentePorId(idIncidente)
    }
}