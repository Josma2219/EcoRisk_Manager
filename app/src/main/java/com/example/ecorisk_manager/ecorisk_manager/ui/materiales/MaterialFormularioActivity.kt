package com.example.ecorisk_manager.ui.materiales

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.R
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.data.repository.MaterialRepository
import com.example.ecorisk_manager.databinding.ActivityMaterialFormularioBinding
import com.example.ecorisk_manager.viewmodel.MaterialViewModel
import com.example.ecorisk_manager.viewmodel.MaterialViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.ecorisk_manager.utils.Constantes

/**
 * Pantalla utilizada para registrar y editar materiales peligrosos.
 */
class MaterialFormularioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaterialFormularioBinding
    private lateinit var materialViewModel: MaterialViewModel

    private var idMaterialActual: Int = 0

    /**
     * Inicializa la pantalla y configura los datos necesarios.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaterialFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idMaterialActual = intent.getIntExtra(Constantes.Extras.EXTRA_ID_MATERIAL, 0)

        prepararViewModel()
        configurarSpinners()
        configurarPantalla()
        observarDatos()
        configurarEventos()

        if (idMaterialActual != 0) {
            materialViewModel.cargarMaterialPorId(idMaterialActual)
        } else {
            colocarFechaActual()
        }
    }

    /**
     * Inicializa el ViewModel que gestionará la información de la pantalla.
     */
    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val materialRepository = MaterialRepository(baseDatos.materialPeligrosoDao())
        val factory = MaterialViewModelFactory(materialRepository)

        materialViewModel = ViewModelProvider(this, factory)[MaterialViewModel::class.java]
    }

    /**
     * Configura los Spinner con las opciones disponibles.
     */
    private fun configurarSpinners() {
        configurarSpinnerConArray(
            spinner = binding.spinnerClasificacionRiesgo,
            arrayId = R.array.clasificaciones_riesgo
        )

        configurarSpinnerConArray(
            spinner = binding.spinnerUnidadMedida,
            arrayId = R.array.unidades_medida
        )

        configurarSpinnerConArray(
            spinner = binding.spinnerEstado,
            arrayId = R.array.estados_material
        )
    }

    /**
     * Asocia un arreglo de recursos a un Spinner.
     */
    private fun configurarSpinnerConArray(spinner: Spinner, arrayId: Int) {
        val adaptador = ArrayAdapter.createFromResource(
            this,
            arrayId,
            android.R.layout.simple_spinner_item
        )

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador
    }

    /**
     * Configura el título y el botón según si se registrará
     * un nuevo material o se editará uno existente.
     */
    private fun configurarPantalla() {
        if (idMaterialActual == 0) {
            binding.textoTituloFormulario.text = "Registrar material"
            binding.botonGuardarMaterial.text = "Guardar"
        } else {
            binding.textoTituloFormulario.text = "Editar material"
            binding.botonGuardarMaterial.text = "Actualizar"
        }
    }

    /**
     * Observa los cambios del ViewModel para actualizar la interfaz
     * y mostrar el resultado de las operaciones.
     */
    private fun observarDatos() {
        materialViewModel.materialSeleccionado.observe(this) { material ->
            if (material != null) {
                llenarFormulario(material)
            }
        }

        materialViewModel.resultadoOperacion.observe(this) { resultado ->
            if (resultado == null) return@observe

            Toast.makeText(this, resultado.mensaje, Toast.LENGTH_SHORT).show()

            if (resultado.exitoso) {
                finish()
            }

            materialViewModel.limpiarResultadoOperacion()
        }
    }

    /**
     * Configura los eventos de los botones de la pantalla.
     */
    private fun configurarEventos() {
        binding.botonGuardarMaterial.setOnClickListener {
            guardarMaterial()
        }

        binding.botonCancelar.setOnClickListener {
            finish()
        }
    }

    /**
     * Obtiene la información ingresada por el usuario
     * y la envía al ViewModel para guardarla.
     */
    private fun guardarMaterial() {
        val codigoMaterial = binding.campoCodigoMaterial.text.toString()
        val nombreComercial = binding.campoNombreComercial.text.toString()
        val descripcion = binding.campoDescripcion.text.toString()
        val clasificacionRiesgo = binding.spinnerClasificacionRiesgo.selectedItem.toString()
        val unidadMedida = binding.spinnerUnidadMedida.selectedItem.toString()
        val fechaRegistro = binding.campoFechaRegistro.text.toString()
        val estado = binding.spinnerEstado.selectedItem.toString()

        materialViewModel.guardarMaterial(
            idMaterial = idMaterialActual,
            codigoMaterial = codigoMaterial,
            nombreComercial = nombreComercial,
            descripcion = descripcion,
            clasificacionRiesgo = clasificacionRiesgo,
            unidadMedida = unidadMedida,
            fechaRegistro = fechaRegistro,
            estado = estado
        )
    }

    /**
     * Completa el formulario con la información de un material existente.
     */
    private fun llenarFormulario(material: MaterialPeligrosoEntity) {
        binding.campoCodigoMaterial.setText(material.codigoMaterial)
        binding.campoNombreComercial.setText(material.nombreComercial)
        binding.campoDescripcion.setText(material.descripcion)
        binding.campoFechaRegistro.setText(material.fechaRegistro)

        seleccionarValorSpinner(binding.spinnerClasificacionRiesgo, material.clasificacionRiesgo)
        seleccionarValorSpinner(binding.spinnerUnidadMedida, material.unidadMedida)
        seleccionarValorSpinner(binding.spinnerEstado, material.estado)
    }

    /**
     * Selecciona un valor específico dentro de un Spinner.
     */
    private fun seleccionarValorSpinner(spinner: Spinner, valor: String) {
        for (posicion in 0 until spinner.adapter.count) {
            val item = spinner.adapter.getItem(posicion).toString()

            if (item == valor) {
                spinner.setSelection(posicion)
                return
            }
        }
    }

    /**
     * Coloca la fecha actual como valor inicial del formulario.
     */
    private fun colocarFechaActual() {
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaActual = formatoFecha.format(Date())

        binding.campoFechaRegistro.setText(fechaActual)
    }
}