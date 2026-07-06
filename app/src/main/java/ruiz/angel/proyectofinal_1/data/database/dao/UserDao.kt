package ruiz.angel.proyectofinal_1.data.database.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ruiz.angel.proyectofinal_1.data.database.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM Users WHERE emailUser = :email AND passwordUser = :password LIMIT 1")
    suspend fun login(email: String, password: String): UserEntity?

    @Query("SELECT * FROM Users WHERE emailUser = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("UPDATE Users SET nameUser = :name WHERE idUser = :userId")
    suspend fun updateName(userId: Long, name: String)

    @Query("UPDATE Users SET passwordUser = :newPassword WHERE idUser = :userId AND passwordUser = :currentPassword")
    suspend fun updatePassword(userId: Long, currentPassword: String, newPassword: String): Int
}