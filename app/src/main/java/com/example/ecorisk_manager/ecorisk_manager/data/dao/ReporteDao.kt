package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.model.IncidenteDetalle
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDao {

    @Query("""
        SELECT * FROM materiales_peligrosos
        ORDER BY clasificacion_riesgo ASC, nombre_comercial ASC
    """)
    fun reporteTodosLosMateriales(): Flow<List<MaterialPeligrosoEntity>>

    @Query("""
        SELECT * FROM materiales_peligrosos
        WHERE clasificacion_riesgo = :clasificacionRiesgo
        ORDER BY nombre_comercial ASC
    """)
    fun reporteMaterialesPorRiesgo(
        clasificacionRiesgo: String
    ): Flow<List<MaterialPeligrosoEntity>>

    @Query("""
        SELECT 
            i.id_incidente AS id_incidente,
            i.fecha_incidente AS fecha_incidente,
            i.tipo_incidente AS tipo_incidente,
            i.descripcion AS descripcion,
            i.nivel_severidad AS nivel_severidad,
            i.acciones_correctivas AS acciones_correctivas,
            i.estado AS estado,
            i.id_material AS id_material,
            m.codigo_material AS codigo_material,
            m.nombre_comercial AS nombre_material,
            m.clasificacion_riesgo AS clasificacion_riesgo
        FROM incidentes i
        INNER JOIN materiales_peligrosos m ON i.id_material = m.id_material
        ORDER BY i.fecha_incidente DESC
    """)
    fun reporteHistorialIncidentes(): Flow<List<IncidenteDetalle>>

    @Query("""
        SELECT 
            i.id_incidente AS id_incidente,
            i.fecha_incidente AS fecha_incidente,
            i.tipo_incidente AS tipo_incidente,
            i.descripcion AS descripcion,
            i.nivel_severidad AS nivel_severidad,
            i.acciones_correctivas AS acciones_correctivas,
            i.estado AS estado,
            i.id_material AS id_material,
            m.codigo_material AS codigo_material,
            m.nombre_comercial AS nombre_material,
            m.clasificacion_riesgo AS clasificacion_riesgo
        FROM incidentes i
        INNER JOIN materiales_peligrosos m ON i.id_material = m.id_material
        WHERE i.estado = :estado
        ORDER BY i.fecha_incidente DESC
    """)
    fun reporteIncidentesPorEstado(
        estado: String
    ): Flow<List<IncidenteDetalle>>

    @Query("""
        SELECT 
            i.id_incidente AS id_incidente,
            i.fecha_incidente AS fecha_incidente,
            i.tipo_incidente AS tipo_incidente,
            i.descripcion AS descripcion,
            i.nivel_severidad AS nivel_severidad,
            i.acciones_correctivas AS acciones_correctivas,
            i.estado AS estado,
            i.id_material AS id_material,
            m.codigo_material AS codigo_material,
            m.nombre_comercial AS nombre_material,
            m.clasificacion_riesgo AS clasificacion_riesgo
        FROM incidentes i
        INNER JOIN materiales_peligrosos m ON i.id_material = m.id_material
        WHERE i.nivel_severidad = :nivelSeveridad
        ORDER BY i.fecha_incidente DESC
    """)
    fun reporteIncidentesPorSeveridad(
        nivelSeveridad: String
    ): Flow<List<IncidenteDetalle>>
}