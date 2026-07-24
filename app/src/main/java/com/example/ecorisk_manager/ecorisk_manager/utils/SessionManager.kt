package com.example.ecorisk_manager.utils

import android.content.Context

class SessionManager(contexto: Context) {

    private val preferencias = contexto.getSharedPreferences(
        Constantes.Sesion.NOMBRE_PREFERENCIAS,
        Context.MODE_PRIVATE
    )

    fun guardarSesion(nombreUsuario: String, rolUsuario: String) {
        preferencias.edit()
            .putBoolean(Constantes.Sesion.CLAVE_SESION_ACTIVA, true)
            .putString(Constantes.Sesion.CLAVE_NOMBRE_USUARIO, nombreUsuario)
            .putString(Constantes.Sesion.CLAVE_ROL_USUARIO, rolUsuario)
            .apply()
    }

    fun haySesionActiva(): Boolean {
        return preferencias.getBoolean(Constantes.Sesion.CLAVE_SESION_ACTIVA, false)
    }

    fun obtenerNombreUsuario(): String {
        return preferencias.getString(
            Constantes.Sesion.CLAVE_NOMBRE_USUARIO,
            "Usuario"
        ) ?: "Usuario"
    }

    fun obtenerRolUsuario(): String {
        return preferencias.getString(
            Constantes.Sesion.CLAVE_ROL_USUARIO,
            Constantes.Roles.CONSULTA
        ) ?: Constantes.Roles.CONSULTA
    }

    fun cerrarSesion() {
        preferencias.edit().clear().apply()
    }
}