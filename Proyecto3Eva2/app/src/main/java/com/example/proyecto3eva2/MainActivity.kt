package com.example.proyecto3eva2

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etTelefono: TextInputEditText
    private lateinit var etCiudad: TextInputEditText
    private lateinit var cbCondiciones: CheckBox
    private lateinit var spNivel: Spinner

    private lateinit var layoutNombre: TextInputLayout
    private lateinit var layoutEmail: TextInputLayout
    private lateinit var layoutTelefono: TextInputLayout
    private lateinit var layoutCiudad: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutNombre = findViewById(R.id.inputLayoutNombre)
        etNombre = findViewById(R.id.etNombre)

        layoutEmail = findViewById(R.id.inputLayoutEmail)
        etEmail = findViewById(R.id.etEmail)

        layoutTelefono = findViewById(R.id.inputLayoutTelefono)
        etTelefono = findViewById(R.id.etTelefono)

        layoutCiudad = findViewById(R.id.inputLayoutCiudad)
        etCiudad = findViewById(R.id.etCiudad)

        spNivel = findViewById(R.id.spNivel)
        cbCondiciones = findViewById(R.id.cbCondiciones)
        val btnVerInfo = findViewById<Button>(R.id.btnVerInfo)

        val opciones = arrayOf("Básico", "Junior", "SemiSenior", "Senior")
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spNivel.adapter = adaptador

        btnVerInfo.setOnClickListener {
            limpiarErrores()

            var hayErrores = false

            // Validación Nombre
            if (etNombre.text.toString().trim().isEmpty()) {
                layoutNombre.error = "Este campo es obligatorio"
                hayErrores = true
            }

            // Validación Email
            val email = etEmail.text.toString().trim()
            if (email.isEmpty()) {
                layoutEmail.error = "Introduce un email"
                hayErrores = true
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                layoutEmail.error = "Correo inválido"
                hayErrores = true
            }

            // Validación Teléfono
            val telefono = etTelefono.text.toString().trim()
            if (telefono.isEmpty()) {
                layoutTelefono.error = "Falta el teléfono"
                hayErrores = true
            } else if (telefono.length < 9) {
                layoutTelefono.error = "Debe tener al menos 9 números"
                hayErrores = true
            }

            // Validación Ciudad
            if (etCiudad.text.toString().trim().isEmpty()) {
                layoutCiudad.error = "Falta la ciudad"
                hayErrores = true
            }

            // Validación Checkbox
            if (!cbCondiciones.isChecked) {
                Toast.makeText(this, "Debes aceptar las condiciones", Toast.LENGTH_SHORT).show()
                hayErrores = true
            }


            if (!hayErrores) {
                val intent = Intent(this, ResumenActivity::class.java)
                intent.putExtra("NOMBRE", etNombre.text.toString())
                intent.putExtra("EMAIL", etEmail.text.toString())
                intent.putExtra("TELEFONO", etTelefono.text.toString())
                intent.putExtra("CIUDAD", etCiudad.text.toString())
                intent.putExtra("NIVEL", spNivel.selectedItem.toString())
                intent.putExtra("CONDICIONES", "ACEPTADAS")
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Borramos los textos y desmarcamos el checkbox
        etNombre.text = null
        etEmail.text = null
        etTelefono.text = null
        etCiudad.text = null
        cbCondiciones.isChecked = false
        spNivel.setSelection(0)
        limpiarErrores()
        etNombre.requestFocus()
    }

    private fun limpiarErrores() {
        layoutNombre.error = null
        layoutEmail.error = null
        layoutTelefono.error = null
        layoutCiudad.error = null
    }
}