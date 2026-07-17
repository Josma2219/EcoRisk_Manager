package com.example.ecorisk_manager.ui.respaldo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.databinding.ActivityRespaldoBinding

class RespaldoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRespaldoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRespaldoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarEventos()
    }

    private fun configurarEventos() {
        binding.botonGenerarRespaldo.setOnClickListener {
            Toast.makeText(this, "Luego generamos respaldo de los datos", Toast.LENGTH_SHORT).show()
        }

        binding.botonRestaurarRespaldo.setOnClickListener {
            Toast.makeText(this, "Luego restauramos información desde respaldo", Toast.LENGTH_SHORT).show()
        }

        binding.botonVolver.setOnClickListener {
            finish()
        }
    }
}