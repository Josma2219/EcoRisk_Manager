package com.example.ecorisk_manager.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.databinding.ActivityHomeBinding
import com.example.ecorisk_manager.ui.hojas.HojaListaActivity
import com.example.ecorisk_manager.ui.incidentes.IncidenteListaActivity
import com.example.ecorisk_manager.ui.login.LoginActivity
import com.example.ecorisk_manager.ui.materiales.MaterialListaActivity
import com.example.ecorisk_manager.ui.materialproveedor.MaterialProveedorActivity
import com.example.ecorisk_manager.ui.proveedores.ProveedorListaActivity
import com.example.ecorisk_manager.ui.reportes.ReporteMenuActivity
import com.example.ecorisk_manager.ui.respaldo.RespaldoActivity
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

        // Por ahora son datos temporales.
        // Más adelante estos números salen de Room desde HomeViewModel.
        binding.tarjetaMateriales.text = "0\nMateriales"
        binding.tarjetaProveedores.text = "0\nProveedores"
    }

    private fun configurarEventos() {
        binding.botonMateriales.setOnClickListener {
            abrirPantalla(MaterialListaActivity::class.java)
        }

        binding.botonHojas.setOnClickListener {
            abrirPantalla(HojaListaActivity::class.java)
        }

        binding.botonProveedores.setOnClickListener {
            abrirPantalla(ProveedorListaActivity::class.java)
        }

        binding.botonMaterialProveedor.setOnClickListener {
            abrirPantalla(MaterialProveedorActivity::class.java)
        }

        binding.botonIncidentes.setOnClickListener {
            abrirPantalla(IncidenteListaActivity::class.java)
        }

        binding.botonReportes.setOnClickListener {
            abrirPantalla(ReporteMenuActivity::class.java)
        }

        binding.botonRespaldo.setOnClickListener {
            abrirPantalla(RespaldoActivity::class.java)
        }

        binding.botonCerrarSesion.setOnClickListener {
            sessionManager.cerrarSesion()
            abrirLogin()
        }
    }

    private fun abrirPantalla(pantalla: Class<*>) {
        val intent = Intent(this, pantalla)
        startActivity(intent)
    }

    private fun abrirLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}