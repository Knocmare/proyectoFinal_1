package ruiz.angel.proyectofinal_1.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ruiz.angel.proyectofinal_1.data.database.entity.EventEntity
import ruiz.angel.proyectofinal_1.data.database.relation.EventWithTasks

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("DELETE FROM Events WHERE idEvent = :eventId")
    suspend fun deleteEvent(eventId: Long)

    @Transaction
    @Query("SELECT * FROM Events WHERE userId = :userId")
    fun getEventsWithTasks(userId: Long): Flow<List<EventWithTasks>>

    @Transaction
    @Query("SELECT * FROM Events WHERE idEvent = :eventId")
    fun getEventWithTasks(eventId: Long): Flow<EventWithTasks?>
}
