// alterarsenha.kt
package com.example.teste3.perfil

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.teste3.R


class AlterarSenhaActivity : AppCompatActivity() {

    private lateinit var etSenhaAtual: EditText
    private lateinit var etNovaSenha: EditText
    private lateinit var etConfirmarSenha: EditText

    private lateinit var ivToggleSenhaAtual: ImageView
    private lateinit var ivToggleNovaSenha: ImageView
    private lateinit var ivToggleConfirmar: ImageView

    private lateinit var tvErroSenha: TextView
    private lateinit var btnSalvar: Button
    private lateinit var btnVoltar: ImageButton

    private lateinit var barForca1: View
    private lateinit var barForca2: View
    private lateinit var barForca3: View
    private lateinit var barForca4: View

    private var senhaAtualVisivel = false
    private var novaSenhaVisivel = false
    private var confirmarVisivel = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterarsenha)

        inicializarViews()
        configurarListeners()
    }

    private fun inicializarViews() {
        etSenhaAtual      = findViewById(R.id.etSenhaAtual)
        etNovaSenha       = findViewById(R.id.etNovaSenha)
        etConfirmarSenha  = findViewById(R.id.etConfirmarSenha)

        ivToggleSenhaAtual = findViewById(R.id.ivToggleSenhaAtual)
        ivToggleNovaSenha  = findViewById(R.id.ivToggleNovaSenha)
        ivToggleConfirmar  = findViewById(R.id.ivToggleConfirmar)

        tvErroSenha = findViewById(R.id.tvErroSenha)
        btnSalvar   = findViewById(R.id.btnSalvarAlteracoes)

        btnVoltar   = findViewById(R.id.btnVoltar)

        barForca1 = findViewById(R.id.barForca1)
        barForca2 = findViewById(R.id.barForca2)
        barForca3 = findViewById(R.id.barForca3)
        barForca4 = findViewById(R.id.barForca4)
    }

    private fun configurarListeners() {

        // Voltar
        btnVoltar.setOnClickListener { finish() }

        // Toggle visibilidade — Senha Atual
        ivToggleSenhaAtual.setOnClickListener {
            senhaAtualVisivel = !senhaAtualVisivel
            etSenhaAtual.transformationMethod =
                if (senhaAtualVisivel) HideReturnsTransformationMethod.getInstance()
                else PasswordTransformationMethod.getInstance()
            ivToggleSenhaAtual.setImageResource(
                if (senhaAtualVisivel) R.drawable.ic_visibility else R.drawable.ic_visibility_off
            )
            etSenhaAtual.setSelection(etSenhaAtual.length())
        }

        // Toggle visibilidade — Nova Senha
        ivToggleNovaSenha.setOnClickListener {
            novaSenhaVisivel = !novaSenhaVisivel
            etNovaSenha.transformationMethod =
                if (novaSenhaVisivel) HideReturnsTransformationMethod.getInstance()
                else PasswordTransformationMethod.getInstance()
            ivToggleNovaSenha.setImageResource(
                if (novaSenhaVisivel) R.drawable.ic_visibility else R.drawable.ic_visibility_off
            )
            etNovaSenha.setSelection(etNovaSenha.length())
        }

        // Toggle visibilidade — Confirmar
        ivToggleConfirmar.setOnClickListener {
            confirmarVisivel = !confirmarVisivel
            etConfirmarSenha.transformationMethod =
                if (confirmarVisivel) HideReturnsTransformationMethod.getInstance()
                else PasswordTransformationMethod.getInstance()
            ivToggleConfirmar.setImageResource(
                if (confirmarVisivel) R.drawable.ic_visibility else R.drawable.ic_visibility_off
            )
            etConfirmarSenha.setSelection(etConfirmarSenha.length())
        }

        // Barra de força ao digitar nova senha
        etNovaSenha.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                atualizarForcaSenha(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        // Salvar
        btnSalvar.setOnClickListener {
            if (validarCampos()) {
                Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun validarCampos(): Boolean {
        val senhaAtual     = etSenhaAtual.text.toString().trim()
        val novaSenha      = etNovaSenha.text.toString().trim()
        val confirmarSenha = etConfirmarSenha.text.toString().trim()

        if (senhaAtual.isEmpty()) {
            etSenhaAtual.error = "Informe a senha atual"
            etSenhaAtual.requestFocus()
            return false
        }
        if (novaSenha.length < 8) {
            etNovaSenha.error = "A senha deve ter pelo menos 8 caracteres"
            etNovaSenha.requestFocus()
            return false
        }
        if (novaSenha != confirmarSenha) {
            tvErroSenha.visibility = View.VISIBLE
            etConfirmarSenha.background =
                ContextCompat.getDrawable(this, R.drawable.input_background_error)
            return false
        }

        tvErroSenha.visibility = View.GONE
        return true
    }

    private fun atualizarForcaSenha(senha: String) {
        val forca    = calcularForca(senha)
        val corFraco = ContextCompat.getColor(this, R.color.forca_fraca)
        val corMedio = ContextCompat.getColor(this, R.color.forca_media)
        val corForte = ContextCompat.getColor(this, R.color.forca_forte)
        val corVazio = ContextCompat.getColor(this, R.color.forca_vazio)

        barForca1.setBackgroundColor(if (forca >= 1) corFraco else corVazio)
        barForca2.setBackgroundColor(if (forca >= 2) corMedio else corVazio)
        barForca3.setBackgroundColor(if (forca >= 3) corForte else corVazio)
        barForca4.setBackgroundColor(if (forca >= 4) corForte else corVazio)
    }

    private fun calcularForca(senha: String): Int {
        var pontos = 0
        if (senha.length >= 8)                                     pontos++
        if (senha.contains(Regex("[A-Z]")))                        pontos++
        if (senha.contains(Regex("[0-9]")))                        pontos++
        if (senha.contains(Regex("[!@#\$%^&*()_+\\-=\\[\\]{}]"))) pontos++
        return pontos
    }
}