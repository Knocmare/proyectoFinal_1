package ruiz.angel.proyectofinal_1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import ruiz.angel.proyectofinal_1.ui.theme.ProyectoFinal_1Theme
import ruiz.angel.proyectofinal_1.data.models.Subtask
import ruiz.angel.proyectofinal_1.data.models.Task
import ruiz.angel.proyectofinal_1.ui.theme.Azul
import ruiz.angel.proyectofinal_1.ui.theme.FondoPantalla
import ruiz.angel.proyectofinal_1.ui.theme.FondoTarjeta
import ruiz.angel.proyectofinal_1.ui.theme.Verde
import ruiz.angel.proyectofinal_1.viewModel.EventsViewModel
import ruiz.angel.proyectofinal_1.viewModel.SubtasksViewModel
import ruiz.angel.proyectofinal_1.viewModel.TasksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtaskListScreen(
    taskId: Long,
    eventsViewModel: EventsViewModel,
    tasksViewModel: TasksViewModel,
    subtasksViewModel: SubtasksViewModel,
    onBackClick: () -> Unit,
    onCompareClick: (Long) -> Unit,
    onAddSubtask: (Long) -> Unit,
    onEditSubtask: (Long) -> Unit
) {
    val task = eventsViewModel.eventsListState
        .flatMap { it.tasks }
        .firstOrNull { it.id == taskId }

    SubtaskListContent(
        task = task,
        onBackClick = onBackClick,
        onDeleteTask = {
            tasksViewModel.deleteTask(taskId)
            onBackClick()
        },
        onAddSubtask = onAddSubtask,
        onToggleSubtask = { subtask ->
            subtasksViewModel.toggleSubtask(subtask.id, !subtask.completed)
        },
        onEditSubtask = onEditSubtask,
        onDeleteSubtask = { subtaskId ->
            subtasksViewModel.deleteSubtask(subtaskId)
        },
        onCompareSubtask = onCompareClick,
        onUpdateTask = { name, description, estimatedPrice ->
            if (task != null) {
                tasksViewModel.updateTask(
                    taskId = task.id,
                    eventId = task.eventId,
                    name = name,
                    description = description,
                    estimatedPrice = estimatedPrice,
                    realPrice = task.realPrice,
                    completed = task.completed
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtaskListContent(
    task: Task?,
    onBackClick: () -> Unit,
    onDeleteTask: (Long) -> Unit,
    onAddSubtask: (Long) -> Unit,
    onToggleSubtask: (Subtask) -> Unit,
    onEditSubtask: (Long) -> Unit,
    onDeleteSubtask: (Long) -> Unit,
    onCompareSubtask: (Long) -> Unit,
    onUpdateTask: (String, String, Int) -> Unit
) {
    var showEditTaskDialog by remember { mutableStateOf(false) }
    var subtaskToDelete by remember { mutableStateOf<Subtask?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(task?.name ?: "Subtareas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    if (task != null) {
                        IconButton(onClick = { showEditTaskDialog = true }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar tarea")
                        }
                        IconButton(onClick = {
                            onDeleteTask(task.id)
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar tarea")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            if (task != null) {
                FloatingActionButton(
                    onClick = { onAddSubtask(task.id) },
                    containerColor = Azul,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar subtarea")
                }
            }
        },
        containerColor = FondoPantalla
    ) { padding ->
        if (task == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Tarea no encontrada", color = Color.Gray)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                TaskHeaderCard(task = task)

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(4.dp)) }
                    if (task.subtasks.isEmpty()) {
                        item {
                            Text(
                                "Aún no hay subtareas. Usa el botón + para agregar la primera.",
                                color = Color.Gray,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(vertical = 24.dp)
                            )
                        }
                    }
                    items(task.subtasks, key = { it.id }) { subtask ->
                        SubtaskManageRow(
                            subtask = subtask,
                            onToggle = { onToggleSubtask(subtask) },
                            onEdit = { onEditSubtask(subtask.id) },
                            onDelete = { subtaskToDelete = subtask },
                            onCompare = { onCompareSubtask(subtask.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    if (showEditTaskDialog && task != null) {
        TaskFormDialog(
            task = task,
            onDismiss = { showEditTaskDialog = false },
            onConfirm = { name, description, estimatedPrice ->
                onUpdateTask(name, description, estimatedPrice)
                showEditTaskDialog = false
            }
        )
    }

    if (subtaskToDelete != null) {
        AlertDialog(
            onDismissRequest = { subtaskToDelete = null },
            title = { Text("¿Eliminar subtarea?") },
            text = { Text("¿Estás seguro de que deseas eliminar \"${subtaskToDelete?.name}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        subtaskToDelete?.let { onDeleteSubtask(it.id) }
                        subtaskToDelete = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { subtaskToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TaskHeaderCard(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            if (task.description.isNotBlank()) {
                Text(task.description, fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BudgetColumn("ESTIMADO", String.format("$%,d", task.price), Azul)
                BudgetColumn("GASTADO", String.format("$%,d", task.spent), Azul)
                BudgetColumn("SUBTAREAS", task.subtaskCount.toString(), Color.Black)
            }
        }
    }
}

@Composable
private fun SubtaskManageRow(
    subtask: Subtask,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCompare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = subtask.completed, onCheckedChange = { onToggle() })
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subtask.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textDecoration = if (subtask.completed) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (subtask.completed) Color.Gray else Color.Black
                    )
                    if (subtask.description.isNotBlank()) {
                        Text(subtask.description, fontSize = 12.sp, color = Color.Gray)
                    }
                }
                IconButton(onClick = onCompare) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Comparar precios", tint = Azul)
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar subtarea", tint = Color.Gray)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar subtarea", tint = Color.Gray)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 44.dp, top = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Estimado: ${String.format("$%,d", subtask.estimatedPrice)}", fontSize = 12.sp, color = Color.Gray)
                if (subtask.realPrice != null) {
                    Text("Real: ${String.format("$%,d", subtask.realPrice)}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Verde)
                }
                if (!subtask.place.isNullOrBlank()) {
                    Text("· ${subtask.place}", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun TaskFormDialog(
    task: Task,
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String, estimatedPrice: Int) -> Unit
) {
    var name by remember { mutableStateOf(task.name) }
    var description by remember { mutableStateOf(task.description) }
    var estimatedPrice by remember { mutableStateOf(task.estimatedPrice.toString()) }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; error = null },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = estimatedPrice,
                    onValueChange = { estimatedPrice = it.filter { c -> c.isDigit() }; error = null },
                    label = { Text("Presupuesto estimado") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                if (error != null) {
                    Text(error!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isBlank()) {
                    error = "Escribe un nombre"
                } else {
                    onConfirm(name.trim(), description.trim(), estimatedPrice.toIntOrNull() ?: 0)
                }
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SubtaskListScreenPreview() {
    val dummySubtasks = listOf(
        Subtask(id = 1, taskId = 1, name = "Pastel de chocolate", description = "De 3 leches", estimatedPrice = 500, realPrice = 450, place = "Pastelería", completed = true),
        Subtask(id = 2, taskId = 1, name = "Decoraciones globos", description = "Color azul y plata", estimatedPrice = 200, realPrice = null, place = "Tienda de fiestas", completed = false)
    )

    val dummyTask = Task(
        id = 1,
        eventId = 1,
        name = "Comida y Bebida",
        description = "Organizar el catering para 50 personas",
        estimatedPrice = 1000,
        realPrice = 0,
        completed = false,
        subtasks = dummySubtasks
    )

    ProyectoFinal_1Theme {
        SubtaskListContent(
            task = dummyTask,
            onBackClick = {},
            onDeleteTask = {},
            onAddSubtask = {},
            onToggleSubtask = {},
            onEditSubtask = {},
            onDeleteSubtask = {},
            onCompareSubtask = {},
            onUpdateTask = { _, _, _ -> }
        )
    }
}
