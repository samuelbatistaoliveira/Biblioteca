package com.example.teste3.salas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.teste3.R
import com.example.teste3.home_aluno.HomeActivity
import com.example.teste3.login.ChatbotActivity
import com.example.teste3.mapa.MapaPrincipal
import com.example.teste3.perfil.PrincipalPerfil as PerfilActivity

class saladetalhe : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val origem = intent.getStringExtra("origem") ?: "aluno"

        if (origem == "admin") {
            setContentView(R.layout.activity_saladetalhe_admin)
        } else {
            setContentView(R.layout.activity_saladetalhe)
        }

        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets -> insets }

        val nomeSala   = intent.getStringExtra("SALA_NOME")       ?: "Sala"
        val andar      = intent.getStringExtra("SALA_ANDAR")      ?: ""
        val capacidade = intent.getStringExtra("SALA_CAPACIDADE") ?: ""
        val status     = intent.getStringExtra("SALA_STATUS")     ?: "OCUPADA"

        findViewById<TextView>(R.id.tvNomeSala)?.text    = nomeSala
        findViewById<TextView>(R.id.tvAndar)?.text       = andar
        findViewById<TextView>(R.id.tvCapacidade)?.text  = "$capacidade pessoas"
        findViewById<TextView>(R.id.tvLocalizacao)?.text = andar

        // Esconde o badge "OCUPADA" do header
        findViewById<TextView>(R.id.tvBadge)?.visibility = View.GONE

        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        tvStatus?.text = if (status == "OCUPADA") "Ocupada" else "Disponível"
        val corStatus = if (status == "OCUPADA") 0xFFB00020.toInt() else 0xFF4CAF50.toInt()
        tvStatus?.setTextColor(corStatus)

        findViewById<ImageView>(R.id.btnVoltar)?.setOnClickListener { finish() }

        if (origem == "admin") {
            configurarNavAdmin()
        } else {
            configurarNavAluno()
        }
    }

    private fun configurarNavAluno() {
        setNavAtivo("reservas")
        findViewById<LinearLayout>(R.id.navMenu)?.setOnClickListener {
            startActivity(Intent(this, ChatbotActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.navSalas)?.setOnClickListener {
            startActivity(Intent(this, MapaPrincipal::class.java))
        }
        findViewById<LinearLayout>(R.id.navReservas)?.setOnClickListener {
            startActivity(Intent(this, Disponivel::class.java))
        }
        findViewById<LinearLayout>(R.id.navPerfil)?.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }
    }

    private fun configurarNavAdmin() {
        // Ícone calendário dourado
        findViewById<ImageView>(R.id.iconCalendar)?.isSelected = true

        findViewById<LinearLayout>(R.id.navChat)?.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.admin.AluguelAdmin::class.java))
        }
        findViewById<LinearLayout>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.admin.HomeAdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
        findViewById<LinearLayout>(R.id.navCalendar)?.setOnClickListener {
            startActivity(Intent(this, Disponivel::class.java).apply {
                putExtra("origem", "admin")
            })
        }
        findViewById<LinearLayout>(R.id.navCategories)?.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.mapa.MapaLivroActivity::class.java).apply {
                putExtra("origem", "admin")
            })
        }
        findViewById<LinearLayout>(R.id.navProfile)?.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.admin.perfiladm::class.java))
        }
    }

    private fun setNavAtivo(ativo: String) {
        data class NavItem(val layoutId: Int, val iconId: Int)

        val itens = mapOf(
            "chat"     to NavItem(R.id.navMenu,     R.id.iconChat),
            "home"     to NavItem(R.id.navHome,     R.id.iconHome),
            "salas"    to NavItem(R.id.navSalas,    R.id.iconSalas),
            "reservas" to NavItem(R.id.navReservas, R.id.iconReservas),
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