package ruiz.angel.proyectofinal_1.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ruiz.angel.proyectofinal_1.data.database.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM Users WHERE emailUser = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?

    @Query("SELECT * FROM Users WHERE idUser = :userId LIMIT 1")
    fun getById(userId: Long): Flow<UserEntity?>

    @Query("SELECT * FROM Users WHERE idUser = :userId LIMIT 1")
    suspend fun getByIdOnce(userId: Long): UserEntity?

    @Query("UPDATE Users SET nameUser = :name WHERE idUser = :userId")
    suspend fun updateName(userId: Long, name: String)

    @Query("UPDATE Users SET passwordUser = :passwordHash, saltUser = :salt WHERE idUser = :userId")
    suspend fun updatePassword(userId: Long, passwordHash: String, salt: String)
}
