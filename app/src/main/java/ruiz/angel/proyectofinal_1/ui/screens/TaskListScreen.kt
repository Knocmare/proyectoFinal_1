package ruiz.angel.proyectofinal_1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ruiz.angel.proyectofinal_1.data.models.Event
import ruiz.angel.proyectofinal_1.data.models.Subtask
import ruiz.angel.proyectofinal_1.data.models.Task
import ruiz.angel.proyectofinal_1.ui.theme.Azul
import ruiz.angel.proyectofinal_1.ui.theme.AzulClaro
import ruiz.angel.proyectofinal_1.ui.theme.Borde
import ruiz.angel.proyectofinal_1.ui.theme.FondoPantalla
import ruiz.angel.proyectofinal_1.ui.theme.FondoSubtarea
import ruiz.angel.proyectofinal_1.ui.theme.FondoTarjeta
import ruiz.angel.proyectofinal_1.ui.theme.Verde
import ruiz.angel.proyectofinal_1.ui.theme.VerdeFondo
import ruiz.angel.proyectofinal_1.viewModel.EventsViewModel

@Composable
fun TaskListScreen(
    viewModel: EventsViewModel,
    name: String,
    email: String,
    iniciales: String,
    onCreateEvent: () -> Unit = {},
    onAddTask: (Long) -> Unit = {},
    onOpenTask: (Long) -> Unit = {},
    onOpenSummary: (Long) -> Unit = {},
    onOpenAccount: () -> Unit = {},
    onLeave: () -> Unit = {}
) {
    val events = viewModel.eventsListState

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
                name = name,
                email = email,
                iniciales = iniciales,
                onOpenAccount = onOpenAccount,
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
                    Text(text = "${events.size} evento${if (events.size == 1) "" else "s"}", fontSize = 13.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (events.isEmpty()) {
                    Text(
                        "Aún no tienes eventos. Crea el primero con el botón de abajo.",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }

                events.forEach { event ->
                    EventCard(
                        event = event,
                        onToggleTask = { taskId, completed -> viewModel.toggleTask(taskId, completed) },
                        onToggleSubtask = { subtaskId, completed -> viewModel.toggleSubtask(subtaskId, completed) },
                        onOpenTask = onOpenTask,
                        onAddTask = { onAddTask(event.id) },
                        onOpenSummary = { onOpenSummary(event.id) },
                        onDeleteEvent = { viewModel.deleteEvent(event.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun UserHeader(
    name: String,
    email: String,
    iniciales: String,
    onOpenAccount: () -> Unit,
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onOpenAccount() }
            ) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onOpenAccount) {
                    Icon(Icons.Filled.Settings, contentDescription = "Configuración de cuenta", tint = Color.White)
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
}

@Composable
fun EventCard(
    event: Event,
    onToggleTask: (Long, Boolean) -> Unit,
    onToggleSubtask: (Long, Boolean) -> Unit,
    onOpenTask: (Long) -> Unit,
    onAddTask: () -> Unit,
    onOpenSummary: () -> Unit,
    onDeleteEvent: () -> Unit = {}
) {
    var expandedTaskId by remember { mutableStateOf<Long?>(null) }

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
                    text = event.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Text(text = String.format("$%,d", event.estimated), fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Azul)
                IconButton(onClick = onDeleteEvent, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar evento", tint = Color.Gray, modifier = Modifier.size(16.dp))
                }
            }

            Text(
                text = "${event.taskCount} tareas · ${event.taskCompletedCount} completada · ${event.date}",
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
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ver resumen →",
                    fontSize = 11.sp,
                    color = Azul,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onOpenSummary() }
                )
                Text(
                    text = "${event.taskCompletedCount} de ${event.taskCount} tareas",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.End
                )
            }

            HorizontalDivider(color = Borde, thickness = 0.5.dp)

            event.tasks.forEachIndexed { index, task ->
                if (task.completed) {
                    TaskCompleted(
                        task = task,
                        onClick = { onToggleTask(task.id, !task.completed) },
                        onManage = { onOpenTask(task.id) }
                    )
                } else {
                    TaskRow(
                        task = task,
                        expandida = expandedTaskId == task.id,
                        onClickTask = {
                            expandedTaskId = if (expandedTaskId == task.id) null else task.id
                        },
                        onToggleTask = { onToggleTask(task.id, !task.completed) },
                        onToggleSubtask = { subtaskId, completed -> onToggleSubtask(subtaskId, completed) },
                        onManageSubtasks = { onOpenTask(task.id) }
                    )
                }
                if (index < event.tasks.lastIndex) {
                    HorizontalDivider(color = Borde, thickness = 0.5.dp)
                }
            }

            Row(
                modifier = Modifier
                    .clickable { onAddTask() }
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
    task: Task,
    expandida: Boolean,
    onClickTask: () -> Unit,
    onToggleTask: () -> Unit,
    onToggleSubtask: (Long, Boolean) -> Unit,
    onManageSubtasks: () -> Unit
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
                text = task.name,
                fontSize = 14.sp,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClickTask() }
            )
            Text(
                text = if (task.subtaskCount > 0) "${task.subtaskCount} sub ›" else "Gestionar ›",
                fontSize = 12.sp,
                color = Azul,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clickable { onManageSubtasks() }
            )
            Text(text = String.format("$%,d", task.price), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Azul)
        }

        if (expandida && task.subtasks.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(FondoSubtarea)
            ) {
                task.subtasks.forEachIndexed { i, subtask ->
                    SubtaskRow(
                        subtask = subtask,
                        onToggle = { onToggleSubtask(subtask.id, !subtask.completed) }
                    )
                    if (i < task.subtasks.lastIndex) {
                        HorizontalDivider(color = Borde, thickness = 0.5.dp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun SubtaskRow(subtask: Subtask, onToggle: () -> Unit) {
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
                .background(if (subtask.completed) Azul else Color.White)
                .border(1.5.dp, if (subtask.completed) Azul else Color.LightGray, RoundedCornerShape(3.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (subtask.completed) {
                Text(text = "✓", color = Color.White, fontSize = 10.sp)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = subtask.name,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f),
            color = if (subtask.completed) Color.Gray else Color.Black,
            textDecoration = if (subtask.completed) TextDecoration.LineThrough else TextDecoration.None
        )
        Text(text = String.format("$%,d", subtask.estimatedPrice), fontSize = 13.sp, color = Color.Gray)
    }
}

@Composable
fun TaskCompleted(task: Task, onClick: () -> Unit, onManage: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(VerdeFondo)
            .clickable { onManage() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(Verde)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "✓", color = Color.White, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = task.name,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.weight(1f),
            textDecoration = TextDecoration.LineThrough
        )
        Text(text = "✓ lista", fontSize = 12.sp, color = Verde, modifier = Modifier.padding(end = 8.dp))
        Text(text = String.format("$%,d", task.price), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Verde)
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
