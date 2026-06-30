package ruiz.angel.proyectofinal_1.data.models

data class Evento(
    val nombre: String,
    val fecha: String,
    val nombreUsuario: String,
    val emailUsuario: String,
    val inicialesUsuario: String,
    val tareas: List<Tarea> = emptyList()
) {
    val taskCount: Int
        get() = tareas.size

    val taskCompletedCount: Int
        get() = tareas.count { tarea ->
            tarea.completada || (tarea.subtareas.isNotEmpty() && tarea.subtareas.all { it.completed })
        }

    val estimated: Int
        get() = tareas.sumOf { it.precio }

    val spent: Int
        get() = tareas.sumOf { tarea ->
            val isCompleted = tarea.completada || (tarea.subtareas.isNotEmpty() && tarea.subtareas.all { it.completed })
            if (isCompleted) tarea.precio else 0
        }

    val remaining: Int
        get() = estimated - spent
}