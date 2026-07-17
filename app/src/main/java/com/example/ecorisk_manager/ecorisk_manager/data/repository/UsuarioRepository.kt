package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.UsuarioDao
import com.example.ecorisk_manager.data.entity.UsuarioEntity
import com.example.ecorisk_manager.utils.Constantes

class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {

    suspend fun crearAdministradorInicialSiHaceFalta() {
        val cantidadUsuarios = usuarioDao.contarUsuarios()

        if (cantidadUsuarios == 0) {
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

    suspend fun validarCredenciales(usuario: String, contrasena: String): UsuarioEntity? {
        return usuarioDao.validarCredenciales(usuario, contrasena)
    }
}