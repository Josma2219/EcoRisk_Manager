package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialPeligrosoDao {

    @Insert
    suspend fun insertarMaterial(material: MaterialPeligrosoEntity): Long

    @Update
    suspend fun actualizarMaterial(material: MaterialPeligrosoEntity)

    @Delete
    suspend fun eliminarMaterial(material: MaterialPeligrosoEntity)

    @Query("SELECT * FROM materiales_peligrosos ORDER BY nombre_comercial ASC")
    fun obtenerMateriales(): Flow<List<MaterialPeligrosoEntity>>

    @Query("SELECT * FROM materiales_peligrosos WHERE id_material = :idMaterial LIMIT 1")
    suspend fun obtenerMaterialPorId(idMaterial: Int): MaterialPeligrosoEntity?

    @Query("""
        SELECT * FROM materiales_peligrosos
        WHERE nombre_comercial LIKE '%' || :texto || '%'
        OR codigo_material LIKE '%' || :texto || '%'
        ORDER BY nombre_comercial ASC
    """)
    fun buscarMateriales(texto: String): Flow<List<MaterialPeligrosoEntity>>

    @Query("""
        SELECT * FROM materiales_peligrosos
        WHERE clasificacion_riesgo = :clasificacionRiesgo
        ORDER BY nombre_comercial ASC
    """)
    fun obtenerMaterialesPorRiesgo(clasificacionRiesgo: String): Flow<List<MaterialPeligrosoEntity>>

    @Query("SELECT COUNT(*) FROM materiales_peligrosos WHERE codigo_material = :codigoMaterial")
    suspend fun contarMaterialPorCodigo(codigoMaterial: String): Int

    @Query("""
        SELECT COUNT(*) FROM materiales_peligrosos
        WHERE codigo_material = :codigoMaterial
        AND id_material != :idMaterial
    """)
    suspend fun contarMaterialPorCodigoEnOtroRegistro(
        codigoMaterial: String,
        idMaterial: Int
    ): Int

    @Query("SELECT COUNT(*) FROM materiales_peligrosos")
    suspend fun contarMateriales(): Int

    @Query("SELECT COUNT(*) FROM materiales_peligrosos WHERE estado = 'Activo'")
    suspend fun contarMaterialesActivos(): Int
}