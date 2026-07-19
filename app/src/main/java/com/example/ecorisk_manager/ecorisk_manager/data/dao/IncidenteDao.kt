package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.IncidenteEntity
import com.example.ecorisk_manager.model.IncidenteDetalle
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy

@Dao
interface IncidenteDao {

    @Insert
    suspend fun insertarIncidente(incidente: IncidenteEntity): Long

    @Update
    suspend fun actualizarIncidente(incidente: IncidenteEntity)

    @Delete
    suspend fun eliminarIncidente(incidente: IncidenteEntity)

    @Query("SELECT * FROM incidentes ORDER BY fecha_incidente DESC")
    fun obtenerIncidentes(): Flow<List<IncidenteEntity>>

    @Query("SELECT * FROM incidentes WHERE id_incidente = :idIncidente LIMIT 1")
    suspend fun obtenerIncidentePorId(idIncidente: Int): IncidenteEntity?

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
    fun obtenerIncidentesDetalle(): Flow<List<IncidenteDetalle>>

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
        WHERE i.id_material = :idMaterial
        ORDER BY i.fecha_incidente DESC
    """)
    fun obtenerIncidentesDetallePorMaterial(idMaterial: Int): Flow<List<IncidenteDetalle>>

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
    fun obtenerIncidentesDetallePorEstado(estado: String): Flow<List<IncidenteDetalle>>

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
    fun obtenerIncidentesDetallePorSeveridad(nivelSeveridad: String): Flow<List<IncidenteDetalle>>

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
        WHERE i.id_incidente = :idIncidente
        LIMIT 1
    """)
    suspend fun obtenerIncidenteDetallePorId(idIncidente: Int): IncidenteDetalle?

    @Query("SELECT COUNT(*) FROM incidentes WHERE estado = 'Abierto'")
    suspend fun contarIncidentesAbiertos(): Int

    @Query("SELECT COUNT(*) FROM incidentes WHERE estado = 'Cerrado'")
    suspend fun contarIncidentesCerrados(): Int

    @Query("DELETE FROM incidentes WHERE id_incidente = :idIncidente")
    suspend fun eliminarIncidentePorId(idIncidente: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarIncidentes(incidentes: List<IncidenteEntity>)

    @Query("SELECT * FROM incidentes ORDER BY id_incidente ASC")
    suspend fun obtenerIncidentesParaRespaldo(): List<IncidenteEntity>

    @Query("DELETE FROM incidentes")
    suspend fun eliminarTodosIncidentes()
}