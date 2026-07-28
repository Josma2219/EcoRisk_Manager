package com.example.ecorisk_manager.ui.materialproveedor

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecorisk_manager.adapter.MaterialProveedorAdapter
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import com.example.ecorisk_manager.data.repository.MaterialProveedorRepository
import com.example.ecorisk_manager.databinding.ActivityMaterialProveedorBinding
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.MaterialProveedorViewModel
import com.example.ecorisk_manager.viewmodel.MaterialProveedorViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Pantalla encargada de administrar las relaciones
 * entre materiales peligrosos y proveedores.
 */
class MaterialProveedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaterialProveedorBinding
    private lateinit var materialProveedorViewModel: MaterialProveedorViewModel
    private lateinit var materialProveedorAdapter: MaterialProveedorAdapter

    // Listas utilizadas para cargar la información de los Spinner.
    private val listaMateriales = mutableListOf<MaterialPeligrosoEntity>()
    private val listaProveedores = mutableListOf<ProveedorEntity>()

    private var idMaterialFiltrado: Int = 0
    private var idProveedorFiltrado: Int = 0

    /**
     * Inicializa la pantalla y configura sus componentes.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaterialProveedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idMaterialFiltrado = intent.getIntExtra(Constantes.Extras.EXTRA_ID_MATERIAL, 0)
        idProveedorFiltrado = intent.getIntExtra(Constantes.Extras.EXTRA_ID_PROVEEDOR, 0)

        prepararViewModel()
        configurarRecycler()
        observarRelaciones()
        observarResultado()
        cargarSpinners()
        configurarEventos()
        cargarRelacionesSegunOrigen()
    }

    /**
     * Actualiza la información al regresar desde otra pantalla.
     */
    override fun onResume() {
        super.onResume()

        // Refresca los datos mostrados en los Spinner y la lista.
        cargarSpinners()
        cargarRelacionesSegunOrigen()
    }

    /**
     * Inicializa el ViewModel que gestionará la información de la pantalla.
     */
    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = MaterialProveedorRepository(baseDatos.materialProveedorDao())
        val factory = MaterialProveedorViewModelFactory(repository)

        materialProveedorViewModel = ViewModelProvider(this, factory)[MaterialProveedorViewModel::class.java]
    }

    /**
     * Configura el RecyclerView y su adaptador.
     */
    private fun configurarRecycler() {
        materialProveedorAdapter = MaterialProveedorAdapter(
            alEliminarRelacion = { relacion ->
                confirmarEliminacion(relacion.idMaterialProveedor)
            }
        )

        binding.recyclerRelaciones.layoutManager = LinearLayoutManager(this)
        binding.recyclerRelaciones.adapter = materialProveedorAdapter
    }

    /**
     * Observa los cambios en la lista de relaciones para actualizar la interfaz.
     */
    private fun observarRelaciones() {
        materialProveedorViewModel.relaciones.observe(this) { relaciones ->
            materialProveedorAdapter.actualizarLista(relaciones)

            val listaVacia = relaciones.isEmpty()
            binding.textoListaVacia.visibility = if (listaVacia) View.VISIBLE else View.GONE
            binding.recyclerRelaciones.visibility = if (listaVacia) View.GONE else View.VISIBLE
        }
    }

    /**
     * Observa el resultado de las operaciones realizadas sobre las relaciones.
     */
    private fun observarResultado() {
        materialProveedorViewModel.resultadoOperacion.observe(this) { resultado ->
            if (resultado == null) return@observe

            Toast.makeText(this, resultado.mensaje, Toast.LENGTH_SHORT).show()

            if (resultado.exitoso) {
                limpiarFormulario()
                cargarRelacionesSegunOrigen()
            }

            materialProveedorViewModel.limpiarResultadoOperacion()
        }
    }

    /**
     * Obtiene la información de materiales y proveedores
     * para llenar los Spinner.
     */
    private fun cargarSpinners() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)

        lifecycleScope.launch {
            baseDatos.materialPeligrosoDao().obtenerMateriales().collectLatest { materiales ->
                listaMateriales.clear()
                listaMateriales.addAll(materiales)

                configurarSpinnerMateriales()
            }
        }

        lifecycleScope.launch {
            baseDatos.proveedorDao().obtenerProveedores().collectLatest { proveedores ->
                listaProveedores.clear()
                listaProveedores.addAll(proveedores)

                configurarSpinnerProveedores()
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

        if (idMaterialFiltrado != 0) {
            seleccionarMaterialFiltrado()
        }
    }

    /**
     * Configura el Spinner con los proveedores disponibles.
     */
    private fun configurarSpinnerProveedores() {
        val nombresProveedores = mutableListOf("Seleccione proveedor")
        nombresProveedores.addAll(
            listaProveedores.map { proveedor ->
                proveedor.nombre
            }
        )

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nombresProveedores
        )

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProveedor.adapter = adaptador

        if (idProveedorFiltrado != 0) {
            seleccionarProveedorFiltrado()
        }
    }

    /**
     * Selecciona automáticamente el material recibido
     * y evita que pueda modificarse.
     */
    private fun seleccionarMaterialFiltrado() {
        val posicion = listaMateriales.indexOfFirst { it.idMaterial == idMaterialFiltrado }

        if (posicion >= 0) {
            binding.spinnerMaterial.setSelection(posicion + 1)
            binding.spinnerMaterial.isEnabled = false
            binding.textoTituloModulo.text = "Proveedores del material"
        }
    }

    /**
     * Selecciona automáticamente el proveedor recibido
     * y evita que pueda modificarse.
     */
    private fun seleccionarProveedorFiltrado() {
        val posicion = listaProveedores.indexOfFirst { it.idProveedor == idProveedorFiltrado }

        if (posicion >= 0) {
            binding.spinnerProveedor.setSelection(posicion + 1)
            binding.spinnerProveedor.isEnabled = false
            binding.textoTituloModulo.text = "Materiales del proveedor"
        }
    }

    /**
     * Configura los eventos de los botones de la pantalla.
     */
    private fun configurarEventos() {
        binding.botonAsociar.setOnClickListener {
            guardarRelacion()
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Obtiene la información ingresada por el usuario
     * y la envía al ViewModel para guardar la relación.
     */
    private fun guardarRelacion() {
        val idMaterial = obtenerIdMaterialSeleccionado()
        val idProveedor = obtenerIdProveedorSeleccionado()
        val precioReferencia = binding.campoPrecioReferencia.text.toString()

        materialProveedorViewModel.guardarRelacion(
            idMaterial = idMaterial,
            idProveedor = idProveedor,
            precioReferenciaTexto = precioReferencia
        )
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
     * Obtiene el identificador del proveedor seleccionado en el Spinner.
     */
    private fun obtenerIdProveedorSeleccionado(): Int {
        val posicion = binding.spinnerProveedor.selectedItemPosition

        if (posicion <= 0) {
            return 0
        }

        return listaProveedores[posicion - 1].idProveedor
    }

    /**
     * Carga las relaciones según el contexto desde el que fue abierta la pantalla.
     */
    private fun cargarRelacionesSegunOrigen() {
        when {
            idMaterialFiltrado != 0 -> {
                materialProveedorViewModel.cargarRelacionesPorMaterial(idMaterialFiltrado)
            }

            idProveedorFiltrado != 0 -> {
                materialProveedorViewModel.cargarRelacionesPorProveedor(idProveedorFiltrado)
            }

            else -> {
                materialProveedorViewModel.cargarRelaciones()
            }
        }
    }

    /**
     * Muestra un cuadro de confirmación antes de eliminar una relación.
     */
    private fun confirmarEliminacion(idRelacion: Int) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar relación")
            .setMessage("¿Seguro que quiere eliminar esta relación?")
            .setPositiveButton("Sí, eliminar") { _, _ ->
                materialProveedorViewModel.eliminarRelacion(idRelacion)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Restablece los controles del formulario después de guardar una relación.
     */
    private fun limpiarFormulario() {
        binding.campoPrecioReferencia.setText("")

        if (idMaterialFiltrado == 0) {
            binding.spinnerMaterial.setSelection(0)
        }

        if (idProveedorFiltrado == 0) {
            binding.spinnerProveedor.setSelection(0)
        }
    }
}