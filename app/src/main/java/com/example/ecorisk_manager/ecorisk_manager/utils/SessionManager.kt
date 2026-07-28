package com.example.ecorisk_manager.utils

import android.content.Context

/**
 * Administra la sesión del usuario utilizando SharedPreferences,
 * permitiendo guardar, consultar y eliminar la información
 * necesaria para mantener el acceso a la aplicación.
 */
class SessionManager(contexto: Context) {

    private val preferencias = contexto.getSharedPreferences(
        Constantes.Sesion.NOMBRE_PREFERENCIAS,
        Context.MODE_PRIVATE
    )

    /**
     * Guarda la información básica del usuario para mantener
     * la sesión activa entre ejecuciones de la aplicación.
     */
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

    /**
     * Elimina toda la información almacenada de la sesión
     * para finalizar el acceso del usuario.
     */
    fun cerrarSesion() {
        preferencias.edit().clear().apply()
    }
}