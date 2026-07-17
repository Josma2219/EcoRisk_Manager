package com.example.ecorisk_manager.ui.reportes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.databinding.ActivityReporteMenuBinding

class ReporteMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReporteMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReporteMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarEventos()
    }

    private fun configurarEventos() {
        binding.botonReporteRiesgo.setOnClickListener {
            Toast.makeText(this, "Luego abrimos el reporte por categoría de riesgo", Toast.LENGTH_SHORT).show()
        }

        binding.botonReporteIncidentes.setOnClickListener {
            Toast.makeText(this, "Luego abrimos el historial de incidentes", Toast.LENGTH_SHORT).show()
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }
}