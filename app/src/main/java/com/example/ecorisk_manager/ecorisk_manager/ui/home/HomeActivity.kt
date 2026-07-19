package com.example.ecorisk_manager.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.repository.HomeRepository
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
import com.example.ecorisk_manager.viewmodel.HomeViewModel
import com.example.ecorisk_manager.viewmodel.HomeViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        protegerPantalla()
        prepararViewModel()
        observarDashboard()
        cargarDatosUsuario()
        configurarEventos()
    }

    override fun onResume() {
        super.onResume()

        // Cada vez que volvamos al Home, refrescamos los números.
        // Cuando registremos materiales/proveedores/incidentes, esto se va a notar.
        homeViewModel.cargarResumenDashboard()
    }

    private fun protegerPantalla() {
        if (!sessionManager.haySesionActiva()) {
            abrirLogin()
        }
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)

        val homeRepository = HomeRepository(
            materialPeligrosoDao = baseDatos.materialPeligrosoDao(),
            proveedorDao = baseDatos.proveedorDao(),
            incidenteDao = baseDatos.incidenteDao()
        )

        val factory = HomeViewModelFactory(homeRepository)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    private fun observarDashboard() {
        homeViewModel.resumenDashboard.observe(this) { resumen ->
            binding.tarjetaMateriales.text = "${resumen.totalMateriales}\nMateriales"
            binding.tarjetaProveedores.text = "${resumen.totalProveedores}\nProveedores"
            binding.tarjetaIncidentes.text = "${resumen.incidentesAbiertos}\nIncidentes abiertos"
        }
    }

    private fun cargarDatosUsuario() {
        val nombreUsuario = sessionManager.obtenerNombreUsuario()
        val rolUsuario = sessionManager.obtenerRolUsuario()

        binding.textoDatosUsuario.text = "Bienvenido $nombreUsuario - Rol: $rolUsuario"
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