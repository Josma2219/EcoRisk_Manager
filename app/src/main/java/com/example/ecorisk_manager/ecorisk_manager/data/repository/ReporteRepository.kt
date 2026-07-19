package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.ReporteDao
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.model.IncidenteDetalle
import kotlinx.coroutines.flow.Flow

class ReporteRepository(
    private val reporteDao: ReporteDao
) {

    fun obtenerTodosLosMateriales(): Flow<List<MaterialPeligrosoEntity>> {
        return reporteDao.reporteTodosLosMateriales()
    }

    fun obtenerMaterialesPorRiesgo(
        clasificacionRiesgo: String
    ): Flow<List<MaterialPeligrosoEntity>> {
        return reporteDao.reporteMaterialesPorRiesgo(clasificacionRiesgo)
    }

    fun obtenerHistorialIncidentes(): Flow<List<IncidenteDetalle>> {
        return reporteDao.reporteHistorialIncidentes()
    }

    fun obtenerIncidentesPorEstado(
        estado: String
    ): Flow<List<IncidenteDetalle>> {
        return reporteDao.reporteIncidentesPorEstado(estado)
    }

    fun obtenerIncidentesPorSeveridad(
        nivelSeveridad: String
    ): Flow<List<IncidenteDetalle>> {
        return reporteDao.reporteIncidentesPorSeveridad(nivelSeveridad)
    }
}