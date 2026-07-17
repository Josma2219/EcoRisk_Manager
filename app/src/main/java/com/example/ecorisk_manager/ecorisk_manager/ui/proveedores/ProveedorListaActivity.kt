package com.example.ecorisk_manager.ui.proveedores

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.databinding.ActivityProveedorListaBinding

class ProveedorListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProveedorListaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProveedorListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarPantalla()
        configurarEventos()
    }

    private fun configurarPantalla() {
        binding.textoTituloModulo.text = "Proveedores"
        binding.textoDescripcionModulo.text =
            "Aquí vamos a gestionar los proveedores que suministran materiales peligrosos."

        // Luego aquí se conectan proveedores reales desde la base de datos.
        binding.textoEstadoModulo.text =
            "Pendiente: conectar registro, lista, detalle y edición de proveedores."
    }

    private fun configurarEventos() {
        binding.botonVolver.setOnClickListener {
            finish()
        }
    }
}