package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy

/**
 * DAO encargado de gestionar las operaciones relacionadas
 * con los usuarios en la base de datos.
 */
@Dao
interface UsuarioDao {

    /**
     * Registra un nuevo usuario.
     */
    @Insert
    suspend fun insertarUsuario(usuario: UsuarioEntity): Long

    /**
     * Actualiza la información de un usuario existente.
     */
    @Update
    suspend fun actualizarUsuario(usuario: UsuarioEntity)

    /**
     * Obtiene todos los usuarios ordenados por nombre.
     */
    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    fun obtenerUsuarios(): Flow<List<UsuarioEntity>>

    /**
     * Busca un usuario utilizando su identificador.
     */
    @Query("SELECT * FROM usuarios WHERE id_usuario = :idUsuario LIMIT 1")
    suspend fun obtenerUsuarioPorId(idUsuario: Int): UsuarioEntity?

    /**
     * Valida las credenciales de un usuario activo para permitir el inicio de sesión.
     */
    @Query("""
        SELECT * FROM usuarios
        WHERE usuario = :usuario
        AND contrasena = :contrasena
        AND estado = 'Activo'
        LIMIT 1
    """)
    suspend fun validarCredenciales(usuario: String, contrasena: String): UsuarioEntity?

    /**
     * Verifica cuántos usuarios existen con el mismo nombre de usuario.
     */
    @Query("SELECT COUNT(*) FROM usuarios WHERE usuario = :usuario")
    suspend fun contarUsuarioPorNombre(usuario: String): Int

    /**
     * Cuenta la cantidad total de usuarios registrados.
     */
    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contarUsuarios(): Int

    /**
     * Inserta una lista de usuarios, reemplazando los existentes si es necesario.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuarios(usuarios: List<UsuarioEntity>)

    /**
     * Obtiene todos los usuarios para generar un respaldo.
     */
    @Query("SELECT * FROM usuarios ORDER BY id_usuario ASC")
    suspend fun obtenerUsuariosParaRespaldo(): List<UsuarioEntity>

    /**
     * Elimina todos los usuarios registrados.
     */
    @Query("DELETE FROM usuarios")
    suspend fun eliminarTodosUsuarios()
}