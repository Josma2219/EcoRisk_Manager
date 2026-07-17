package com.example.ecorisk_manager.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.repository.UsuarioRepository
import com.example.ecorisk_manager.databinding.ActivityLoginBinding
import com.example.ecorisk_manager.model.EstadoLogin
import com.example.ecorisk_manager.ui.home.HomeActivity
import com.example.ecorisk_manager.utils.SessionManager
import com.example.ecorisk_manager.viewmodel.LoginViewModel
import com.example.ecorisk_manager.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        prepararViewModel()
        observarLogin()
        configurarEventos()

        // Esto crea el admin inicial solo si la tabla usuarios está vacía.
        loginViewModel.prepararUsuarioAdministrador()
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val usuarioRepository = UsuarioRepository(baseDatos.usuarioDao())
        val factory = LoginViewModelFactory(usuarioRepository)

        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun configurarEventos() {
        binding.botonEntrar.setOnClickListener {
            val usuario = binding.campoUsuario.text.toString()
            val contrasena = binding.campoContrasena.text.toString()

            loginViewModel.iniciarSesion(usuario, contrasena)
        }
    }

    private fun observarLogin() {
        loginViewModel.estadoLogin.observe(this) { estado ->
            when (estado) {
                is EstadoLogin.Inicial -> {
                    cambiarEstadoCarga(false)
                }

                is EstadoLogin.Cargando -> {
                    cambiarEstadoCarga(true)
                }

                is EstadoLogin.Exito -> {
                    cambiarEstadoCarga(false)

                    sessionManager.guardarSesion(
                        nombreUsuario = estado.nombreUsuario,
                        rolUsuario = estado.rolUsuario
                    )

                    abrirHome()
                }

                is EstadoLogin.Error -> {
                    cambiarEstadoCarga(false)
                    Toast.makeText(this, estado.mensaje, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun cambiarEstadoCarga(estaCargando: Boolean) {
        binding.progresoLogin.visibility = if (estaCargando) View.VISIBLE else View.GONE
        binding.botonEntrar.isEnabled = !estaCargando
        binding.campoUsuario.isEnabled = !estaCargando
        binding.campoContrasena.isEnabled = !estaCargando
    }

    private fun abrirHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}