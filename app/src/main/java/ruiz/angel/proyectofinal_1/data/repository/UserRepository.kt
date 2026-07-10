package ruiz.angel.proyectofinal_1.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
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
        return try {
            // 1. Registrar en Firebase Auth
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(normalizedEmail, password)
                .await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                // 2. Guardar en la base de datos local (Room) para persistencia local
                val salt = PasswordHasher.generateSalt()
                val hash = PasswordHasher.hash(password, salt)
                val userId = userDao.insertUser(
                    UserEntity(name = name.trim(), email = normalizedEmail, password = hash, salt = salt)
                )
                sessionManager.saveSession(userId)
                AuthResult.Success(User(id = userId, name = name.trim(), email = normalizedEmail))
            } else {
                AuthResult.Error("Error al crear el usuario en Firebase")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error en el registro")
        }
    }

    suspend fun login(email: String, password: String): AuthResult {
        val normalizedEmail = email.trim().lowercase()
        return try {
            // 1. Iniciar sesión en Firebase Auth
            val authResult = FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(normalizedEmail, password)
                .await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                // 2. Verificar o crear usuario en Room
                var localUser = userDao.findByEmail(normalizedEmail)
                if (localUser == null) {
                    // Si el usuario existe en Firebase pero no en Room (ej. nueva instalación)
                    val userId = userDao.insertUser(
                        UserEntity(
                            name = firebaseUser.displayName ?: "Usuario",
                            email = normalizedEmail,
                            password = "", // No guardamos pass real de Firebase en local por seguridad
                            salt = ""
                        )
                    )
                    localUser = userDao.getByIdOnce(userId)
                }

                if (localUser != null) {
                    sessionManager.saveSession(localUser.id)
                    AuthResult.Success(localUser.toDomain())
                } else {
                    AuthResult.Error("Error de sincronización local")
                }
            } else {
                AuthResult.Error("Error al obtener usuario de Firebase")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AuthResult.Error("Correo o contraseña incorrectos")
        }
    }

    suspend fun loginWithGoogle(idToken: String): AuthResult {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = FirebaseAuth.getInstance().signInWithCredential(credential).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                val email = firebaseUser.email ?: ""
                var localUser = userDao.findByEmail(email)
                if (localUser == null) {
                    val userId = userDao.insertUser(
                        UserEntity(
                            name = firebaseUser.displayName ?: "Usuario de Google",
                            email = email,
                            password = "",
                            salt = ""
                        )
                    )
                    localUser = userDao.getByIdOnce(userId)
                }

                if (localUser != null) {
                    sessionManager.saveSession(localUser.id)
                    AuthResult.Success(localUser.toDomain())
                } else {
                    AuthResult.Error("Error al sincronizar usuario local")
                }
            } else {
                AuthResult.Error("Error al obtener usuario de Firebase")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error en la autenticación de Google")
        }
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }

    suspend fun updateName(userId: Long, name: String) {
        userDao.updateName(userId, name.trim())
    }

    suspend fun changePassword(userId: Long, currentPassword: String, newPassword: String): AuthResult {
        return try {
            val firebaseUser = FirebaseAuth.getInstance().currentUser 
                ?: return AuthResult.Error("Sesión expirada. Por favor, inicia sesión de nuevo.")
            
            val email = firebaseUser.email ?: return AuthResult.Error("No se pudo obtener el correo del usuario.")

            val credential = EmailAuthProvider.getCredential(email, currentPassword)
            firebaseUser.reauthenticate(credential).await()

            firebaseUser.updatePassword(newPassword).await()

            val localUser = userDao.getByIdOnce(userId) ?: return AuthResult.Error("Usuario no encontrado localmente")
            val newSalt = PasswordHasher.generateSalt()
            val newHash = PasswordHasher.hash(newPassword, newSalt)
            userDao.updatePassword(userId, newHash, newSalt)
            
            AuthResult.Success(localUser.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            AuthResult.Error("La contraseña actual es incorrecta o hubo un error de conexión.")
        }
    }
}

private fun UserEntity.toDomain(): User = User(id = id, name = name, email = email)
