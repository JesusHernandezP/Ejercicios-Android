package com.example.proyecto4eva2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaHundirFlota()
                }
            }
        }
    }
}

enum class EstadoCasilla { OCULTA, AGUA, TOCADO, BARCO }

@Composable
fun PantallaHundirFlota() {

    fun generarBarcos(): Set<Pair<Int, Int>> {
        val posiciones = mutableSetOf<Pair<Int, Int>>()
        while (posiciones.size < 3) {
            val fila = (0..4).random()
            val col = (0..4).random()
            posiciones.add(Pair(fila, col))
        }
        return posiciones
    }

    fun evaluarPrecision(porcentaje: Int): String {
        return when {
            porcentaje >= 60 -> "🌟 Excelente puntería"
            porcentaje >= 30 -> "👍 Aceptable"
            else -> "💪 A mejorar"
        }
    }

    var tablero by remember { mutableStateOf(List(5) { List(5) { EstadoCasilla.OCULTA } }) }
    var barcos by remember { mutableStateOf(generarBarcos()) }

    var disparos by remember { mutableIntStateOf(0) }
    var aciertos by remember { mutableIntStateOf(0) }
    var juegoTerminado by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("🎯 Toca una casilla para disparar") }

    fun nuevaPartida() {
        tablero = List(5) { List(5) { EstadoCasilla.OCULTA } }
        barcos = generarBarcos()
        disparos = 0
        aciertos = 0
        juegoTerminado = false
        mensaje = "🎮 Nueva partida. ¡Buena suerte!"
    }

    val precision = if (disparos == 0) 0 else ((aciertos.toFloat() / disparos) * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🚢 Hundir la Flota (5×5)",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )

        // HUD
        Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("🎯 $disparos", fontWeight = FontWeight.SemiBold)
                Text("💥 $aciertos/3", fontWeight = FontWeight.SemiBold)
                Text("📈 $precision%", fontWeight = FontWeight.SemiBold)
            }
        }

        // Mensaje
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = if (juegoTerminado) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                text = mensaje,
                modifier = Modifier.padding(12.dp),
                fontWeight = FontWeight.Medium
            )
        }

        // Tablero 5x5
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for (fila in 0 until 5) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (col in 0 until 5) {

                        Casilla(
                            estado = tablero[fila][col],
                            deshabilitada = juegoTerminado,
                            onClick = {
                                if (juegoTerminado) {
                                    mensaje = "🏁 La partida terminó. Pulsa 'Nueva partida'."
                                    return@Casilla
                                }

                                if (tablero[fila][col] != EstadoCasilla.OCULTA) {
                                    mensaje = "⚠ Ya disparaste aquí"
                                    return@Casilla
                                }

                                disparos++
                                val esBarco = barcos.contains(Pair(fila, col))

                                tablero = tablero.mapIndexed { fi, row ->
                                    row.mapIndexed { co, cell ->
                                        when {
                                            fi == fila && co == col && esBarco -> EstadoCasilla.TOCADO
                                            fi == fila && co == col && !esBarco -> EstadoCasilla.AGUA
                                            else -> cell
                                        }
                                    }
                                }

                                if (esBarco) {
                                    aciertos++
                                    mensaje = "💥 ¡Tocado! ($aciertos/3)"

                                    if (aciertos == 3) {
                                        juegoTerminado = true
                                        val porcentaje = ((aciertos.toFloat() / disparos) * 100).toInt()
                                        val evaluacion = evaluarPrecision(porcentaje)

                                        // Revelar barcos restantes
                                        tablero = tablero.mapIndexed { fi, row ->
                                            row.mapIndexed { co, cell ->
                                                if (barcos.contains(Pair(fi, co)) && cell == EstadoCasilla.OCULTA)
                                                    EstadoCasilla.BARCO
                                                else cell
                                            }
                                        }

                                        mensaje = "🏆 ¡Hundiste los 3 barcos!\n" +
                                                "🎯 Disparos: $disparos | 💥 Aciertos: $aciertos\n" +
                                                "📈 Precisión: $porcentaje% → $evaluacion"
                                    }
                                } else {
                                    mensaje = "🌊 Agua... sigue intentando"
                                }
                            }
                        )
                    }
                }
            }
        }

        // Botón nueva partida
        Button(
            onClick = { nuevaPartida() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🔄 Nueva partida")
        }
    }
}

@Composable
fun Casilla(estado: EstadoCasilla, deshabilitada: Boolean, onClick: () -> Unit) {
    val (texto, colores) = when (estado) {
        EstadoCasilla.OCULTA -> " " to ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
        EstadoCasilla.AGUA -> "🌊" to ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
        EstadoCasilla.TOCADO -> "💥" to ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
        EstadoCasilla.BARCO -> "🚢" to ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }

    Button(
        onClick = onClick,
        enabled = !deshabilitada,
        modifier = Modifier.size(56.dp),
        colors = colores,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(texto, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
