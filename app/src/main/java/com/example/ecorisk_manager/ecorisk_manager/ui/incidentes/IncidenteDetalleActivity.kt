package com.example.ecorisk_manager.ui.incidentes

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.repository.IncidenteRepository
import com.example.ecorisk_manager.databinding.ActivityIncidenteDetalleBinding
import com.example.ecorisk_manager.model.IncidenteDetalle
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.IncidenteViewModel
import com.example.ecorisk_manager.viewmodel.IncidenteViewModelFactory

class IncidenteDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncidenteDetalleBinding
    private lateinit var incidenteViewModel: IncidenteViewModel

    private var idIncidenteActual: Int = 0
    private var incidenteActual: IncidenteDetalle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIncidenteDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idIncidenteActual = intent.getIntExtra(Constantes.Extras.EXTRA_ID_INCIDENTE, 0)

        prepararViewModel()
        observarDatos()
        configurarEventos()
        validarYCargarIncidente()
    }

    override fun onResume() {
        super.onResume()

        if (idIncidenteActual != 0) {
            incidenteViewModel.cargarIncidenteDetallePorId(idIncidenteActual)
        }
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = IncidenteRepository(baseDatos.incidenteDao())
        val factory = IncidenteViewModelFactory(repository)

        incidenteViewModel = ViewModelProvider(this, factory)[IncidenteViewModel::class.java]
    }

    private fun observarDatos() {
        incidenteViewModel.incidenteDetalleSeleccionado.observe(this) { incidente ->
            if (incidente == null) {
                return@observe
            }

            incidenteActual = incidente
            mostrarDatosIncidente(incidente)
        }

        incidenteViewModel.resultadoOperacion.observe(this) { resultado ->
            if (resultado == null) return@observe

            Toast.makeText(this, resultado.mensaje, Toast.LENGTH_SHORT).show()

            if (resultado.exitoso) {
                finish()
            }

            incidenteViewModel.limpiarResultadoOperacion()
        }
    }

    private fun validarYCargarIncidente() {
        if (idIncidenteActual == 0) {
            Toast.makeText(this, "No se recibió el incidente seleccionado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        incidenteViewModel.cargarIncidenteDetallePorId(idIncidenteActual)
    }

    private fun mostrarDatosIncidente(incidente: IncidenteDetalle) {
        binding.textoTipoIncidente.text = incidente.tipoIncidente
        binding.textoMaterialIncidente.text = "Material: ${incidente.nombreMaterial}"
        binding.textoCodigoMaterial.text = "Código: ${incidente.codigoMaterial}"
        binding.textoFechaIncidente.text = "Fecha: ${incidente.fechaIncidente}"
        binding.textoSeveridadIncidente.text = "Severidad: ${incidente.nivelSeveridad}"
        binding.textoEstadoIncidente.text = "Estado: ${incidente.estado}"
        binding.textoDescripcionIncidente.text = "Descripción: ${incidente.descripcion}"

        binding.textoAccionesCorrectivas.text = if (incidente.accionesCorrectivas.isBlank()) {
            "Acciones correctivas: pendiente de registrar"
        } else {
            "Acciones correctivas: ${incidente.accionesCorrectivas}"
        }
    }

    private fun configurarEventos() {
        binding.botonEditarIncidente.setOnClickListener {
            abrirFormularioEdicion()
        }

        binding.botonEliminarIncidente.setOnClickListener {
            confirmarEliminacion()
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    private fun abrirFormularioEdicion() {
        val intent = Intent(this, IncidenteFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_INCIDENTE, idIncidenteActual)

        incidenteActual?.let { incidente ->
            intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, incidente.idMaterial)
        }

        startActivity(intent)
    }

    private fun confirmarEliminacion() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar incidente")
            .setMessage("¿Seguro que quiere eliminar este incidente?")
            .setPositiveButton("Sí, eliminar") { _, _ ->
                incidenteViewModel.eliminarIncidente(idIncidenteActual)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}