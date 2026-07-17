package com.example.ecorisk_manager.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.R
import com.example.ecorisk_manager.databinding.ActivityLoginBinding
import com.example.ecorisk_manager.ui.home.HomeActivity
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.utils.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        configurarEventos()
    }

    private fun configurarEventos() {
        binding.botonEntrar.setOnClickListener {
            validarLogin()
        }
    }

    private fun validarLogin() {
        val usuario = binding.campoUsuario.text.toString().trim()
        val contrasena = binding.campoContrasena.text.toString().trim()

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_campos_login), Toast.LENGTH_SHORT).show()
            return
        }

        // Login temporal para poder avanzar con la app.
        // Más adelante esto se cambia por consulta real a Room.
        val credencialesCorrectas =
            usuario == Constantes.UsuarioTemporal.USUARIO &&
                    contrasena == Constantes.UsuarioTemporal.CONTRASENA

        if (credencialesCorrectas) {
            sessionManager.guardarSesion(
                nombreUsuario = Constantes.UsuarioTemporal.NOMBRE,
                rolUsuario = Constantes.UsuarioTemporal.ROL
            )

            abrirHome()
        } else {
            Toast.makeText(this, getString(R.string.error_credenciales), Toast.LENGTH_SHORT).show()
        }
    }

    private fun abrirHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}