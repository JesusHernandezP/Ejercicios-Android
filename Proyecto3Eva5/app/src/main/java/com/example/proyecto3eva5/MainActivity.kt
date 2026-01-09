package com.example.proyecto3eva5

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ESTADO
            var isDarkTheme by remember { mutableStateOf(false) }

            val colors = if (isDarkTheme) {
                darkColorScheme(
                    primary = Color(0xFF80CBC4), // Teal claro
                    onPrimary = Color(0xFF004D40),
                    background = Color(0xFF121212),
                    surface = Color(0xFF1E1E1E)
                )
            } else {
                lightColorScheme(
                    primary = Color(0xFF00695C), // Teal oscuro
                    onPrimary = Color.White,
                    background = Color(0xFFF5F5F5),
                    surface = Color.White
                )
            }

            MaterialTheme(colorScheme = colors) {
                MainScreen(isDarkTheme) { nuevoValor -> isDarkTheme = nuevoValor }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var pantallActual by remember { mutableStateOf("Inicio") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                            )
                        ),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Jesus Hernandez", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("jesus.dev@android.com", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // OPCIONES DE MENÚ
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = pantallActual == "Inicio",
                    icon = { Icon(Icons.Outlined.Home, null) },
                    onClick = { pantallActual = "Inicio"; scope.launch { drawerState.close() } },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Mi Perfil") },
                    selected = pantallActual == "Perfil",
                    icon = { Icon(Icons.Outlined.Person, null) },
                    onClick = { pantallActual = "Perfil"; scope.launch { drawerState.close() } },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Ajustes") },
                    selected = pantallActual == "Ajustes",
                    icon = { Icon(Icons.Outlined.Settings, null) },
                    onClick = { pantallActual = "Ajustes"; scope.launch { drawerState.close() } },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(pantallActual, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Menú")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                when (pantallActual) {
                    "Inicio" -> ContenidoInicio()
                    "Perfil" -> ContenidoPerfil()
                    "Ajustes" -> ContenidoAjustes(isDarkTheme, onThemeChange)
                }
            }
        }
    }
}

// PANTALLA 1: INICIO
@Composable
fun ContenidoInicio() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Hola, Jesus 👋", color = MaterialTheme.colorScheme.onPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Tienes 3 tareas para hoy", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                }
                Icon(Icons.Default.Notifications, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(32.dp))
            }
        }

        Text("Resumen", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // TAREAS
            DashboardItem(
                modifier = Modifier.weight(1f),
                titulo = "Tareas",
                dato = "12",
                icono = Icons.Default.CheckCircle,
                colorIcono = Color(0xFF66BB6A) // Verde
            )
            // MENSAJES
            DashboardItem(
                modifier = Modifier.weight(1f),
                titulo = "Mensajes",
                dato = "5",
                icono = Icons.Default.Email,
                colorIcono = Color(0xFF42A5F5) // Azul
            )
        }

        // PROGRESO
        DashboardItem(
            modifier = Modifier.fillMaxWidth(),
            titulo = "Progreso del curso",
            dato = "72%",
            icono = Icons.Default.Star,
            colorIcono = Color(0xFFFFCA28) // Amarillo
        )
    }
}

@Composable
fun DashboardItem(modifier: Modifier = Modifier, titulo: String, dato: String, icono: ImageVector, colorIcono: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icono, contentDescription = null, tint = colorIcono, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(dato, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(titulo, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}

//  PANTALLA 2: PERFIL
@Composable
fun ContenidoPerfil() {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

        Box(modifier = Modifier.height(240.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFF263238))
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.BottomCenter)
                    .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFF009688)),
                contentAlignment = Alignment.Center
            ) {
                Text("JD", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        // DATOS DEL USUARIO
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Jesus Hernandez", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Text("Desarrollador Junior Android", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta de Información
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ItemPerfil(Icons.Default.Email, "Email", "jesus.dev@android.com")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ItemPerfil(Icons.Default.Phone, "Teléfono", "+34 666 123 456")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ItemPerfil(Icons.Default.LocationOn, "Ubicación", "Madrid, España")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ItemPerfil(Icons.Default.Star, "Experiencia", "1 Año en Android")


                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { Toast.makeText(context, "Perfil editado", Toast.LENGTH_SHORT).show() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Editar Perfil")
            }
        }
    }
}

@Composable
fun ItemPerfil(icono: ImageVector, label: String, valor: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(valor, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

//PANTALLA 3: AJUSTES
@Composable
fun ContenidoAjustes(isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    var notificaciones by remember { mutableStateOf(true) }
    var descargas by remember { mutableStateOf(false) }


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("General", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if(isDarkTheme) Icons.Default.Star else Icons.Default.Face, null, tint = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Modo Oscuro", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text(if(isDarkTheme) "Activado" else "Desactivado", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Switch(checked = isDarkTheme, onCheckedChange = onThemeChange)
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, null, tint = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Notificaciones", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Recibir alertas push", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Switch(checked = notificaciones, onCheckedChange = { notificaciones = it })
                }
                Divider(modifier = Modifier.padding(vertical = 12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, null, tint = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Descargas automáticas", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Actualizar contenido solo con WI-FI", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Switch(checked = descargas, onCheckedChange = { descargas = it })
                }
            }
        }
    }
}