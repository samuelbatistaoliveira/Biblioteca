package com.example.teste3.home_aluno

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.teste3.R
import com.example.teste3.databinding.ActivityHomeBinding
import com.example.teste3.detalhe_livro.BookDetailActivity
import com.example.teste3.login.ChatbotActivity
import com.example.teste3.perfil.PrincipalPerfil as PerfilActivity
import com.example.teste3.salas.Disponivel
import com.example.teste3.mapa.MapaPrincipal
import com.google.firebase.firestore.FirebaseFirestore

data class Book(
    val id: Int = 0,
    val title: String = "",
    val author: String = "",
    val coverUrl: String = "",
    val year: String = "-",
    val genre: String = "-",
    val status: String = "Disponível",
    val andar: Int = 0,
    val pontoX: Float = 0f,
    val pontoY: Float = 0f,
    val localizacao: String = ""
)

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var db: FirebaseFirestore

    // ← guarda todos os livros carregados do Firebase
    private val todosOsLivros = mutableListOf<Map<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.tvUserName.text = "Olá, Narak!"

        // Bottom nav (sem alteração)
        findViewById<LinearLayout>(R.id.navChat).setOnClickListener {
            startActivity(Intent(this, ChatbotActivity::class.java))
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

        // ← clique na searchBar abre o teclado e ativa a pesquisa inline
        binding.searchBar.setOnClickListener {
            mostrarCaixaDePesquisa()
        }

        carregarLivros()
    }

    override fun onResume() {
        super.onResume()
        carregarLivros()
    }

    // ─── mostra um AlertDialog com campo de texto para pesquisar ───────────
    private fun mostrarCaixaDePesquisa() {
        val editText = android.widget.EditText(this).apply {
            hint = "Digite o nome do livro..."
            setPadding(40, 20, 40, 20)
        }

        android.app.AlertDialog.Builder(this)
            .setTitle("Pesquisar livro")
            .setView(editText)
            .setPositiveButton("Buscar") { _, _ ->
                val query = editText.text.toString().trim()
                filtrarLivros(query)
            }
            .setNegativeButton("Limpar") { _, _ ->
                filtrarLivros("") // mostra todos novamente
            }
            .show()
    }

    // ─── filtra a lista e coloca o resultado no topo ────────────────────────
    private fun filtrarLivros(query: String) {
        val listaFiltrada = if (query.isEmpty()) {
            todosOsLivros
        } else {
            // livros que CONTÊM o texto pesquisado vêm primeiro
            val encontrados  = todosOsLivros.filter {
                it["nome"]?.contains(query, ignoreCase = true) == true
            }
            val restantes = todosOsLivros.filter {
                it["nome"]?.contains(query, ignoreCase = true) != true
            }
            encontrados + restantes // ← encontrados no TOPO
        }

        renderizarGrid(listaFiltrada)
    }

    // ─── carrega do Firebase e salva em todosOsLivros ───────────────────────
    private fun carregarLivros() {
        db.collection("livros")
            .get()
            .addOnSuccessListener { documents ->
                todosOsLivros.clear()
                documents.forEach { doc ->
                    todosOsLivros.add(
                        mapOf(
                            "nome"     to (doc.getString("nome")     ?: "Sem título"),
                            "autor"    to (doc.getString("autor")    ?: ""),
                            "ano"      to (doc.getString("ano")      ?: "-"),
                            "genero"   to (doc.getString("genero")   ?: "-"),
                            "coverUrl" to (doc.getString("coverUrl") ?: "")
                        )
                    )
                }
                renderizarGrid(todosOsLivros) // exibe todos normalmente
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar livros", Toast.LENGTH_SHORT).show()
            }
    }

    // ─── monta o grid com qualquer lista passada ────────────────────────────
    private fun renderizarGrid(lista: List<Map<String, String>>) {
        val grid = binding.gridBooks
        grid.removeAllViews()

        val itemWidth = (resources.displayMetrics.widthPixels -
                (32 * resources.displayMetrics.density).toInt()) / 3

        lista.forEach { livro ->
            val itemView = LayoutInflater.from(this).inflate(R.layout.item_book, grid, false)

            itemView.layoutParams = GridLayout.LayoutParams().apply {
                width  = itemWidth
                height = GridLayout.LayoutParams.WRAP_CONTENT
            }

            val nome  = livro["nome"]     ?: "Sem título"
            val autor = livro["autor"]    ?: ""
            val ano   = livro["ano"]      ?: "-"
            val genero= livro["genero"]   ?: "-"
            val capa  = livro["coverUrl"] ?: ""

            itemView.findViewById<TextView>(R.id.tvBookName).text    = nome
            itemView.findViewById<TextView>(R.id.tvCoverTitle).text  = nome

            if (capa.isNotEmpty()) {
                itemView.findViewById<ImageView>(R.id.imgCover).load(capa)
            }

            itemView.setOnClickListener {
                startActivity(Intent(this, BookDetailActivity::class.java).apply {
                    putExtra("book_title",  nome)
                    putExtra("book_author", autor)
                    putExtra("book_cover",  capa)
                    putExtra("book_year",   ano)
                    putExtra("book_genre",  genero)
                })
            }

            grid.addView(itemView)
        }
    }
}