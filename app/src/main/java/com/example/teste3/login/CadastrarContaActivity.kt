package com.example.teste3.login

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CadastrarContaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        // Campos
        val etNome = findViewById<TextInputEditText>(R.id.etNome)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etSenha = findViewById<TextInputEditText>(R.id.etSenha)

        // Layouts (para mostrar erros)
        val layoutNome = findViewById<TextInputLayout>(R.id.layoutNome)
        val layoutEmail = findViewById<TextInputLayout>(R.id.layoutEmail)
        val layoutSenha = findViewById<TextInputLayout>(R.id.layoutSenha)

        // Botão Cadastrar
        val btnCadastrar = findViewById<MaterialButton>(R.id.btnCadastrar)
        btnCadastrar.setOnClickListener {
            val nome = etNome.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val senha = etSenha.text.toString().trim()

            // Limpa erros anteriores
            layoutNome.error = null
            layoutEmail.error = null
            layoutSenha.error = null

            var valido = true

            // Validação do Nome
            if (nome.isEmpty()) {
                layoutNome.error = "Informe seu nome completo"
                valido = false
            }

            // Validação do Email
            if (email.isEmpty()) {
                layoutEmail.error = "Informe seu e-mail"
                valido = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                layoutEmail.error = "E-mail inválido"
                valido = false
            }

            // Validação da Senha
            if (senha.isEmpty()) {
                layoutSenha.error = "Informe uma senha"
                valido = false
            } else if (senha.length < 6) {
                layoutSenha.error = "A senha deve ter no mínimo 6 caracteres"
                valido = false
            }

            // Se tudo válido, vai para o Login
            if (valido) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        // Link "Já possui uma conta?"
        val tvJaPossuiConta = findViewById<TextView>(R.id.tvJaPossuiConta)
        tvJaPossuiConta.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}