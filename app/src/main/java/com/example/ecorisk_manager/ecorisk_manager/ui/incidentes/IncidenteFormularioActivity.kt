package com.example.ecorisk_manager.ui.incidentes

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.ecorisk_manager.R
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.entity.IncidenteEntity
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.data.repository.IncidenteRepository
import com.example.ecorisk_manager.databinding.ActivityIncidenteFormularioBinding
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.IncidenteViewModel
import com.example.ecorisk_manager.viewmodel.IncidenteViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IncidenteFormularioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncidenteFormularioBinding
    private lateinit var incidenteViewModel: IncidenteViewModel

    private val listaMateriales = mutableListOf<MaterialPeligrosoEntity>()

    private var idIncidenteActual: Int = 0
    private var idMaterialFiltrado: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIncidenteFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idIncidenteActual = intent.getIntExtra(Constantes.Extras.EXTRA_ID_INCIDENTE, 0)
        idMaterialFiltrado = intent.getIntExtra(Constantes.Extras.EXTRA_ID_MATERIAL, 0)

        prepararViewModel()
        configurarPantalla()
        configurarSpinnersFijos()
        observarDatos()
        cargarMateriales()
        configurarEventos()

        if (idIncidenteActual != 0) {
            incidenteViewModel.cargarIncidentePorId(idIncidenteActual)
        } else {
            colocarFechaActual()
        }
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = IncidenteRepository(baseDatos.incidenteDao())
        val factory = IncidenteViewModelFactory(repository)

        incidenteViewModel = ViewModelProvider(this, factory)[IncidenteViewModel::class.java]
    }

    private fun configurarPantalla() {
        if (idIncidenteActual == 0) {
            binding.textoTituloFormulario.text = "Registrar incidente"
            binding.botonGuardarIncidente.text = "Guardar"
        } else {
            binding.textoTituloFormulario.text = "Editar incidente"
            binding.botonGuardarIncidente.text = "Actualizar"
        }
    }

    private fun configurarSpinnersFijos() {
        configurarSpinnerConArray(binding.spinnerTipoIncidente, R.array.tipos_incidente)
        configurarSpinnerConArray(binding.spinnerSeveridad, R.array.niveles_severidad)
        configurarSpinnerConArray(binding.spinnerEstado, R.array.estados_incidente)
    }

    private fun configurarSpinnerConArray(spinner: Spinner, arrayId: Int) {
        val adaptador = ArrayAdapter.createFromResource(
            this,
            arrayId,
            android.R.layout.simple_spinner_item
        )

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador
    }

    private fun observarDatos() {
        incidenteViewModel.incidenteSeleccionado.observe(this) { incidente ->
            if (incidente != null) {
                llenarFormulario(incidente)
            }
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

    private fun cargarMateriales() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)

        lifecycleScope.launch {
            baseDatos.materialPeligrosoDao().obtenerMateriales().collectLatest { materiales ->
                listaMateriales.clear()
                listaMateriales.addAll(materiales)

                configurarSpinnerMateriales()
            }
        }
    }

    private fun configurarSpinnerMateriales() {
        val nombresMateriales = mutableListOf("Seleccione material")

        nombresMateriales.addAll(
            listaMateriales.map { material ->
                "${material.codigoMaterial} - ${material.nombreComercial}"
            }
        )

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nombresMateriales
        )

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMaterial.adapter = adaptador

        if (idMaterialFiltrado != 0) {
            seleccionarMaterialPorId(idMaterialFiltrado)
            binding.spinnerMaterial.isEnabled = false
        }
    }

    private fun configurarEventos() {
        binding.botonGuardarIncidente.setOnClickListener {
            guardarIncidente()
        }

        binding.botonCancelar.setOnClickListener {
            finish()
        }
    }

    private fun guardarIncidente() {
        val idMaterial = obtenerIdMaterialSeleccionado()
        val fechaIncidente = binding.campoFechaIncidente.text.toString()
        val tipoIncidente = binding.spinnerTipoIncidente.selectedItem.toString()
        val descripcion = binding.campoDescripcion.text.toString()
        val nivelSeveridad = binding.spinnerSeveridad.selectedItem.toString()
        val accionesCorrectivas = binding.campoAccionesCorrectivas.text.toString()
        val estado = binding.spinnerEstado.selectedItem.toString()

        incidenteViewModel.guardarIncidente(
            idIncidente = idIncidenteActual,
            fechaIncidente = fechaIncidente,
            tipoIncidente = tipoIncidente,
            descripcion = descripcion,
            nivelSeveridad = nivelSeveridad,
            accionesCorrectivas = accionesCorrectivas,
            estado = estado,
            idMaterial = idMaterial
        )
    }

    private fun llenarFormulario(incidente: IncidenteEntity) {
        binding.campoFechaIncidente.setText(incidente.fechaIncidente)
        binding.campoDescripcion.setText(incidente.descripcion)
        binding.campoAccionesCorrectivas.setText(incidente.accionesCorrectivas)

        seleccionarMaterialPorId(incidente.idMaterial)
        seleccionarValorSpinner(binding.spinnerTipoIncidente, incidente.tipoIncidente)
        seleccionarValorSpinner(binding.spinnerSeveridad, incidente.nivelSeveridad)
        seleccionarValorSpinner(binding.spinnerEstado, incidente.estado)
    }

    private fun obtenerIdMaterialSeleccionado(): Int {
        val posicion = binding.spinnerMaterial.selectedItemPosition

        if (posicion <= 0) {
            return 0
        }

        return listaMateriales[posicion - 1].idMaterial
    }

    private fun seleccionarMaterialPorId(idMaterial: Int) {
        val posicion = listaMateriales.indexOfFirst { material ->
            material.idMaterial == idMaterial
        }

        if (posicion >= 0) {
            binding.spinnerMaterial.setSelection(posicion + 1)
        }
    }

    private fun seleccionarValorSpinner(spinner: Spinner, valor: String) {
        for (posicion in 0 until spinner.adapter.count) {
            val item = spinner.adapter.getItem(posicion).toString()

            if (item == valor) {
                spinner.setSelection(posicion)
                return
            }
        }
    }

    private fun colocarFechaActual() {
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaActual = formatoFecha.format(Date())

        binding.campoFechaIncidente.setText(fechaActual)
    }
}