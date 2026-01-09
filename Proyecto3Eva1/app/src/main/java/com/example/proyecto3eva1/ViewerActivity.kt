package com.example.proyecto3eva1

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)

        val tvTexto = findViewById<TextView>(R.id.tvTextoRecibido)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        // 1. Lógica Volver
        btnVolver.setOnClickListener {
            finish()
        }

        // 2. Recibir texto (Igual que antes)
        if (intent?.action == Intent.ACTION_SEND && "text/plain" == intent.type) {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let { tvTexto.text = it }
        } else {
            val texto = intent.getStringExtra("CLAVE_MENSAJE")
            tvTexto.text = texto ?: "No se recibió texto"
        }


    }
}