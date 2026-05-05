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
import com.example.teste3.BookRepository
import com.example.teste3.R
import com.example.teste3.databinding.ActivityCadastroLivroBinding
import com.example.teste3.home_aluno.Book

class CadastroLivroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroLivroBinding
    private var capaUri: Uri? = null

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
        binding = ActivityCadastroLivroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.imgCapa.setOnClickListener { abrirGaleria() }
        binding.fabEditCapa.setOnClickListener { abrirGaleria() }

        binding.btnAdicionar.setOnClickListener {
            val nome   = binding.etNome.text.toString().trim()
            val autor  = binding.etAutor.text.toString().trim()
            val ano    = binding.etAno.text.toString().trim()
            val genero = binding.etGenero.text.toString().trim() // ← novo
            val codigo = binding.etCodigo.text.toString().trim()

            if (nome.isEmpty() || autor.isEmpty() || ano.isEmpty() || genero.isEmpty() || codigo.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val novoLivro = Book(
                id       = BookRepository.getAll().size + 1,
                title    = nome,
                author   = autor,
                coverUrl = capaUri?.toString() ?: "",
                year     = ano,
                genre    = genero, // ← agora usa o campo correto
            )
            BookRepository.add(novoLivro)

            Toast.makeText(this, "Livro \"$nome\" cadastrado com sucesso!", Toast.LENGTH_LONG).show()

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