package com.example.proyecto3eva4

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF00695C),
                    secondary = Color(0xFFE57373)
                )
            ) {
                ListaTareasScreen()
            }
        }
    }
}

// 1. MODELO DE DATOS
data class Tarea(
    val id: Long,
    val texto: String,
    val completada: Boolean,
    val esUrgente: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTareasScreen() {
    val context = LocalContext.current

    // ESTADO
    val tareas = remember { mutableStateListOf<Tarea>() }
    var nuevoTexto by remember { mutableStateOf("") }
    var esUrgenteInput by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas", fontWeight = FontWeight.Bold) },
                actions = {
                    // Botón Borrar Completadas
                    IconButton(onClick = {
                        val cantidad = tareas.count { it.completada }
                        if (cantidad > 0) {
                            tareas.removeAll { it.completada }
                            Toast.makeText(context, "Limpieza completada ($cantidad)", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "No hay tareas completadas", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Limpiar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // CREACIÓN DE TAREAS
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = nuevoTexto,
                        onValueChange = { nuevoTexto = it },
                        label = { Text("Nueva tarea...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = esUrgenteInput,
                                onCheckedChange = { esUrgenteInput = it },
                                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.secondary)
                            )
                            Text("¡Es urgente!", fontSize = 14.sp, color = if(esUrgenteInput) MaterialTheme.colorScheme.secondary else Color.Gray)
                        }

                        // Añadir
                        Button(
                            onClick = {
                                if (nuevoTexto.isNotBlank()) {
                                    tareas.add(0, Tarea(System.currentTimeMillis(), nuevoTexto, false, esUrgenteInput))
                                    nuevoTexto = ""
                                    esUrgenteInput = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Add, null)
                            Spacer(Modifier.width(4.dp))
                            Text("Añadir")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (tareas.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "¡Todo listo!",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Gray
                        )
                        Text(
                            "Añade una tarea para empezar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tareas, key = { it.id }) { tarea ->
                        ItemTareaPro(
                            tarea = tarea,
                            onCheckChange = { isChecked ->
                                val index = tareas.indexOfFirst { it.id == tarea.id }
                                if (index != -1) {
                                    tareas[index] = tarea.copy(completada = isChecked)
                                }
                            },
                            onDeleteClick = {
                                tareas.remove(tarea)
                            }
                        )
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(50),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
            ) {
                Text(
                    text = "Completadas: ${tareas.count { it.completada }} / ${tareas.size}",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ItemTareaPro(
    tarea: Tarea,
    onCheckChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("¿Eliminar tarea?") },
            text = { Text("¿Seguro que quieres borrar '${tarea.texto}'?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteClick()
                    mostrarDialogo = false
                }) {
                    Text("Borrar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }
            }
        )
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        border = if (tarea.esUrgente && !tarea.completada) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE57373)) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = onCheckChange,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )

            Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                if (tarea.esUrgente && !tarea.completada) {
                    Text(
                        "URGENTE",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = tarea.texto,
                    style = if (tarea.completada) {
                        TextStyle(textDecoration = TextDecoration.LineThrough, color = Color.Gray)
                    } else {
                        TextStyle(textDecoration = TextDecoration.None, color = Color.Black, fontSize = 16.sp)
                    }
                )
            }

            IconButton(onClick = { mostrarDialogo = true }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Borrar",
                    tint = Color.Gray.copy(alpha = 0.6f)
                )
            }
        }
    }
}