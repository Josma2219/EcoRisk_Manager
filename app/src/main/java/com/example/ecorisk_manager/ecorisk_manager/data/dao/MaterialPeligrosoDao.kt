package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy

/**
 * DAO encargado de gestionar las operaciones relacionadas
 * con los materiales peligrosos en la base de datos.
 */
@Dao
interface MaterialPeligrosoDao {

    /**
     * Registra un nuevo material peligroso.
     */
    @Insert
    suspend fun insertarMaterial(material: MaterialPeligrosoEntity): Long

    /**
     * Actualiza la información de un material existente.
     */
    @Update
    suspend fun actualizarMaterial(material: MaterialPeligrosoEntity)

    /**
     * Elimina un material peligroso.
     */
    @Delete
    suspend fun eliminarMaterial(material: MaterialPeligrosoEntity)

    /**
     * Obtiene todos los materiales ordenados por nombre.
     */
    @Query("SELECT * FROM materiales_peligrosos ORDER BY nombre_comercial ASC")
    fun obtenerMateriales(): Flow<List<MaterialPeligrosoEntity>>

    /**
     * Busca un material utilizando su identificador.
     */
    @Query("SELECT * FROM materiales_peligrosos WHERE id_material = :idMaterial LIMIT 1")
    suspend fun obtenerMaterialPorId(idMaterial: Int): MaterialPeligrosoEntity?

    /**
     * Busca materiales por nombre o código.
     */
    @Query("""
        SELECT * FROM materiales_peligrosos
        WHERE nombre_comercial LIKE '%' || :texto || '%'
        OR codigo_material LIKE '%' || :texto || '%'
        ORDER BY nombre_comercial ASC
    """)
    fun buscarMateriales(texto: String): Flow<List<MaterialPeligrosoEntity>>

    /**
     * Obtiene los materiales que pertenecen a una clasificación de riesgo.
     */
    @Query("""
        SELECT * FROM materiales_peligrosos
        WHERE clasificacion_riesgo = :clasificacionRiesgo
        ORDER BY nombre_comercial ASC
    """)
    fun obtenerMaterialesPorRiesgo(clasificacionRiesgo: String): Flow<List<MaterialPeligrosoEntity>>

    /**
     * Verifica cuántos materiales existen con el mismo código.
     */
    @Query("SELECT COUNT(*) FROM materiales_peligrosos WHERE codigo_material = :codigoMaterial")
    suspend fun contarMaterialPorCodigo(codigoMaterial: String): Int

    /**
     * Verifica si otro material ya utiliza el mismo código.
     */
    @Query("""
        SELECT COUNT(*) FROM materiales_peligrosos
        WHERE codigo_material = :codigoMaterial
        AND id_material != :idMaterial
    """)
    suspend fun contarMaterialPorCodigoEnOtroRegistro(
        codigoMaterial: String,
        idMaterial: Int
    ): Int

    /**
     * Cuenta la cantidad total de materiales registrados.
     */
    @Query("SELECT COUNT(*) FROM materiales_peligrosos")
    suspend fun contarMateriales(): Int

    /**
     * Cuenta la cantidad de materiales que se encuentran activos.
     */
    @Query("SELECT COUNT(*) FROM materiales_peligrosos WHERE estado = 'Activo'")
    suspend fun contarMaterialesActivos(): Int

    /**
     * Inserta una lista de materiales, reemplazando los existentes si es necesario.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMateriales(materiales: List<MaterialPeligrosoEntity>)

    /**
     * Obtiene todos los materiales para generar un respaldo.
     */
    @Query("SELECT * FROM materiales_peligrosos ORDER BY id_material ASC")
    suspend fun obtenerMaterialesParaRespaldo(): List<MaterialPeligrosoEntity>

    /**
     * Elimina todos los materiales registrados.
     */
    @Query("DELETE FROM materiales_peligrosos")
    suspend fun eliminarTodosMateriales()
}