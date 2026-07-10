package ruiz.angel.proyectofinal_1.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import ruiz.angel.proyectofinal_1.ui.theme.Azul
import ruiz.angel.proyectofinal_1.ui.theme.FondoPantalla

/**
 * Pantalla de creación de un nuevo evento.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    onCancel: () -> Unit,
    onSave: (name: String, date: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo evento", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
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
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text("Nombre del evento", fontSize = 13.sp, color = Color.Gray)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; error = null },
                placeholder = { Text("Ej. Boda, Cumpleaños, Graduación") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text("Fecha", fontSize = 13.sp, color = Color.Gray)
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = day,
                        onValueChange = { if (it.length <= 2) { day = it.filter { c -> c.isDigit() }; error = null } },
                        placeholder = { Text("Día") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = month,
                        onValueChange = { if (it.length <= 2) { month = it.filter { c -> c.isDigit() }; error = null } },
                        placeholder = { Text("Mes") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = year,
                        onValueChange = { if (it.length <= 4) { year = it.filter { c -> c.isDigit() }; error = null } },
                        placeholder = { Text("Año") },
                        modifier = Modifier.weight(1.2f),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
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

            Column(modifier = Modifier.padding(top = 28.dp)) {
                Button(
                    onClick = {
                        val d = day.toIntOrNull()
                        val m = month.toIntOrNull()
                        val y = year.toIntOrNull()

                        if (name.isBlank()) {
                            error = "Escribe un nombre para el evento"
                        } else if (day.isBlank() || month.isBlank() || year.isBlank()) {
                            error = "Completa la fecha"
                        } else if (d == null || d !in 1..31 || m == null || m !in 1..12 || y == null || y < 2000) {
                            error = "Fecha no válida"
                        } else {
                            val formattedDate = "$day/${month.padStart(2, '0')}/$year"
                            onSave(name.trim(), formattedDate)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Azul, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Guardar", fontWeight = FontWeight.Medium)
                }
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth().height(48.dp).padding(top = 10.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateEventScreenPreview() {
    CreateEventScreen(onCancel = {}, onSave = { _, _ -> })
}
