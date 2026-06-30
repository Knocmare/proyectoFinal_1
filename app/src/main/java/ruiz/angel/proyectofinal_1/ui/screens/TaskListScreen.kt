package ruiz.angel.proyectofinal_1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ruiz.angel.proyectofinal_1.data.models.Evento
import ruiz.angel.proyectofinal_1.data.models.Subtarea
import ruiz.angel.proyectofinal_1.data.models.Tarea
import ruiz.angel.proyectofinal_1.ui.theme.Azul
import ruiz.angel.proyectofinal_1.ui.theme.AzulClaro
import ruiz.angel.proyectofinal_1.ui.theme.Borde
import ruiz.angel.proyectofinal_1.ui.theme.FondoPantalla
import ruiz.angel.proyectofinal_1.ui.theme.FondoSubtarea
import ruiz.angel.proyectofinal_1.ui.theme.FondoTarjeta
import ruiz.angel.proyectofinal_1.ui.theme.Verde
import ruiz.angel.proyectofinal_1.ui.theme.VerdeFondo

@Composable
fun TaskListScreen(
    event: Evento,
    onCreateEvent: () -> Unit = {},
    onLeave: () -> Unit = {}
) {
    var tareas by remember {
        mutableStateOf(event.tareas)
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FondoPantalla)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onCreateEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Azul,
                        contentColor = Color.White
                    )
                ) {
                    Text("Crear evento", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantalla)
                .padding(innerPadding)
        ) {
            UserHeader(
                name = event.nombreUsuario,
                email = event.emailUsuario,
                iniciales = event.inicialesUsuario,
                onLeave = onLeave
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mis eventos",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(text = "1 evento", fontSize = 13.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(12.dp))

                EventCard(
                    event = event.copy(
                        tareas = tareas
                    ),
                    onToggleTask = { tareaIndex ->
                        tareas = tareas.toMutableList().also { list ->
                            val tarea = list[tareaIndex]
                            list[tareaIndex] = tarea.copy(completada = !tarea.completada)
                        }
                    },
                    onToggleSubtask = { tareaIndex, subtareaIndex ->
                        tareas = tareas.toMutableList().also { list ->
                            val tarea = list[tareaIndex]
                            val subs = tarea.subtareas.toMutableList()
                            subs[subtareaIndex] = subs[subtareaIndex].copy(completed = !subs[subtareaIndex].completed)
                            val todasCompletas = subs.all { it.completed }
                            list[tareaIndex] = tarea.copy(subtareas = subs, completada = todasCompletas)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun UserHeader(
    name: String,
    email: String,
    iniciales: String,
    onLeave: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Azul)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(AzulClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iniciales,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text(text = email, color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
                }
            }
            Button(
                onClick = onLeave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulClaro,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Salir", fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun EventCard(
    event: Evento,
    onToggleTask: (Int) -> Unit,
    onToggleSubtask: (Int, Int) -> Unit
) {
    var expandedTaskIndex by remember { mutableStateOf(-1) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Azul)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = event.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Text(text = String.format("$%,d", event.estimated), fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Azul)
                Text(text = " ▾", color = Azul, fontSize = 14.sp)
            }

            Text(
                text = "${event.taskCount} tareas · ${event.taskCompletedCount} completada · ${event.fecha}",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 18.dp, top = 2.dp, bottom = 8.dp)
            )

            LinearProgressIndicator(
                progress = { event.taskCompletedCount.toFloat() / event.taskCount.coerceAtLeast(1) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Azul,
                trackColor = Color(0xFFE0E0E0)
            )
            Text(
                text = "${event.taskCompletedCount} de ${event.taskCount} tareas",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp),
                textAlign = TextAlign.End
            )

            HorizontalDivider(color = Borde, thickness = 0.5.dp)

            event.tareas.forEachIndexed { index, tarea ->
                if (tarea.completada) {
                    TaskCompleted(
                        tarea = tarea,
                        onClick = { onToggleTask(index) }
                    )
                } else {
                    TaskRow(
                        tarea = tarea,
                        expandida = expandedTaskIndex == index,
                        onClickTask = {
                            expandedTaskIndex = if (expandedTaskIndex == index) -1 else index
                        },
                        onToggleTask = { onToggleTask(index) },
                        onToggleSubtask = { subIndex -> onToggleSubtask(index, subIndex) }
                    )
                }
                if (index < event.tareas.lastIndex) {
                    HorizontalDivider(color = Borde, thickness = 0.5.dp)
                }
            }

            Row(
                modifier = Modifier
                    .clickable { }
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "+ ", fontSize = 13.sp, color = Azul, fontWeight = FontWeight.Medium)
                Text("Agregar tarea", fontSize = 13.sp, color = Azul)
            }

            HorizontalDivider(color = Borde, thickness = 0.5.dp, modifier = Modifier.padding(top = 12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BudgetColumn("ESTIMADO", String.format("$%,d", event.estimated), Azul)
                BudgetColumn("GASTADO", String.format("$%,d", event.spent), Azul)
                BudgetColumn("RESTANTE", String.format("$%,d", event.remaining), Color.Black)
            }
        }
    }
}

@Composable
fun TaskRow(
    tarea: Tarea,
    expandida: Boolean,
    onClickTask: () -> Unit,
    onToggleTask: () -> Unit,
    onToggleSubtask: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, Color.LightGray, CircleShape)
                    .clickable { onToggleTask() }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = tarea.nombre,
                fontSize = 14.sp,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClickTask() }
            )
            if (tarea.subCount > 0) {
                Text(
                    text = "${tarea.subCount} sub ›",
                    fontSize = 12.sp,
                    color = Azul,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable { onClickTask() }
                )
            }
            Text(text = String.format("$%,d", tarea.precio), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Azul)
        }

        if (expandida && tarea.subtareas.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(FondoSubtarea)
            ) {
                tarea.subtareas.forEachIndexed { i, subtarea ->
                    SubtaskRow(
                        subtarea = subtarea,
                        onToggle = { onToggleSubtask(i) }
                    )
                    if (i < tarea.subtareas.lastIndex) {
                        HorizontalDivider(color = Borde, thickness = 0.5.dp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun SubtaskRow(subtarea: Subtarea, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(17.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(if (subtarea.completed) Azul else Color.White)
                .border(1.5.dp, if (subtarea.completed) Azul else Color.LightGray, RoundedCornerShape(3.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (subtarea.completed) {
                Text(text = "✓", color = Color.White, fontSize = 10.sp)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = subtarea.name,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f),
            color = if (subtarea.completed) Color.Gray else Color.Black,
            textDecoration = if (subtarea.completed) TextDecoration.LineThrough else TextDecoration.None
        )
        Text(text = String.format("$%,d", subtarea.price), fontSize = 13.sp, color = Color.Gray)
    }
}

@Composable
fun TaskCompleted(tarea: Tarea, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(VerdeFondo)
            .clickable { onClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(Verde),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "✓", color = Color.White, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = tarea.nombre,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.weight(1f),
            textDecoration = TextDecoration.LineThrough
        )
        Text(text = "✓ lista", fontSize = 12.sp, color = Verde, modifier = Modifier.padding(end = 8.dp))
        Text(text = String.format("$%,d", tarea.precio), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Verde)
    }
}

@Composable
fun BudgetColumn(label: String, valor: String, colorValor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = Color.Gray, letterSpacing = 0.5.sp)
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = valor, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = colorValor)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskListScreenPreview() {
    TaskListScreen(
        event = Evento(
            nombre = "Boda",
            fecha = "15 Jul 2026",
            nombreUsuario = "Joel Ruben",
            emailUsuario = "joel@itson.edu.mx",
            inicialesUsuario = "JR",
            tareas = listOf(
                Tarea(
                    nombre = "Salón de eventos",
                    completada = false,
                    subtareas = listOf(
                        Subtarea("Cotización de salones", 0, completed = true),
                        Subtarea("Reserva del salón", 5000, completed = false),
                        Subtarea("Decoración", 7000, completed = false),
                    )
                ),
                Tarea("Comida", completada = false, subtareas = listOf(
                    Subtarea("Carne Azada", 5000, completed = false)
                )),
                Tarea("Fotografía y video", completada = false),
                Tarea("Invitaciones", completada = true, subtareas = listOf(
                    Subtarea("Crear invitaciones", 500, completed = true),
                    Subtarea("Entregar invitaciones", 0, completed = true)
                ))
            )
        )
    )
}
