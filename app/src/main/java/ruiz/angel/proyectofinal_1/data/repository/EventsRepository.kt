package ruiz.angel.proyectofinal_1.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ruiz.angel.proyectofinal_1.data.database.AppDatabase
import ruiz.angel.proyectofinal_1.data.database.entity.EventEntity
import ruiz.angel.proyectofinal_1.data.database.entity.SubtaskEntity
import ruiz.angel.proyectofinal_1.data.database.entity.TaskEntity
import ruiz.angel.proyectofinal_1.data.database.relation.EventWithTasks
import ruiz.angel.proyectofinal_1.data.database.relation.TaskWithSubtasks
import ruiz.angel.proyectofinal_1.data.models.Event
import ruiz.angel.proyectofinal_1.data.models.Subtask
import ruiz.angel.proyectofinal_1.data.models.Task

class EventsRepository(private val database: AppDatabase) {
    private val eventDao = database.eventDao()
    private val taskDao = database.taskDao()
    private val subtaskDao = database.subtaskDao()

    fun getEvents(userId: Long): Flow<List<Event>> =
        eventDao.getEventsWithTasks(userId).map { list -> list.map { it.toDomain() } }

    suspend fun createEvent(userId: Long, event: Event): Long {
        val eventId = eventDao.insertEvent(EventEntity(userId = userId, name = event.name, date = event.date))
        event.tasks.forEach { task ->
            val taskId = taskDao.insertTask(TaskEntity(eventId = eventId, name = task.name, completed = task.completed))
            task.subtasks.forEach { sub ->
                subtaskDao.insertSubtask(SubtaskEntity(taskId = taskId, name = sub.name, price = sub.price, completed = sub.completed))
            }
        }
        return eventId
    }

    suspend fun toggleTask(taskId: Long, completed: Boolean) = taskDao.setCompleted(taskId, completed)
    suspend fun toggleSubtask(subtaskId: Long, completed: Boolean) = subtaskDao.setCompleted(subtaskId, completed)
}

private fun EventWithTasks.toDomain(): Event = Event(
    id = event.id, name = event.name, date = event.date,
    tasks = tasks.map { it.toDomain() }
)

private fun TaskWithSubtasks.toDomain(): Task = Task(
    id = task.id, name = task.name, completed = task.completed,
    subtasks = subtasks.map { Subtask(it.id, it.name, it.price, it.completed) }
)