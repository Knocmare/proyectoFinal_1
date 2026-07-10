package ruiz.angel.proyectofinal_1.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ruiz.angel.proyectofinal_1.data.database.entity.TaskEntity
import ruiz.angel.proyectofinal_1.data.database.relation.TaskWithSubtasks

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("DELETE FROM Tasks WHERE idTask = :taskId")
    suspend fun deleteTask(taskId: Long)

    @Query("UPDATE Tasks SET completedTask = :completed WHERE idTask = :taskId")
    suspend fun setCompleted(taskId: Long, completed: Boolean)

    @Query("UPDATE Tasks SET realPriceTask = :realPrice WHERE idTask = :taskId")
    suspend fun setRealPrice(taskId: Long, realPrice: Int?)

    @Transaction
    @Query("SELECT * FROM Tasks WHERE idTask = :taskId")
    fun getTaskWithSubtasks(taskId: Long): Flow<TaskWithSubtasks?>

    @Query("SELECT * FROM Tasks WHERE idTask = :taskId")
    suspend fun getTaskById(taskId: Long): TaskEntity?
}
