package com.example.proyecto3eva1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etMensaje = findViewById<EditText>(R.id.etMensaje)
        val btnCompartir = findViewById<Button>(R.id.btnCompartir)
        val btnAbrirVisor = findViewById<Button>(R.id.btnAbrirVisor)

        btnCompartir.setOnClickListener {
            val mensaje = etMensaje.text.toString()
            if (mensaje.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, mensaje)
                val chooser = Intent.createChooser(intent, "Compartir usando...")
                startActivity(chooser)
            } else {
                Toast.makeText(this, "Escribe algo para compartir", Toast.LENGTH_SHORT).show()
            }
        }

        btnAbrirVisor.setOnClickListener {
            val mensaje = etMensaje.text.toString()

            if (mensaje.isNotEmpty()) {
                val intent = Intent(this, ViewerActivity::class.java)

                intent.putExtra("CLAVE_MENSAJE", mensaje)

                startActivity(intent)
            } else {
                Toast.makeText(this, "Escribe un mensaje primero", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

