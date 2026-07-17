package com.example.ecorisk_manager.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.R
import com.example.ecorisk_manager.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val botonEntrar = findViewById<Button>(R.id.botonEntrar)
        botonEntrar.setOnClickListener {
            // Por ahora navega directo, sin validar usuario.
            // La validación real se conecta en la Etapa 6.
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}