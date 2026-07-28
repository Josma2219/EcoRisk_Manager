package com.example.ecorisk_manager.ui.respaldo

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.repository.RespaldoRepository
import com.example.ecorisk_manager.databinding.ActivityRespaldoBinding
import com.example.ecorisk_manager.viewmodel.RespaldoViewModel
import com.example.ecorisk_manager.viewmodel.RespaldoViewModelFactory

/**
 * Pantalla encargada de generar y restaurar respaldos
 * de la información almacenada en la aplicación.
 */
class RespaldoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRespaldoBinding
    private lateinit var respaldoViewModel: RespaldoViewModel

    /**
     * Inicializa la pantalla y prepara sus componentes.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRespaldoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepararViewModel()
        observarResultado()
        configurarEventos()
    }

    /**
     * Inicializa el ViewModel utilizado para gestionar
     * las operaciones de respaldo y restauración.
     */
    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = RespaldoRepository(baseDatos)
        val factory = RespaldoViewModelFactory(repository)

        respaldoViewModel = ViewModelProvider(this, factory)[RespaldoViewModel::class.java]
    }

    /**
     * Observa el resultado de las operaciones realizadas
     * para actualizar la interfaz y mostrar mensajes al usuario.
     */
    private fun observarResultado() {
        respaldoViewModel.resultadoOperacion.observe(this) { resultado ->
            if (resultado == null) return@observe

            binding.textoEstadoRespaldo.text = resultado.mensaje
            Toast.makeText(this, resultado.mensaje, Toast.LENGTH_LONG).show()

            respaldoViewModel.limpiarResultadoOperacion()
        }
    }

    /**
     * Configura los eventos de los botones de la pantalla.
     */
    private fun configurarEventos() {
        binding.botonGenerarRespaldo.setOnClickListener {
            binding.textoEstadoRespaldo.text = "Generando respaldo..."
            respaldoViewModel.generarRespaldo(this)
        }

        binding.botonRestaurarRespaldo.setOnClickListener {
            confirmarRestauracion()
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Solicita confirmación antes de restaurar el último respaldo disponible.
     */
    private fun confirmarRestauracion() {
        AlertDialog.Builder(this)
            .setTitle("Restaurar respaldo")
            .setMessage("Esto reemplazará los datos actuales con el último respaldo generado. ¿Desea continuar?")
            .setPositiveButton("Sí, restaurar") { _, _ ->
                binding.textoEstadoRespaldo.text = "Restaurando respaldo..."
                respaldoViewModel.restaurarUltimoRespaldo(this)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}