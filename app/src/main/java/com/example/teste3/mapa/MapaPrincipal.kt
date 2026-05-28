package com.example.teste3.mapa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MapaPrincipal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val origem = intent.getStringExtra("origem") ?: "aluno"

        val intent = Intent(this, MapaLivroActivity::class.java).apply {
            putExtra("andar", 0)
            putExtra("ponto_x", 0.65f)
            putExtra("ponto_y", 0.30f)
            putExtra("localizacao_texto", "Ciências — Estante 3, Prat. B")
            putExtra("origem", origem)
        }
        startActivity(intent)
        finish()
    }
}