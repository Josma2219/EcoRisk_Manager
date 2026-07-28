package com.example.ecorisk_manager.ui.reportes

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecorisk_manager.R
import com.example.ecorisk_manager.adapter.ReporteIncidenteAdapter
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.repository.ReporteRepository
import com.example.ecorisk_manager.databinding.ActivityReporteHistorialIncidentesBinding
import com.example.ecorisk_manager.model.IncidenteDetalle
import com.example.ecorisk_manager.utils.GeneradorPdf
import com.example.ecorisk_manager.viewmodel.ReporteViewModel
import com.example.ecorisk_manager.viewmodel.ReporteViewModelFactory

/**
 * Pantalla encargada de mostrar el historial de incidentes.
 * Permite filtrar la información y exportarla en formato PDF.
 */
class ReporteHistorialIncidentesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReporteHistorialIncidentesBinding
    private lateinit var reporteViewModel: ReporteViewModel
    private lateinit var reporteIncidenteAdapter: ReporteIncidenteAdapter

    // Guarda la lista mostrada actualmente para exportarla al PDF.
    private var listaActualIncidentes = emptyList<IncidenteDetalle>()

    // Indica el filtro aplicado al reporte.
    private var filtroActual = "Historial completo"

    /**
     * Inicializa la pantalla y carga la información del reporte.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReporteHistorialIncidentesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepararViewModel()
        configurarRecycler()
        configurarSpinners()
        observarIncidentes()
        configurarEventos()

        reporteViewModel.cargarHistorialIncidentes()
    }

    /**
     * Inicializa el ViewModel utilizado por la pantalla.
     */
    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = ReporteRepository(baseDatos.reporteDao())
        val factory = ReporteViewModelFactory(repository)

        reporteViewModel = ViewModelProvider(this, factory)[ReporteViewModel::class.java]
    }

    /**
     * Configura el RecyclerView donde se muestran los incidentes.
     */
    private fun configurarRecycler() {
        reporteIncidenteAdapter = ReporteIncidenteAdapter()

        binding.recyclerIncidentesReporte.layoutManager = LinearLayoutManager(this)
        binding.recyclerIncidentesReporte.adapter = reporteIncidenteAdapter
    }

    /**
     * Configura los Spinner utilizados para filtrar la información.
     */
    private fun configurarSpinners() {
        val adaptadorEstado = ArrayAdapter.createFromResource(
            this,
            R.array.estados_incidente,
            android.R.layout.simple_spinner_item
        )

        adaptadorEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEstado.adapter = adaptadorEstado

        val adaptadorSeveridad = ArrayAdapter.createFromResource(
            this,
            R.array.niveles_severidad,
            android.R.layout.simple_spinner_item
        )

        adaptadorSeveridad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSeveridad.adapter = adaptadorSeveridad
    }

    /**
     * Observa los cambios en la información del reporte para actualizar la interfaz.
     */
    private fun observarIncidentes() {
        reporteViewModel.incidentesReporte.observe(this) { listaIncidentes ->
            listaActualIncidentes = listaIncidentes

            reporteIncidenteAdapter.actualizarLista(listaIncidentes)
            binding.textoTotalIncidentes.text = "Total en reporte: ${listaIncidentes.size}"

            val listaVacia = listaIncidentes.isEmpty()
            binding.textoListaVacia.visibility = if (listaVacia) View.VISIBLE else View.GONE
            binding.recyclerIncidentesReporte.visibility = if (listaVacia) View.GONE else View.VISIBLE
        }
    }

    /**
     * Configura los eventos de los controles de la pantalla.
     */
    private fun configurarEventos() {
        binding.botonFiltrarEstado.setOnClickListener {
            val estado = binding.spinnerEstado.selectedItem.toString()

            if (estado.startsWith("Seleccione")) {
                Toast.makeText(this, "Seleccione un estado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            filtroActual = "Estado: $estado"
            reporteViewModel.cargarIncidentesPorEstado(estado)
        }

        binding.botonFiltrarSeveridad.setOnClickListener {
            val severidad = binding.spinnerSeveridad.selectedItem.toString()

            if (severidad.startsWith("Seleccione")) {
                Toast.makeText(this, "Seleccione una severidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            filtroActual = "Severidad: $severidad"
            reporteViewModel.cargarIncidentesPorSeveridad(severidad)
        }

        binding.botonVerTodos.setOnClickListener {
            binding.spinnerEstado.setSelection(0)
            binding.spinnerSeveridad.setSelection(0)
            filtroActual = "Historial completo"
            reporteViewModel.cargarHistorialIncidentes()
        }

        binding.botonExportarPdf.setOnClickListener {
            exportarPdf()
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Genera un archivo PDF con la información mostrada en el reporte.
     */
    private fun exportarPdf() {
        if (listaActualIncidentes.isEmpty()) {
            Toast.makeText(this, "No hay incidentes para exportar", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val archivo = GeneradorPdf.generarReporteHistorialIncidentes(
                contexto = this,
                filtroAplicado = filtroActual,
                incidentes = listaActualIncidentes
            )

            val mensaje = "PDF generado:\n${archivo.absolutePath}"
            binding.textoEstadoPdf.text = mensaje

            Toast.makeText(this, "PDF generado correctamente", Toast.LENGTH_LONG).show()
        } catch (error: Exception) {
            binding.textoEstadoPdf.text = "No se pudo generar el PDF"
            Toast.makeText(this, "No se pudo generar el PDF", Toast.LENGTH_SHORT).show()
        }
    }
}