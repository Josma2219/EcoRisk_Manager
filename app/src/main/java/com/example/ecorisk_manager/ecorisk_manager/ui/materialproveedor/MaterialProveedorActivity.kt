package com.example.ecorisk_manager.ui.materialproveedor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.databinding.ActivityMaterialProveedorBinding

class MaterialProveedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaterialProveedorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaterialProveedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarPantalla()
        configurarEventos()
    }

    private fun configurarPantalla() {
        binding.textoTituloModulo.text = "Material por proveedor"
        binding.textoDescripcionModulo.text =
            "Aquí vamos a asociar materiales peligrosos con sus proveedores."

        // Este módulo conecta dos tablas: materiales y proveedores.
        binding.textoEstadoModulo.text =
            "Pendiente: seleccionar material, seleccionar proveedor y registrar precio de referencia."
    }

    private fun configurarEventos() {
        binding.botonVolver.setOnClickListener {
            finish()
        }
    }
}