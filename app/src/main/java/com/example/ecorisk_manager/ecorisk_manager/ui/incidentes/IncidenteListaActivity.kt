package com.example.ecorisk_manager.ui.incidentes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.databinding.ActivityIncidenteListaBinding

class IncidenteListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncidenteListaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIncidenteListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarPantalla()
        configurarEventos()
    }

    private fun configurarPantalla() {
        binding.textoTituloModulo.text = "Incidentes"
        binding.textoDescripcionModulo.text =
            "Aquí vamos a registrar y consultar incidentes relacionados con materiales peligrosos."

        // Luego aquí se conectan los incidentes reales y sus filtros.
        binding.textoEstadoModulo.text =
            "Pendiente: registrar incidentes, filtrar por severidad, estado y material."
    }

    private fun configurarEventos() {
        binding.botonVolver.setOnClickListener {
            finish()
        }
    }
}