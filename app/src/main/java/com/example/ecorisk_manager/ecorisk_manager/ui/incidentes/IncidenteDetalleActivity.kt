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

/**
 * Pantalla encargada de mostrar la información detallada
 * de un incidente.
 */
class IncidenteDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncidenteDetalleBinding
    private lateinit var incidenteViewModel: IncidenteViewModel

    private var idIncidenteActual: Int = 0
    private var incidenteActual: IncidenteDetalle? = null

    /**
     * Inicializa la pantalla y carga la información del incidente seleccionado.
     */
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

    /**
     * Actualiza la información al regresar desde la pantalla de edición.
     */
    override fun onResume() {
        super.onResume()

        if (idIncidenteActual != 0) {
            incidenteViewModel.cargarIncidenteDetallePorId(idIncidenteActual)
        }
    }

    /**
     * Inicializa el ViewModel que gestionará la información de la pantalla.
     */
    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = IncidenteRepository(baseDatos.incidenteDao())
        val factory = IncidenteViewModelFactory(repository)

        incidenteViewModel = ViewModelProvider(this, factory)[IncidenteViewModel::class.java]
    }

    /**
     * Observa los cambios del ViewModel para actualizar la interfaz
     * y mostrar el resultado de las operaciones.
     */
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

    /**
     * Verifica que se haya recibido un incidente válido
     * antes de solicitar su información.
     */
    private fun validarYCargarIncidente() {
        if (idIncidenteActual == 0) {
            Toast.makeText(this, "No se recibió el incidente seleccionado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        incidenteViewModel.cargarIncidenteDetallePorId(idIncidenteActual)
    }

    /**
     * Muestra la información del incidente en la pantalla.
     */
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

    /**
     * Configura los eventos de los botones de la pantalla.
     */
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

    /**
     * Abre el formulario para editar el incidente actual.
     */
    private fun abrirFormularioEdicion() {
        val intent = Intent(this, IncidenteFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_INCIDENTE, idIncidenteActual)

        incidenteActual?.let { incidente ->
            intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, incidente.idMaterial)
        }

        startActivity(intent)
    }

    /**
     * Muestra un cuadro de confirmación antes de eliminar el incidente.
     */
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