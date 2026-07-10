package ruiz.angel.proyectofinal_1.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Register

@Serializable
object TaskList

@Serializable
object Configuration

@Serializable
object CreateEvent

@Serializable
data class CreateTask(val eventId: Long)

@Serializable
data class SubtaskList(val taskId: Long)

@Serializable
data class SubtaskForm(val taskId: Long, val subtaskId: Long = -1L)

@Serializable
data class PriceComparisonRoute(val subtaskId: Long)

@Serializable
data class Summary(val eventId: Long)
