package com.example.ecorisk_manager.model

/**
 * Representa el resultado de una operación realizada en la aplicación.
 * Indica si la operación fue exitosa y el mensaje que debe mostrarse al usuario.
 */
data class ResultadoOperacion(
    val exitoso: Boolean,
    val mensaje: String
)