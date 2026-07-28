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

/**
 * Pantalla que muestra la información detallada de un proveedor.
 * También permite editar el registro o consultar los materiales asociados.
 */
class ProveedorDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProveedorDetalleBinding
    private lateinit var proveedorViewModel: ProveedorViewModel

    // Identificador del proveedor seleccionado.
    private var idProveedorActual: Int = 0

    // Guarda el proveedor cargado actualmente.
    private var proveedorActual: ProveedorEntity? = null

    /**
     * Inicializa la pantalla y carga la información del proveedor.
     */
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

    /**
     * Recarga la información cuando se regresa desde otra pantalla.
     */
    override fun onResume() {
        super.onResume()

        // Si volvemos de editar, se recargan los datos del proveedor.
        if (idProveedorActual != 0) {
            proveedorViewModel.cargarProveedorPorId(idProveedorActual)
        }
    }

    /**
     * Inicializa el ViewModel utilizado por la pantalla.
     */
    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val proveedorRepository = ProveedorRepository(baseDatos.proveedorDao())
        val factory = ProveedorViewModelFactory(proveedorRepository)

        proveedorViewModel = ViewModelProvider(this, factory)[ProveedorViewModel::class.java]
    }

    /**
     * Verifica que se haya recibido un proveedor válido antes de cargarlo.
     */
    private fun validarYCargarProveedor() {
        if (idProveedorActual == 0) {
            Toast.makeText(this, "No se recibió el proveedor seleccionado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        proveedorViewModel.cargarProveedorPorId(idProveedorActual)
    }

    /**
     * Observa los cambios del proveedor seleccionado para actualizar la interfaz.
     */
    private fun observarProveedor() {
        proveedorViewModel.proveedorSeleccionado.observe(this) { proveedor ->
            if (proveedor == null) {
                return@observe
            }

            proveedorActual = proveedor
            mostrarDatosProveedor(proveedor)
        }
    }

    /**
     * Muestra la información del proveedor en la pantalla.
     */
    private fun mostrarDatosProveedor(proveedor: ProveedorEntity) {
        binding.textoNombreProveedor.text = proveedor.nombre
        binding.textoTelefonoProveedor.text = "Teléfono: ${proveedor.telefono}"
        binding.textoCorreoProveedor.text = "Correo: ${proveedor.correo}"
        binding.textoContactoPrincipal.text = "Contacto principal: ${proveedor.contactoPrincipal}"
        binding.textoDireccionProveedor.text = "Dirección: ${proveedor.direccion}"
    }

    /**
     * Configura los eventos de los botones disponibles.
     */
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

    /**
     * Abre el formulario para editar el proveedor actual.
     */
    private fun abrirFormularioEdicion() {
        val intent = Intent(this, ProveedorFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_PROVEEDOR, idProveedorActual)
        startActivity(intent)
    }

    /**
     * Abre la pantalla que muestra los materiales asociados al proveedor.
     */
    private fun abrirMaterialesSuministrados() {
        val intent = Intent(this, MaterialProveedorActivity::class.java)

        // Se envía el proveedor para mostrar únicamente sus materiales relacionados.
        intent.putExtra(Constantes.Extras.EXTRA_ID_PROVEEDOR, idProveedorActual)

        startActivity(intent)
    }
}