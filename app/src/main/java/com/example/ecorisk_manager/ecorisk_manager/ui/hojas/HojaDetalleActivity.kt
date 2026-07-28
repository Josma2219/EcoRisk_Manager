package com.example.ecorisk_manager.ui.hojas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.repository.HojaSeguridadRepository
import com.example.ecorisk_manager.databinding.ActivityHojaDetalleBinding
import com.example.ecorisk_manager.model.HojaSeguridadDetalle
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.HojaSeguridadViewModel
import com.example.ecorisk_manager.viewmodel.HojaSeguridadViewModelFactory

/**
 * Pantalla encargada de mostrar la información detallada
 * de una hoja de seguridad.
 */
class HojaDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHojaDetalleBinding
    private lateinit var hojaViewModel: HojaSeguridadViewModel

    private var idHojaActual: Int = 0
    private var hojaActual: HojaSeguridadDetalle? = null

    /**
     * Inicializa la pantalla y carga la información de la hoja seleccionada.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHojaDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idHojaActual = intent.getIntExtra(Constantes.Extras.EXTRA_ID_HOJA, 0)

        prepararViewModel()
        observarDatos()
        configurarEventos()
        validarYCargarHoja()
    }

    /**
     * Actualiza la información al regresar desde la pantalla de edición.
     */
    override fun onResume() {
        super.onResume()

        if (idHojaActual != 0) {
            hojaViewModel.cargarHojaDetallePorId(idHojaActual)
        }
    }

    /**
     * Inicializa el ViewModel que gestionará la información de la pantalla.
     */
    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = HojaSeguridadRepository(baseDatos.hojaSeguridadDao())
        val factory = HojaSeguridadViewModelFactory(repository)

        hojaViewModel = ViewModelProvider(this, factory)[HojaSeguridadViewModel::class.java]
    }

    /**
     * Observa los cambios del ViewModel para actualizar la interfaz
     * y mostrar el resultado de las operaciones.
     */
    private fun observarDatos() {
        hojaViewModel.hojaDetalleSeleccionada.observe(this) { hoja ->
            if (hoja == null) {
                return@observe
            }

            hojaActual = hoja
            mostrarDatosHoja(hoja)
        }

        hojaViewModel.resultadoOperacion.observe(this) { resultado ->
            if (resultado == null) return@observe

            Toast.makeText(this, resultado.mensaje, Toast.LENGTH_SHORT).show()

            if (resultado.exitoso) {
                finish()
            }

            hojaViewModel.limpiarResultadoOperacion()
        }
    }

    /**
     * Verifica que se haya recibido una hoja válida
     * antes de solicitar su información.
     */
    private fun validarYCargarHoja() {
        if (idHojaActual == 0) {
            Toast.makeText(this, "No se recibió la hoja seleccionada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        hojaViewModel.cargarHojaDetallePorId(idHojaActual)
    }

    /**
     * Muestra la información de la hoja de seguridad en la pantalla.
     */
    private fun mostrarDatosHoja(hoja: HojaSeguridadDetalle) {
        binding.textoMaterialHoja.text = hoja.nombreMaterial
        binding.textoCodigoMaterial.text = "Código: ${hoja.codigoMaterial}"
        binding.textoClasificacionRiesgo.text = "Riesgo: ${hoja.clasificacionRiesgo}"
        binding.textoVersionHoja.text = "Versión: ${hoja.version}"
        binding.textoFechaEmision.text = "Fecha emisión: ${hoja.fechaEmision}"
        binding.textoArchivoPdf.text = "Archivo PDF: ${hoja.archivoPdf}"

        binding.textoObservaciones.text = if (hoja.observaciones.isBlank()) {
            "Observaciones: sin observaciones registradas"
        } else {
            "Observaciones: ${hoja.observaciones}"
        }
    }

    /**
     * Configura los eventos de los botones de la pantalla.
     */
    private fun configurarEventos() {
        binding.botonEditarHoja.setOnClickListener {
            abrirFormularioEdicion()
        }

        binding.botonEliminarHoja.setOnClickListener {
            confirmarEliminacion()
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Abre el formulario para editar la hoja de seguridad actual.
     */
    private fun abrirFormularioEdicion() {
        val intent = Intent(this, HojaFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_HOJA, idHojaActual)

        hojaActual?.let { hoja ->
            intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, hoja.idMaterial)
        }

        startActivity(intent)
    }

    /**
     * Muestra un cuadro de confirmación antes de eliminar la hoja.
     */
    private fun confirmarEliminacion() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar hoja")
            .setMessage("¿Seguro que quiere eliminar esta hoja de seguridad?")
            .setPositiveButton("Sí, eliminar") { _, _ ->
                hojaViewModel.eliminarHoja(idHojaActual)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}