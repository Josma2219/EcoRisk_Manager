package com.example.ecorisk_manager.model

/**
 * Representa los diferentes estados que puede tener
 * el proceso de inicio de sesión.
 */
sealed class EstadoLogin {

    /**
     * Estado inicial antes de realizar el intento de inicio de sesión.
     */
    object Inicial : EstadoLogin()

    /**
     * Indica que la validación de credenciales está en proceso.
     */
    object Cargando : EstadoLogin()

    /**
     * Indica que el inicio de sesión fue exitoso.
     */
    data class Exito(
        val nombreUsuario: String,
        val rolUsuario: String
    ) : EstadoLogin()

    /**
     * Indica que ocurrió un error durante el inicio de sesión.
     */
    data class Error(
        val mensaje: String
    ) : EstadoLogin()
}