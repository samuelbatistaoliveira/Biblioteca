package com.example.teste3.login

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CadastrarContaActivity : AppCompatActivity() {

    // Instanciando o banco
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cadastro)

        db = FirebaseFirestore.getInstance()

        // Campos
        val etNome = findViewById<TextInputEditText>(R.id.etNome)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etSenha = findViewById<TextInputEditText>(R.id.etSenha)

        // Layouts
        val layoutNome = findViewById<TextInputLayout>(R.id.layoutNome)
        val layoutEmail = findViewById<TextInputLayout>(R.id.layoutEmail)
        val layoutSenha = findViewById<TextInputLayout>(R.id.layoutSenha)

        // Botão cadastrar
        val btnCadastrar = findViewById<MaterialButton>(R.id.btnCadastrar)

        btnCadastrar.setOnClickListener {

            val nome = etNome.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val senha = etSenha.text.toString().trim()

            // Limpa erros
            layoutNome.error = null
            layoutEmail.error = null
            layoutSenha.error = null

            var valido = true

            // Validação nome
            if (nome.isEmpty()) {
                layoutNome.error = "Informe seu nome completo"
                valido = false
            }

            // Validação email
            if (email.isEmpty()) {
                layoutEmail.error = "Informe seu e-mail"
                valido = false

            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                layoutEmail.error = "E-mail inválido"
                valido = false
            }

            // Validação senha
            if (senha.isEmpty()) {

                layoutSenha.error = "Informe uma senha"
                valido = false

            } else if (senha.length < 6) {

                layoutSenha.error = "A senha deve ter no mínimo 6 caracteres"
                valido = false
            }

            //Cadastro
            if (valido) {

                val auth = FirebaseAuth.getInstance()

                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {

                            val usuario = hashMapOf(
                                "nome" to nome,
                                "email" to email
                            )

                            db.collection("usuarios")
                                .add(usuario)
                                .addOnSuccessListener {

                                    Toast.makeText(
                                        this,
                                        "Cadastro realizado com sucesso",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    startActivity(
                                        Intent(this, LoginActivity::class.java)
                                    )

                                    finish()
                                }

                                .addOnFailureListener {

                                    Toast.makeText(
                                        this,
                                        "Erro ao salvar dados",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                        } else {

                            Toast.makeText(
                                this,
                                "Erro ao cadastrar usuário",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        // Já possui conta
        val tvJaPossuiConta = findViewById<TextView>(R.id.tvJaPossuiConta)

        tvJaPossuiConta.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
