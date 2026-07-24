package com.example.ecorisk_manager.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.databinding.ActivitySplashBinding
import com.example.ecorisk_manager.ui.home.HomeActivity
import com.example.ecorisk_manager.ui.login.LoginActivity
import com.example.ecorisk_manager.utils.SessionManager

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sessionManager: SessionManager

    // Tiempo corto para que se vea la pantalla, pero sin hacer esperar demasiado.
    private val duracionSplash: Long = 1400

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            abrirPantallaSiguiente()
        }, duracionSplash)
    }

    private fun abrirPantallaSiguiente() {
        val pantallaDestino = if (sessionManager.haySesionActiva()) {
            HomeActivity::class.java
        } else {
            LoginActivity::class.java
        }

        startActivity(Intent(this, pantallaDestino))
        finish()
    }
}