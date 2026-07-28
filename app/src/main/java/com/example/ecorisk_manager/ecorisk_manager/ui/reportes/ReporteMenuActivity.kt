package com.example.ecorisk_manager.ui.reportes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.databinding.ActivityReporteMenuBinding

/**
 * Pantalla principal del módulo de reportes.
 * Desde aquí el usuario puede acceder a los diferentes
 * reportes disponibles en la aplicación.
 */
class ReporteMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReporteMenuBinding

    /**
     * Inicializa la pantalla y configura sus eventos.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReporteMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarEventos()
    }

    /**
     * Configura los eventos de los botones del menú.
     */
    private fun configurarEventos() {
        binding.botonReporteRiesgo.setOnClickListener {
            val intent = Intent(this, ReporteMaterialesRiesgoActivity::class.java)
            startActivity(intent)
        }

        binding.botonReporteIncidentes.setOnClickListener {
            val intent = Intent(this, ReporteHistorialIncidentesActivity::class.java)
            startActivity(intent)
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }
}