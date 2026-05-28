package com.example.teste3.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R
import com.example.teste3.admin.perfiladm
import com.example.teste3.admin.AlterarSenhaActivity
import com.example.teste3.login.LoginActivity

class configadm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_configadm)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            val intent = Intent(this, perfiladm::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        val itemMeuPerfil = findViewById<RelativeLayout>(R.id.itemMeuPerfil)
        itemMeuPerfil.setOnClickListener {
            val intent = Intent(this, perfiladm::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        val itemAlterarSenha = findViewById<RelativeLayout>(R.id.itemAlterarSenha)
        itemAlterarSenha.setOnClickListener {
            startActivity(Intent(this, AlterarSenhaActivity::class.java))
        }

        val itemSair = findViewById<RelativeLayout>(R.id.itemSair)
        itemSair.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}