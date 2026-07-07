package ruiz.angel.proyectofinal_1.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ruiz.angel.proyectofinal_1.data.database.AppDatabase
import ruiz.angel.proyectofinal_1.data.database.entity.UserEntity
import ruiz.angel.proyectofinal_1.data.models.User
import ruiz.angel.proyectofinal_1.data.security.PasswordHasher
import ruiz.angel.proyectofinal_1.data.security.SessionManager

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class UserRepository(
    database: AppDatabase,
    private val sessionManager: SessionManager
) {
    private val userDao = database.userDao()

    val currentUserId: Flow<Long?> = sessionManager.userId

    fun observeUser(userId: Long): Flow<User?> =
        userDao.getById(userId).map { it?.toDomain() }

    suspend fun register(name: String, email: String, password: String): AuthResult {
        val normalizedEmail = email.trim().lowercase()
        if (userDao.findByEmail(normalizedEmail) != null) {
            return AuthResult.Error("Ya existe una cuenta con ese correo electrónico")
        }
        val salt = PasswordHasher.generateSalt()
        val hash = PasswordHasher.hash(password, salt)
        val userId = userDao.insertUser(
            UserEntity(name = name.trim(), email = normalizedEmail, password = hash, salt = salt)
        )
        sessionManager.saveSession(userId)
        return AuthResult.Success(User(id = userId, name = name.trim(), email = normalizedEmail))
    }

    suspend fun login(email: String, password: String): AuthResult {
        val normalizedEmail = email.trim().lowercase()
        val user = userDao.findByEmail(normalizedEmail)
            ?: return AuthResult.Error("Correo o contraseña incorrectos")
        if (!PasswordHasher.verify(password, user.salt, user.password)) {
            return AuthResult.Error("Correo o contraseña incorrectos")
        }
        sessionManager.saveSession(user.id)
        return AuthResult.Success(user.toDomain())
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }

    suspend fun updateName(userId: Long, name: String) {
        userDao.updateName(userId, name.trim())
    }

    suspend fun changePassword(userId: Long, currentPassword: String, newPassword: String): AuthResult {
        val user = userDao.getByIdOnce(userId)
            ?: return AuthResult.Error("Usuario no encontrado")
        if (!PasswordHasher.verify(currentPassword, user.salt, user.password)) {
            return AuthResult.Error("La contraseña actual no es correcta")
        }
        val newSalt = PasswordHasher.generateSalt()
        val newHash = PasswordHasher.hash(newPassword, newSalt)
        userDao.updatePassword(userId, newHash, newSalt)
        return AuthResult.Success(user.toDomain())
    }
}

private fun UserEntity.toDomain(): User = User(id = id, name = name, email = email)
