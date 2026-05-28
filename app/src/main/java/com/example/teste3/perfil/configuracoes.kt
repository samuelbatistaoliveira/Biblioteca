package com.example.teste3.perfil

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R
import com.example.teste3.login.ChatbotActivity
import com.example.teste3.login.LoginActivity

class configuracoes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        val itemMeuPerfil = findViewById<RelativeLayout>(R.id.itemMeuPerfil)
        itemMeuPerfil.setOnClickListener {
            startActivity(Intent(this, PrincipalPerfil::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }

        val itemAlterarSenha = findViewById<RelativeLayout>(R.id.itemAlterarSenha)
        itemAlterarSenha.setOnClickListener {
            startActivity(Intent(this, AlterarSenhaActivity::class.java))
        }

        val itemAjuda = findViewById<RelativeLayout>(R.id.itemAjuda)
        itemAjuda.setOnClickListener {
            startActivity(Intent(this, ChatbotActivity::class.java))
        }

        val itemSair = findViewById<RelativeLayout>(R.id.itemSair)
        itemSair.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
    }
}