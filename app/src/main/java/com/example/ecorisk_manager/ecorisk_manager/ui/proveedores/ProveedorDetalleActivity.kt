package com.example.ecorisk_manager.ui.proveedores

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import com.example.ecorisk_manager.data.repository.ProveedorRepository
import com.example.ecorisk_manager.databinding.ActivityProveedorDetalleBinding
import com.example.ecorisk_manager.ui.materialproveedor.MaterialProveedorActivity
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.ProveedorViewModel
import com.example.ecorisk_manager.viewmodel.ProveedorViewModelFactory

class ProveedorDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProveedorDetalleBinding
    private lateinit var proveedorViewModel: ProveedorViewModel

    private var idProveedorActual: Int = 0
    private var proveedorActual: ProveedorEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProveedorDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idProveedorActual = intent.getIntExtra(Constantes.Extras.EXTRA_ID_PROVEEDOR, 0)

        prepararViewModel()
        observarProveedor()
        configurarEventos()

        validarYCargarProveedor()
    }

    override fun onResume() {
        super.onResume()

        // Si volvemos de editar, se recargan los datos del proveedor.
        if (idProveedorActual != 0) {
            proveedorViewModel.cargarProveedorPorId(idProveedorActual)
        }
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val proveedorRepository = ProveedorRepository(baseDatos.proveedorDao())
        val factory = ProveedorViewModelFactory(proveedorRepository)

        proveedorViewModel = ViewModelProvider(this, factory)[ProveedorViewModel::class.java]
    }

    private fun validarYCargarProveedor() {
        if (idProveedorActual == 0) {
            Toast.makeText(this, "No se recibió el proveedor seleccionado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        proveedorViewModel.cargarProveedorPorId(idProveedorActual)
    }

    private fun observarProveedor() {
        proveedorViewModel.proveedorSeleccionado.observe(this) { proveedor ->
            if (proveedor == null) {
                return@observe
            }

            proveedorActual = proveedor
            mostrarDatosProveedor(proveedor)
        }
    }

    private fun mostrarDatosProveedor(proveedor: ProveedorEntity) {
        binding.textoNombreProveedor.text = proveedor.nombre
        binding.textoTelefonoProveedor.text = "Teléfono: ${proveedor.telefono}"
        binding.textoCorreoProveedor.text = "Correo: ${proveedor.correo}"
        binding.textoContactoPrincipal.text = "Contacto principal: ${proveedor.contactoPrincipal}"
        binding.textoDireccionProveedor.text = "Dirección: ${proveedor.direccion}"
    }

    private fun configurarEventos() {
        binding.botonEditarProveedor.setOnClickListener {
            abrirFormularioEdicion()
        }

        binding.botonVerMateriales.setOnClickListener {
            abrirMaterialesSuministrados()
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    private fun abrirFormularioEdicion() {
        val intent = Intent(this, ProveedorFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_PROVEEDOR, idProveedorActual)
        startActivity(intent)
    }

    private fun abrirMaterialesSuministrados() {
        val intent = Intent(this, MaterialProveedorActivity::class.java)

        // Este id se usará cuando conectemos el módulo material-proveedor.
        intent.putExtra(Constantes.Extras.EXTRA_ID_PROVEEDOR, idProveedorActual)

        startActivity(intent)
    }
}