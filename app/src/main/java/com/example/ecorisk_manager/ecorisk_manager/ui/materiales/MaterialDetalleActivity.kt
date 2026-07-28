package com.example.ecorisk_manager.ui.materiales

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.data.repository.MaterialRepository
import com.example.ecorisk_manager.databinding.ActivityMaterialDetalleBinding
import com.example.ecorisk_manager.ui.hojas.HojaListaActivity
import com.example.ecorisk_manager.ui.incidentes.IncidenteListaActivity
import com.example.ecorisk_manager.ui.materialproveedor.MaterialProveedorActivity
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.MaterialViewModel
import com.example.ecorisk_manager.viewmodel.MaterialViewModelFactory

/**
 * Pantalla encargada de mostrar la información detallada
 * de un material peligroso.
 */
class MaterialDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaterialDetalleBinding
    private lateinit var materialViewModel: MaterialViewModel

    private var idMaterialActual: Int = 0
    private var materialActual: MaterialPeligrosoEntity? = null

    /**
     * Inicializa la pantalla y carga la información del material seleccionado.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaterialDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idMaterialActual = intent.getIntExtra(Constantes.Extras.EXTRA_ID_MATERIAL, 0)

        prepararViewModel()
        observarMaterial()
        configurarEventos()

        validarYCargarMaterial()
    }

    /**
     * Actualiza la información al regresar desde la pantalla de edición.
     */
    override fun onResume() {
        super.onResume()

        // Recarga la información para mostrar los cambios realizados.
        if (idMaterialActual != 0) {
            materialViewModel.cargarMaterialPorId(idMaterialActual)
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
     * Verifica que se haya recibido un material válido
     * antes de solicitar su información.
     */
    private fun validarYCargarMaterial() {
        if (idMaterialActual == 0) {
            Toast.makeText(this, "No se recibió el material seleccionado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        materialViewModel.cargarMaterialPorId(idMaterialActual)
    }

    /**
     * Observa los cambios del ViewModel para actualizar la información mostrada.
     */
    private fun observarMaterial() {
        materialViewModel.materialSeleccionado.observe(this) { material ->
            if (material == null) {
                return@observe
            }

            materialActual = material
            mostrarDatosMaterial(material)
        }
    }

    /**
     * Muestra la información del material en la pantalla.
     */
    private fun mostrarDatosMaterial(material: MaterialPeligrosoEntity) {
        binding.textoNombreComercial.text = material.nombreComercial
        binding.textoCodigoMaterial.text = "Código: ${material.codigoMaterial}"
        binding.textoClasificacionRiesgo.text = "Clasificación: ${material.clasificacionRiesgo}"
        binding.textoUnidadMedida.text = "Unidad de medida: ${material.unidadMedida}"
        binding.textoFechaRegistro.text = "Fecha de registro: ${material.fechaRegistro}"
        binding.textoEstadoMaterial.text = "Estado: ${material.estado}"
        binding.textoDescripcionMaterial.text = material.descripcion
    }

    /**
     * Configura los eventos de los botones de la pantalla.
     */
    private fun configurarEventos() {
        binding.botonEditarMaterial.setOnClickListener {
            abrirFormularioEdicion()
        }

        binding.botonVerHojas.setOnClickListener {
            abrirPantallaRelacionada(HojaListaActivity::class.java)
        }

        binding.botonVerProveedores.setOnClickListener {
            abrirPantallaRelacionada(MaterialProveedorActivity::class.java)
        }

        binding.botonVerIncidentes.setOnClickListener {
            abrirPantallaRelacionada(IncidenteListaActivity::class.java)
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Abre el formulario para editar el material actual.
     */
    private fun abrirFormularioEdicion() {
        val intent = Intent(this, MaterialFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, idMaterialActual)
        startActivity(intent)
    }

    /**
     * Abre una pantalla relacionada con el material seleccionado.
     */
    private fun abrirPantallaRelacionada(pantalla: Class<*>) {
        val intent = Intent(this, pantalla)

        // Envía el identificador del material para mostrar únicamente
        // la información relacionada con él.
        intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, idMaterialActual)

        startActivity(intent)
    }
}