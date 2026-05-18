package com.example.teste3.salas

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.teste3.R
import com.example.teste3.admin.AluguelAdmin
import com.example.teste3.admin.HomeAdminActivity
import com.example.teste3.admin.perfiladm
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial

class CheckAdmin : AppCompatActivity() {

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
        setContentView(R.layout.activity_check_salas)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val switches = listOf(
            Pair(findViewById<SwitchMaterial>(R.id.switchTurno1), findViewById<ImageView>(R.id.iconTurno1)),
            Pair(findViewById<SwitchMaterial>(R.id.switchTurno2), findViewById<ImageView>(R.id.iconTurno2)),
            Pair(findViewById<SwitchMaterial>(R.id.switchTurno3), findViewById<ImageView>(R.id.iconTurno3)),
            Pair(findViewById<SwitchMaterial>(R.id.switchTurno4), findViewById<ImageView>(R.id.iconTurno4)),
            Pair(findViewById<SwitchMaterial>(R.id.switchTurno5), findViewById<ImageView>(R.id.iconTurno5))
        )

        switches.forEach { (switch, icon) ->
            atualizarIcone(switch.isChecked, icon)
            switch.setOnCheckedChangeListener { _, isChecked ->
                atualizarIcone(isChecked, icon)
            }
        }

        findViewById<MaterialButton>(R.id.btnSalvar).setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Sucesso")
                .setMessage("Salas reservadas com sucesso!")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
        }

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
            startActivity(Intent(this, AdmSalas::class.java))
        }
        findViewById<LinearLayout>(R.id.navCategories)?.setOnClickListener {
            startActivity(Intent(this, com.example.teste3.mapa.MapaLivroActivity::class.java).apply {
                putExtra("origem", "admin")
            })
        }
        findViewById<LinearLayout>(R.id.navProfile)?.setOnClickListener {
            startActivity(Intent(this, perfiladm::class.java))
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

    private fun atualizarIcone(ativo: Boolean, icon: ImageView) {
        icon.imageTintList = android.content.res.ColorStateList.valueOf(
            android.graphics.Color.parseColor(if (ativo) "#3B2A0E" else "#888888")
        )
        icon.setBackgroundResource(
            if (ativo) R.drawable.circle_gold_bg else R.drawable.circle_gray_bg
        )
    }
}