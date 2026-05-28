package com.example.teste3.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R
import com.example.teste3.salas.AdmSalas
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.firestore.FirebaseFirestore

class CheckAdmin : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var nomeSala: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_salas)

        db = FirebaseFirestore.getInstance()
        nomeSala = intent.getStringExtra("SALA_NOME") ?: "Sala"

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = nomeSala
        toolbar.setNavigationOnClickListener { finish() }

        val switch1 = findViewById<SwitchMaterial>(R.id.switchTurno1)
        val switch2 = findViewById<SwitchMaterial>(R.id.switchTurno2)
        val switch3 = findViewById<SwitchMaterial>(R.id.switchTurno3)
        val switch4 = findViewById<SwitchMaterial>(R.id.switchTurno4)
        val switch5 = findViewById<SwitchMaterial>(R.id.switchTurno5)

        db.collection("reservas").document(nomeSala).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    switch1.isChecked = doc.getBoolean("turno1") ?: true
                    switch2.isChecked = doc.getBoolean("turno2") ?: true
                    switch3.isChecked = doc.getBoolean("turno3") ?: true
                    switch4.isChecked = doc.getBoolean("turno4") ?: false
                    switch5.isChecked = doc.getBoolean("turno5") ?: false
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
            }

        findViewById<MaterialButton>(R.id.btnSalvar).setOnClickListener {
            val dados = mapOf(
                "turno1" to switch1.isChecked,
                "turno2" to switch2.isChecked,
                "turno3" to switch3.isChecked,
                "turno4" to switch4.isChecked,
                "turno5" to switch5.isChecked
            )
            db.collection("reservas").document(nomeSala)
                .set(dados)
                .addOnSuccessListener {
                    Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show()
                }
        }

        // ✅ MENU ADICIONADO AQUI
        configurarNav()
    }

    private fun configurarNav() {
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
        val itens = mapOf(
            "chat"       to Pair(R.id.navChat,       R.id.iconChat),
            "home"       to Pair(R.id.navHome,       R.id.iconHome),
            "calendar"   to Pair(R.id.navCalendar,   R.id.iconCalendar),
            "categories" to Pair(R.id.navCategories, R.id.iconCategories),
            "profile"    to Pair(R.id.navProfile,    R.id.iconProfile)
        )

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