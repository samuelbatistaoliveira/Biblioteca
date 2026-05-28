package com.example.teste3.salas

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.teste3.R
import com.example.teste3.home_aluno.HomeActivity
import com.example.teste3.login.ChatbotActivity
import com.example.teste3.mapa.MapaPrincipal
import com.example.teste3.perfil.PrincipalPerfil as PerfilActivity

class salahorarios : AppCompatActivity() {

    private var origem = "aluno"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        origem = intent.getStringExtra("origem") ?: "aluno"

        if (origem == "admin") {
            setContentView(R.layout.activity_check_salas) // layout admin
        } else {
            setContentView(R.layout.activity_salahorarios) // layout aluno (troque se tiver um diferente)
        }

        val switches = listOf(
            Pair(R.id.switchTurno1, R.id.iconTurno1),
            Pair(R.id.switchTurno2, R.id.iconTurno2),
            Pair(R.id.switchTurno3, R.id.iconTurno3),
            Pair(R.id.switchTurno4, R.id.iconTurno4),
            Pair(R.id.switchTurno5, R.id.iconTurno5)
        )

        switches.forEach { (switchId, iconId) ->
            val switch = findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(switchId) ?: return@forEach
            val icon = findViewById<ImageView>(iconId) ?: return@forEach

            // Define cor inicial baseada no estado do switch
            atualizarCorIcone(icon, switch.isChecked)

            // Atualiza quando mudar
            switch.setOnCheckedChangeListener { _, isChecked ->
                atualizarCorIcone(icon, isChecked)
            }
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nomeSala   = intent.getStringExtra("SALA_NOME")       ?: "Sala"
        val andar      = intent.getStringExtra("SALA_ANDAR")      ?: ""
        val capacidade = intent.getStringExtra("SALA_CAPACIDADE") ?: ""

        findViewById<TextView>(R.id.tvSalaTitulo).text = "$nomeSala, $andar"
        findViewById<TextView>(R.id.tvCapacidade).text = "Cap. $capacidade pessoas"

        if (origem == "admin") {
            configurarNavAdmin()
        } else {
            configurarNavAluno()
        }
    }

    private fun configurarNavAluno() {
        setNavAtivo("reservas", admin = false)

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
            startActivity(Intent(this, Disponivel::class.java).apply {
                putExtra("origem", "aluno")
            })
        }
        findViewById<LinearLayout>(R.id.navPerfil)?.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }
    }

    private fun configurarNavAdmin() {
        setNavAtivo("calendar", admin = true)

        findViewById<LinearLayout>(R.id.navChat)?.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.admin.AluguelAdmin::class.java))
        }
        findViewById<LinearLayout>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.admin.HomeAdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
        findViewById<LinearLayout>(R.id.navCalendar)?.setOnClickListener {
            startActivity(Intent(this, AdmSalas::class.java))
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

    private fun setNavAtivo(ativo: String, admin: Boolean) {
        val itens = if (admin) {
            mapOf(
                "chat"       to Pair(R.id.navChat,       R.id.iconChat),
                "home"       to Pair(R.id.navHome,       R.id.iconHome),
                "calendar"   to Pair(R.id.navCalendar,   R.id.iconCalendar),
                "categories" to Pair(R.id.navCategories, R.id.iconCategories),
                "profile"    to Pair(R.id.navProfile,    R.id.iconProfile)
            )
        } else {
            mapOf(
                "chat"     to Pair(R.id.navMenu,     R.id.iconChat),
                "home"     to Pair(R.id.navHome,     R.id.iconHome),
                "salas"    to Pair(R.id.navSalas,    R.id.iconSalas),
                "reservas" to Pair(R.id.navReservas, R.id.iconReservas),
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
    private fun atualizarCorIcone(icon: ImageView, ativo: Boolean) {
        icon.imageTintList = android.content.res.ColorStateList.valueOf(
            android.graphics.Color.parseColor(if (ativo) "#3B2A0E" else "#888888")
        )
        icon.setBackgroundResource(
            if (ativo) R.drawable.circle_gold_bg else R.drawable.circle_gray_bg
        )
    }
}