package com.example.ecorisk_manager.ui.proveedores

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import com.example.ecorisk_manager.data.repository.ProveedorRepository
import com.example.ecorisk_manager.databinding.ActivityProveedorFormularioBinding
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.ProveedorViewModel
import com.example.ecorisk_manager.viewmodel.ProveedorViewModelFactory

class ProveedorFormularioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProveedorFormularioBinding
    private lateinit var proveedorViewModel: ProveedorViewModel

    private var idProveedorActual: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProveedorFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idProveedorActual = intent.getIntExtra(Constantes.Extras.EXTRA_ID_PROVEEDOR, 0)

        prepararViewModel()
        configurarPantalla()
        observarDatos()
        configurarEventos()

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

    private fun configurarPantalla() {
        if (idProveedorActual == 0) {
            binding.textoTituloFormulario.text = "Registrar proveedor"
            binding.botonGuardarProveedor.text = "Guardar"
        } else {
            binding.textoTituloFormulario.text = "Editar proveedor"
            binding.botonGuardarProveedor.text = "Actualizar"
        }
    }

    private fun observarDatos() {
        proveedorViewModel.proveedorSeleccionado.observe(this) { proveedor ->
            if (proveedor != null) {
                llenarFormulario(proveedor)
            }
        }

        proveedorViewModel.resultadoOperacion.observe(this) { resultado ->
            if (resultado == null) return@observe

            Toast.makeText(this, resultado.mensaje, Toast.LENGTH_SHORT).show()

            if (resultado.exitoso) {
                finish()
            }

            proveedorViewModel.limpiarResultadoOperacion()
        }
    }

    private fun configurarEventos() {
        binding.botonGuardarProveedor.setOnClickListener {
            guardarProveedor()
        }

        binding.botonCancelar.setOnClickListener {
            finish()
        }
    }

    private fun guardarProveedor() {
        val nombre = binding.campoNombreProveedor.text.toString()
        val telefono = binding.campoTelefonoProveedor.text.toString()
        val correo = binding.campoCorreoProveedor.text.toString()
        val direccion = binding.campoDireccionProveedor.text.toString()
        val contactoPrincipal = binding.campoContactoPrincipal.text.toString()

        proveedorViewModel.guardarProveedor(
            idProveedor = idProveedorActual,
            nombre = nombre,
            telefono = telefono,
            correo = correo,
            direccion = direccion,
            contactoPrincipal = contactoPrincipal
        )
    }

    private fun llenarFormulario(proveedor: ProveedorEntity) {
        binding.campoNombreProveedor.setText(proveedor.nombre)
        binding.campoTelefonoProveedor.setText(proveedor.telefono)
        binding.campoCorreoProveedor.setText(proveedor.correo)
        binding.campoDireccionProveedor.setText(proveedor.direccion)
        binding.campoContactoPrincipal.setText(proveedor.contactoPrincipal)
    }
}