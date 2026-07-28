package com.example.ecorisk_manager.ui.incidentes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecorisk_manager.R
import com.example.ecorisk_manager.adapter.IncidenteAdapter
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.repository.IncidenteRepository
import com.example.ecorisk_manager.databinding.ActivityIncidenteListaBinding
import com.example.ecorisk_manager.utils.Constantes
import com.example.ecorisk_manager.viewmodel.IncidenteViewModel
import com.example.ecorisk_manager.viewmodel.IncidenteViewModelFactory

/**
 * Pantalla encargada de mostrar el listado de incidentes.
 * Permite consultar, filtrar, registrar, editar y acceder al detalle de cada incidente.
 */
class IncidenteListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncidenteListaBinding
    private lateinit var incidenteViewModel: IncidenteViewModel
    private lateinit var incidenteAdapter: IncidenteAdapter

    private var idMaterialFiltrado: Int = 0

    /**
     * Inicializa la pantalla y configura sus componentes.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIncidenteListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idMaterialFiltrado = intent.getIntExtra(Constantes.Extras.EXTRA_ID_MATERIAL, 0)

        prepararViewModel()
        configurarPantalla()
        configurarRecycler()
        configurarFiltros()
        observarIncidentes()
        configurarEventos()
        cargarIncidentesSegunOrigen()
    }

    /**
     * Actualiza la lista al regresar desde otra pantalla.
     */
    override fun onResume() {
        super.onResume()
        cargarIncidentesSegunOrigen()
    }

    /**
     * Inicializa el ViewModel que gestionará la información de la pantalla.
     */
    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = IncidenteRepository(baseDatos.incidenteDao())
        val factory = IncidenteViewModelFactory(repository)

        incidenteViewModel = ViewModelProvider(this, factory)[IncidenteViewModel::class.java]
    }

    /**
     * Configura el título y la descripción según el origen desde donde
     * fue abierta la pantalla.
     */
    private fun configurarPantalla() {
        if (idMaterialFiltrado != 0) {
            binding.textoTituloModulo.text = "Incidentes del material"
            binding.textoDescripcionModulo.text =
                "Incidentes registrados para el material seleccionado."

            // Si la pantalla se abrió desde un material, los filtros no son necesarios.
            binding.contenedorFiltros.visibility = View.GONE
        } else {
            binding.textoTituloModulo.text = "Incidentes"
            binding.textoDescripcionModulo.text =
                "Registro y seguimiento de incidentes relacionados con materiales peligrosos."
            binding.contenedorFiltros.visibility = View.VISIBLE
        }
    }

    /**
     * Configura el RecyclerView y su adaptador.
     */
    private fun configurarRecycler() {
        incidenteAdapter = IncidenteAdapter(
            alVerDetalleIncidente = { incidente ->
                abrirDetalleIncidente(incidente.idIncidente)
            },
            alEditarIncidente = { incidente ->
                abrirFormularioIncidente(
                    idIncidente = incidente.idIncidente,
                    idMaterial = incidente.idMaterial
                )
            }
        )

        binding.recyclerIncidentes.layoutManager = LinearLayoutManager(this)
        binding.recyclerIncidentes.adapter = incidenteAdapter
    }

    /**
     * Configura los filtros disponibles para la búsqueda de incidentes.
     */
    private fun configurarFiltros() {
        val adaptadorEstado = ArrayAdapter.createFromResource(
            this,
            R.array.estados_incidente,
            android.R.layout.simple_spinner_item
        )

        adaptadorEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFiltroEstado.adapter = adaptadorEstado

        val adaptadorSeveridad = ArrayAdapter.createFromResource(
            this,
            R.array.niveles_severidad,
            android.R.layout.simple_spinner_item
        )

        adaptadorSeveridad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFiltroSeveridad.adapter = adaptadorSeveridad
    }

    /**
     * Observa los cambios en la lista de incidentes para actualizar la interfaz.
     */
    private fun observarIncidentes() {
        incidenteViewModel.incidentes.observe(this) { listaIncidentes ->
            incidenteAdapter.actualizarLista(listaIncidentes)

            val listaVacia = listaIncidentes.isEmpty()
            binding.textoListaVacia.visibility = if (listaVacia) View.VISIBLE else View.GONE
            binding.recyclerIncidentes.visibility = if (listaVacia) View.GONE else View.VISIBLE
        }
    }

    /**
     * Configura los eventos de los botones de la pantalla.
     */
    private fun configurarEventos() {
        binding.botonAgregarIncidente.setOnClickListener {
            abrirFormularioIncidente(idMaterial = idMaterialFiltrado)
        }

        binding.botonFiltrarEstado.setOnClickListener {
            val estado = binding.spinnerFiltroEstado.selectedItem.toString()

            if (estado.startsWith("Seleccione")) {
                Toast.makeText(this, "Seleccione un estado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            incidenteViewModel.cargarIncidentesPorEstado(estado)
        }

        binding.botonFiltrarSeveridad.setOnClickListener {
            val severidad = binding.spinnerFiltroSeveridad.selectedItem.toString()

            if (severidad.startsWith("Seleccione")) {
                Toast.makeText(this, "Seleccione una severidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            incidenteViewModel.cargarIncidentesPorSeveridad(severidad)
        }

        binding.botonLimpiarFiltros.setOnClickListener {
            binding.spinnerFiltroEstado.setSelection(0)
            binding.spinnerFiltroSeveridad.setSelection(0)
            incidenteViewModel.cargarIncidentes()
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Carga los incidentes según el contexto de la pantalla.
     * Si existe un material seleccionado, solo muestra sus incidentes.
     */
    private fun cargarIncidentesSegunOrigen() {
        if (idMaterialFiltrado != 0) {
            incidenteViewModel.cargarIncidentesPorMaterial(idMaterialFiltrado)
        } else {
            incidenteViewModel.cargarIncidentes()
        }
    }

    /**
     * Abre el formulario para registrar o editar un incidente.
     */
    private fun abrirFormularioIncidente(
        idIncidente: Int = 0,
        idMaterial: Int = 0
    ) {
        val intent = Intent(this, IncidenteFormularioActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_INCIDENTE, idIncidente)
        intent.putExtra(Constantes.Extras.EXTRA_ID_MATERIAL, idMaterial)
        startActivity(intent)
    }

    /**
     * Abre la pantalla con el detalle de un incidente.
     */
    private fun abrirDetalleIncidente(idIncidente: Int) {
        val intent = Intent(this, IncidenteDetalleActivity::class.java)
        intent.putExtra(Constantes.Extras.EXTRA_ID_INCIDENTE, idIncidente)
        startActivity(intent)
    }
}