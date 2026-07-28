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

/**
 * Pantalla encargada de mostrar el listado de hojas de seguridad.
 * Permite consultar, registrar, editar y acceder al detalle de cada hoja.
 */
class HojaListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHojaListaBinding
    private lateinit var hojaViewModel: HojaSeguridadViewModel
    private lateinit var hojaAdapter: HojaSeguridadAdapter

    private var idMaterialFiltrado: Int = 0

    /**
     * Inicializa la pantalla y configura sus componentes.
     */
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

    /**
     * Actualiza la lista al regresar desde otra pantalla.
     */
    override fun onResume() {
        super.onResume()

        // Al volver de registrar/editar, refrescamos la lista.
        cargarHojasSegunOrigen()
    }

    /**
     * Inicializa el ViewModel que gestionará la información de la pantalla.
     */
    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = HojaSeguridadRepository(baseDatos.hojaSeguridadDao())
        val factory = HojaSeguridadViewModelFactory(repository)

        hojaViewModel = ViewModelProvider(this, factory)[HojaSeguridadViewModel::class.java]
    }

    /**
     * Configura el título y la descripción según el origen desde donde
     * fue abierta la pantalla.
     */
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

    /**
     * Configura el RecyclerView y su adaptador.
     */
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

    /**
     * Observa los cambios en la lista de hojas para actualizar la interfaz.
     */
    private fun observarHojas() {
        hojaViewModel.hojas.observe(this) { listaHojas ->
            hojaAdapter.actualizarLista(listaHojas)

            val listaVacia = listaHojas.isEmpty()
            binding.textoListaVacia.visibility = if (listaVacia) View.VISIBLE else View.GONE
            binding.recyclerHojas.visibility = if (listaVacia) View.GONE else View.VISIBLE
        }
    }

    /**
     * Configura los eventos de los botones de la pantalla.
     */
    private fun configurarEventos() {
        binding.botonAgregarHoja.setOnClickListener {
            abrirFormularioHoja(idMaterial = idMaterialFiltrado)
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Carga las hojas de seguridad según el contexto de la pantalla.
     * Si existe un material seleccionado, solo muestra sus hojas.
     */
    private fun cargarHojasSegunOrigen() {
        if (idMaterialFiltrado != 0) {
            hojaViewModel.cargarHojasPorMaterial(idMaterialFiltrado)
        } else {
            hojaViewModel.cargarHojas()
        }
    }

    /**
     * Abre el formulario para registrar o editar una hoja de seguridad.
     */
    private fun abrirFormularioHoja(
        idHoja: Int = 0,
        idMaterial: Int = 0
    ) {
        val intent = Intent(this, HojaFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_HOJA, idHoja)
        intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, idMaterial)
        startActivity(intent)
    }

    /**
     * Abre la pantalla con el detalle de una hoja de seguridad.
     */
    private fun abrirDetalleHoja(idHoja: Int) {
        val intent = Intent(this, HojaDetalleActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_HOJA, idHoja)
        startActivity(intent)
    }
}