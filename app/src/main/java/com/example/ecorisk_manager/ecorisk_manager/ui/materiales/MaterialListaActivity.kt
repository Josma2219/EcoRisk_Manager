package com.example.ecorisk_manager.ui.materiales

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.databinding.ActivityMaterialListaBinding

class MaterialListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaterialListaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaterialListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarPantalla()
        configurarEventos()
    }

    private fun configurarPantalla() {
        binding.textoTituloModulo.text = "Materiales peligrosos"
        binding.textoDescripcionModulo.text =
            "Aquí vamos a listar, registrar, editar y consultar materiales peligrosos."

        // Estos textos son temporales. Luego los cambiamos por datos reales desde Room.
        binding.textoEstadoModulo.text =
            "Pendiente: conectar lista de materiales, buscador y formulario de registro."
    }

    private fun configurarEventos() {
        binding.botonVolver.setOnClickListener {
            finish()
        }
    }
}