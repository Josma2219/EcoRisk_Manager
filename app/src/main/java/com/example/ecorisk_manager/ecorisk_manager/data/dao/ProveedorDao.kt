package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy

/**
 * DAO encargado de gestionar las operaciones relacionadas
 * con los proveedores en la base de datos.
 */
@Dao
interface ProveedorDao {

    /**
     * Registra un nuevo proveedor.
     */
    @Insert
    suspend fun insertarProveedor(proveedor: ProveedorEntity): Long

    /**
     * Actualiza la información de un proveedor existente.
     */
    @Update
    suspend fun actualizarProveedor(proveedor: ProveedorEntity)

    /**
     * Elimina un proveedor.
     */
    @Delete
    suspend fun eliminarProveedor(proveedor: ProveedorEntity)

    /**
     * Obtiene todos los proveedores ordenados por nombre.
     */
    @Query("SELECT * FROM proveedores ORDER BY nombre ASC")
    fun obtenerProveedores(): Flow<List<ProveedorEntity>>

    /**
     * Busca un proveedor utilizando su identificador.
     */
    @Query("SELECT * FROM proveedores WHERE id_proveedor = :idProveedor LIMIT 1")
    suspend fun obtenerProveedorPorId(idProveedor: Int): ProveedorEntity?

    /**
     * Busca proveedores por nombre, correo o contacto principal.
     */
    @Query("""
        SELECT * FROM proveedores
        WHERE nombre LIKE '%' || :texto || '%'
        OR correo LIKE '%' || :texto || '%'
        OR contacto_principal LIKE '%' || :texto || '%'
        ORDER BY nombre ASC
    """)
    fun buscarProveedores(texto: String): Flow<List<ProveedorEntity>>

    /**
     * Verifica cuántos proveedores existen con el mismo correo.
     */
    @Query("SELECT COUNT(*) FROM proveedores WHERE correo = :correo")
    suspend fun contarProveedorPorCorreo(correo: String): Int

    /**
     * Verifica si otro proveedor ya utiliza el mismo correo.
     */
    @Query("""
        SELECT COUNT(*) FROM proveedores
        WHERE correo = :correo
        AND id_proveedor != :idProveedor
    """)
    suspend fun contarProveedorPorCorreoEnOtroRegistro(
        correo: String,
        idProveedor: Int
    ): Int

    /**
     * Cuenta la cantidad total de proveedores registrados.
     */
    @Query("SELECT COUNT(*) FROM proveedores")
    suspend fun contarProveedores(): Int

    /**
     * Inserta una lista de proveedores, reemplazando los existentes si es necesario.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProveedores(proveedores: List<ProveedorEntity>)

    /**
     * Obtiene todos los proveedores para generar un respaldo.
     */
    @Query("SELECT * FROM proveedores ORDER BY id_proveedor ASC")
    suspend fun obtenerProveedoresParaRespaldo(): List<ProveedorEntity>

    /**
     * Elimina todos los proveedores registrados.
     */
    @Query("DELETE FROM proveedores")
    suspend fun eliminarTodosProveedores()
}