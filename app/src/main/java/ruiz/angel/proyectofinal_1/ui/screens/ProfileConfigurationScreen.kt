package ruiz.angel.proyectofinal_1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProfileConfigurationScreen() {
    var name by remember { mutableStateOf("Angel") } //usuario.nombreCompleto
    var email by remember { mutableStateOf("a@mail.com") } //usuario.email
    var password by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column() {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("<-   Configuración", modifier = Modifier.padding(5.dp))
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Editar Cuenta", modifier = Modifier.align(Alignment.Start).padding(5.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; errorMessage = "" },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = "" },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                singleLine = true,
                readOnly = true
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Cambiar Contraseña", modifier = Modifier.align(Alignment.Start).padding(5.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = "" },
                label = { Text("Contraseña Actual") },
                modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it; errorMessage = "" },
                label = { Text("Nueva Contraseña") },
                modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
        }

        // Mostrar error si los datos son incorrectos
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0061a4),
                contentColor = Color.White
            )
        ) {
            Text("Guardar Contraseña")
        }

        Spacer(modifier = Modifier.height(48.dp))

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom) {
            Button(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFa40000),
                    contentColor = Color.White
                )
                ) {
                Text("Cerrar Sesión")
            }
            Button(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0061a4),
                    contentColor = Color.White
                )
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileConfigurationScreenPreview() {
    ProfileConfigurationScreen()
}