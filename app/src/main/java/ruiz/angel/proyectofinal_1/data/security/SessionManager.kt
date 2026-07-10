package ruiz.angel.proyectofinal_1.data.security

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore(name = "session")

/**
 * Guarda el id del usuario logueado en DataStore para que la sesión
 * sobreviva a reinicios de la app (auto-login).
 */
class SessionManager(private val context: Context) {
    private val userIdKey = longPreferencesKey("logged_in_user_id")

    val userId: Flow<Long?> = context.sessionDataStore.data.map { prefs ->
        prefs[userIdKey]?.takeIf { it != 0L }
    }

    suspend fun saveSession(userId: Long) {
        context.sessionDataStore.edit { it[userIdKey] = userId }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { it.remove(userIdKey) }
    }
}
