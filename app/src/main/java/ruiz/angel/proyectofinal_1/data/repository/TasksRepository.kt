package ruiz.angel.proyectofinal_1.data.repository

import ruiz.angel.proyectofinal_1.data.database.AppDatabase
import ruiz.angel.proyectofinal_1.data.database.entity.TaskEntity

class TasksRepository(private val database: AppDatabase) {
    private val taskDao = database.taskDao()

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
}
