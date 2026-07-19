package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertarUsuario(usuario: UsuarioEntity): Long

    @Update
    suspend fun actualizarUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    fun obtenerUsuarios(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE id_usuario = :idUsuario LIMIT 1")
    suspend fun obtenerUsuarioPorId(idUsuario: Int): UsuarioEntity?

    @Query("""
        SELECT * FROM usuarios
        WHERE usuario = :usuario
        AND contrasena = :contrasena
        AND estado = 'Activo'
        LIMIT 1
    """)
    suspend fun validarCredenciales(usuario: String, contrasena: String): UsuarioEntity?

    @Query("SELECT COUNT(*) FROM usuarios WHERE usuario = :usuario")
    suspend fun contarUsuarioPorNombre(usuario: String): Int

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contarUsuarios(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuarios(usuarios: List<UsuarioEntity>)

    @Query("SELECT * FROM usuarios ORDER BY id_usuario ASC")
    suspend fun obtenerUsuariosParaRespaldo(): List<UsuarioEntity>

    @Query("DELETE FROM usuarios")
    suspend fun eliminarTodosUsuarios()
}