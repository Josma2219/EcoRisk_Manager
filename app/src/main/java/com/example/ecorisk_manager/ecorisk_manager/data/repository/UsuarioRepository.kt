package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.UsuarioDao
import com.example.ecorisk_manager.data.entity.UsuarioEntity
import com.example.ecorisk_manager.utils.Constantes

/**
 * Repositorio encargado de gestionar las operaciones
 * relacionadas con los usuarios.
 */
class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {

    /**
     * Crea un usuario administrador por defecto si la base de datos
     * aún no tiene usuarios registrados.
     */
    suspend fun crearAdministradorInicialSiHaceFalta() {
        val cantidadUsuarios = usuarioDao.contarUsuarios()

        if (cantidadUsuarios == 0) {

            // Registra el administrador inicial utilizando los datos definidos en las constantes.
            val usuarioAdministrador = UsuarioEntity(
                nombre = Constantes.UsuarioTemporal.NOMBRE,
                usuario = Constantes.UsuarioTemporal.USUARIO,
                contrasena = Constantes.UsuarioTemporal.CONTRASENA,
                rol = Constantes.UsuarioTemporal.ROL,
                estado = Constantes.Estados.ACTIVO
            )

            usuarioDao.insertarUsuario(usuarioAdministrador)
        }
    }

    /**
     * Valida las credenciales de un usuario para permitir el inicio de sesión.
     */
    suspend fun validarCredenciales(usuario: String, contrasena: String): UsuarioEntity? {
        return usuarioDao.validarCredenciales(usuario, contrasena)
    }
}