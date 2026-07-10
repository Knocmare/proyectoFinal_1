package ruiz.angel.proyectofinal_1.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ruiz.angel.proyectofinal_1.data.models.User
import ruiz.angel.proyectofinal_1.data.repository.AuthResult
import ruiz.angel.proyectofinal_1.data.repository.UserRepository

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val currentUser: StateFlow<User?> = repository.currentUserId
        .flatMapLatest { userId ->
            if (userId == null) flowOf(null) else repository.observeUser(userId)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Ingresa tu correo y contraseña"
            return
        }
        viewModelScope.launch {
            isLoading = true
            when (val result = repository.login(email, password)) {
                is AuthResult.Success -> {
                    errorMessage = null
                    onSuccess()
                }
                is AuthResult.Error -> errorMessage = result.message
            }
            isLoading = false
        }
    }

    fun loginWithGoogle(idToken: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            when (val result = repository.loginWithGoogle(idToken)) {
                is AuthResult.Success -> {
                    errorMessage = null
                    onSuccess()
                }
                is AuthResult.Error -> errorMessage = result.message
            }
            isLoading = false
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String, onSuccess: () -> Unit) {
        when {
            name.isBlank() || email.isBlank() || password.isBlank() -> {
                errorMessage = "Completa todos los campos"
                return
            }
            password != confirmPassword -> {
                errorMessage = "Las contraseñas no coinciden"
                return
            }
            password.length < 6 -> {
                errorMessage = "La contraseña debe tener al menos 6 caracteres"
                return
            }
        }
        viewModelScope.launch {
            isLoading = true
            when (val result = repository.register(name, email, password)) {
                is AuthResult.Success -> {
                    errorMessage = null
                    onSuccess()
                }
                is AuthResult.Error -> errorMessage = result.message
            }
            isLoading = false
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            onComplete()
        }
    }

    fun updateName(userId: Long, name: String) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.updateName(userId, name) }
    }

    fun changePassword(userId: Long, currentPassword: String, newPassword: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            when (val result = repository.changePassword(userId, currentPassword, newPassword)) {
                is AuthResult.Success -> {
                    errorMessage = null
                    onSuccess()
                }
                is AuthResult.Error -> errorMessage = result.message
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
