package ruiz.angel.proyectofinal_1.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ruiz.angel.proyectofinal_1.data.database.AppDatabase
import ruiz.angel.proyectofinal_1.data.database.entity.TaskEntity

class TasksRepository(private val database: AppDatabase) {
    private val taskDao = database.taskDao()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun createTask(eventId: Long, name: String, description: String, estimatedPrice: Int): Long {
        val taskId = taskDao.insertTask(
            TaskEntity(
                eventId = eventId,
                name = name,
                description = description,
                estimatedPrice = estimatedPrice,
                completed = false
            )
        )

        try {
            val taskData = hashMapOf(
                "eventId" to eventId,
                "name" to name,
                "description" to description,
                "estimatedPrice" to estimatedPrice,
                "completed" to false
            )
            firestore.collection("tasks").document(taskId.toString()).set(taskData).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return taskId
    }

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

        try {
            val taskData = hashMapOf(
                "eventId" to eventId,
                "name" to name,
                "description" to description,
                "estimatedPrice" to estimatedPrice,
                "realPrice" to realPrice,
                "completed" to completed
            )
            firestore.collection("tasks").document(taskId.toString()).set(taskData).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteTask(taskId: Long) {
        taskDao.deleteTask(taskId)
        try {
            firestore.collection("tasks").document(taskId.toString()).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun toggleTask(taskId: Long, completed: Boolean) {
        taskDao.setCompleted(taskId, completed)
        try {
            firestore.collection("tasks").document(taskId.toString()).update("completed", completed).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun setTaskRealPrice(taskId: Long, realPrice: Int?) {
        taskDao.setRealPrice(taskId, realPrice)
        try {
            firestore.collection("tasks").document(taskId.toString()).update("realPrice", realPrice).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
