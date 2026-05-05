package com.example.teste3.mapa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.teste3.R

class MapaLivroActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabTerreo: TextView
    private lateinit var tabPrimeiroAndar: TextView
    private lateinit var tvNomeLivro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val andarDoLivro = intent.getIntExtra("andar", 0)
        val pontoX       = intent.getFloatExtra("ponto_x", 0.5f)
        val pontoY       = intent.getFloatExtra("ponto_y", 0.5f)
        val localizacao  = intent.getStringExtra("localizacao_texto") ?: "Localização do livro"
        val origem       = intent.getStringExtra("origem") ?: "aluno"

        // ── Escolhe o layout pelo perfil ──
        if (origem == "admin") {
            setContentView(R.layout.activity_mapa_livro)
        } else {
            setContentView(R.layout.activity_mapa_livro_aluno)
        }

        viewPager        = findViewById(R.id.viewPagerMapas)
        tabTerreo        = findViewById(R.id.tabTerreo)
        tabPrimeiroAndar = findViewById(R.id.tabPrimeiroAndar)
        tvNomeLivro      = findViewById(R.id.tvNomeLivro)

        tvNomeLivro.text = "📍 $localizacao"

        val andares = listOf(
            AndarMapa(
                drawableRes = R.drawable.map_terreo,
                pontoX      = if (andarDoLivro == 0) pontoX else 0f,
                pontoY      = if (andarDoLivro == 0) pontoY else 0f,
                temPonto    = andarDoLivro == 0
            ),
            AndarMapa(
                drawableRes = R.drawable.map_primeiro_andar,
                pontoX      = if (andarDoLivro == 1) pontoX else 0f,
                pontoY      = if (andarDoLivro == 1) pontoY else 0f,
                temPonto    = andarDoLivro == 1
            )
        )

        viewPager.adapter = MapaAdapter(andares)
        viewPager.setCurrentItem(andarDoLivro, false)

        atualizarTabs(andarDoLivro)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) { atualizarTabs(position) }
        })

        tabTerreo.setOnClickListener { viewPager.currentItem = 0 }
        tabPrimeiroAndar.setOnClickListener { viewPager.currentItem = 1 }

        findViewById<ImageView>(R.id.btnVoltar).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (origem == "admin") {
            setNavAtivo(R.id.iconCategories)

            findViewById<LinearLayout>(R.id.navChat).setOnClickListener {
                startActivity(Intent(this, com.example.teste3.admin.AluguelAdmin::class.java))
            }
            findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
                startActivity(Intent(this, com.example.teste3.admin.HomeAdminActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                })
            }
            findViewById<LinearLayout>(R.id.navCalendar).setOnClickListener {
                startActivity(Intent(this, com.example.teste3.salas.Disponivel::class.java).apply {
                    putExtra("origem", "admin")
                })
            }
            findViewById<LinearLayout>(R.id.navCategories).setOnClickListener { }
            findViewById<LinearLayout>(R.id.navProfile).setOnClickListener {
                startActivity(Intent(this, com.example.teste3.admin.perfiladm::class.java))
            }

        } else {
            setNavAtivo(R.id.iconSalas)

            findViewById<LinearLayout>(R.id.navChat).setOnClickListener {
                startActivity(Intent(this, com.example.teste3.login.ChatbotActivity::class.java))
            }
            findViewById<LinearLayout>(R.id.navHome).setOnClickListener {
                startActivity(Intent(this, com.example.teste3.home_aluno.HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                })
            }
            findViewById<LinearLayout>(R.id.navReservas).setOnClickListener {
                startActivity(Intent(this, com.example.teste3.salas.Disponivel::class.java).apply {
                    putExtra("origem", "aluno")
                })
            }
            findViewById<LinearLayout>(R.id.navSalas).setOnClickListener { }
            findViewById<LinearLayout>(R.id.navPerfil).setOnClickListener {
                startActivity(Intent(this, com.example.teste3.perfil.PrincipalPerfil::class.java))
            }
        }
    }

    private fun setNavAtivo(idAtivo: Int) {
        val ids = if (intent.getStringExtra("origem") == "admin") {
            listOf(R.id.iconChat, R.id.iconHome, R.id.iconCalendar, R.id.iconCategories, R.id.iconProfile)
        } else {
            listOf(R.id.iconChat, R.id.iconHome, R.id.iconReservas, R.id.iconSalas, R.id.iconPerfil)
        }

        ids.forEach { id ->
            val cor = if (id == idAtivo) "#C9A84C" else "#8A8A8A"
            findViewById<ImageView>(id)?.setColorFilter(
                android.graphics.Color.parseColor(cor),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun atualizarTabs(posicao: Int) {
        if (posicao == 0) {
            tabTerreo.setBackgroundResource(R.drawable.bg_tab_selected)
            tabTerreo.setTextColor(android.graphics.Color.parseColor("#2C1F0E"))
            tabPrimeiroAndar.setBackgroundResource(R.drawable.bg_tab_unselected)
            tabPrimeiroAndar.setTextColor(android.graphics.Color.parseColor("#7A6A55"))
        } else {
            tabPrimeiroAndar.setBackgroundResource(R.drawable.bg_tab_selected)
            tabPrimeiroAndar.setTextColor(android.graphics.Color.parseColor("#2C1F0E"))
            tabTerreo.setBackgroundResource(R.drawable.bg_tab_unselected)
            tabTerreo.setTextColor(android.graphics.Color.parseColor("#7A6A55"))
        }
    }
}