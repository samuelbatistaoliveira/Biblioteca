package com.example.teste3.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.teste3.databinding.ActivityDetalhesAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class DetalhesAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesAdminBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val bookId = intent.getStringExtra("book_id") ?: ""
        val cover  = intent.getStringExtra("book_cover") ?: ""

        binding.imgBookCover.load(cover)

        binding.btnEditar.setOnClickListener {
            startActivity(Intent(this, EditarLivroActivity::class.java).apply {
                putExtra("book_id",     bookId)
                putExtra("book_title",  binding.tvBookTitle.text.toString())
                putExtra("book_author", binding.tvBookAuthor.text.toString())
                putExtra("book_year",   binding.tvBookYear.text.toString())
                putExtra("book_genre",  binding.tvBookGenre.text.toString())
                putExtra("book_status", binding.tvBookStatus.text.toString())
                putExtra("book_cover",  cover)
            })
        }

        binding.btnDeletar.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Deletar livro")
                .setMessage("Tem certeza que deseja deletar?")
                .setPositiveButton("Deletar") { _, _ ->
                    db.collection("livros").document(bookId).delete()
                    finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        binding.btnLocalizar.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.mapa.MapaPrincipal::class.java).apply {
                putExtra("origem", "admin")
            })
        }

        binding.bottomNav.navChat.setOnClickListener {
            startActivity(Intent(this, AluguelAdmin::class.java))
        }
        binding.bottomNav.navHome.setOnClickListener {
            startActivity(Intent(this, HomeAdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
        binding.bottomNav.navCalendar.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.salas.AdmSalas::class.java))
        }
        binding.bottomNav.navCategories.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.mapa.MapaPrincipal::class.java).apply {
                putExtra("origem", "admin")
            })
        }
        binding.bottomNav.navProfile.setOnClickListener {
            startActivity(Intent(this, perfiladm::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val bookId = intent.getStringExtra("book_id") ?: ""

        db.collection("livros")
            .document(bookId)
            .get()
            .addOnSuccessListener { doc ->
                binding.tvBookTitle.text  = doc.getString("nome") ?: ""
                binding.tvBookAuthor.text = doc.getString("autor") ?: ""
                binding.tvBookYear.text   = doc.getString("ano") ?: ""
                binding.tvBookGenre.text  = doc.getString("genero") ?: ""
                binding.tvBookStatus.text = doc.getString("codigo") ?: ""

                val status = doc.getString("codigo") ?: ""
                binding.tvBookStatus.setTextColor(
                    if (status == "Disponível")
                        getColor(android.R.color.holo_green_dark)
                    else
                        getColor(android.R.color.holo_red_dark)
                )
            }
    }

    private fun setNavAtivo(ativo: String) {
        data class NavItem(val layout: LinearLayout, val icon: ImageView)

        val itens = mapOf(
            "chat"       to NavItem(binding.bottomNav.navChat,       binding.bottomNav.iconChat),
            "home"       to NavItem(binding.bottomNav.navHome,       binding.bottomNav.iconHome),
            "calendar"   to NavItem(binding.bottomNav.navCalendar,   binding.bottomNav.iconCalendar),
            "categories" to NavItem(binding.bottomNav.navCategories, binding.bottomNav.iconCategories),
            "profile"    to NavItem(binding.bottomNav.navProfile,    binding.bottomNav.iconProfile)
        )

        itens.forEach { (item, nav) ->
            val selecionado = item == ativo
            nav.layout.isSelected = selecionado
            nav.icon.isSelected   = selecionado
        }
    }
}