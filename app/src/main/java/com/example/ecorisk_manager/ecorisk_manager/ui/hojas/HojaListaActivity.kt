package com.example.ecorisk_manager.ui.hojas

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecorisk_manager.adapter.HojaSeguridadAdapter
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.repository.HojaSeguridadRepository
import com.example.ecorisk_manager.databinding.ActivityHojaListaBinding
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.HojaSeguridadViewModel
import com.example.ecorisk_manager.viewmodel.HojaSeguridadViewModelFactory

class HojaListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHojaListaBinding
    private lateinit var hojaViewModel: HojaSeguridadViewModel
    private lateinit var hojaAdapter: HojaSeguridadAdapter

    private var idMaterialFiltrado: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHojaListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idMaterialFiltrado = intent.getIntExtra(Constantes.Extras.EXTRA_ID_MATERIAL, 0)

        prepararViewModel()
        configurarPantalla()
        configurarRecycler()
        observarHojas()
        configurarEventos()
        cargarHojasSegunOrigen()
    }

    override fun onResume() {
        super.onResume()

        // Al volver de registrar/editar, refrescamos la lista.
        cargarHojasSegunOrigen()
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = HojaSeguridadRepository(baseDatos.hojaSeguridadDao())
        val factory = HojaSeguridadViewModelFactory(repository)

        hojaViewModel = ViewModelProvider(this, factory)[HojaSeguridadViewModel::class.java]
    }

    private fun configurarPantalla() {
        if (idMaterialFiltrado != 0) {
            binding.textoTituloModulo.text = "Hojas del material"
            binding.textoDescripcionModulo.text =
                "Hojas de seguridad asociadas al material seleccionado."
        } else {
            binding.textoTituloModulo.text = "Hojas de seguridad"
            binding.textoDescripcionModulo.text =
                "Registro y consulta de hojas de seguridad asociadas a materiales."
        }
    }

    private fun configurarRecycler() {
        hojaAdapter = HojaSeguridadAdapter(
            alVerDetalleHoja = { hoja ->
                abrirDetalleHoja(hoja.idHoja)
            },
            alEditarHoja = { hoja ->
                abrirFormularioHoja(hoja.idHoja, hoja.idMaterial)
            }
        )

        binding.recyclerHojas.layoutManager = LinearLayoutManager(this)
        binding.recyclerHojas.adapter = hojaAdapter
    }

    private fun observarHojas() {
        hojaViewModel.hojas.observe(this) { listaHojas ->
            hojaAdapter.actualizarLista(listaHojas)

            val listaVacia = listaHojas.isEmpty()
            binding.textoListaVacia.visibility = if (listaVacia) View.VISIBLE else View.GONE
            binding.recyclerHojas.visibility = if (listaVacia) View.GONE else View.VISIBLE
        }
    }

    private fun configurarEventos() {
        binding.botonAgregarHoja.setOnClickListener {
            abrirFormularioHoja(idMaterial = idMaterialFiltrado)
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    private fun cargarHojasSegunOrigen() {
        if (idMaterialFiltrado != 0) {
            hojaViewModel.cargarHojasPorMaterial(idMaterialFiltrado)
        } else {
            hojaViewModel.cargarHojas()
        }
    }

    private fun abrirFormularioHoja(
        idHoja: Int = 0,
        idMaterial: Int = 0
    ) {
        val intent = Intent(this, HojaFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_HOJA, idHoja)
        intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, idMaterial)
        startActivity(intent)
    }

    private fun abrirDetalleHoja(idHoja: Int) {
        val intent = Intent(this, HojaDetalleActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_HOJA, idHoja)
        startActivity(intent)
    }
}