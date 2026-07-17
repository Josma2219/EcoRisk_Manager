package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.ecorisk_manager.data.entity.IncidenteEntity
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDao {

    @Query("""
        SELECT * FROM materiales_peligrosos
        WHERE clasificacion_riesgo = :clasificacionRiesgo
        ORDER BY nombre_comercial ASC
    """)
    fun reporteMaterialesPorRiesgo(clasificacionRiesgo: String): Flow<List<MaterialPeligrosoEntity>>

    @Query("""
        SELECT * FROM incidentes
        ORDER BY fecha_incidente DESC
    """)
    fun reporteHistorialIncidentes(): Flow<List<IncidenteEntity>>

    @Query("""
        SELECT * FROM incidentes
        WHERE id_material = :idMaterial
        ORDER BY fecha_incidente DESC
    """)
    fun reporteIncidentesPorMaterial(idMaterial: Int): Flow<List<IncidenteEntity>>

    @Query("""
        SELECT * FROM incidentes
        WHERE estado = :estado
        ORDER BY fecha_incidente DESC
    """)
    fun reporteIncidentesPorEstado(estado: String): Flow<List<IncidenteEntity>>

    @Query("""
        SELECT * FROM incidentes
        WHERE nivel_severidad = :nivelSeveridad
        ORDER BY fecha_incidente DESC
    """)
    fun reporteIncidentesPorSeveridad(nivelSeveridad: String): Flow<List<IncidenteEntity>>
}