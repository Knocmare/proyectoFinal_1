package ruiz.angel.proyectofinal_1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ruiz.angel.proyectofinal_1.ui.theme.Azul
import ruiz.angel.proyectofinal_1.ui.theme.FondoPantalla

@Composable
fun ProfileConfigurationScreen(
    name: String,
    email: String,
    passwordError: String? = null,
    onBackClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onSaveProfile: (newName: String) -> Unit = {},
    onChangePassword: (currentPassword: String, newPassword: String) -> Unit = { _, _ -> }
) {
    var name by remember { mutableStateOf(name) } //usuario.nombreCompleto
    var email by remember { mutableStateOf(email) } //usuario.email
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }

    val errorMessage = localError.ifEmpty { passwordError.orEmpty() }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
            .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "←",
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .clickable { onBackClick() }
                        .padding(end = 12.dp)
                )
                Text(
                    text = "Configuración",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Editar Cuenta",
                    modifier = Modifier.align(Alignment.Start).padding(5.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; localError = "" },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                    singleLine = true,
                    readOnly = true
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Cambiar Contraseña",
                    modifier = Modifier.align(Alignment.Start).padding(5.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it; localError = "" },
                    label = { Text("Contraseña Actual") },
                    modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it; localError = "" },
                    label = { Text("Nueva Contraseña") },
                    modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; localError = "" },
                    label = { Text("Confirmar Nueva Contraseña") },
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
                    when {
                        currentPassword.isBlank() -> localError = "Escribe tu contraseña actual"
                        newPassword.length < 6 -> localError = "La nueva contraseña debe tener al menos 6 caracteres"
                        newPassword != confirmPassword -> localError = "Las contraseñas no coinciden"
                        else -> {
                            localError = ""
                            onChangePassword(currentPassword, newPassword)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Azul,
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Guardar Contraseña")
            }

            Spacer(modifier = Modifier.height(150.dp))

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom) {
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFa40000),
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Cerrar Sesión")
                }
                Button(
                    onClick = { onSaveProfile(name) },
                    modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Azul,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Guardar Cambios")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileConfigurationScreenPreview() {
    ProfileConfigurationScreen(
        "An", "a@mail.com"
    )
}