package ruiz.angel.proyectofinal_1.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ruiz.angel.proyectofinal_1.data.models.Event
import ruiz.angel.proyectofinal_1.data.repository.EventsRepository

class EventsViewModel(private val repository: EventsRepository) : ViewModel() {

    var eventsListState by mutableStateOf(listOf<Event>())
        private set

    private var loadJob: Job? = null

    fun loadEvents(userId: Long) {
        loadJob?.cancel()
        loadJob = repository.getEvents(userId).onEach { eventsListState = it }.launchIn(viewModelScope)
    }

    fun createEvent(userId: Long, event: Event) {
        viewModelScope.launch { repository.createEvent(userId, event) }
    }

    fun toggleTask(taskId: Long, completed: Boolean) {
        viewModelScope.launch { repository.toggleTask(taskId, completed) }
    }

    fun toggleSubtask(subtaskId: Long, completed: Boolean) {
        viewModelScope.launch { repository.toggleSubtask(subtaskId, completed) }
    }
}