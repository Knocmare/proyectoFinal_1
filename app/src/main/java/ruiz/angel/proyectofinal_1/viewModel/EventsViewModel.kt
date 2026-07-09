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
import ruiz.angel.proyectofinal_1.data.models.PriceOption
import ruiz.angel.proyectofinal_1.data.repository.EventsRepository

class EventsViewModel(private val repository: EventsRepository) : ViewModel() {

    var eventsListState by mutableStateOf(listOf<Event>())
        private set

    var priceOptionsState by mutableStateOf(listOf<PriceOption>())
        private set

    private var loadJob: Job? = null
    private var priceOptionsJob: Job? = null

    fun loadEvents(userId: Long) {
        loadJob?.cancel()
        loadJob = repository.getEvents(userId).onEach { eventsListState = it }.launchIn(viewModelScope)
    }

    fun createEvent(userId: Long, name: String, date: String) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.createEvent(userId, name, date) }
    }

    fun deleteEvent(eventId: Long) {
        viewModelScope.launch { repository.deleteEvent(eventId) }
    }

    fun createTask(eventId: Long, name: String, description: String, estimatedPrice: Int) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.createTask(eventId, name, description, estimatedPrice) }
    }

    fun updateTask(
        taskId: Long,
        eventId: Long,
        name: String,
        description: String,
        estimatedPrice: Int,
        realPrice: Int? = null,
        completed: Boolean = false
    ) {
        viewModelScope.launch {
            repository.updateTask(taskId, eventId, name, description, estimatedPrice, realPrice, completed)
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch { repository.deleteTask(taskId) }
    }

    fun toggleTask(taskId: Long, completed: Boolean) {
        viewModelScope.launch { repository.toggleTask(taskId, completed) }
    }

    fun createSubtask(taskId: Long, name: String, description: String, estimatedPrice: Int) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.createSubtask(taskId, name, description, estimatedPrice) }
    }

    fun updateSubtask(
        subtaskId: Long,
        taskId: Long,
        name: String,
        description: String,
        estimatedPrice: Int,
        realPrice: Int?,
        place: String?,
        completed: Boolean
    ) {
        viewModelScope.launch {
            repository.updateSubtask(subtaskId, taskId, name, description, estimatedPrice, realPrice, place, completed)
        }
    }

    fun deleteSubtask(subtaskId: Long) {
        viewModelScope.launch { repository.deleteSubtask(subtaskId) }
    }

    fun toggleSubtask(subtaskId: Long, completed: Boolean) {
        viewModelScope.launch { repository.toggleSubtask(subtaskId, completed) }
    }

    fun registerSubtaskPurchase(subtaskId: Long, place: String, realPrice: Int) {
        viewModelScope.launch { repository.registerSubtaskPurchase(subtaskId, place, realPrice) }
    }

    fun loadPriceOptions(subtaskId: Long) {
        priceOptionsJob?.cancel()
        priceOptionsJob = repository.getPriceOptions(subtaskId).onEach { priceOptionsState = it }.launchIn(viewModelScope)
    }

    fun addPriceOption(subtaskId: Long, place: String, cost: Int) {
        if (place.isBlank()) return
        viewModelScope.launch { repository.addPriceOption(subtaskId, place, cost) }
    }

    fun deletePriceOption(id: Long) {
        viewModelScope.launch { repository.deletePriceOption(id) }
    }
}
