package ruiz.angel.proyectofinal_1.ui.screens

import androidx.compose.foundation.background
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
import ruiz.angel.proyectofinal_1.data.models.Subtask
import ruiz.angel.proyectofinal_1.data.models.Task
import ruiz.angel.proyectofinal_1.ui.theme.Azul
import ruiz.angel.proyectofinal_1.ui.theme.FondoPantalla
import ruiz.angel.proyectofinal_1.ui.theme.FondoTarjeta
import ruiz.angel.proyectofinal_1.ui.theme.Verde
import ruiz.angel.proyectofinal_1.viewModel.EventsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtaskListScreen(
    taskId: Long,
    viewModel: EventsViewModel,
    onBackClick: () -> Unit,
    onCompareClick: (Long) -> Unit
) {
    val task = viewModel.eventsListState
        .flatMap { it.tasks }
        .firstOrNull { it.id == taskId }

    var showSubtaskDialog by remember { mutableStateOf(false) }
    var editingSubtask by remember { mutableStateOf<Subtask?>(null) }
    var showEditTaskDialog by remember { mutableStateOf(false) }

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
                            viewModel.deleteTask(taskId)
                            onBackClick()
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar tarea")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { editingSubtask = null; showSubtaskDialog = true },
                containerColor = Azul,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar subtarea")
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
                            onToggle = { viewModel.toggleSubtask(subtask.id, !subtask.completed) },
                            onEdit = { editingSubtask = subtask; showSubtaskDialog = true },
                            onDelete = { viewModel.deleteSubtask(subtask.id) },
                            onCompare = { onCompareClick(subtask.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    if (showSubtaskDialog) {
        SubtaskFormDialog(
            initial = editingSubtask,
            onDismiss = { showSubtaskDialog = false },
            onConfirm = { name, description, estimatedPrice ->
                val current = editingSubtask
                if (current == null) {
                    viewModel.createSubtask(taskId, name, description, estimatedPrice)
                } else {
                    viewModel.updateSubtask(
                        subtaskId = current.id,
                        taskId = taskId,
                        name = name,
                        description = description,
                        estimatedPrice = estimatedPrice,
                        realPrice = current.realPrice,
                        place = current.place,
                        completed = current.completed
                    )
                }
                showSubtaskDialog = false
            }
        )
    }

    if (showEditTaskDialog && task != null) {
        TaskFormDialog(
            task = task,
            onDismiss = { showEditTaskDialog = false },
            onConfirm = { name, description, estimatedPrice ->
                viewModel.updateTask(
                    taskId = task.id,
                    eventId = task.eventId,
                    name = name,
                    description = description,
                    estimatedPrice = estimatedPrice,
                    realPrice = task.realPrice,
                    completed = task.completed
                )
                showEditTaskDialog = false
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
private fun SubtaskFormDialog(
    initial: Subtask?,
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String, estimatedPrice: Int) -> Unit
) {
    var name by remember { mutableStateOf(initial?.name.orEmpty()) }
    var description by remember { mutableStateOf(initial?.description.orEmpty()) }
    var estimatedPrice by remember { mutableStateOf(if (initial != null) initial.estimatedPrice.toString() else "") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Agregar subtarea" else "Editar subtarea") },
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
                    label = { Text("Costo estimado") },
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

