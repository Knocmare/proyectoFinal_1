package ruiz.angel.proyectofinal_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import ruiz.angel.proyectofinal_1.data.database.AppDatabase
import ruiz.angel.proyectofinal_1.data.repository.EventsRepository
import ruiz.angel.proyectofinal_1.data.repository.TasksRepository
import ruiz.angel.proyectofinal_1.data.repository.SubtasksRepository
import ruiz.angel.proyectofinal_1.data.repository.UserRepository
import ruiz.angel.proyectofinal_1.data.security.SessionManager
import ruiz.angel.proyectofinal_1.navigation.Navigation
import ruiz.angel.proyectofinal_1.ui.theme.ProyectoFinal_1Theme
import ruiz.angel.proyectofinal_1.viewModel.AuthViewModel
import ruiz.angel.proyectofinal_1.viewModel.EventsViewModel
import ruiz.angel.proyectofinal_1.viewModel.TasksViewModel
import ruiz.angel.proyectofinal_1.viewModel.SubtasksViewModel

class MainActivity : ComponentActivity() {

   private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
auth = Firebase.auth
        val database = AppDatabase.getInstance(this)
        val sessionManager = SessionManager(this)
        val eventsViewModel = EventsViewModel(EventsRepository(database))
        val tasksViewModel = TasksViewModel(TasksRepository(database))
        val subtasksViewModel = SubtasksViewModel(SubtasksRepository(database))
        val authViewModel = AuthViewModel(UserRepository(database, sessionManager))

        enableEdgeToEdge()
        setContent {
            ProyectoFinal_1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(
                        innerPadding,
                        eventsViewModel,
                        tasksViewModel,
                        subtasksViewModel,
                        authViewModel
                    )
                }
            }
        }
    }

    override fun onStart(){
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){

        }
    }
}
