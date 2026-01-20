package com.example.adivinanumero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.random.Random


enum class EstadoMensaje { NEUTRAL, PISTA, ERROR, EXITO }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaAdivinaNumero()
                }
            }
        }
    }
}

@Composable
fun PantallaAdivinaNumero() {
    var numeroSecreto by remember { mutableIntStateOf(Random.nextInt(1, 101)) }
    var entradaUsuario by remember { mutableStateOf("") }
    var intentos by remember { mutableIntStateOf(0) }
    var mensaje by remember { mutableStateOf("🎯 ¡Escribe un número del 1 al 100!") }
    var juegoTerminado by remember { mutableStateOf(false) }
    var estadoMensaje by remember { mutableStateOf(EstadoMensaje.NEUTRAL) }

    var historial by remember { mutableStateOf(listOf<Int>()) }
    var ultimaDiferencia by remember { mutableIntStateOf(100) }

    val focusRequester = remember { FocusRequester() }

    fun reiniciarJuego() {
        numeroSecreto = Random.nextInt(1, 101)
        entradaUsuario = ""
        intentos = 0
        mensaje = "🎮 ¡Nueva partida! Elige un número del 1 al 100."
        juegoTerminado = false
        estadoMensaje = EstadoMensaje.NEUTRAL
        historial = emptyList()
        ultimaDiferencia = 100
        focusRequester.requestFocus()
    }

    fun evaluarDesempeno(ints: Int): String =
        when {
            ints <= 3 -> "🌟 ¡Lo lograste rapidísimo!"
            ints in 4..7 -> "👍 ¡Bien hecho!"
            else -> "💪 ¡Puedes mejorar! 😉"
        }

    fun procesarIntento() {
        if (juegoTerminado) return

        val texto = entradaUsuario.trim()

        if (texto.isEmpty()) {
            estadoMensaje = EstadoMensaje.ERROR
            mensaje = "⚠️ ¡Escribe un número para jugar!"
            return
        }

        val intento = texto.toIntOrNull()
        if (intento == null) {
            estadoMensaje = EstadoMensaje.ERROR
            mensaje = "🤖 Eso no es un número. Prueba otra vez 😉"
            entradaUsuario = ""
            focusRequester.requestFocus()
            return
        }

        if (intento !in 1..100) {
            estadoMensaje = EstadoMensaje.ERROR
            mensaje = "🚫 Debe estar entre 1 y 100."
            entradaUsuario = ""
            focusRequester.requestFocus()
            return
        }

        intentos++
        historial = historial + intento
        ultimaDiferencia = abs(numeroSecreto - intento)

        when {
            intento < numeroSecreto -> {
                estadoMensaje = EstadoMensaje.PISTA
                mensaje = "📈 ¡Más alto! El número secreto es mayor que $intento."
            }
            intento > numeroSecreto -> {
                estadoMensaje = EstadoMensaje.PISTA
                mensaje = "📉 ¡Más bajo! El número secreto es menor que $intento."
            }
            else -> {
                estadoMensaje = EstadoMensaje.EXITO
                juegoTerminado = true
                mensaje = "🏆 ¡Correcto! Era $numeroSecreto.\n" +
                        "🔢 Intentos: $intentos\n\n" +
                        evaluarDesempeno(intentos)
            }
        }

        entradaUsuario = ""
        focusRequester.requestFocus()
    }

    val colorContenedor = when (estadoMensaje) {
        EstadoMensaje.EXITO -> Color(0xFF00C853)
        EstadoMensaje.ERROR -> MaterialTheme.colorScheme.errorContainer
        EstadoMensaje.PISTA -> MaterialTheme.colorScheme.secondaryContainer
        EstadoMensaje.NEUTRAL -> MaterialTheme.colorScheme.surfaceVariant
    }

    val colorTexto = when (estadoMensaje) {
        EstadoMensaje.EXITO -> Color.White
        EstadoMensaje.ERROR -> MaterialTheme.colorScheme.onErrorContainer
        EstadoMensaje.PISTA -> MaterialTheme.colorScheme.onSecondaryContainer
        EstadoMensaje.NEUTRAL -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val progresoCercania = (1f - (ultimaDiferencia.coerceIn(0, 100) / 100f)).coerceIn(0f, 1f)
    val textoCercania = when {
        juegoTerminado -> "🏁 Partida finalizada"
        ultimaDiferencia >= 70 -> "🧊 Muy frío"
        ultimaDiferencia >= 40 -> "❄️ Frío"
        ultimaDiferencia >= 20 -> "🌤️ Tibio"
        ultimaDiferencia >= 10 -> "🔥 Caliente"
        else -> "🌋 ¡Ardiendo!"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🕹️ Adivina el Número",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                   Text("📌 Intentos: $intentos", fontWeight = FontWeight.SemiBold)

                    OutlinedTextField(
                        value = entradaUsuario,
                        onValueChange = { entradaUsuario = it },
                        label = { Text("Un numero del 1 al 100") },
                        singleLine = true,
                        enabled = !juegoTerminado,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { procesarIntento() }
                        )
                    )


                    Button(
                        onClick = { procesarIntento() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !juegoTerminado
                    ) { Text("🎯 Probar") }

                    OutlinedButton(
                        onClick = { reiniciarJuego() },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("🔄 Nueva partida") }

                    Text("📍 Pista: $textoCercania", fontWeight = FontWeight.SemiBold)

                    LinearProgressIndicator(
                        progress = { progresoCercania },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )



                    OutlinedButton(
                        onClick = {
                            juegoTerminado = true
                            estadoMensaje = EstadoMensaje.ERROR
                            mensaje = "🏳️ Te rendiste 😅 El número era $numeroSecreto.\n" +
                                    "Vuelve a intentarlo con 'Nueva partida'."
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !juegoTerminado
                    ) { Text("🏳️ Rendirse") }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colorContenedor,
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = mensaje,
                    color = colorTexto,
                    modifier = Modifier.padding(14.dp),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
