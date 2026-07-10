package ruiz.angel.proyectofinal_1.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ruiz.angel.proyectofinal_1.data.repository.TasksRepository

class TasksViewModel(private val repository: TasksRepository) : ViewModel() {

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
}
