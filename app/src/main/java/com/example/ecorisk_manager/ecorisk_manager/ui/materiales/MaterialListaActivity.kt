package com.example.ecorisk_manager.ui.materiales

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecorisk_manager.R
import com.example.ecorisk_manager.adapter.MaterialAdapter
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.repository.MaterialRepository
import com.example.ecorisk_manager.databinding.ActivityMaterialListaBinding
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.MaterialViewModel
import com.example.ecorisk_manager.viewmodel.MaterialViewModelFactory

/**
 * Pantalla encargada de mostrar el listado de materiales peligrosos.
 * Permite consultar, buscar, filtrar, registrar, editar y acceder al detalle de cada material.
 */
class MaterialListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaterialListaBinding
    private lateinit var materialViewModel: MaterialViewModel
    private lateinit var materialAdapter: MaterialAdapter

    /**
     * Inicializa la pantalla y configura sus componentes.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaterialListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepararViewModel()
        configurarRecycler()
        configurarFiltroRiesgo()
        observarMateriales()
        configurarEventos()

        materialViewModel.cargarMateriales()
    }

    /**
     * Actualiza la lista al regresar desde otra pantalla.
     */
    override fun onResume() {
        super.onResume()

        // Refresca la información por si hubo registros o cambios.
        materialViewModel.cargarMateriales()
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
     * Configura el RecyclerView y su adaptador.
     */
    private fun configurarRecycler() {
        materialAdapter = MaterialAdapter(
            alVerDetalleMaterial = { material ->
                abrirDetalleMaterial(material.idMaterial)
            },
            alEditarMaterial = { material ->
                abrirFormularioMaterial(material.idMaterial)
            }
        )

        binding.recyclerMateriales.layoutManager = LinearLayoutManager(this)
        binding.recyclerMateriales.adapter = materialAdapter
    }

    /**
     * Configura el filtro por clasificación de riesgo.
     */
    private fun configurarFiltroRiesgo() {
        val adaptador = ArrayAdapter.createFromResource(
            this,
            R.array.clasificaciones_riesgo,
            android.R.layout.simple_spinner_item
        )

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFiltroRiesgo.adapter = adaptador
    }

    /**
     * Observa los cambios en la lista de materiales para actualizar la interfaz.
     */
    private fun observarMateriales() {
        materialViewModel.materiales.observe(this) { listaMateriales ->
            materialAdapter.actualizarLista(listaMateriales)

            val listaVacia = listaMateriales.isEmpty()
            binding.textoListaVacia.visibility = if (listaVacia) View.VISIBLE else View.GONE
            binding.recyclerMateriales.visibility = if (listaVacia) View.GONE else View.VISIBLE
        }
    }

    /**
     * Configura los eventos de los botones de la pantalla.
     */
    private fun configurarEventos() {
        binding.botonAgregarMaterial.setOnClickListener {
            abrirFormularioMaterial()
        }

        binding.botonBuscarMaterial.setOnClickListener {
            val textoBusqueda = binding.campoBuscarMaterial.text.toString()
            materialViewModel.buscarMateriales(textoBusqueda)
        }

        binding.botonLimpiarBusqueda.setOnClickListener {
            binding.campoBuscarMaterial.setText("")
            binding.spinnerFiltroRiesgo.setSelection(0)
            materialViewModel.cargarMateriales()
        }

        binding.botonFiltrarRiesgo.setOnClickListener {
            val clasificacion = binding.spinnerFiltroRiesgo.selectedItem.toString()

            if (clasificacion.startsWith("Seleccione")) {
                Toast.makeText(this, "Seleccione una clasificación", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            materialViewModel.filtrarPorRiesgo(clasificacion)
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Abre el formulario para registrar o editar un material.
     */
    private fun abrirFormularioMaterial(idMaterial: Int = 0) {
        val intent = Intent(this, MaterialFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, idMaterial)
        startActivity(intent)
    }

    /**
     * Abre la pantalla con el detalle de un material.
     */
    private fun abrirDetalleMaterial(idMaterial: Int) {
        val intent = Intent(this, MaterialDetalleActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, idMaterial)
        startActivity(intent)
    }
}