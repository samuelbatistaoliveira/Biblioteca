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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class EditarLivroActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_editar_livro)

        db = FirebaseFirestore.getInstance()

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val bookId = intent.getStringExtra("book_id") ?: ""

        val etNome   = findViewById<TextInputEditText>(R.id.etNome)
        val etAutor  = findViewById<TextInputEditText>(R.id.etAutor)
        val etAno    = findViewById<TextInputEditText>(R.id.etAno)
        val etGenero = findViewById<TextInputEditText>(R.id.etGenero)
        val etEstado = findViewById<TextInputEditText>(R.id.etEstado)
        val etCapa   = findViewById<TextInputEditText>(R.id.etCapa)

        etNome.setText(intent.getStringExtra("book_title"))
        etAutor.setText(intent.getStringExtra("book_author"))
        etAno.setText(intent.getStringExtra("book_year"))
        etGenero.setText(intent.getStringExtra("book_genre"))
        etEstado.setText(intent.getStringExtra("book_status"))
        etCapa.setText(intent.getStringExtra("book_cover"))

        // Abre galeria ao clicar no FAB
        findViewById<FloatingActionButton>(R.id.fabEditCapa).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galeriaLauncher.launch(intent)
        }

        // Nav bar
        findViewById<android.widget.LinearLayout>(R.id.navChat).setOnClickListener {
            startActivity(Intent(this, AluguelAdmin::class.java))
        }
        findViewById<android.widget.LinearLayout>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, HomeAdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
        findViewById<android.widget.LinearLayout>(R.id.navCalendar).setOnClickListener {
            startActivity(Intent(this, com.example.teste3.salas.AdmSalas::class.java))
        }
        findViewById<android.widget.LinearLayout>(R.id.navCategories).setOnClickListener {
            startActivity(Intent(this, com.example.teste3.mapa.MapaPrincipal::class.java).apply {
                putExtra("origem", "admin")
            })
        }
        findViewById<android.widget.LinearLayout>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, perfiladm::class.java))
        }

        findViewById<android.widget.Button>(R.id.btnSalvarAlteracoes).setOnClickListener {

            val novoNome   = etNome.text.toString().trim()
            val novoAutor  = etAutor.text.toString().trim()
            val novoAno    = etAno.text.toString().trim()
            val novoGenero = etGenero.text.toString().trim()
            val novoEstado = etEstado.text.toString().trim()
            val novaCapa   = capaUri?.toString() ?: etCapa.text.toString().trim()

            if (novoNome.isEmpty() || novoAutor.isEmpty() || novoAno.isEmpty() || novoGenero.isEmpty() || novoEstado.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("livros")
                .document(bookId)
                .update(mapOf(
                    "nome"     to novoNome,
                    "autor"    to novoAutor,
                    "ano"      to novoAno,
                    "genero"   to novoGenero,
                    "codigo"   to novoEstado,
                    "coverUrl" to novaCapa
                ))
                .addOnSuccessListener {
                    Toast.makeText(this, "Livro atualizado!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao atualizar", Toast.LENGTH_SHORT).show()
                }
        }
    }
}