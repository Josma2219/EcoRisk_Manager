package com.example.ecorisk_manager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecorisk_manager.data.repository.UsuarioRepository
import com.example.ecorisk_manager.model.EstadoLogin
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar el proceso de autenticación
 * del usuario y comunicar el estado del inicio de sesión a la interfaz.
 */
class LoginViewModel(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _estadoLogin = MutableLiveData<EstadoLogin>(EstadoLogin.Inicial)
    val estadoLogin: LiveData<EstadoLogin> = _estadoLogin

    /**
     * Crea el usuario administrador por defecto únicamente cuando
     * la base de datos aún no contiene usuarios registrados.
     */
    fun prepararUsuarioAdministrador() {
        viewModelScope.launch {
            try {
                usuarioRepository.crearAdministradorInicialSiHaceFalta()
            } catch (error: Exception) {
                _estadoLogin.value = EstadoLogin.Error(
                    "No se pudo preparar el usuario inicial"
                )
            }
        }
    }

    /**
     * Valida las credenciales ingresadas y actualiza el estado
     * del proceso de autenticación según el resultado obtenido.
     */
    fun iniciarSesion(usuario: String, contrasena: String) {

        // Validación básica antes de consultar la base de datos.
        if (usuario.isBlank() || contrasena.isBlank()) {
            _estadoLogin.value = EstadoLogin.Error("Digite usuario y contraseña")
            return
        }

        _estadoLogin.value = EstadoLogin.Cargando

        viewModelScope.launch {
            try {
                val usuarioEncontrado = usuarioRepository.validarCredenciales(
                    usuario = usuario.trim(),
                    contrasena = contrasena.trim()
                )

                if (usuarioEncontrado != null) {
                    _estadoLogin.value = EstadoLogin.Exito(
                        nombreUsuario = usuarioEncontrado.nombre,
                        rolUsuario = usuarioEncontrado.rol
                    )
                } else {
                    _estadoLogin.value = EstadoLogin.Error(
                        "Usuario o contraseña incorrectos"
                    )
                }
            } catch (error: Exception) {
                _estadoLogin.value = EstadoLogin.Error(
                    "Ocurrió un error al iniciar sesión"
                )
            }
        }
    }
}