package com.example.teste3.admin

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
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class HomeAdminActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private val cadastroLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) {
        // Não importa o resultado, sempre recarrega ao voltar do cadastro
        carregarLivros()
    }

    private val detalhesLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) {
        // Recarrega ao voltar da tela de detalhes (cobre deletar, editar, etc.)
        carregarLivros()
    }

    // ← guarda todos os livros carregados do Firebase
    private val todosOsLivros = mutableListOf<Map<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        db = FirebaseFirestore.getInstance()

        // Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Olá, Admin!"

        // Botão adicionar livro
        findViewById<FloatingActionButton>(R.id.fabAddBook).setOnClickListener {
            cadastroLauncher.launch(Intent(this, CadastroLivroActivity::class.java))
        }

        // Bottom nav (sem alteração)
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

        // ← clique na searchBar abre a pesquisa
        findViewById<LinearLayout>(R.id.searchBar).setOnClickListener {
            mostrarCaixaDePesquisa()
        }

        carregarLivros()
    }


    // ─── abre dialog de pesquisa ────────────────────────────────────────────
    private fun mostrarCaixaDePesquisa() {
        val editText = android.widget.EditText(this).apply {
            hint = "Digite o nome do livro..."
            setPadding(40, 20, 40, 20)
        }

        android.app.AlertDialog.Builder(this)
            .setTitle("Pesquisar livro")
            .setView(editText)
            .setPositiveButton("Buscar") { _, _ ->
                filtrarLivros(editText.text.toString().trim())
            }
            .setNegativeButton("Limpar") { _, _ ->
                filtrarLivros("") // volta todos
            }
            .show()
    }

    // ─── filtra e coloca encontrados no topo ────────────────────────────────
    private fun filtrarLivros(query: String) {
        val listaFiltrada = if (query.isEmpty()) {
            todosOsLivros
        } else {
            val encontrados = todosOsLivros.filter {
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
                            "id"       to doc.id,
                            "nome"     to (doc.getString("nome")     ?: "Sem título"),
                            "autor"    to (doc.getString("autor")    ?: ""),
                            "ano"      to (doc.getString("ano")      ?: ""),
                            "genero"   to (doc.getString("genero")   ?: ""),
                            "codigo"   to (doc.getString("codigo")   ?: ""),
                            "coverUrl" to (doc.getString("coverUrl") ?: "")
                        )
                    )
                }
                renderizarGrid(todosOsLivros)
            }
            .addOnFailureListener { exception ->
                android.util.Log.e("FIREBASE", "Erro: ${exception.message}", exception)
                Toast.makeText(this, "Erro: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }


    // ─── monta o grid com qualquer lista passada ────────────────────────────
    private fun renderizarGrid(lista: List<Map<String, String>>) {
        val grid = findViewById<GridLayout>(R.id.gridBooks)
        grid.removeAllViews()

        val spacing = (4 * resources.displayMetrics.density).toInt()
        val columns = 3

        // Espera o GridLayout terminar de medir antes de calcular largura real dos itens.
        // Isso evita o bug de usar a largura da tela inteira (que ignora padding
        // e causa inconsistência de constraints em diferentes densidades de tela).
        grid.post {
            val gridWidth = grid.width - grid.paddingStart - grid.paddingEnd
            val itemWidth = (gridWidth - spacing * 2 * columns) / columns

            lista.forEach { livro ->
                val itemView = LayoutInflater.from(this).inflate(R.layout.item_book, grid, false)

                itemView.layoutParams = GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)
                ).apply {
                    width  = itemWidth
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    setMargins(spacing, spacing, spacing, spacing)
                }

                val nome   = livro["nome"]     ?: "Sem título"
                val autor  = livro["autor"]    ?: ""
                val ano    = livro["ano"]      ?: ""
                val genero = livro["genero"]   ?: ""
                val codigo = livro["codigo"]   ?: ""
                val capa   = livro["coverUrl"] ?: ""
                val docId  = livro["id"]       ?: ""

                itemView.findViewById<TextView>(R.id.tvBookName).text = nome

                itemView.findViewById<ImageView>(R.id.imgCover).load(capa.ifEmpty {
                    "https://placehold.co/150x200/png"
                })

                itemView.setOnClickListener {
                    detalhesLauncher.launch(Intent(this, DetalhesAdminActivity::class.java).apply {
                        putExtra("book_id",     docId)
                        putExtra("book_title",  nome)
                        putExtra("book_author", autor)
                        putExtra("book_cover",  capa)
                        putExtra("book_year",   ano)
                        putExtra("book_genre",  genero)
                        putExtra("book_status", codigo)
                    })
                }

                grid.addView(itemView)
            }
        }
    }
}