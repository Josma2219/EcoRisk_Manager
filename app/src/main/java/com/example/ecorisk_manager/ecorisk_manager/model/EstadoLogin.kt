package com.example.ecorisk_manager.model

sealed class EstadoLogin {
    object Inicial : EstadoLogin()
    object Cargando : EstadoLogin()

    data class Exito(
        val nombreUsuario: String,
        val rolUsuario: String
    ) : EstadoLogin()

    data class Error(
        val mensaje: String
    ) : EstadoLogin()
}