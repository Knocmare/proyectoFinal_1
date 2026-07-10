package ruiz.angel.proyectofinal_1.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ruiz.angel.proyectofinal_1.data.models.Subtask
import ruiz.angel.proyectofinal_1.ui.theme.Azul
import ruiz.angel.proyectofinal_1.ui.theme.FondoPantalla
import ruiz.angel.proyectofinal_1.ui.theme.FondoTarjeta
import ruiz.angel.proyectofinal_1.ui.theme.Rojo

private val TareaPrincipalFondo = Color(0xFFE3F0FE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtaskFormScreen(
    taskName: String,
    initial: Subtask?,
    onBack: () -> Unit,
    onCompareClick: (() -> Unit)? = null,
    onSave: (name: String, description: String, estimatedPrice: Int, realPrice: Int?, place: String) -> Unit
) {
    val isEditing = initial != null

    var name by remember { mutableStateOf(initial?.name.orEmpty()) }
    var description by remember { mutableStateOf(initial?.description.orEmpty()) }
    var estimatedPrice by remember {
        mutableStateOf(if (initial != null && initial.estimatedPrice != 0) initial.estimatedPrice.toString() else "")
    }
    var realPrice by remember {
        mutableStateOf(initial?.realPrice?.toString().orEmpty())
    }
    var place by remember { mutableStateOf(initial?.place.orEmpty()) }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Editar subtarea" else "Nueva subtarea",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = FondoPantalla
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Tarea principal
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = TareaPrincipalFondo)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Tarea principal", fontSize = 12.sp, color = Azul)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(taskName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Azul)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Información
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "INFORMACIÓN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Nombre de la subtarea", fontSize = 13.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; error = null },
                        placeholder = { Text("Ej. Reserva del salón") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Descripción", fontSize = 13.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("Detalles de la subtarea") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Costo
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "COSTO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (isEditing) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Costo estimado", fontSize = 13.sp, color = Color.Gray)
                                OutlinedTextField(
                                    value = estimatedPrice,
                                    onValueChange = { estimatedPrice = it.filter { c -> c.isDigit() }; error = null },
                                    placeholder = { Text("$ 0") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Costo real", fontSize = 13.sp, color = Color.Gray)
                                OutlinedTextField(
                                    value = realPrice,
                                    onValueChange = { realPrice = it.filter { c -> c.isDigit() } },
                                    placeholder = { Text("$ 0") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    } else {
                        Text("Costo estimado", fontSize = 13.sp, color = Color.Gray)
                        OutlinedTextField(
                            value = estimatedPrice,
                            onValueChange = { estimatedPrice = it.filter { c -> c.isDigit() }; error = null },
                            placeholder = { Text("$ 0") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Lugar de adquisición", fontSize = 13.sp, color = Color.Gray)
                    OutlinedTextField(
                        value = place,
                        onValueChange = { place = it },
                        placeholder = { Text("Ej. Salón Las Palmas") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    if (isEditing && onCompareClick != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Comparar precios",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Azul,
                            modifier = Modifier.clickable { onCompareClick() }
                        )
                    }
                }
            }

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            fun trySave() {
                if (name.isBlank()) {
                    error = "Escribe un nombre para la subtarea"
                } else {
                    onSave(
                        name.trim(),
                        description.trim(),
                        estimatedPrice.toIntOrNull() ?: 0,
                        if (isEditing) realPrice.toIntOrNull() else null,
                        place.trim()
                    )
                }
            }

            if (isEditing) {
                Row(modifier = Modifier.fillMaxWidth()) {

                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = { trySave() },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Azul, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Guardar cambios", fontWeight = FontWeight.Medium)
                    }
                }
            } else {
                Button(
                    onClick = { trySave() },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Azul, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Guardar subtarea", fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SubtaskFormScreenCreatePreview() {
    SubtaskFormScreen(
        taskName = "Salón de eventos",
        initial = null,
        onBack = {},
        onSave = { _, _, _, _, _ -> }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SubtaskFormScreenEditPreview() {
    SubtaskFormScreen(
        taskName = "Salón de eventos",
        initial = Subtask(
            id = 1,
            name = "Reserva del salón",
            description = "Pago de local para su uso en el evento.",
            estimatedPrice = 5000,
            realPrice = 0,
            completed = false
        ),
        onBack = {},
        onCompareClick = {},
        onSave = { _, _, _, _, _ -> }
    )
}
