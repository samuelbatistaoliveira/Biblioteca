package com.example.teste3.detalhe_livro

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.teste3.BookRepository
import com.example.teste3.R
import com.example.teste3.aluguel_livros.AluguelActivity
import com.example.teste3.databinding.ActivityBookDetailBinding
import com.example.teste3.home_aluno.HomeActivity
import com.example.teste3.login.ChatbotActivity
import com.example.teste3.perfil.PrincipalPerfil as PerfilActivity

class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title    = intent.getStringExtra("book_title")  ?: "Sem título"
        val author   = intent.getStringExtra("book_author") ?: "Autor desconhecido"
        val coverUrl = intent.getStringExtra("book_cover")  ?: ""
        val year     = intent.getStringExtra("book_year")   ?: "-"
        val genre    = intent.getStringExtra("book_genre")  ?: "-"
        val status   = intent.getStringExtra("book_status") ?: "Disponível"

        binding.tvBookTitle.text  = title
        binding.tvBookAuthor.text = author
        binding.tvYear.text       = year
        binding.tvGenre.text      = genre
        binding.tvStatus.text     = status
        binding.imgBookCover.load(coverUrl)

        binding.tvStatus.setTextColor(
            if (status == "Disponível")
                getColor(android.R.color.holo_green_dark)
            else
                getColor(android.R.color.holo_red_dark)
        )

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        setNavAtivo("home")

        binding.btnRent.setOnClickListener {
            if (status != "Disponível") {
                Toast.makeText(this, "Livro indisponível para aluguel", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            BookRepository.updateStatus(title, "Alugado")
            startActivity(Intent(this, AluguelActivity::class.java).apply {
                putExtra("book_title",  title)
                putExtra("book_author", author)
                putExtra("book_cover",  coverUrl)
                putExtra("book_year",   year)
                putExtra("book_genre",  genre)
                putExtra("book_status", "Alugado")
            })
        }

        binding.navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }

        binding.navChat.setOnClickListener {
            startActivity(Intent(this, ChatbotActivity::class.java))
        }

        binding.navReservas.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.salas.Disponivel::class.java))
        }

        binding.navPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        binding.navSalas.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.mapa.MapaPrincipal::class.java))
        }

        binding.btnLocalizar.setOnClickListener {
            val intent = Intent(this, com.example.teste3.mapa.MapaLivroActivity::class.java).apply {
                putExtra("andar", 0)
                putExtra("ponto_x", 0.5f)
                putExtra("ponto_y", 0.5f)
                putExtra("localizacao_texto", "Seção: $title")
            }
            startActivity(intent)
        } // ✅ fecha o setOnClickListener

    } // ✅ fecha o onCreate

    private fun setNavAtivo(ativo: String) {
        val itens = mapOf(
            "chat"     to Pair(binding.navChat,     binding.iconChat),
            "home"     to Pair(binding.navHome,     binding.iconHome),
            "reservas" to Pair(binding.navReservas, binding.iconReservas),
            "salas"    to Pair(binding.navSalas,    binding.iconSalas),
            "perfil"   to Pair(binding.navPerfil,   binding.iconPerfil)
        )

        itens.forEach { (item, views) ->
            val selecionado = item == ativo
            views.first.isSelected  = selecionado
            views.second.isSelected = selecionado
        }
    }
}