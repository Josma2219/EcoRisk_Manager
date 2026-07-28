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

/**
 * Pantalla utilizada para registrar y editar hojas de seguridad.
 */
class HojaFormularioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHojaFormularioBinding
    private lateinit var hojaViewModel: HojaSeguridadViewModel

    // Lista utilizada para cargar los materiales disponibles en el Spinner.
    private val listaMateriales = mutableListOf<MaterialPeligrosoEntity>()

    private var idHojaActual: Int = 0
    private var idMaterialFiltrado: Int = 0

    /**
     * Inicializa la pantalla y configura los datos necesarios.
     */
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
     * Configura el título y el botón según si se registrará
     * una nueva hoja o se editará una existente.
     */
    private fun configurarPantalla() {
        if (idHojaActual == 0) {
            binding.textoTituloFormulario.text = "Registrar hoja"
            binding.botonGuardarHoja.text = "Guardar"
        } else {
            binding.textoTituloFormulario.text = "Editar hoja"
            binding.botonGuardarHoja.text = "Actualizar"
        }
    }

    /**
     * Observa los cambios del ViewModel para actualizar la interfaz
     * y mostrar el resultado de las operaciones.
     */
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

    /**
     * Obtiene la lista de materiales registrados para llenar el Spinner.
     */
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

    /**
     * Configura el Spinner con los materiales disponibles.
     */
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

        // Si la pantalla fue abierta desde un material específico,
        // se selecciona automáticamente y se bloquea el cambio.
        if (idMaterialFiltrado != 0) {
            seleccionarMaterialPorId(idMaterialFiltrado)
            binding.spinnerMaterial.isEnabled = false
        }
    }

    /**
     * Configura los eventos de los botones de la pantalla.
     */
    private fun configurarEventos() {
        binding.botonGuardarHoja.setOnClickListener {
            guardarHoja()
        }

        binding.botonCancelar.setOnClickListener {
            finish()
        }
    }

    /**
     * Obtiene la información ingresada por el usuario
     * y la envía al ViewModel para guardarla.
     */
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

    /**
     * Completa el formulario con la información de una hoja existente.
     */
    private fun llenarFormulario(hoja: HojaSeguridadEntity) {
        binding.campoVersion.setText(hoja.version)
        binding.campoFechaEmision.setText(hoja.fechaEmision)
        binding.campoArchivoPdf.setText(hoja.archivoPdf)
        binding.campoObservaciones.setText(hoja.observaciones)

        seleccionarMaterialPorId(hoja.idMaterial)
    }

    /**
     * Obtiene el identificador del material seleccionado en el Spinner.
     */
    private fun obtenerIdMaterialSeleccionado(): Int {
        val posicion = binding.spinnerMaterial.selectedItemPosition

        if (posicion <= 0) {
            return 0
        }

        return listaMateriales[posicion - 1].idMaterial
    }

    /**
     * Selecciona un material en el Spinner utilizando su identificador.
     */
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