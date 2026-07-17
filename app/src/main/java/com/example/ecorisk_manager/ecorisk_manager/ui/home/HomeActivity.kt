package com.example.ecorisk_manager.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.databinding.ActivityHomeBinding
import com.example.ecorisk_manager.ui.login.LoginActivity
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.utils.SessionManager

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        protegerPantalla()
        cargarDatosUsuario()
        configurarEventos()
    }

    private fun protegerPantalla() {
        if (!sessionManager.haySesionActiva()) {
            abrirLogin()
        }
    }

    private fun cargarDatosUsuario() {
        val nombreUsuario = sessionManager.obtenerNombreUsuario()
        val rolUsuario = sessionManager.obtenerRolUsuario()

        binding.textoDatosUsuario.text = "Bienvenido $nombreUsuario - Rol: $rolUsuario"

        // Estos valores están en cero por ahora.
        // Luego van a salir de la base de datos desde HomeViewModel.
        binding.tarjetaMateriales.text = "0\nMateriales"
        binding.tarjetaProveedores.text = "0\nProveedores"
    }

    private fun configurarEventos() {
        binding.botonMateriales.setOnClickListener {
            mostrarModuloEnProceso()
        }

        binding.botonHojas.setOnClickListener {
            mostrarModuloEnProceso()
        }

        binding.botonProveedores.setOnClickListener {
            mostrarModuloEnProceso()
        }

        binding.botonMaterialProveedor.setOnClickListener {
            mostrarModuloEnProceso()
        }

        binding.botonIncidentes.setOnClickListener {
            mostrarModuloEnProceso()
        }

        binding.botonReportes.setOnClickListener {
            mostrarModuloEnProceso()
        }

        binding.botonRespaldo.setOnClickListener {
            mostrarModuloEnProceso()
        }

        binding.botonCerrarSesion.setOnClickListener {
            sessionManager.cerrarSesion()
            abrirLogin()
        }
    }

    private fun mostrarModuloEnProceso() {
        Toast.makeText(
            this,
            Constantes.Mensajes.MODULO_EN_PROCESO,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun abrirLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}