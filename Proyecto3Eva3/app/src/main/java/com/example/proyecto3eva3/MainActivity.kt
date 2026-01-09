package com.example.proyecto3eva3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF009688), // Teal principal
                    secondary = Color(0xFF00796B)
                )
            ) {
                CatalogoCursosScreen()
            }
        }
    }
}

// 1. MODELO DE DATOS
data class Curso(
    val titulo: String,
    val nivel: String,
    val horas: Int,
    val etiquetas: List<String>,
    val colorNivel: Color
)

// 2. DATOS DE EJEMPLO
val listaCursos = listOf(
    Curso("Introducción a Kotlin", "Básico", 12, listOf("Kotlin", "Android", "Fundamentos"), Color(0xFFC8E6C9)),
    Curso("Android con Jetpack Compose", "Intermedio", 20, listOf("Compose", "UI moderna", "Material3"), Color(0xFFFFF9C4)),
    Curso("Arquitectura MVVM", "Avanzado", 18, listOf("Arquitectura", "Clean Code"), Color(0xFFFFCDD2)),
    Curso("Consumo de APIs REST", "Intermedio", 16, listOf("Retrofit", "JSON", "Corrutinas"), Color(0xFFFFF9C4)),
    Curso("Testing en Android", "Avanzado", 14, listOf("JUnit", "Mockk", "Espresso"), Color(0xFFFFCDD2)),
    Curso("Diseño UX/UI Móvil", "Básico", 10, listOf("Figma", "Material Design"), Color(0xFFC8E6C9))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoCursosScreen() {
    val context = LocalContext.current

    // ESTADOS PARA LA LÓGICA
    var textoBusqueda by remember { mutableStateOf("") }
    var nivelFiltro by remember { mutableStateOf("Todos") }
    val seleccionados = remember { mutableStateListOf<String>() }


    val cursosFiltrados = listaCursos.filter { curso ->
        val coincideTexto = curso.titulo.contains(textoBusqueda, ignoreCase = true)
        val coincideNivel = if (nivelFiltro == "Todos") true else curso.nivel == nivelFiltro
        coincideTexto && coincideNivel
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Academia Android", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Menu, "Menú") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                // 1. Campo de Búsqueda
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    placeholder = { Text("Buscar curso...") },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Filtro
                val niveles = listOf("Todos", "Básico", "Intermedio", "Avanzado")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(niveles) { nivel ->
                        FilterChipPropio(
                            texto = nivel,
                            seleccionado = nivelFiltro == nivel,
                            alClick = { nivelFiltro = nivel }
                        )
                    }
                }
            }

            // --- LISTA DE CURSOS ---
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cursosFiltrados) { curso ->
                    val isSelected = seleccionados.contains(curso.titulo)

                    ItemCursoMejorado(
                        curso = curso,
                        isSelected = isSelected,
                        onClick = {
                            if (isSelected) seleccionados.remove(curso.titulo)
                            else seleccionados.add(curso.titulo)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterChipPropio(texto: String, seleccionado: Boolean, alClick: () -> Unit) {
    Surface(
        color = if (seleccionado) MaterialTheme.colorScheme.primary else Color(0xFFEEEEEE),
        contentColor = if (seleccionado) Color.White else Color.Black,
        shape = RoundedCornerShape(50),
        modifier = Modifier.clickable { alClick() }
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ItemCursoMejorado(curso: Curso, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFFE0F2F1) else Color.White
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {

                Surface(
                    color = curso.colorNivel,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = curso.nivel.uppercase(),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                }

                Text(
                    text = curso.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF212121)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    curso.etiquetas.take(2).forEach { tag ->
                        Text(
                            text = "#$tag",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFB300), // Amarillo estrella
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${curso.horas}h",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}