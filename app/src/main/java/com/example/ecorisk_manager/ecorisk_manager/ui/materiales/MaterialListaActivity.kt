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

class MaterialListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaterialListaBinding
    private lateinit var materialViewModel: MaterialViewModel
    private lateinit var materialAdapter: MaterialAdapter

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

    override fun onResume() {
        super.onResume()

        // Al volver del formulario o detalle, refrescamos la lista.
        materialViewModel.cargarMateriales()
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val materialRepository = MaterialRepository(baseDatos.materialPeligrosoDao())
        val factory = MaterialViewModelFactory(materialRepository)

        materialViewModel = ViewModelProvider(this, factory)[MaterialViewModel::class.java]
    }

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

    private fun configurarFiltroRiesgo() {
        val adaptador = ArrayAdapter.createFromResource(
            this,
            R.array.clasificaciones_riesgo,
            android.R.layout.simple_spinner_item
        )

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFiltroRiesgo.adapter = adaptador
    }

    private fun observarMateriales() {
        materialViewModel.materiales.observe(this) { listaMateriales ->
            materialAdapter.actualizarLista(listaMateriales)

            val listaVacia = listaMateriales.isEmpty()
            binding.textoListaVacia.visibility = if (listaVacia) View.VISIBLE else View.GONE
            binding.recyclerMateriales.visibility = if (listaVacia) View.GONE else View.VISIBLE
        }
    }

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

    private fun abrirFormularioMaterial(idMaterial: Int = 0) {
        val intent = Intent(this, MaterialFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, idMaterial)
        startActivity(intent)
    }

    private fun abrirDetalleMaterial(idMaterial: Int) {
        val intent = Intent(this, MaterialDetalleActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, idMaterial)
        startActivity(intent)
    }
}