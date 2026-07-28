package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.HojaSeguridadEntity
import com.example.ecorisk_manager.model.HojaSeguridadDetalle
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy

/**
 * DAO encargado de gestionar las operaciones relacionadas
 * con las hojas de seguridad en la base de datos.
 */
@Dao
interface HojaSeguridadDao {

    /**
     * Registra una nueva hoja de seguridad.
     */
    @Insert
    suspend fun insertarHoja(hoja: HojaSeguridadEntity): Long

    /**
     * Actualiza la información de una hoja de seguridad existente.
     */
    @Update
    suspend fun actualizarHoja(hoja: HojaSeguridadEntity)

    /**
     * Elimina una hoja de seguridad.
     */
    @Delete
    suspend fun eliminarHoja(hoja: HojaSeguridadEntity)

    /**
     * Obtiene todas las hojas de seguridad ordenadas por fecha de emisión.
     */
    @Query("SELECT * FROM hojas_seguridad ORDER BY fecha_emision DESC")
    fun obtenerHojas(): Flow<List<HojaSeguridadEntity>>

    /**
     * Busca una hoja de seguridad utilizando su identificador.
     */
    @Query("SELECT * FROM hojas_seguridad WHERE id_hoja = :idHoja LIMIT 1")
    suspend fun obtenerHojaPorId(idHoja: Int): HojaSeguridadEntity?

    /**
     * Obtiene las hojas de seguridad asociadas a un material específico.
     */
    @Query("""
        SELECT * FROM hojas_seguridad
        WHERE id_material = :idMaterial
        ORDER BY fecha_emision DESC
    """)
    fun obtenerHojasPorMaterial(idMaterial: Int): Flow<List<HojaSeguridadEntity>>

    /**
     * Obtiene el detalle completo de todas las hojas de seguridad.
     */
    @Query("""
        SELECT 
            h.id_hoja AS id_hoja,
            h.version AS version,
            h.fecha_emision AS fecha_emision,
            h.archivo_pdf AS archivo_pdf,
            h.observaciones AS observaciones,
            h.id_material AS id_material,
            m.codigo_material AS codigo_material,
            m.nombre_comercial AS nombre_material,
            m.clasificacion_riesgo AS clasificacion_riesgo
        FROM hojas_seguridad h
        INNER JOIN materiales_peligrosos m ON h.id_material = m.id_material
        ORDER BY h.fecha_emision DESC
    """)
    fun obtenerHojasDetalle(): Flow<List<HojaSeguridadDetalle>>

    /**
     * Obtiene el detalle de las hojas de seguridad de un material específico.
     */
    @Query("""
        SELECT 
            h.id_hoja AS id_hoja,
            h.version AS version,
            h.fecha_emision AS fecha_emision,
            h.archivo_pdf AS archivo_pdf,
            h.observaciones AS observaciones,
            h.id_material AS id_material,
            m.codigo_material AS codigo_material,
            m.nombre_comercial AS nombre_material,
            m.clasificacion_riesgo AS clasificacion_riesgo
        FROM hojas_seguridad h
        INNER JOIN materiales_peligrosos m ON h.id_material = m.id_material
        WHERE h.id_material = :idMaterial
        ORDER BY h.fecha_emision DESC
    """)
    fun obtenerHojasDetallePorMaterial(idMaterial: Int): Flow<List<HojaSeguridadDetalle>>

    /**
     * Obtiene el detalle de una hoja de seguridad utilizando su identificador.
     */
    @Query("""
        SELECT 
            h.id_hoja AS id_hoja,
            h.version AS version,
            h.fecha_emision AS fecha_emision,
            h.archivo_pdf AS archivo_pdf,
            h.observaciones AS observaciones,
            h.id_material AS id_material,
            m.codigo_material AS codigo_material,
            m.nombre_comercial AS nombre_material,
            m.clasificacion_riesgo AS clasificacion_riesgo
        FROM hojas_seguridad h
        INNER JOIN materiales_peligrosos m ON h.id_material = m.id_material
        WHERE h.id_hoja = :idHoja
        LIMIT 1
    """)
    suspend fun obtenerHojaDetallePorId(idHoja: Int): HojaSeguridadDetalle?

    /**
     * Verifica cuántas hojas existen con la misma versión para un material.
     */
    @Query("""
        SELECT COUNT(*) FROM hojas_seguridad
        WHERE id_material = :idMaterial
        AND version = :version
    """)
    suspend fun contarVersionPorMaterial(idMaterial: Int, version: String): Int

    /**
     * Verifica si otra hoja del mismo material utiliza la misma versión.
     */
    @Query("""
        SELECT COUNT(*) FROM hojas_seguridad
        WHERE id_material = :idMaterial
        AND version = :version
        AND id_hoja != :idHoja
    """)
    suspend fun contarVersionPorMaterialEnOtraHoja(
        idMaterial: Int,
        version: String,
        idHoja: Int
    ): Int

    /**
     * Elimina una hoja de seguridad utilizando su identificador.
     */
    @Query("DELETE FROM hojas_seguridad WHERE id_hoja = :idHoja")
    suspend fun eliminarHojaPorId(idHoja: Int)

    /**
     * Inserta una lista de hojas de seguridad, reemplazando las existentes si es necesario.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarHojas(hojas: List<HojaSeguridadEntity>)

    /**
     * Obtiene todas las hojas de seguridad para generar un respaldo.
     */
    @Query("SELECT * FROM hojas_seguridad ORDER BY id_hoja ASC")
    suspend fun obtenerHojasParaRespaldo(): List<HojaSeguridadEntity>

    /**
     * Elimina todas las hojas de seguridad registradas.
     */
    @Query("DELETE FROM hojas_seguridad")
    suspend fun eliminarTodasHojas()
}