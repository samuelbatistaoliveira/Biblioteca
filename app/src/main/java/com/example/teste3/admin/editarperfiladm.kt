package com.example.teste3.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.teste3.R
import com.example.teste3.databinding.ActivityEditarperfiladmBinding
import com.google.android.material.appbar.MaterialToolbar

class editarperfiladm : AppCompatActivity() {

    private lateinit var binding: ActivityEditarperfiladmBinding
    private var fotoUri: Uri? = null

    private val galeriaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            fotoUri = result.data?.data
            fotoUri?.let { binding.ivAvatar.setImageURI(it) } // ← atualiza foto na tela
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarperfiladmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        // Abre galeria ao clicar em Editar foto
        binding.btnEditarFoto.setOnClickListener {
            abrirGaleria()
        }

        // Salva e volta pro perfil
        binding.btnSalvar.setOnClickListener {
            if (fotoUri != null) {
                Toast.makeText(this, "Foto atualizada com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Nenhuma alteração feita.", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galeriaLauncher.launch(intent)
    }
}