package ruiz.angel.proyectofinal_1.data.repository
import ruiz.angel.proyectofinal_1.data.database.AppDatabase
import ruiz.angel.proyectofinal_1.data.database.entity.UserEntity
import ruiz.angel.proyectofinal_1.data.models.User

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data object InvalidCredentials : AuthResult()
    data object EmailAlreadyRegistered : AuthResult()
}

sealed class PasswordChangeResult {
    data object Success : PasswordChangeResult()
    data object WrongCurrentPassword : PasswordChangeResult()
}

class UsersRepository(private val database: AppDatabase) {
    private val userDao = database.userDao()

    suspend fun register(name: String, email: String, password: String): AuthResult {
        val existing = userDao.getByEmail(email)
        if (existing != null) return AuthResult.EmailAlreadyRegistered

        val newId = userDao.insertUser(
            UserEntity(name = name, email = email, password = password)
        )
        return AuthResult.Success(User(id = newId, name = name, email = email))
    }

    suspend fun login(email: String, password: String): AuthResult {
        val entity = userDao.login(email, password) ?: return AuthResult.InvalidCredentials
        return AuthResult.Success(entity.toDomain())
    }

    suspend fun updateName(userId: Long, name: String) {
        userDao.updateName(userId, name)
    }

    suspend fun changePassword(userId: Long, currentPassword: String, newPassword: String): PasswordChangeResult {
        val rowsUpdated = userDao.updatePassword(userId, currentPassword, newPassword)
        return if (rowsUpdated > 0) PasswordChangeResult.Success else PasswordChangeResult.WrongCurrentPassword
    }
}

private fun UserEntity.toDomain(): User = User(id = id, name = name, email = email)