package ruiz.angel.proyectofinal_1.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ruiz.angel.proyectofinal_1.data.database.AppDatabase
import ruiz.angel.proyectofinal_1.data.database.entity.PriceOptionEntity
import ruiz.angel.proyectofinal_1.data.database.entity.SubtaskEntity
import ruiz.angel.proyectofinal_1.data.models.PriceOption
import ruiz.angel.proyectofinal_1.data.models.Subtask

class SubtasksRepository(private val database: AppDatabase) {
    private val subtaskDao = database.subtaskDao()
    private val priceOptionDao = database.priceOptionDao()

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
