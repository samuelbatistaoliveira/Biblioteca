package com.example.teste3.admin


import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R


class solicitacao : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitacao_admin)

        val btnVoltar   = findViewById<ImageButton>(R.id.btnVoltar)
        val etNome      = findViewById<EditText>(R.id.etNome)
        val etLivro     = findViewById<EditText>(R.id.etLivro)
        val etData      = findViewById<EditText>(R.id.etData)
        val rgPrazo     = findViewById<RadioGroup>(R.id.rgPrazo)
        val rgPendencia = findViewById<RadioGroup>(R.id.rgPendencia)
        val btnAprovar  = findViewById<Button>(R.id.btnAprovar)
        val btnRecusar  = findViewById<Button>(R.id.btnRecusar)

        // Força os backgrounds após o layout estar pronto
        btnAprovar.post {
            val bg = GradientDrawable()
            bg.setColor(0xFFC8A84B.toInt())
            bg.cornerRadius = 8f * resources.displayMetrics.density
            btnAprovar.background = bg
            btnAprovar.setTextColor(0xFFFFFFFF.toInt())
        }

        btnRecusar.post {
            val bg = GradientDrawable()
            bg.setColor(0xFFFFFFFF.toInt())
            bg.setStroke(
                (1.5f * resources.displayMetrics.density).toInt(),
                0xFFC8A84B.toInt()
            )
            bg.cornerRadius = 8f * resources.displayMetrics.density
            btnRecusar.background = bg
            btnRecusar.setTextColor(0xFF2C1A0E.toInt())
        }

        btnVoltar.setOnClickListener { finish() }

        btnAprovar.setOnClickListener {
            val nome  = etNome.text.toString().trim()
            val livro = etLivro.text.toString().trim()
            val data  = etData.text.toString().trim()

            if (nome.isEmpty() || livro.isEmpty() || data.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (rgPrazo.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Informe se o aluno está dentro do prazo.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (rgPendencia.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Informe se o aluno possui pendências.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dentroDoPrazo = rgPrazo.checkedRadioButtonId == R.id.rbPrazoSim
            val temPendencias = rgPendencia.checkedRadioButtonId == R.id.rbPendenciaSim

            if (!dentroDoPrazo || temPendencias) {
                Toast.makeText(this, "Não é possível aprovar: verifique prazo e pendências.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Solicitação de \"$livro\" aprovada para $nome!", Toast.LENGTH_LONG).show()
            finish()
        }

        btnRecusar.setOnClickListener {
            Toast.makeText(this, "Ação cancelada.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}