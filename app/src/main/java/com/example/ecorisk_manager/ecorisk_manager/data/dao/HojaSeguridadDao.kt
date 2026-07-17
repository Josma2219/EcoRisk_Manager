package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.HojaSeguridadEntity
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
        SELECT COUNT(*) FROM hojas_seguridad
        WHERE id_material = :idMaterial
        AND version = :version
    """)
    suspend fun contarVersionPorMaterial(idMaterial: Int, version: String): Int
}