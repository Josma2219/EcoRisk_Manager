package com.example.ecorisk_manager.ui.hojas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.databinding.ActivityHojaListaBinding

class HojaListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHojaListaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHojaListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarPantalla()
        configurarEventos()
    }

    private fun configurarPantalla() {
        binding.textoTituloModulo.text = "Hojas de seguridad"
        binding.textoDescripcionModulo.text =
            "Aquí vamos a registrar y consultar las hojas de seguridad asociadas a cada material."

        // Luego aquí van las hojas reales que pertenecen a los materiales.
        binding.textoEstadoModulo.text =
            "Pendiente: conectar hojas de seguridad con materiales peligrosos."
    }

    private fun configurarEventos() {
        binding.botonVolver.setOnClickListener {
            finish()
        }
    }
}