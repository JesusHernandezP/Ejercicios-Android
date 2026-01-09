package com.example.proyecto3eva2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResumenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen)

        // 1. Recibimos los datos de la "maleta" (Intent)
        val nombre = intent.getStringExtra("NOMBRE") ?: ""
        val email = intent.getStringExtra("EMAIL") ?: ""
        val telefono = intent.getStringExtra("TELEFONO") ?: ""
        val ciudad = intent.getStringExtra("CIUDAD") ?: ""
        val nivel = intent.getStringExtra("NIVEL") ?: ""
        val condiciones = intent.getStringExtra("CONDICIONES") ?: ""

        // 2. Referenciamos los TextViews y asignamos el texto
        findViewById<TextView>(R.id.tvTituloResumen).text = "Resumen de $nombre"
        findViewById<TextView>(R.id.tvNombre).text = "Nombre: $nombre"
        findViewById<TextView>(R.id.tvEmail).text = "Email: $email"
        findViewById<TextView>(R.id.tvTelefono).text = "Teléfono: $telefono"
        findViewById<TextView>(R.id.tvCiudad).text = "Ciudad: $ciudad"
        findViewById<TextView>(R.id.tvNivel).text = "Nivel: $nivel"
        findViewById<TextView>(R.id.tvCondiciones).text = "Condiciones: $condiciones"

        // 3. Botón Volver
        findViewById<Button>(R.id.btnVolverResumen).setOnClickListener {
            finish()
        }
    }
}