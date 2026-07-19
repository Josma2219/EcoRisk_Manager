package com.example.ecorisk_manager.ui.reportes

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecorisk_manager.R
import com.example.ecorisk_manager.adapter.ReporteMaterialAdapter
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.repository.ReporteRepository
import com.example.ecorisk_manager.databinding.ActivityReporteMaterialesRiesgoBinding
import com.example.ecorisk_manager.viewmodel.ReporteViewModel
import com.example.ecorisk_manager.viewmodel.ReporteViewModelFactory

class ReporteMaterialesRiesgoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReporteMaterialesRiesgoBinding
    private lateinit var reporteViewModel: ReporteViewModel
    private lateinit var reporteMaterialAdapter: ReporteMaterialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReporteMaterialesRiesgoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepararViewModel()
        configurarRecycler()
        configurarSpinner()
        observarMateriales()
        configurarEventos()

        reporteViewModel.cargarTodosLosMateriales()
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = ReporteRepository(baseDatos.reporteDao())
        val factory = ReporteViewModelFactory(repository)

        reporteViewModel = ViewModelProvider(this, factory)[ReporteViewModel::class.java]
    }

    private fun configurarRecycler() {
        reporteMaterialAdapter = ReporteMaterialAdapter()

        binding.recyclerMaterialesReporte.layoutManager = LinearLayoutManager(this)
        binding.recyclerMaterialesReporte.adapter = reporteMaterialAdapter
    }

    private fun configurarSpinner() {
        val adaptador = ArrayAdapter.createFromResource(
            this,
            R.array.clasificaciones_riesgo,
            android.R.layout.simple_spinner_item
        )

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRiesgo.adapter = adaptador
    }

    private fun observarMateriales() {
        reporteViewModel.materialesReporte.observe(this) { listaMateriales ->
            reporteMaterialAdapter.actualizarLista(listaMateriales)

            binding.textoTotalMateriales.text = "Total en reporte: ${listaMateriales.size}"

            val listaVacia = listaMateriales.isEmpty()
            binding.textoListaVacia.visibility = if (listaVacia) View.VISIBLE else View.GONE
            binding.recyclerMaterialesReporte.visibility = if (listaVacia) View.GONE else View.VISIBLE
        }
    }

    private fun configurarEventos() {
        binding.botonGenerarReporte.setOnClickListener {
            val clasificacion = binding.spinnerRiesgo.selectedItem.toString()

            if (clasificacion.startsWith("Seleccione")) {
                Toast.makeText(this, "Seleccione una clasificación de riesgo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            reporteViewModel.cargarMaterialesPorRiesgo(clasificacion)
        }

        binding.botonVerTodos.setOnClickListener {
            binding.spinnerRiesgo.setSelection(0)
            reporteViewModel.cargarTodosLosMateriales()
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }
}