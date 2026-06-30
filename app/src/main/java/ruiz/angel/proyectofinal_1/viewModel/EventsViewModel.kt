package ruiz.angel.proyectofinal_1.viewModel

import androidx.lifecycle.ViewModel
import ruiz.angel.proyectofinal_1.data.EventsRepository

class EventsViewModel: ViewModel() {
    val repository = EventsRepository()
}