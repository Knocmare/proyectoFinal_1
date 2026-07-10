package ruiz.angel.proyectofinal_1.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ruiz.angel.proyectofinal_1.data.database.AppDatabase
import ruiz.angel.proyectofinal_1.data.database.entity.EventEntity
import ruiz.angel.proyectofinal_1.data.database.entity.SubtaskEntity
import ruiz.angel.proyectofinal_1.data.database.relation.EventWithTasks
import ruiz.angel.proyectofinal_1.data.database.relation.TaskWithSubtasks
import ruiz.angel.proyectofinal_1.data.models.Event
import ruiz.angel.proyectofinal_1.data.models.Subtask
import ruiz.angel.proyectofinal_1.data.models.Task

class EventsRepository(private val database: AppDatabase) {
    private val eventDao = database.eventDao()

    fun getEvents(userId: Long): Flow<List<Event>> =
        eventDao.getEventsWithTasks(userId).map { list -> list.map { it.toDomain() } }

    suspend fun createEvent(userId: Long, name: String, date: String): Long =
        eventDao.insertEvent(EventEntity(userId = userId, name = name, date = date))

    suspend fun updateEvent(eventId: Long, userId: Long, name: String, date: String) {
        eventDao.updateEvent(EventEntity(id = eventId, userId = userId, name = name, date = date))
    }

    suspend fun deleteEvent(eventId: Long) = eventDao.deleteEvent(eventId)
}

private fun EventWithTasks.toDomain(): Event = Event(
    id = event.id,
    userId = event.userId,
    name = event.name,
    date = event.date,
    tasks = tasks.map { it.toDomain() }
)

private fun TaskWithSubtasks.toDomain(): Task = Task(
    id = task.id,
    eventId = task.eventId,
    name = task.name,
    description = task.description,
    estimatedPrice = task.estimatedPrice,
    realPrice = task.realPrice,
    completed = task.completed,
    subtasks = subtasks.map { it.toDomain() }
)

private fun SubtaskEntity.toDomain(): Subtask = Subtask(
    id = id,
    taskId = taskId,
    name = name,
    description = description,
    estimatedPrice = estimatedPrice,
    realPrice = realPrice,
    place = place,
    completed = completed
)
