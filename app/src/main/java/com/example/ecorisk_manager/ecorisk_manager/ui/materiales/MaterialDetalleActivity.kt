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

class MaterialDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaterialDetalleBinding
    private lateinit var materialViewModel: MaterialViewModel

    private var idMaterialActual: Int = 0
    private var materialActual: MaterialPeligrosoEntity? = null

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

    override fun onResume() {
        super.onResume()

        // Si volvemos de editar, recargamos para enseñar los datos actualizados.
        if (idMaterialActual != 0) {
            materialViewModel.cargarMaterialPorId(idMaterialActual)
        }
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val materialRepository = MaterialRepository(baseDatos.materialPeligrosoDao())
        val factory = MaterialViewModelFactory(materialRepository)

        materialViewModel = ViewModelProvider(this, factory)[MaterialViewModel::class.java]
    }

    private fun validarYCargarMaterial() {
        if (idMaterialActual == 0) {
            Toast.makeText(this, "No se recibió el material seleccionado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        materialViewModel.cargarMaterialPorId(idMaterialActual)
    }

    private fun observarMaterial() {
        materialViewModel.materialSeleccionado.observe(this) { material ->
            if (material == null) {
                return@observe
            }

            materialActual = material
            mostrarDatosMaterial(material)
        }
    }

    private fun mostrarDatosMaterial(material: MaterialPeligrosoEntity) {
        binding.textoNombreComercial.text = material.nombreComercial
        binding.textoCodigoMaterial.text = "Código: ${material.codigoMaterial}"
        binding.textoClasificacionRiesgo.text = "Clasificación: ${material.clasificacionRiesgo}"
        binding.textoUnidadMedida.text = "Unidad de medida: ${material.unidadMedida}"
        binding.textoFechaRegistro.text = "Fecha de registro: ${material.fechaRegistro}"
        binding.textoEstadoMaterial.text = "Estado: ${material.estado}"
        binding.textoDescripcionMaterial.text = material.descripcion
    }

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

    private fun abrirFormularioEdicion() {
        val intent = Intent(this, MaterialFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, idMaterialActual)
        startActivity(intent)
    }

    private fun abrirPantallaRelacionada(pantalla: Class<*>) {
        val intent = Intent(this, pantalla)

        // Guardamos el id para que las próximas etapas puedan filtrar por material.
        intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, idMaterialActual)

        startActivity(intent)
    }
}