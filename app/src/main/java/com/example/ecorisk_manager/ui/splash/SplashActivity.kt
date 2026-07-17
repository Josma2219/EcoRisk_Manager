package com.example.ecorisk_manager.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.ecorisk_manager.R
import com.example.ecorisk_manager.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    // Tiempo que se muestra la pantalla de bienvenida antes de pasar al login
    private val duracionSplash: Long = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, duracionSplash)
    }
}