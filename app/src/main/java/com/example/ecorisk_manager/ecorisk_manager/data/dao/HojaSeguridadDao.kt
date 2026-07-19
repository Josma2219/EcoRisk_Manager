package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.HojaSeguridadEntity
import com.example.ecorisk_manager.model.HojaSeguridadDetalle
import kotlinx.coroutines.flow.Flow

@Dao
interface HojaSeguridadDao {

    @Insert
    suspend fun insertarHoja(hoja: HojaSeguridadEntity): Long

    @Update
    suspend fun actualizarHoja(hoja: HojaSeguridadEntity)

    @Delete
    suspend fun eliminarHoja(hoja: HojaSeguridadEntity)

    @Query("SELECT * FROM hojas_seguridad ORDER BY fecha_emision DESC")
    fun obtenerHojas(): Flow<List<HojaSeguridadEntity>>

    @Query("SELECT * FROM hojas_seguridad WHERE id_hoja = :idHoja LIMIT 1")
    suspend fun obtenerHojaPorId(idHoja: Int): HojaSeguridadEntity?

    @Query("""
        SELECT * FROM hojas_seguridad
        WHERE id_material = :idMaterial
        ORDER BY fecha_emision DESC
    """)
    fun obtenerHojasPorMaterial(idMaterial: Int): Flow<List<HojaSeguridadEntity>>

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

    @Query("""
        SELECT COUNT(*) FROM hojas_seguridad
        WHERE id_material = :idMaterial
        AND version = :version
    """)
    suspend fun contarVersionPorMaterial(idMaterial: Int, version: String): Int

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

    @Query("DELETE FROM hojas_seguridad WHERE id_hoja = :idHoja")
    suspend fun eliminarHojaPorId(idHoja: Int)
}