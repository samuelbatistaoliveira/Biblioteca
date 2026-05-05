package com.example.teste3.admin

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.teste3.R

class perfiladm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_adm)

        // Botão lápis → vai para Editar Perfil
        findViewById<FrameLayout>(R.id.btnEditarPerfil).setOnClickListener {
            startActivity(Intent(this, editarperfiladm::class.java))
        }

        // Botão engrenagem → vai para Configurações
        findViewById<FrameLayout>(R.id.btnConfiguracoes).setOnClickListener {
            startActivity(Intent(this, configadm::class.java))
        }

        // Card Acervo → vai para Home Admin
        findViewById<CardView>(R.id.cardGerenciarAcervo).setOnClickListener {
            startActivity(Intent(this, HomeAdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }

        // Card Atrasos e Multas → vai para Gerenciamento de Aluguel
        findViewById<CardView>(R.id.cardVerAtrasos).setOnClickListener {
            startActivity(Intent(this, AluguelAdmin::class.java))
        }

        setNavAtivo("perfil")

        findViewById<LinearLayout>(R.id.navMenu)?.setOnClickListener {
            startActivity(Intent(this, AluguelAdmin::class.java))
        }
        findViewById<LinearLayout>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, HomeAdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
        findViewById<LinearLayout>(R.id.navCalendar)?.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.salas.AdmSalas::class.java))
        }
        findViewById<LinearLayout>(R.id.navSignpost)?.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.mapa.MapaPrincipal::class.java).apply {
                putExtra("origem", "admin")
            })
        }
        findViewById<LinearLayout>(R.id.navPerfil)?.setOnClickListener {
            // já está nesta tela
        }
    }

    private fun setNavAtivo(ativo: String) {
        data class NavItem(val layoutId: Int, val iconId: Int)

        val itens = mapOf(
            "menu"     to NavItem(R.id.navMenu,     R.id.iconMenu),
            "home"     to NavItem(R.id.navHome,     R.id.iconHome),
            "calendar" to NavItem(R.id.navCalendar, R.id.iconCalendar),
            "signpost" to NavItem(R.id.navSignpost, R.id.iconSignpost),
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