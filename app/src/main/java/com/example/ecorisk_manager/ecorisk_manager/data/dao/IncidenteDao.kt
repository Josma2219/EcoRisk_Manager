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

/**
 * DAO encargado de gestionar las operaciones relacionadas
 * con los incidentes en la base de datos.
 */
@Dao
interface IncidenteDao {

    /**
     * Registra un nuevo incidente.
     */
    @Insert
    suspend fun insertarIncidente(incidente: IncidenteEntity): Long

    /**
     * Actualiza la información de un incidente existente.
     */
    @Update
    suspend fun actualizarIncidente(incidente: IncidenteEntity)

    /**
     * Elimina un incidente.
     */
    @Delete
    suspend fun eliminarIncidente(incidente: IncidenteEntity)

    /**
     * Obtiene todos los incidentes ordenados por fecha.
     */
    @Query("SELECT * FROM incidentes ORDER BY fecha_incidente DESC")
    fun obtenerIncidentes(): Flow<List<IncidenteEntity>>

    /**
     * Busca un incidente utilizando su identificador.
     */
    @Query("SELECT * FROM incidentes WHERE id_incidente = :idIncidente LIMIT 1")
    suspend fun obtenerIncidentePorId(idIncidente: Int): IncidenteEntity?

    /**
     * Obtiene el detalle completo de todos los incidentes.
     */
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

    /**
     * Obtiene el detalle de los incidentes asociados a un material.
     */
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

    /**
     * Obtiene los incidentes filtrados por estado.
     */
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

    /**
     * Obtiene los incidentes filtrados por nivel de severidad.
     */
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

    /**
     * Obtiene el detalle de un incidente utilizando su identificador.
     */
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

    /**
     * Cuenta la cantidad de incidentes que se encuentran abiertos.
     */
    @Query("SELECT COUNT(*) FROM incidentes WHERE estado = 'Abierto'")
    suspend fun contarIncidentesAbiertos(): Int

    /**
     * Cuenta la cantidad de incidentes que se encuentran cerrados.
     */
    @Query("SELECT COUNT(*) FROM incidentes WHERE estado = 'Cerrado'")
    suspend fun contarIncidentesCerrados(): Int

    /**
     * Elimina un incidente utilizando su identificador.
     */
    @Query("DELETE FROM incidentes WHERE id_incidente = :idIncidente")
    suspend fun eliminarIncidentePorId(idIncidente: Int)

    /**
     * Inserta una lista de incidentes, reemplazando los existentes si es necesario.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarIncidentes(incidentes: List<IncidenteEntity>)

    /**
     * Obtiene todos los incidentes para generar un respaldo.
     */
    @Query("SELECT * FROM incidentes ORDER BY id_incidente ASC")
    suspend fun obtenerIncidentesParaRespaldo(): List<IncidenteEntity>

    /**
     * Elimina todos los incidentes registrados.
     */
    @Query("DELETE FROM incidentes")
    suspend fun eliminarTodosIncidentes()
}