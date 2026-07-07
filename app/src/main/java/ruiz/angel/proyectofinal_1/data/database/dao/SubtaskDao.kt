package ruiz.angel.proyectofinal_1.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ruiz.angel.proyectofinal_1.data.database.entity.SubtaskEntity

@Dao
interface SubtaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtask(subtask: SubtaskEntity): Long

    @Update
    suspend fun updateSubtask(subtask: SubtaskEntity)

    @Query("DELETE FROM Subtasks WHERE idSubtask = :subtaskId")
    suspend fun deleteSubtask(subtaskId: Long)

    @Query("UPDATE Subtasks SET completedSubtask = :completed WHERE idSubtask = :subtaskId")
    suspend fun setCompleted(subtaskId: Long, completed: Boolean)

    @Query("UPDATE Subtasks SET realPriceSubtask = :realPrice, placeSubtask = :place WHERE idSubtask = :subtaskId")
    suspend fun setPurchaseInfo(subtaskId: Long, realPrice: Int, place: String)

    @Query("SELECT * FROM Subtasks WHERE idSubtask = :subtaskId")
    suspend fun getSubtaskById(subtaskId: Long): SubtaskEntity?
}
