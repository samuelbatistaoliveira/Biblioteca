package com.example.teste3.home_aluno

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.teste3.R
import com.example.teste3.BookRepository
import com.example.teste3.databinding.ActivityHomeBinding
import com.example.teste3.detalhe_livro.BookDetailActivity
import com.example.teste3.login.ChatbotActivity
import com.example.teste3.perfil.PrincipalPerfil as PerfilActivity
import com.example.teste3.salas.Disponivel
import com.example.teste3.mapa.MapaPrincipal

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val coverUrl: String,
    val year: String = "-",
    val genre: String = "-",
    val status: String = "Disponível",
    val andar: Int = 0,
    val pontoX: Float = 0f,
    val pontoY: Float = 0f,
    val localizacao: String = "",
)
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private data class NavItem(val layoutId: Int, val iconId: Int)

    private val navItens = mapOf(
        "menu"     to NavItem(R.id.navChat,     R.id.iconChat),
        "home"     to NavItem(R.id.navHome,     R.id.iconHome),
        "reservas" to NavItem(R.id.navReservas, R.id.iconReservas),
        "salas"    to NavItem(R.id.navSalas,    R.id.iconSalas),
        "perfil"   to NavItem(R.id.navPerfil,   R.id.iconPerfil)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvUserName.text = "Olá, Narak!"

        binding.searchBar.setOnClickListener {
            Toast.makeText(this, "Pesquisar...", Toast.LENGTH_SHORT).show()
        }

        setupBottomNav()
        loadBooks(BookRepository.getAll())
    }



    private fun setupBottomNav() {
        setNavAtivo("home")

        findViewById<LinearLayout>(R.id.navChat).setOnClickListener {
            startActivity(Intent(this, ChatbotActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            // já estamos na home
        }
        findViewById<LinearLayout>(R.id.navReservas).setOnClickListener {
            startActivity(Intent(this, Disponivel::class.java))
        }
        findViewById<LinearLayout>(R.id.navSalas).setOnClickListener {
            startActivity(Intent(this, MapaPrincipal::class.java))
        }
        findViewById<LinearLayout>(R.id.navPerfil).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }
    }

    private fun setNavAtivo(ativo: String) {
        navItens.forEach { (item, nav) ->
            val selecionado = item == ativo
            findViewById<LinearLayout>(nav.layoutId)?.isSelected = selecionado
            val icon = findViewById<ImageView>(nav.iconId)
            icon?.isSelected = selecionado
            icon?.imageTintList = android.content.res.ColorStateList.valueOf(
                if (selecionado) android.graphics.Color.parseColor("#C9A84C")
                else android.graphics.Color.parseColor("#888888")
            )
        }
    }

    override fun onResume() {
        super.onResume()
        setNavAtivo("home")
        loadBooks(BookRepository.getAll())
    }

    private fun loadBooks(books: List<Book>) {
        val grid = binding.gridBooks
        grid.removeAllViews()

        val screenWidth = resources.displayMetrics.widthPixels
        val padding = (32 * resources.displayMetrics.density).toInt()
        val gap = (10 * resources.displayMetrics.density).toInt()
        val itemWidth = (screenWidth - padding - gap * 2) / 3

        books.forEach { book ->
            val itemView = LayoutInflater.from(this)
                .inflate(R.layout.item_book, grid, false)

            val params = android.widget.GridLayout.LayoutParams().apply {
                width  = itemWidth
                height = android.widget.GridLayout.LayoutParams.WRAP_CONTENT
                setMargins(gap / 2, 0, gap / 2, (14 * resources.displayMetrics.density).toInt())
            }
            itemView.layoutParams = params

            itemView.findViewById<ImageView>(R.id.imgCover).load(book.coverUrl)
            itemView.findViewById<TextView>(R.id.tvCoverTitle).text = book.title
            itemView.findViewById<TextView>(R.id.tvBookName).text = book.title

            itemView.setOnClickListener {
                val intent = Intent(this, BookDetailActivity::class.java).apply {
                    putExtra("book_title",  book.title)
                    putExtra("book_author", book.author)
                    putExtra("book_cover",  book.coverUrl)
                    putExtra("book_year",   book.year)
                    putExtra("book_genre",  book.genre)
                    putExtra("book_status", book.status)
                }
                startActivity(intent)
            }

            grid.addView(itemView)
        }
    }

}