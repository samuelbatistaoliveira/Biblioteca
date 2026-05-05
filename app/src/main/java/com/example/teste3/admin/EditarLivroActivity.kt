package com.example.teste3.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import android.content.res.ColorStateList
import coil.load
import com.example.teste3.BookRepository
import com.example.teste3.R
import com.example.teste3.databinding.ActivityEditarLivroBinding
import com.example.teste3.home_aluno.Book

class EditarLivroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarLivroBinding
    private var capaUri: Uri? = null
    private var capaOriginal: String = ""

    private val galeriaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            capaUri = result.data?.data
            capaUri?.let { binding.imgCapa.setImageURI(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarLivroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val oldTitle = intent.getStringExtra("book_title")  ?: ""
        val author   = intent.getStringExtra("book_author") ?: ""
        val cover    = intent.getStringExtra("book_cover")  ?: ""
        val year     = intent.getStringExtra("book_year")   ?: ""
        val genre    = intent.getStringExtra("book_genre")  ?: ""
        val status   = intent.getStringExtra("book_status") ?: ""

        capaOriginal = cover

        binding.etNome.setText(oldTitle)
        binding.etAutor.setText(author)
        binding.etAno.setText(year)
        binding.etGenero.setText(genre)
        binding.etEstado.setText(status)
        binding.imgCapa.load(cover)

        binding.imgCapa.setOnClickListener { abrirGaleria() }
        binding.fabEditCapa.setOnClickListener { abrirGaleria() }

        binding.btnSalvarAlteracoes.setOnClickListener {
            val novoNome   = binding.etNome.text.toString().trim()
            val novoAutor  = binding.etAutor.text.toString().trim()
            val novoAno    = binding.etAno.text.toString().trim()
            val novoGenero = binding.etGenero.text.toString().trim()
            val novoEstado = binding.etEstado.text.toString().trim()

            if (novoNome.isEmpty() || novoAutor.isEmpty() || novoAno.isEmpty() || novoGenero.isEmpty() || novoEstado.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val novaCapa = capaUri?.toString() ?: capaOriginal

            val livroAtualizado = Book(
                id = 0,
                title = novoNome,
                author = novoAutor,
                coverUrl = novaCapa,
                year = novoAno,
                genre = novoGenero,
                status = novoEstado
            )

            BookRepository.update(oldTitle, livroAtualizado)
            Toast.makeText(this, "Livro atualizado com sucesso!", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, HomeAdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
            finish()
        }

        setupBottomNav()
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galeriaLauncher.launch(intent)
    }

    private fun setupBottomNav() {
        // ícone home ativo
        ImageViewCompat.setImageTintList(
            findViewById(R.id.iconHome),
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gold))
        )

        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, HomeAdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }

        findViewById<LinearLayout>(R.id.navChat).setOnClickListener {
            startActivity(Intent(this, AluguelAdmin::class.java))
        }

        findViewById<LinearLayout>(R.id.navCalendar).setOnClickListener {
            startActivity(Intent(this, com.example.teste3.salas.AdmSalas::class.java))
        }

        findViewById<LinearLayout>(R.id.navCategories).setOnClickListener {
            startActivity(Intent(this, com.example.teste3.mapa.MapaPrincipal::class.java).apply {
                putExtra("origem", "admin")
            })
        }

        findViewById<LinearLayout>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, perfiladm::class.java))
        }
    }
}