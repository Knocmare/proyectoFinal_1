package ruiz.angel.proyectofinal_1.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ruiz.angel.proyectofinal_1.data.database.entity.PriceOptionEntity

@Dao
interface PriceOptionDao {
    @Insert
    suspend fun insertOption(option: PriceOptionEntity): Long

    @Query("DELETE FROM PriceOptions WHERE idPriceOption = :id")
    suspend fun deleteOption(id: Long)

    @Query("SELECT * FROM PriceOptions WHERE subtaskId = :subtaskId ORDER BY costOption ASC")
    fun getForSubtask(subtaskId: Long): Flow<List<PriceOptionEntity>>
}
