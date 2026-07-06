package ruiz.angel.proyectofinal_1.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ruiz.angel.proyectofinal_1.data.models.Event
import ruiz.angel.proyectofinal_1.ui.screens.LoginScreen
import ruiz.angel.proyectofinal_1.ui.screens.PriceComparationScreen
import ruiz.angel.proyectofinal_1.ui.screens.ProfileConfigurationScreen
import ruiz.angel.proyectofinal_1.ui.screens.RegisterScreen
import ruiz.angel.proyectofinal_1.ui.screens.TaskListScreen
import ruiz.angel.proyectofinal_1.viewModel.EventsViewModel

@Composable
fun Navigation(innerPadding: PaddingValues, eventsViewModel: EventsViewModel = viewModel()) {
    val navController = rememberNavController()

//    val userId = 1L
//    val name = "prueba"
//    val email = "prueba@itson.edu.mx"
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var event by remember { mutableStateOf(Event(0, "", "")) }

    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable<Login> {
            LoginScreen(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                onLoginClick = { navController.navigate(TaskList) },
                onForgotPasswordClick = { },
                onRegisterClick = { navController.navigate(Register) }
            )
        }
        composable<Register> {
            RegisterScreen(
                name = name,
                onNameChange = { name = it },
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                onBackClick = { navController.popBackStack() },
                onRegisterClick = { navController.navigate(PriceComparison) },
                onLoginClick = { navController.navigate(Login) }
            )
        }
        composable<TaskList> {
            TaskListScreen(
                viewModel = eventsViewModel,
                name = name,
                email = email,
                iniciales = "P",
                onCreateEvent = { navController.navigate(Configuration) },
                onLeave = { navController.navigate(Login) }
            )
        }
        composable<Configuration> {
            ProfileConfigurationScreen(
                name = name,
                email = email,
                onBackClick = { navController.popBackStack() },
                onLogout = { navController.navigate(Login) },
                onSaveProfile = {  },
                onChangePassword = { current, new ->  }
            )
        }
        composable<PriceComparison> {
            PriceComparationScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}