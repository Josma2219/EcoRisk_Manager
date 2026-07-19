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

class RespaldoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRespaldoBinding
    private lateinit var respaldoViewModel: RespaldoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRespaldoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepararViewModel()
        observarResultado()
        configurarEventos()
    }

    private fun prepararViewModel() {
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val repository = RespaldoRepository(baseDatos)
        val factory = RespaldoViewModelFactory(repository)

        respaldoViewModel = ViewModelProvider(this, factory)[RespaldoViewModel::class.java]
    }

    private fun observarResultado() {
        respaldoViewModel.resultadoOperacion.observe(this) { resultado ->
            if (resultado == null) return@observe

            binding.textoEstadoRespaldo.text = resultado.mensaje
            Toast.makeText(this, resultado.mensaje, Toast.LENGTH_LONG).show()

            respaldoViewModel.limpiarResultadoOperacion()
        }
    }

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