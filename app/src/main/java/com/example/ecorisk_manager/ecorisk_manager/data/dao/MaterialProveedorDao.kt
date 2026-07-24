package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.MaterialProveedorEntity
import com.example.ecorisk_manager.model.MaterialProveedorDetalle
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy

@Dao
interface MaterialProveedorDao {

    @Insert
    suspend fun insertarRelacion(relacion: MaterialProveedorEntity): Long

    @Update
    suspend fun actualizarRelacion(relacion: MaterialProveedorEntity)

    @Delete
    suspend fun eliminarRelacion(relacion: MaterialProveedorEntity)

    @Query("SELECT * FROM materiales_proveedores ORDER BY id_material_proveedor DESC")
    fun obtenerRelaciones(): Flow<List<MaterialProveedorEntity>>

    @Query("""
        SELECT * FROM materiales_proveedores
        WHERE id_material_proveedor = :idRelacion
        LIMIT 1
    """)
    suspend fun obtenerRelacionPorId(idRelacion: Int): MaterialProveedorEntity?

    @Query("""
        SELECT 
            mp.id_material_proveedor AS id_material_proveedor,
            mp.id_material AS id_material,
            mp.id_proveedor AS id_proveedor,
            m.nombre_comercial AS nombre_material,
            m.codigo_material AS codigo_material,
            m.clasificacion_riesgo AS clasificacion_riesgo,
            p.nombre AS nombre_proveedor,
            p.correo AS correo_proveedor,
            mp.precio_referencia AS precio_referencia
        FROM materiales_proveedores mp
        INNER JOIN materiales_peligrosos m ON mp.id_material = m.id_material
        INNER JOIN proveedores p ON mp.id_proveedor = p.id_proveedor
        ORDER BY m.nombre_comercial ASC
    """)
    fun obtenerRelacionesDetalle(): Flow<List<MaterialProveedorDetalle>>

    @Query("""
        SELECT 
            mp.id_material_proveedor AS id_material_proveedor,
            mp.id_material AS id_material,
            mp.id_proveedor AS id_proveedor,
            m.nombre_comercial AS nombre_material,
            m.codigo_material AS codigo_material,
            m.clasificacion_riesgo AS clasificacion_riesgo,
            p.nombre AS nombre_proveedor,
            p.correo AS correo_proveedor,
            mp.precio_referencia AS precio_referencia
        FROM materiales_proveedores mp
        INNER JOIN materiales_peligrosos m ON mp.id_material = m.id_material
        INNER JOIN proveedores p ON mp.id_proveedor = p.id_proveedor
        WHERE mp.id_material = :idMaterial
        ORDER BY p.nombre ASC
    """)
    fun obtenerRelacionesDetallePorMaterial(idMaterial: Int): Flow<List<MaterialProveedorDetalle>>

    @Query("""
        SELECT 
            mp.id_material_proveedor AS id_material_proveedor,
            mp.id_material AS id_material,
            mp.id_proveedor AS id_proveedor,
            m.nombre_comercial AS nombre_material,
            m.codigo_material AS codigo_material,
            m.clasificacion_riesgo AS clasificacion_riesgo,
            p.nombre AS nombre_proveedor,
            p.correo AS correo_proveedor,
            mp.precio_referencia AS precio_referencia
        FROM materiales_proveedores mp
        INNER JOIN materiales_peligrosos m ON mp.id_material = m.id_material
        INNER JOIN proveedores p ON mp.id_proveedor = p.id_proveedor
        WHERE mp.id_proveedor = :idProveedor
        ORDER BY m.nombre_comercial ASC
    """)
    fun obtenerRelacionesDetallePorProveedor(idProveedor: Int): Flow<List<MaterialProveedorDetalle>>

    @Query("""
        SELECT * FROM materiales_proveedores
        WHERE id_material = :idMaterial
        ORDER BY precio_referencia ASC
    """)
    fun obtenerRelacionesPorMaterial(idMaterial: Int): Flow<List<MaterialProveedorEntity>>

    @Query("""
        SELECT * FROM materiales_proveedores
        WHERE id_proveedor = :idProveedor
        ORDER BY precio_referencia ASC
    """)
    fun obtenerRelacionesPorProveedor(idProveedor: Int): Flow<List<MaterialProveedorEntity>>

    @Query("""
        SELECT COUNT(*) FROM materiales_proveedores
        WHERE id_material = :idMaterial
        AND id_proveedor = :idProveedor
    """)
    suspend fun contarRelacionExistente(idMaterial: Int, idProveedor: Int): Int

    @Query("SELECT COUNT(*) FROM materiales_proveedores")
    suspend fun contarRelaciones(): Int

    @Query("DELETE FROM materiales_proveedores WHERE id_material_proveedor = :idRelacion")
    suspend fun eliminarRelacionPorId(idRelacion: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRelaciones(relaciones: List<MaterialProveedorEntity>)

    @Query("SELECT * FROM materiales_proveedores ORDER BY id_material_proveedor ASC")
    suspend fun obtenerRelacionesParaRespaldo(): List<MaterialProveedorEntity>

    @Query("DELETE FROM materiales_proveedores")
    suspend fun eliminarTodasRelaciones()
}