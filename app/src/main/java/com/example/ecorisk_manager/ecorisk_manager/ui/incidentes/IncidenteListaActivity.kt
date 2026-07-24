package com.example.ecorisk_manager.ui.incidentes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecorisk_manager.R
import com.example.ecorisk_manager.adapter.IncidenteAdapter
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.repository.IncidenteRepository
import com.example.ecorisk_manager.databinding.ActivityIncidenteListaBinding
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.IncidenteViewModel
import com.example.ecorisk_manager.viewmodel.IncidenteViewModelFactory

class IncidenteListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncidenteListaBinding
    private lateinit var incidenteViewModel: IncidenteViewModel
    private lateinit var incidenteAdapter: IncidenteAdapter

    private var idMaterialFiltrado: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIncidenteListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idMaterialFiltrado = intent.getIntExtra(Constantes.Extras.EXTRA_ID_MATERIAL, 0)

        prepararViewModel()
        configurarPantalla()
        configurarRecycler()
        configurarFiltros()
        observarIncidentes()
        configurarEventos()
        cargarIncidentesSegunOrigen()
    }

    override fun onResume() {
        super.onResume()
        cargarIncidentesSegunOrigen()
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = IncidenteRepository(baseDatos.incidenteDao())
        val factory = IncidenteViewModelFactory(repository)

        incidenteViewModel = ViewModelProvider(this, factory)[IncidenteViewModel::class.java]
    }

    private fun configurarPantalla() {
        if (idMaterialFiltrado != 0) {
            binding.textoTituloModulo.text = "Incidentes del material"
            binding.textoDescripcionModulo.text =
                "Incidentes registrados para el material seleccionado."

            // Cuando venimos desde un material, mostramos solo los incidentes de ese material.
            binding.contenedorFiltros.visibility = View.GONE
        } else {
            binding.textoTituloModulo.text = "Incidentes"
            binding.textoDescripcionModulo.text =
                "Registro y seguimiento de incidentes relacionados con materiales peligrosos."
            binding.contenedorFiltros.visibility = View.VISIBLE
        }
    }

    private fun configurarRecycler() {
        incidenteAdapter = IncidenteAdapter(
            alVerDetalleIncidente = { incidente ->
                abrirDetalleIncidente(incidente.idIncidente)
            },
            alEditarIncidente = { incidente ->
                abrirFormularioIncidente(
                    idIncidente = incidente.idIncidente,
                    idMaterial = incidente.idMaterial
                )
            }
        )

        binding.recyclerIncidentes.layoutManager = LinearLayoutManager(this)
        binding.recyclerIncidentes.adapter = incidenteAdapter
    }

    private fun configurarFiltros() {
        val adaptadorEstado = ArrayAdapter.createFromResource(
            this,
            R.array.estados_incidente,
            android.R.layout.simple_spinner_item
        )

        adaptadorEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFiltroEstado.adapter = adaptadorEstado

        val adaptadorSeveridad = ArrayAdapter.createFromResource(
            this,
            R.array.niveles_severidad,
            android.R.layout.simple_spinner_item
        )

        adaptadorSeveridad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFiltroSeveridad.adapter = adaptadorSeveridad
    }

    private fun observarIncidentes() {
        incidenteViewModel.incidentes.observe(this) { listaIncidentes ->
            incidenteAdapter.actualizarLista(listaIncidentes)

            val listaVacia = listaIncidentes.isEmpty()
            binding.textoListaVacia.visibility = if (listaVacia) View.VISIBLE else View.GONE
            binding.recyclerIncidentes.visibility = if (listaVacia) View.GONE else View.VISIBLE
        }
    }

    private fun configurarEventos() {
        binding.botonAgregarIncidente.setOnClickListener {
            abrirFormularioIncidente(idMaterial = idMaterialFiltrado)
        }

        binding.botonFiltrarEstado.setOnClickListener {
            val estado = binding.spinnerFiltroEstado.selectedItem.toString()

            if (estado.startsWith("Seleccione")) {
                Toast.makeText(this, "Seleccione un estado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            incidenteViewModel.cargarIncidentesPorEstado(estado)
        }

        binding.botonFiltrarSeveridad.setOnClickListener {
            val severidad = binding.spinnerFiltroSeveridad.selectedItem.toString()

            if (severidad.startsWith("Seleccione")) {
                Toast.makeText(this, "Seleccione una severidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            incidenteViewModel.cargarIncidentesPorSeveridad(severidad)
        }

        binding.botonLimpiarFiltros.setOnClickListener {
            binding.spinnerFiltroEstado.setSelection(0)
            binding.spinnerFiltroSeveridad.setSelection(0)
            incidenteViewModel.cargarIncidentes()
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    private fun cargarIncidentesSegunOrigen() {
        if (idMaterialFiltrado != 0) {
            incidenteViewModel.cargarIncidentesPorMaterial(idMaterialFiltrado)
        } else {
            incidenteViewModel.cargarIncidentes()
        }
    }

    private fun abrirFormularioIncidente(
        idIncidente: Int = 0,
        idMaterial: Int = 0
    ) {
        val intent = Intent(this, IncidenteFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_INCIDENTE, idIncidente)
        intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, idMaterial)
        startActivity(intent)
    }

    private fun abrirDetalleIncidente(idIncidente: Int) {
        val intent = Intent(this, IncidenteDetalleActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_INCIDENTE, idIncidente)
        startActivity(intent)
    }
}