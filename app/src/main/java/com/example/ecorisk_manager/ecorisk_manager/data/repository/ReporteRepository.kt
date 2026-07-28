package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.ReporteDao
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.model.IncidenteDetalle
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de obtener la información
 * utilizada para generar los reportes de la aplicación.
 */
class ReporteRepository(
    private val reporteDao: ReporteDao
) {

    /**
     * Obtiene el reporte con todos los materiales registrados.
     */
    fun obtenerTodosLosMateriales(): Flow<List<MaterialPeligrosoEntity>> {
        return reporteDao.reporteTodosLosMateriales()
    }

    /**
     * Obtiene un reporte de materiales filtrados por clasificación de riesgo.
     */
    fun obtenerMaterialesPorRiesgo(
        clasificacionRiesgo: String
    ): Flow<List<MaterialPeligrosoEntity>> {
        return reporteDao.reporteMaterialesPorRiesgo(clasificacionRiesgo)
    }

    /**
     * Obtiene el historial completo de incidentes.
     */
    fun obtenerHistorialIncidentes(): Flow<List<IncidenteDetalle>> {
        return reporteDao.reporteHistorialIncidentes()
    }

    /**
     * Obtiene un reporte de incidentes filtrados por estado.
     */
    fun obtenerIncidentesPorEstado(
        estado: String
    ): Flow<List<IncidenteDetalle>> {
        return reporteDao.reporteIncidentesPorEstado(estado)
    }

    /**
     * Obtiene un reporte de incidentes filtrados por nivel de severidad.
     */
    fun obtenerIncidentesPorSeveridad(
        nivelSeveridad: String
    ): Flow<List<IncidenteDetalle>> {
        return reporteDao.reporteIncidentesPorSeveridad(nivelSeveridad)
    }
}