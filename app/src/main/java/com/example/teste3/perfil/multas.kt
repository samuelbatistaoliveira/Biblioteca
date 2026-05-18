package com.example.teste3.perfil

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R
import com.example.teste3.databinding.ActivityMultasBinding
import com.example.teste3.home_aluno.HomeActivity
import com.example.teste3.login.ChatbotActivity

class multas : AppCompatActivity() {

    private lateinit var binding: ActivityMultasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        binding.navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }

        binding.navChat.setOnClickListener {
            startActivity(Intent(this, ChatbotActivity::class.java))
        }

        binding.navReservas.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.salas.Disponivel::class.java))
        }

        binding.navSalas.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.mapa.MapaPrincipal::class.java))
        }

        binding.navPerfil.setOnClickListener {
            startActivity(Intent(this, PrincipalPerfil::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
    }
}