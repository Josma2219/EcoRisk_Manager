package com.example.ecorisk_manager.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.example.ecorisk_manager.utils.PobladorDatosPrueba
import com.example.ecorisk_manager.utils.SessionManager
import com.example.ecorisk_manager.viewmodel.HomeViewModel
import com.example.ecorisk_manager.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.launch

/**
 * Pantalla principal de la aplicación.
 * Desde aquí el usuario puede acceder a los distintos módulos del sistema.
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var homeViewModel: HomeViewModel

    /**
     * Inicializa la pantalla y configura los componentes principales.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        protegerPantalla()
        prepararViewModel()

        /*
         USAR SOLO UNA VEZ PARA DEMO.

         Esta línea borra los datos actuales y vuelve a llenar la base de datos
         con materiales, proveedores, hojas de seguridad, relaciones e incidentes.

         Pasos:
         1. Dejarla activa.
         2. Ejecutar la app.
         3. Entrar al Home.
         4. Confirmar que sale el mensaje "Base de datos poblada para demo".
         5. Volver a este archivo y comentar o borrar esta línea.

         Si no la comentas, cada vez que entres al Home se reinicia la base.
        */
        poblarDatosDemoUnaSolaVez()

        // Después de ejecutarlo una vez, déjalo así:
        // poblarDatosDemoUnaSolaVez()

        observarDashboard()
        cargarDatosUsuario()
        configurarEventos()
    }
    /**
     * Actualiza la información del panel al regresar desde otros módulos.
     */
    override fun onResume() {
        super.onResume()

        // Refresca los indicadores para mostrar los datos más recientes.
        homeViewModel.cargarResumenDashboard()
    }

    /**
     * Verifica que exista una sesión activa antes de mostrar el contenido.
     */
    private fun protegerPantalla() {
        if (!sessionManager.haySesionActiva()) {
            abrirLogin()
        }
    }

    /**
     * Inicializa el ViewModel encargado del panel principal.
     */
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

    /**
     * Observa la información del dashboard para actualizar los indicadores.
     */
    private fun observarDashboard() {
        homeViewModel.resumenDashboard.observe(this) { resumen ->
            binding.tarjetaMateriales.text = "${resumen.totalMateriales}\nMateriales"
            binding.tarjetaProveedores.text = "${resumen.totalProveedores}\nProveedores"
            binding.tarjetaIncidentes.text = "${resumen.incidentesAbiertos}\nIncidentes abiertos"
        }
    }

    /**
     * Muestra el nombre y el rol del usuario que inició sesión.
     */
    private fun cargarDatosUsuario() {
        val nombreUsuario = sessionManager.obtenerNombreUsuario()
        val rolUsuario = sessionManager.obtenerRolUsuario()

        binding.textoDatosUsuario.text = "Bienvenido $nombreUsuario - Rol: $rolUsuario"
    }

    /**
     * Configura los eventos de navegación de la pantalla.
     */
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
    /**
     * Reinicia y carga datos de prueba en la base de datos.
     * Este método está pensado únicamente para demostraciones.
     */
    private fun poblarDatosDemoUnaSolaVez() {
        lifecycleScope.launch {
            val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)

            PobladorDatosPrueba.reiniciarYPoblarBaseDatos(baseDatos)

            homeViewModel.cargarResumenDashboard()

            Toast.makeText(
                this@HomeActivity,
                "Base de datos poblada para demo",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Abre la pantalla indicada.
     */
    private fun abrirPantalla(pantalla: Class<*>) {
        val intent = Intent(this, pantalla)
        startActivity(intent)
    }

    /**
     * Regresa a la pantalla de inicio de sesión y finaliza la sesión actual.
     */
    private fun abrirLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}