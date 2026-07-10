package ruiz.angel.proyectofinal_1.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ruiz.angel.proyectofinal_1.data.database.AppDatabase
import ruiz.angel.proyectofinal_1.data.database.entity.EventEntity
import ruiz.angel.proyectofinal_1.data.database.entity.PriceOptionEntity
import ruiz.angel.proyectofinal_1.data.database.entity.SubtaskEntity
import ruiz.angel.proyectofinal_1.data.database.entity.TaskEntity
import ruiz.angel.proyectofinal_1.data.database.relation.EventWithTasks
import ruiz.angel.proyectofinal_1.data.database.relation.TaskWithSubtasks
import ruiz.angel.proyectofinal_1.data.models.Event
import ruiz.angel.proyectofinal_1.data.models.PriceOption
import ruiz.angel.proyectofinal_1.data.models.Subtask
import ruiz.angel.proyectofinal_1.data.models.Task

class EventsRepository(private val database: AppDatabase) {
    private val eventDao = database.eventDao()
    private val taskDao = database.taskDao()
    private val subtaskDao = database.subtaskDao()
    private val priceOptionDao = database.priceOptionDao()

    fun getEvents(userId: Long): Flow<List<Event>> =
        eventDao.getEventsWithTasks(userId).map { list -> list.map { it.toDomain() } }

    suspend fun createEvent(userId: Long, name: String, date: String): Long =
        eventDao.insertEvent(EventEntity(userId = userId, name = name, date = date))

    suspend fun updateEvent(eventId: Long, userId: Long, name: String, date: String) {
        eventDao.updateEvent(EventEntity(id = eventId, userId = userId, name = name, date = date))
    }

    suspend fun deleteEvent(eventId: Long) = eventDao.deleteEvent(eventId)

    suspend fun createTask(eventId: Long, name: String, description: String, estimatedPrice: Int): Long =
        taskDao.insertTask(
            TaskEntity(
                eventId = eventId,
                name = name,
                description = description,
                estimatedPrice = estimatedPrice,
                completed = false
            )
        )

    suspend fun updateTask(
        taskId: Long,
        eventId: Long,
        name: String,
        description: String,
        estimatedPrice: Int,
        realPrice: Int?,
        completed: Boolean
    ) {
        taskDao.updateTask(
            TaskEntity(
                id = taskId,
                eventId = eventId,
                name = name,
                description = description,
                estimatedPrice = estimatedPrice,
                realPrice = realPrice,
                completed = completed
            )
        )
    }

    suspend fun deleteTask(taskId: Long) = taskDao.deleteTask(taskId)

    suspend fun toggleTask(taskId: Long, completed: Boolean) = taskDao.setCompleted(taskId, completed)

    suspend fun setTaskRealPrice(taskId: Long, realPrice: Int?) = taskDao.setRealPrice(taskId, realPrice)

    suspend fun createSubtask(taskId: Long, name: String, description: String, estimatedPrice: Int): Long =
        subtaskDao.insertSubtask(
            SubtaskEntity(
                taskId = taskId,
                name = name,
                description = description,
                estimatedPrice = estimatedPrice,
                completed = false
            )
        )

    suspend fun updateSubtask(
        subtaskId: Long,
        taskId: Long,
        name: String,
        description: String,
        estimatedPrice: Int,
        realPrice: Int?,
        place: String?,
        completed: Boolean
    ) {
        subtaskDao.updateSubtask(
            SubtaskEntity(
                id = subtaskId,
                taskId = taskId,
                name = name,
                description = description,
                estimatedPrice = estimatedPrice,
                realPrice = realPrice,
                place = place,
                completed = completed
            )
        )
    }

    suspend fun deleteSubtask(subtaskId: Long) = subtaskDao.deleteSubtask(subtaskId)

    suspend fun toggleSubtask(subtaskId: Long, completed: Boolean) = subtaskDao.setCompleted(subtaskId, completed)

    suspend fun registerSubtaskPurchase(subtaskId: Long, place: String, realPrice: Int) =
        subtaskDao.setPurchaseInfo(subtaskId, realPrice, place)

    fun getPriceOptions(subtaskId: Long): Flow<List<PriceOption>> =
        priceOptionDao.getForSubtask(subtaskId).map { list -> list.map { it.toDomain() } }

    suspend fun addPriceOption(subtaskId: Long, place: String, cost: Int): Long =
        priceOptionDao.insertOption(PriceOptionEntity(subtaskId = subtaskId, place = place, cost = cost))

    suspend fun deletePriceOption(id: Long) = priceOptionDao.deleteOption(id)
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

private fun PriceOptionEntity.toDomain(): PriceOption = PriceOption(
    id = id,
    subtaskId = subtaskId,
    place = place,
    cost = cost
)
