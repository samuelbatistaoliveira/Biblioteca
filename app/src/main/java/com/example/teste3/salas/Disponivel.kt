package com.example.teste3.salas

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.teste3.R

class Disponivel : AppCompatActivity() {

    private var origem = "aluno"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        origem = intent.getStringExtra("origem") ?: "aluno"

        if (origem == "admin") {
            setContentView(R.layout.activity_disponivel)
        } else {
            setContentView(R.layout.activity_disponivel_aluno)
        }

        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets -> insets }

        // Salas LIVRES
        findViewById<LinearLayout>(R.id.cardSalaA).setOnClickListener {
            abrirHorarios("Sala A", "Andar 1", "6")
        }
        findViewById<LinearLayout>(R.id.cardSalaB).setOnClickListener {
            abrirHorarios("Sala B", "Andar 1", "8")
        }
        findViewById<LinearLayout>(R.id.cardSalaE).setOnClickListener {
            abrirHorarios("Sala E", "Andar 1", "4")
        }

        // Salas OCUPADAS
        findViewById<LinearLayout>(R.id.cardSalaC).setOnClickListener {
            abrirDetalhe("Sala C", "Andar 2", "4", "OCUPADA")
        }
        findViewById<LinearLayout>(R.id.cardSalaD).setOnClickListener {
            abrirDetalhe("Sala D", "Andar 2", "10", "OCUPADA")
        }

        if (origem == "admin") {
            configurarMenuAdmin()
        } else {
            configurarMenuAluno()
        }
    }

    private fun configurarMenuAdmin() {
        setNavAtivo("calendar", isAdmin = true)

        findViewById<LinearLayout>(R.id.navChat).setOnClickListener {
            setNavAtivo("chat", isAdmin = true)
            startActivity(Intent(this, com.example.teste3.admin.AluguelAdmin::class.java))
        }
        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            setNavAtivo("home", isAdmin = true)
            startActivity(Intent(this, com.example.teste3.admin.HomeAdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
        findViewById<LinearLayout>(R.id.navCalendar).setOnClickListener {
            // já estamos na tela de salas
        }
        findViewById<LinearLayout>(R.id.navCategories).setOnClickListener {
            setNavAtivo("categories", isAdmin = true)
            startActivity(Intent(this, com.example.teste3.mapa.MapaLivroActivity::class.java).apply {
                putExtra("andar", 0)
                putExtra("ponto_x", 0.65f)
                putExtra("ponto_y", 0.30f)
                putExtra("localizacao_texto", "Ciências — Estante 3, Prat. B")
                putExtra("origem", "admin")
            })
        }
        findViewById<LinearLayout>(R.id.navProfile).setOnClickListener {
            setNavAtivo("profile", isAdmin = true)
            startActivity(Intent(this, com.example.teste3.admin.perfiladm::class.java))
        }
    }

    private fun configurarMenuAluno() {
        setNavAtivo("reservas", isAdmin = false)

        findViewById<LinearLayout>(R.id.navChat).setOnClickListener {
            startActivity(Intent(this, com.example.teste3.login.ChatbotActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, com.example.teste3.home_aluno.HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
        findViewById<LinearLayout>(R.id.navReservas).setOnClickListener {
            // já estamos na tela de salas
        }
        findViewById<LinearLayout>(R.id.navSalas).setOnClickListener {
            startActivity(Intent(this, com.example.teste3.mapa.MapaLivroActivity::class.java).apply {
                putExtra("andar", 0)
                putExtra("ponto_x", 0.65f)
                putExtra("ponto_y", 0.30f)
                putExtra("localizacao_texto", "Ciências — Estante 3, Prat. B")
                putExtra("origem", "aluno")
            })
        }
        findViewById<LinearLayout>(R.id.navPerfil).setOnClickListener {
            startActivity(Intent(this, com.example.teste3.perfil.PrincipalPerfil::class.java))
        }
    }

    private fun abrirHorarios(nomeSala: String, andar: String, capacidade: String) {
        Intent(this, salahorarios::class.java).also {
            it.putExtra("SALA_NOME", nomeSala)
            it.putExtra("SALA_ANDAR", andar)
            it.putExtra("SALA_CAPACIDADE", capacidade)
            startActivity(it)
        }
    }

    private fun abrirDetalhe(nomeSala: String, andar: String, capacidade: String, status: String) {
        Intent(this, saladetalhe::class.java).also {
            it.putExtra("SALA_NOME", nomeSala)
            it.putExtra("SALA_ANDAR", andar)
            it.putExtra("SALA_CAPACIDADE", capacidade)
            it.putExtra("SALA_STATUS", status)
            startActivity(it)
        }
    }

    private fun setNavAtivo(ativo: String, isAdmin: Boolean) {
        val itens = if (isAdmin) {
            mapOf(
                "chat"       to Pair(R.id.navChat,       R.id.iconChat),
                "home"       to Pair(R.id.navHome,       R.id.iconHome),
                "calendar"   to Pair(R.id.navCalendar,   R.id.iconCalendar),
                "categories" to Pair(R.id.navCategories, R.id.iconCategories),
                "profile"    to Pair(R.id.navProfile,    R.id.iconProfile)
            )
        } else {
            mapOf(
                "chat"     to Pair(R.id.navChat,     R.id.iconChat),
                "home"     to Pair(R.id.navHome,     R.id.iconHome),
                "reservas" to Pair(R.id.navReservas, R.id.iconReservas),
                "salas"    to Pair(R.id.navSalas,    R.id.iconSalas),
                "perfil"   to Pair(R.id.navPerfil,   R.id.iconPerfil)
            )
        }

        itens.forEach { (item, pair) ->
            val layout = findViewById<LinearLayout>(pair.first) ?: return@forEach
            val icon   = findViewById<ImageView>(pair.second)   ?: return@forEach
            val selecionado = item == ativo
            layout.isSelected = selecionado
            icon.isSelected   = selecionado
            icon.imageTintList = android.content.res.ColorStateList.valueOf(
                if (selecionado) android.graphics.Color.parseColor("#C9A84C")
                else android.graphics.Color.parseColor("#888888")
            )
        }
    }
}