package com.example.teste3.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R
import com.example.teste3.admin.HomeAdminActivity
import com.example.teste3.home_aluno.HomeActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RecuperarSenhaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperacao)

        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val btnEnviarLink = findViewById<MaterialButton>(R.id.btnEnviarLink)

        btnEnviarLink.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Digite seu e-mail.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Link enviado para $email!", Toast.LENGTH_SHORT).show()

            // ✅ Admin vai para HomeAdminActivity, aluno vai para HomeActivity
            val destino = Intent(this, LoginActivity::class.java)
            destino.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(destino)
            finish()

            destino.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(destino)
            finish()
        }
    }
}