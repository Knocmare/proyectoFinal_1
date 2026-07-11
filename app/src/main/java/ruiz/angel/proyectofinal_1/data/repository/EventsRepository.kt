package ruiz.angel.proyectofinal_1.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import ruiz.angel.proyectofinal_1.data.database.AppDatabase
import ruiz.angel.proyectofinal_1.data.database.entity.EventEntity
import ruiz.angel.proyectofinal_1.data.database.entity.SubtaskEntity
import ruiz.angel.proyectofinal_1.data.database.relation.EventWithTasks
import ruiz.angel.proyectofinal_1.data.database.relation.TaskWithSubtasks
import ruiz.angel.proyectofinal_1.data.models.Event
import ruiz.angel.proyectofinal_1.data.models.Subtask
import ruiz.angel.proyectofinal_1.data.models.Task

class EventsRepository(private val database: AppDatabase) {
    private val eventDao = database.eventDao()
    private val firestore = FirebaseFirestore.getInstance()

    fun getEvents(userId: Long): Flow<List<Event>> =
        eventDao.getEventsWithTasks(userId).map { list -> list.map { it.toDomain() } }

    suspend fun createEvent(userId: Long, name: String, date: String): Long {
        val eventEntity = EventEntity(userId = userId, name = name, date = date)
        val localId = eventDao.insertEvent(eventEntity)
        
        // Sincronizar con Firestore (en segundo plano o con try-catch para no bloquear el flujo offline)
        try {
            val eventData = hashMapOf(
                "userId" to userId,
                "name" to name,
                "date" to date,
                "localId" to localId
            )
            firestore.collection("events").document(localId.toString()).set(eventData).await()
        } catch (e: Exception) {
            e.printStackTrace() // Si falla por falta de conexión, Room ya guardó el dato localmente
        }
        
        return localId
    }

    suspend fun updateEvent(eventId: Long, userId: Long, name: String, date: String) {
        eventDao.updateEvent(EventEntity(id = eventId, userId = userId, name = name, date = date))
        
        try {
            val eventData = hashMapOf(
                "userId" to userId,
                "name" to name,
                "date" to date
            )
            firestore.collection("events").document(eventId.toString()).update(eventData as Map<String, Any>).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteEvent(eventId: Long) {
        eventDao.deleteEvent(eventId)
        
        try {
            firestore.collection("events").document(eventId.toString()).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Sincroniza los eventos desde Firebase hacia Room para casos de nuevas instalaciones
     */
    suspend fun syncFromFirebase(userId: Long) {
        try {
            val snapshot = firestore.collection("events")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            for (doc in snapshot.documents) {
                val name = doc.getString("name") ?: continue
                val date = doc.getString("date") ?: ""
                val remoteLocalId = doc.getLong("localId") ?: 0L
                
                // Si no existe localmente, lo insertamos
                // Nota: Esto es una sincronización básica. Una robusta requeriría IDs únicos globales (UUID).
                eventDao.insertEvent(EventEntity(name = name, userId = userId, date = date))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

private fun EventWithTasks.toDomain(): Event = Event(
    id = event.id,
    userId = event.userId,
    name = event.name,
    date = event.date,
    tasks = tasks.map { it.toDomain() }
)

private fun TaskWithSubtasks.toDomain(): Task = Task(
    id = task.id,
    eventId = task.eventId,
    name = task.name,
    description = task.description,
    estimatedPrice = task.estimatedPrice,
    realPrice = task.realPrice,
    completed = task.completed,
    subtasks = subtasks.map { it.toDomain() }
)

private fun SubtaskEntity.toDomain(): Subtask = Subtask(
    id = id,
    taskId = taskId,
    name = name,
    description = description,
    estimatedPrice = estimatedPrice,
    realPrice = realPrice,
    place = place,
    completed = completed
)
