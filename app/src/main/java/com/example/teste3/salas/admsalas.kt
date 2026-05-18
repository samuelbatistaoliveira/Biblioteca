package com.example.teste3.salas

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R
import com.example.teste3.admin.AluguelAdmin
import com.example.teste3.admin.HomeAdminActivity
import com.example.teste3.admin.perfiladm

class AdmSalas : AppCompatActivity() {

    private data class NavItem(val layoutId: Int, val iconId: Int)

    private val navItens = mapOf(
        "chat"       to NavItem(R.id.navChat,       R.id.iconChat),
        "home"       to NavItem(R.id.navHome,       R.id.iconHome),
        "calendar"   to NavItem(R.id.navCalendar,   R.id.iconCalendar),
        "categories" to NavItem(R.id.navCategories, R.id.iconCategories),
        "profile"    to NavItem(R.id.navProfile,    R.id.iconProfile)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admsalas)

        setNavAtivo("calendar")

        findViewById<LinearLayout>(R.id.navChat)?.setOnClickListener {
            startActivity(Intent(this, AluguelAdmin::class.java))
        }
        findViewById<LinearLayout>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, HomeAdminActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
        findViewById<LinearLayout>(R.id.navCalendar)?.setOnClickListener {
            // já está nesta tela
        }
        findViewById<LinearLayout>(R.id.navCategories)?.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.mapa.MapaLivroActivity::class.java).apply {
                putExtra("origem", "admin")
            })
        }
        findViewById<LinearLayout>(R.id.navProfile)?.setOnClickListener {
            startActivity(Intent(this, perfiladm::class.java))
        }

        // Salas LIVRES → CheckAdmin
        findViewById<LinearLayout>(R.id.cardSalaA)?.setOnClickListener {
            startActivity(Intent(this, CheckAdmin::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
        }
        findViewById<LinearLayout>(R.id.cardSalaB)?.setOnClickListener {
            startActivity(Intent(this, CheckAdmin::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
        }
        findViewById<LinearLayout>(R.id.cardSalaE)?.setOnClickListener {
            startActivity(Intent(this, CheckAdmin::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
        }

        // Salas OCUPADAS → saladetalhe
        findViewById<LinearLayout>(R.id.cardSalaC)?.setOnClickListener {
            startActivity(Intent(this, saladetalhe::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("SALA_NOME", "Sala C")
                putExtra("SALA_ANDAR", "Andar 2")
                putExtra("SALA_CAPACIDADE", "4")
                putExtra("SALA_STATUS", "OCUPADA")
                putExtra("origem", "admin")
            })
        }
        findViewById<LinearLayout>(R.id.cardSalaD)?.setOnClickListener {
            startActivity(Intent(this, saladetalhe::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("SALA_NOME", "Sala D")
                putExtra("SALA_ANDAR", "Andar 2")
                putExtra("SALA_CAPACIDADE", "10")
                putExtra("SALA_STATUS", "OCUPADA")
                putExtra("origem", "admin")
            })
        }
    }

    private fun setNavAtivo(ativo: String) {
        navItens.forEach { (item, nav) ->
            val selecionado = item == ativo
            val layout = findViewById<LinearLayout>(nav.layoutId) ?: return@forEach
            val icon   = findViewById<ImageView>(nav.iconId)      ?: return@forEach
            layout.isSelected = selecionado
            icon.isSelected   = selecionado
            icon.imageTintList = android.content.res.ColorStateList.valueOf(
                if (selecionado) android.graphics.Color.parseColor("#C9A84C")
                else android.graphics.Color.parseColor("#888888")
            )
        }
    }
}