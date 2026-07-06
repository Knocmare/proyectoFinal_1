package ruiz.angel.proyectofinal_1.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ruiz.angel.proyectofinal_1.data.database.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Query("UPDATE Tasks SET completedTask = :completed WHERE idTask = :taskId")
    suspend fun setCompleted(taskId: Long, completed: Boolean)
}