package com.example.ecorisk_manager.ui.proveedores

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecorisk_manager.adapter.ProveedorAdapter
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.repository.ProveedorRepository
import com.example.ecorisk_manager.databinding.ActivityProveedorListaBinding
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.ProveedorViewModel
import com.example.ecorisk_manager.viewmodel.ProveedorViewModelFactory

class ProveedorListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProveedorListaBinding
    private lateinit var proveedorViewModel: ProveedorViewModel
    private lateinit var proveedorAdapter: ProveedorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProveedorListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepararViewModel()
        configurarRecycler()
        observarProveedores()
        configurarEventos()

        proveedorViewModel.cargarProveedores()
    }

    override fun onResume() {
        super.onResume()

        // Al volver del formulario o detalle, refrescamos la lista.
        proveedorViewModel.cargarProveedores()
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val proveedorRepository = ProveedorRepository(baseDatos.proveedorDao())
        val factory = ProveedorViewModelFactory(proveedorRepository)

        proveedorViewModel = ViewModelProvider(this, factory)[ProveedorViewModel::class.java]
    }

    private fun configurarRecycler() {
        proveedorAdapter = ProveedorAdapter(
            alVerDetalleProveedor = { proveedor ->
                abrirDetalleProveedor(proveedor.idProveedor)
            },
            alEditarProveedor = { proveedor ->
                abrirFormularioProveedor(proveedor.idProveedor)
            }
        )

        binding.recyclerProveedores.layoutManager = LinearLayoutManager(this)
        binding.recyclerProveedores.adapter = proveedorAdapter
    }

    private fun observarProveedores() {
        proveedorViewModel.proveedores.observe(this) { listaProveedores ->
            proveedorAdapter.actualizarLista(listaProveedores)

            val listaVacia = listaProveedores.isEmpty()
            binding.textoListaVacia.visibility = if (listaVacia) View.VISIBLE else View.GONE
            binding.recyclerProveedores.visibility = if (listaVacia) View.GONE else View.VISIBLE
        }
    }

    private fun configurarEventos() {
        binding.botonAgregarProveedor.setOnClickListener {
            abrirFormularioProveedor()
        }

        binding.botonBuscarProveedor.setOnClickListener {
            val textoBusqueda = binding.campoBuscarProveedor.text.toString()
            proveedorViewModel.buscarProveedores(textoBusqueda)
        }

        binding.botonLimpiarBusqueda.setOnClickListener {
            binding.campoBuscarProveedor.setText("")
            proveedorViewModel.cargarProveedores()
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    private fun abrirFormularioProveedor(idProveedor: Int = 0) {
        val intent = Intent(this, ProveedorFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_PROVEEDOR, idProveedor)
        startActivity(intent)
    }

    private fun abrirDetalleProveedor(idProveedor: Int) {
        val intent = Intent(this, ProveedorDetalleActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_PROVEEDOR, idProveedor)
        startActivity(intent)
    }
}