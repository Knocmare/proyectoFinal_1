package ruiz.angel.proyectofinal_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import ruiz.angel.proyectofinal_1.ui.screens.LoginScreen
import ruiz.angel.proyectofinal_1.ui.screens.PriceComparationScreen
import ruiz.angel.proyectofinal_1.ui.screens.RegisterScreen
import ruiz.angel.proyectofinal_1.ui.theme.ProyectoFinal_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoFinal_1Theme {
                AuthFlow()
            }
        }
    }
}

private enum class AuthScreen { Login, Register, PriceComparison }

@Composable
private fun AuthFlow() {
    var screen by remember { mutableStateOf(AuthScreen.Login) }

    // Estados compartidos (Provisional)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    when (screen) {
        AuthScreen.Login -> LoginScreen(
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it },
            onLoginClick = { screen = AuthScreen.PriceComparison },
            onForgotPasswordClick = { },
            onRegisterClick = { screen = AuthScreen.Register }
        )
        AuthScreen.Register -> RegisterScreen(
            name = name,
            onNameChange = { name = it },
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it },
            onBackClick = { screen = AuthScreen.Login },
            onRegisterClick = { screen = AuthScreen.PriceComparison },
            onLoginClick = { screen = AuthScreen.Login }
        )
        AuthScreen.PriceComparison -> PriceComparationScreen(
            onBackClick = { screen = AuthScreen.Login }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthFlowPreview() {
    ProyectoFinal_1Theme {
        AuthFlow()
    }
}