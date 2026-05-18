package com.example.teste3.perfil

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R
import de.hdodenhof.circleimageview.CircleImageView

class EditarPerfil : AppCompatActivity() {

    private lateinit var ivAvatar: CircleImageView

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            ivAvatar.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        ivAvatar = findViewById(R.id.ivAvatar)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val btnEditarFoto = findViewById<Button>(R.id.btnEditarFoto)
        btnEditarFoto.setOnClickListener {
            pickImage.launch("image/*")
        }

        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        btnSalvar.setOnClickListener {
            finish()
        }
    }
}