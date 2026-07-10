package ruiz.angel.proyectofinal_1.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ruiz.angel.proyectofinal_1.ui.screens.CreateEventScreen
import ruiz.angel.proyectofinal_1.ui.screens.CreateTaskScreen
import ruiz.angel.proyectofinal_1.ui.screens.EventSummaryScreen
import ruiz.angel.proyectofinal_1.ui.screens.LoginScreen
import ruiz.angel.proyectofinal_1.ui.screens.PriceComparationScreen
import ruiz.angel.proyectofinal_1.ui.screens.ProfileConfigurationScreen
import ruiz.angel.proyectofinal_1.ui.screens.RegisterScreen
import ruiz.angel.proyectofinal_1.ui.screens.SubtaskFormScreen
import ruiz.angel.proyectofinal_1.ui.screens.SubtaskListScreen
import ruiz.angel.proyectofinal_1.ui.screens.TaskListScreen
import ruiz.angel.proyectofinal_1.viewModel.AuthViewModel
import ruiz.angel.proyectofinal_1.viewModel.EventsViewModel

@Composable
fun Navigation(
    innerPadding: PaddingValues,
    eventsViewModel: EventsViewModel,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val currentUser by authViewModel.currentUser.collectAsState()

    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var registerName by remember { mutableStateOf("") }
    var registerEmail by remember { mutableStateOf("") }
    var registerPassword by remember { mutableStateOf("") }
    var registerConfirmPassword by remember { mutableStateOf("") }

    // Auto-login: si ya hay una sesión guardada (DataStore), saltamos el Login.
    LaunchedEffect(currentUser) {
        val user = currentUser
        if (user != null) {
            eventsViewModel.loadEvents(user.id)
            if (navController.currentDestination?.hasRoute<Login>() == true) {
                navController.navigate(TaskList) {
                    popUpTo(Login) { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable<Login> {
            LoginScreen(
                email = loginEmail,
                onEmailChange = { loginEmail = it },
                password = loginPassword,
                onPasswordChange = { loginPassword = it },
                errorMessage = authViewModel.errorMessage,
                isLoading = authViewModel.isLoading,
                onLoginClick = {
                    authViewModel.login(loginEmail, loginPassword) {
                        navController.navigate(TaskList) {
                            popUpTo(Login) { inclusive = true }
                        }
                    }
                },
                onForgotPasswordClick = { },
                onRegisterClick = {
                    authViewModel.clearError()
                    navController.navigate(Register)
                },
                onGoogleLoginSuccess = { idToken: String ->
                    authViewModel.loginWithGoogle(idToken) {
                        navController.navigate(TaskList) {
                            popUpTo(Login) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable<Register> {
            RegisterScreen(
                name = registerName,
                onNameChange = { registerName = it },
                email = registerEmail,
                onEmailChange = { registerEmail = it },
                password = registerPassword,
                onPasswordChange = { registerPassword = it },
                confirmPassword = registerConfirmPassword,
                onConfirmPasswordChange = { registerConfirmPassword = it },
                errorMessage = authViewModel.errorMessage,
                isLoading = authViewModel.isLoading,
                onBackClick = { navController.popBackStack() },
                onRegisterClick = {
                    authViewModel.register(registerName, registerEmail, registerPassword, registerConfirmPassword) {
                        navController.navigate(TaskList) {
                            popUpTo(Login) { inclusive = true }
                        }
                    }
                },
                onLoginClick = {
                    authViewModel.clearError()
                    navController.navigate(Login)
                }
            )
        }

        composable<TaskList> {
            val user = currentUser
            if (user == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Login) { popUpTo(0) }
                }
            } else {
                TaskListScreen(
                    viewModel = eventsViewModel,
                    name = user.name,
                    email = user.email,
                    iniciales = user.name.take(1).uppercase(),
                    onCreateEvent = { navController.navigate(CreateEvent) },
                    onAddTask = { eventId -> navController.navigate(CreateTask(eventId)) },
                    onOpenTask = { taskId -> navController.navigate(SubtaskList(taskId)) },
                    onOpenSummary = { eventId -> navController.navigate(Summary(eventId)) },
                    onOpenAccount = { navController.navigate(Configuration) },
                    onLeave = {
                        authViewModel.logout {
                            navController.navigate(Login) { popUpTo(0) }
                        }
                    }
                )
            }
        }

        composable<Configuration> {
            val user = currentUser
            ProfileConfigurationScreen(
                name = user?.name.orEmpty(),
                email = user?.email.orEmpty(),
                passwordError = authViewModel.errorMessage,
                onBackClick = {
                    authViewModel.clearError()
                    navController.popBackStack()
                },
                onLogout = {
                    authViewModel.logout {
                        navController.navigate(Login) { popUpTo(0) }
                    }
                },
                onSaveProfile = { newName ->
                    user?.let { authViewModel.updateName(it.id, newName) }
                },
                onChangePassword = { current, new ->
                    user?.let { authViewModel.changePassword(it.id, current, new) {} }
                }
            )
        }

        composable<CreateEvent> {
            val user = currentUser
            CreateEventScreen(
                onCancel = { navController.popBackStack() },
                onSave = { eventName, date ->
                    user?.let { eventsViewModel.createEvent(it.id, eventName, date) }
                    navController.popBackStack()
                }
            )
        }

        composable<CreateTask> { backStackEntry ->
            val route: CreateTask = backStackEntry.toRoute()
            CreateTaskScreen(
                onCancel = { navController.popBackStack() },
                onSave = { taskName, description, estimatedPrice ->
                    eventsViewModel.createTask(route.eventId, taskName, description, estimatedPrice)
                    navController.popBackStack()
                }
            )
        }

        composable<SubtaskList> { backStackEntry ->
            val route: SubtaskList = backStackEntry.toRoute()
            SubtaskListScreen(
                taskId = route.taskId,
                viewModel = eventsViewModel,
                onBackClick = { navController.popBackStack() },
                onCompareClick = { subtaskId -> navController.navigate(PriceComparisonRoute(subtaskId)) },
                onAddSubtask = { taskId -> navController.navigate(SubtaskForm(taskId = taskId)) },
                onEditSubtask = { subtaskId ->
                    navController.navigate(SubtaskForm(taskId = route.taskId, subtaskId = subtaskId))
                }
            )
        }

        composable<SubtaskForm> { backStackEntry ->
            val route: SubtaskForm = backStackEntry.toRoute()
            val task = eventsViewModel.eventsListState.firstOrNull { it.tasks.any { t -> t.id == route.taskId } }
                ?.tasks?.firstOrNull { it.id == route.taskId }
            val existingSubtask = if (route.subtaskId >= 0) {
                task?.subtasks?.firstOrNull { it.id == route.subtaskId }
            } else null

            SubtaskFormScreen(
                taskName = task?.name.orEmpty(),
                initial = existingSubtask,
                onBack = { navController.popBackStack() },
                onCompareClick = if (existingSubtask != null) {
                    { navController.navigate(PriceComparisonRoute(existingSubtask.id)) }
                } else null,
                onSave = { name, description, estimatedPrice, realPrice, place ->
                    if (existingSubtask == null) {
                        eventsViewModel.createSubtask(route.taskId, name, description, estimatedPrice)
                    } else {
                        eventsViewModel.updateSubtask(
                            subtaskId = existingSubtask.id,
                            taskId = route.taskId,
                            name = name,
                            description = description,
                            estimatedPrice = estimatedPrice,
                            realPrice = realPrice,
                            place = place.ifBlank { existingSubtask.place },
                            completed = existingSubtask.completed
                        )
                    }
                    navController.popBackStack()
                }
            )
        }

        composable<PriceComparisonRoute> { backStackEntry ->
            val route: PriceComparisonRoute = backStackEntry.toRoute()
            PriceComparationScreen(
                subtaskId = route.subtaskId,
                viewModel = eventsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Summary> { backStackEntry ->
            val route: Summary = backStackEntry.toRoute()
            val event = eventsViewModel.eventsListState.firstOrNull { it.id == route.eventId }
            if (event != null) {
                EventSummaryScreen(
                    evento = event,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
