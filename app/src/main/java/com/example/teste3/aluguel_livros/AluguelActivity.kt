package com.example.teste3.aluguel_livros

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.teste3.R
import com.example.teste3.databinding.ActivityAluguelBinding
import com.example.teste3.home_aluno.HomeActivity
import com.example.teste3.login.ChatbotActivity
import com.example.teste3.salas.Disponivel
import com.example.teste3.mapa.MapaLivroActivity
import com.example.teste3.perfil.PrincipalPerfil as PerfilActivity

class AluguelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAluguelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAluguelBinding.inflate(layoutInflater)
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
        binding.ivBookCover.load(coverUrl)

        binding.tvStatus.setTextColor(
            if (status == "Disponível")
                getColor(android.R.color.holo_green_dark)
            else
                getColor(android.R.color.holo_red_dark)
        )

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnConfirm.setOnClickListener {
            Toast.makeText(this, "Retirada confirmada! Retire na Biblioteca da Unifor.", Toast.LENGTH_LONG).show()
            finish()
        }

        binding.btnCancel.setOnClickListener {
            Toast.makeText(this, "Aluguel cancelado.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // ── Nenhum item ativo no bottom nav desta tela ──
        setNavAtivo("home")

        binding.navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }

        binding.navChat.setOnClickListener {
            startActivity(Intent(this, ChatbotActivity::class.java))
        }

        binding.navPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        // ✅ Reservas → Disponivel
        binding.navReservas.setOnClickListener {
            startActivity(Intent(this, Disponivel::class.java))
        }

        // ✅ Salas → MapaBibliotecaActivity
        binding.navSalas.setOnClickListener {
            val intent = Intent(this, MapaLivroActivity::class.java)
        }
    }

    private fun setNavAtivo(ativo: String?) {
        data class NavItem(val layoutId: Int, val iconId: Int)

        val itens = mapOf(
            "chat"     to NavItem(R.id.navChat,     R.id.iconChat),
            "home"     to NavItem(R.id.navHome,     R.id.iconHome),
            "reservas" to NavItem(R.id.navReservas, R.id.iconReservas),
            "salas"    to NavItem(R.id.navSalas,    R.id.iconSalas),
            "perfil"   to NavItem(R.id.navPerfil,   R.id.iconPerfil)
        )

        itens.forEach { (item, nav) ->
            val layout = findViewById<LinearLayout>(nav.layoutId) ?: return@forEach
            val icon   = findViewById<ImageView>(nav.iconId)      ?: return@forEach

            val selecionado = item == ativo
            layout.isSelected = selecionado
            icon.isSelected   = selecionado
        }
    }
}