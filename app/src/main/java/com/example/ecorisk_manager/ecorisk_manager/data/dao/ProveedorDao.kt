package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy

@Dao
interface ProveedorDao {

    @Insert
    suspend fun insertarProveedor(proveedor: ProveedorEntity): Long

    @Update
    suspend fun actualizarProveedor(proveedor: ProveedorEntity)

    @Delete
    suspend fun eliminarProveedor(proveedor: ProveedorEntity)

    @Query("SELECT * FROM proveedores ORDER BY nombre ASC")
    fun obtenerProveedores(): Flow<List<ProveedorEntity>>

    @Query("SELECT * FROM proveedores WHERE id_proveedor = :idProveedor LIMIT 1")
    suspend fun obtenerProveedorPorId(idProveedor: Int): ProveedorEntity?

    @Query("""
        SELECT * FROM proveedores
        WHERE nombre LIKE '%' || :texto || '%'
        OR correo LIKE '%' || :texto || '%'
        OR contacto_principal LIKE '%' || :texto || '%'
        ORDER BY nombre ASC
    """)
    fun buscarProveedores(texto: String): Flow<List<ProveedorEntity>>

    @Query("SELECT COUNT(*) FROM proveedores WHERE correo = :correo")
    suspend fun contarProveedorPorCorreo(correo: String): Int

    @Query("""
        SELECT COUNT(*) FROM proveedores
        WHERE correo = :correo
        AND id_proveedor != :idProveedor
    """)
    suspend fun contarProveedorPorCorreoEnOtroRegistro(
        correo: String,
        idProveedor: Int
    ): Int

    @Query("SELECT COUNT(*) FROM proveedores")
    suspend fun contarProveedores(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProveedores(proveedores: List<ProveedorEntity>)

    @Query("SELECT * FROM proveedores ORDER BY id_proveedor ASC")
    suspend fun obtenerProveedoresParaRespaldo(): List<ProveedorEntity>

    @Query("DELETE FROM proveedores")
    suspend fun eliminarTodosProveedores()
}