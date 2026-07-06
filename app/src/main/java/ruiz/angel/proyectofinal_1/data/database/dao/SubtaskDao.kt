package ruiz.angel.proyectofinal_1.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ruiz.angel.proyectofinal_1.data.database.entity.SubtaskEntity

@Dao
interface SubtaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtask(subtask: SubtaskEntity): Long

    @Query("UPDATE Subtasks SET completedSubtask = :completed WHERE idSubtask = :subtaskId")
    suspend fun setCompleted(subtaskId: Long, completed: Boolean)
}