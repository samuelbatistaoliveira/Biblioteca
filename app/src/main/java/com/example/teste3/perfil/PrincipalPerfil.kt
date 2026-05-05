package com.example.teste3.perfil

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R
import com.example.teste3.home_aluno.HomeActivity
import com.example.teste3.login.ChatbotActivity

class PrincipalPerfil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Bottom Nav
        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }

        findViewById<LinearLayout>(R.id.navChat).setOnClickListener {
            startActivity(Intent(this, ChatbotActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.navReservas).setOnClickListener {
            startActivity(Intent(this, com.example.teste3.salas.Disponivel::class.java))
        }

        findViewById<LinearLayout>(R.id.navSalas).setOnClickListener {
            startActivity(Intent(this, com.example.teste3.mapa.MapaPrincipal::class.java))
        }

        // navPerfil não faz nada — já está na tela de perfil
        findViewById<LinearLayout>(R.id.navPerfil).setOnClickListener { }

        // Botões do perfil
        findViewById<FrameLayout>(R.id.btnEditarPerfil).setOnClickListener {
            startActivity(Intent(this, EditarPerfil::class.java))
        }

        findViewById<FrameLayout>(R.id.btnConfiguracoes).setOnClickListener {
            startActivity(Intent(this, configuracoes::class.java))
        }

        findViewById<FrameLayout>(R.id.btnMultas).setOnClickListener {
            startActivity(Intent(this, multas::class.java))
        }

        findViewById<ImageView>(R.id.ivArrowBook1).setOnClickListener {
            startActivity(Intent(this, multas::class.java))
        }
    }
}