package com.example.teste3.admin

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R


class Devolucao_admin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devolucao_admin)

        val etNome   = findViewById<EditText>(R.id.etNome)
        val etLivro  = findViewById<EditText>(R.id.etLivro)
        val etData   = findViewById<EditText>(R.id.etData)
        val etMulta  = findViewById<EditText>(R.id.etMulta)

        val rgPrazo  = findViewById<RadioGroup>(R.id.rgPrazo)
        val rgMulta  = findViewById<RadioGroup>(R.id.rgMulta)
        val rbMultaSim = findViewById<RadioButton>(R.id.rbMultaSim)

        val btnVoltar  = findViewById<ImageButton>(R.id.btnVoltar)
        val btnAprovar = findViewById<Button>(R.id.btnAprovar)

        // Permite vírgula E ponto no campo de multa
        etMulta.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        etMulta.keyListener = android.text.method.DigitsKeyListener.getInstance("0123456789.,")

        btnVoltar.setOnClickListener {
            finish()
        }

        btnAprovar.setOnClickListener {
            val nome  = etNome.text.toString().trim()
            val livro = etLivro.text.toString().trim()
            val data  = etData.text.toString().trim()

            if (nome.isEmpty() || livro.isEmpty() || data.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos obrigatorios.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (rgPrazo.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Informe se o livro foi devolvido no prazo.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (rgMulta.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Informe se a multa sera aplicada.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (rbMultaSim.isChecked && etMulta.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Informe o valor da multa.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val multa = if (rbMultaSim.isChecked) "R$ ${etMulta.text}" else "Sem multa"
            Toast.makeText(
                this,
                "Solicitacao aprovada!\n$nome | $livro\n$multa",
                Toast.LENGTH_LONG
            ).show()

            etNome.text.clear()
            etLivro.text.clear()
            etData.text.clear()
            etMulta.text.clear()
            rgPrazo.clearCheck()
            rgMulta.clearCheck()
        }
    }
}