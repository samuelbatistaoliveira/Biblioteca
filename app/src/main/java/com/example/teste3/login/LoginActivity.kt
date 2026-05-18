package com.example.teste3.login

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.teste3.R
import com.example.teste3.admin.HomeAdminActivity
import com.example.teste3.home_aluno.HomeActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvEsqueceuSenha = findViewById<TextView>(R.id.tvEsqueceuSenha)
        tvEsqueceuSenha.setOnClickListener {
            startActivity(Intent(this, RecuperarSenhaActivity::class.java))
        }

        val tvCadastro = findViewById<TextView>(R.id.tvCadastro)
        tvCadastro.setOnClickListener {
            startActivity(Intent(this, CadastrarContaActivity::class.java))
        }

        val btnEntrar = findViewById<MaterialButton>(R.id.btnEntrar)

        btnEntrar.setOnClickListener {

            val email = findViewById<EditText>(R.id.etEmail).text.toString().trim()
            val senha = findViewById<EditText>(R.id.etSenha).text.toString().trim()

            if (email.isNotEmpty() && senha.isNotEmpty()) {

                val auth = FirebaseAuth.getInstance()

                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {

                            // Verifica se é admin
                            if (email == "admin@escola.com") {

                                startActivity(
                                    Intent(this, HomeAdminActivity::class.java)
                                )

                            } else {

                                startActivity(
                                    Intent(this, HomeActivity::class.java)
                                )
                            }

                            finish()

                        } else {

                            Toast.makeText(
                                this,
                                "Email ou senha incorretos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            } else {

                Toast.makeText(
                    this,
                    "Preencha email e senha",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}