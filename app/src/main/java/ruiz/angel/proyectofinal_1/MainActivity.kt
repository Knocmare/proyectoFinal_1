package ruiz.angel.proyectofinal_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import ruiz.angel.proyectofinal_1.data.database.AppDatabase
import ruiz.angel.proyectofinal_1.data.repository.EventsRepository
import ruiz.angel.proyectofinal_1.navigation.Navigation
import ruiz.angel.proyectofinal_1.ui.theme.ProyectoFinal_1Theme
import ruiz.angel.proyectofinal_1.viewModel.EventsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(this)
        val eventsViewModel = EventsViewModel(EventsRepository(database))

        enableEdgeToEdge()
        setContent {
            ProyectoFinal_1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(innerPadding, eventsViewModel)
                }
            }
        }
    }
}
