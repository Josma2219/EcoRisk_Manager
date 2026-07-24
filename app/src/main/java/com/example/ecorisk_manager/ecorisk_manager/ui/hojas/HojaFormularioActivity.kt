package com.example.ecorisk_manager.ui.hojas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.entity.HojaSeguridadEntity
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.data.repository.HojaSeguridadRepository
import com.example.ecorisk_manager.databinding.ActivityHojaFormularioBinding
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.HojaSeguridadViewModel
import com.example.ecorisk_manager.viewmodel.HojaSeguridadViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HojaFormularioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHojaFormularioBinding
    private lateinit var hojaViewModel: HojaSeguridadViewModel

    private val listaMateriales = mutableListOf<MaterialPeligrosoEntity>()

    private var idHojaActual: Int = 0
    private var idMaterialFiltrado: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHojaFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idHojaActual = intent.getIntExtra(Constantes.Extras.EXTRA_ID_HOJA, 0)
        idMaterialFiltrado = intent.getIntExtra(Constantes.Extras.EXTRA_ID_MATERIAL, 0)

        prepararViewModel()
        configurarPantalla()
        observarDatos()
        cargarMateriales()
        configurarEventos()

        if (idHojaActual != 0) {
            hojaViewModel.cargarHojaPorId(idHojaActual)
        } else {
            colocarFechaActual()
        }
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = HojaSeguridadRepository(baseDatos.hojaSeguridadDao())
        val factory = HojaSeguridadViewModelFactory(repository)

        hojaViewModel = ViewModelProvider(this, factory)[HojaSeguridadViewModel::class.java]
    }

    private fun configurarPantalla() {
        if (idHojaActual == 0) {
            binding.textoTituloFormulario.text = "Registrar hoja"
            binding.botonGuardarHoja.text = "Guardar"
        } else {
            binding.textoTituloFormulario.text = "Editar hoja"
            binding.botonGuardarHoja.text = "Actualizar"
        }
    }

    private fun observarDatos() {
        hojaViewModel.hojaSeleccionada.observe(this) { hoja ->
            if (hoja != null) {
                llenarFormulario(hoja)
            }
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
        binding.botonGuardarHoja.setOnClickListener {
            guardarHoja()
        }

        binding.botonCancelar.setOnClickListener {
            finish()
        }
    }

    private fun guardarHoja() {
        val idMaterial = obtenerIdMaterialSeleccionado()
        val version = binding.campoVersion.text.toString()
        val fechaEmision = binding.campoFechaEmision.text.toString()
        val archivoPdf = binding.campoArchivoPdf.text.toString()
        val observaciones = binding.campoObservaciones.text.toString()

        hojaViewModel.guardarHoja(
            idHoja = idHojaActual,
            idMaterial = idMaterial,
            version = version,
            fechaEmision = fechaEmision,
            archivoPdf = archivoPdf,
            observaciones = observaciones
        )
    }

    private fun llenarFormulario(hoja: HojaSeguridadEntity) {
        binding.campoVersion.setText(hoja.version)
        binding.campoFechaEmision.setText(hoja.fechaEmision)
        binding.campoArchivoPdf.setText(hoja.archivoPdf)
        binding.campoObservaciones.setText(hoja.observaciones)

        seleccionarMaterialPorId(hoja.idMaterial)
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

    private fun colocarFechaActual() {
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaActual = formatoFecha.format(Date())

        binding.campoFechaEmision.setText(fechaActual)
    }
}