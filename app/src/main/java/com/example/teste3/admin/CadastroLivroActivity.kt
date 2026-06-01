package com.example.teste3.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class CadastroLivroActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var capaUri: Uri? = null

    private val galeriaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            capaUri = result.data?.data
            capaUri?.let { findViewById<ImageView>(R.id.imgCapa).setImageURI(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_livro)

        db = FirebaseFirestore.getInstance()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val etNome   = findViewById<TextInputEditText>(R.id.etNome)
        val etAutor  = findViewById<TextInputEditText>(R.id.etAutor)
        val etAno    = findViewById<TextInputEditText>(R.id.etAno)
        val etGenero = findViewById<TextInputEditText>(R.id.etGenero)
        val etCodigo = findViewById<TextInputEditText>(R.id.etCodigo)

        val layoutNome   = findViewById<TextInputLayout>(R.id.layoutNome)
        val layoutAutor  = findViewById<TextInputLayout>(R.id.layoutAutor)
        val layoutAno    = findViewById<TextInputLayout>(R.id.layoutAno)
        val layoutGenero = findViewById<TextInputLayout>(R.id.layoutGenero)
        val layoutCodigo = findViewById<TextInputLayout>(R.id.layoutCodigo)

        // Abre galeria ao clicar no FAB
        findViewById<FloatingActionButton>(R.id.fabEditCapa).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galeriaLauncher.launch(intent)
        }

        val btnAdicionar = findViewById<MaterialButton>(R.id.btnAdicionar)

        btnAdicionar.setOnClickListener {

            val nome   = etNome.text.toString().trim()
            val autor  = etAutor.text.toString().trim()
            val ano    = etAno.text.toString().trim()
            val genero = etGenero.text.toString().trim()
            val codigo = etCodigo.text.toString().trim()
            // Usa URI da galeria se o usuário escolheu imagem, senão salva vazio
            val capa   = capaUri?.toString() ?: ""

            layoutNome.error   = null
            layoutAutor.error  = null
            layoutAno.error    = null
            layoutGenero.error = null
            layoutCodigo.error = null

            var valido = true

            if (nome.isEmpty()) {
                layoutNome.error = "Informe o nome do livro"
                valido = false
            }
            if (autor.isEmpty()) {
                layoutAutor.error = "Informe o autor"
                valido = false
            }
            if (ano.isEmpty()) {
                layoutAno.error = "Informe o ano"
                valido = false
            }
            if (genero.isEmpty()) {
                layoutGenero.error = "Informe o gênero"
                valido = false
            }
            if (codigo.isEmpty()) {
                layoutCodigo.error = "Informe o estado"
                valido = false
            }

            if (valido) {
                val livro = hashMapOf(
                    "nome"     to nome,
                    "autor"    to autor,
                    "ano"      to ano,
                    "genero"   to genero,
                    "codigo"   to codigo,
                    "coverUrl" to capa
                )

                db.collection("livros")
                    .add(livro)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Livro cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao cadastrar livro", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}