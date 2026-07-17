package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.MaterialProveedorEntity
import kotlinx.coroutines.flow.Flow

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
}