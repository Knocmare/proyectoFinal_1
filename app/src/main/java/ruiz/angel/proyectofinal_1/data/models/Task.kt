package ruiz.angel.proyectofinal_1.data.models

data class Task(
    val name: String,
    val completed: Boolean,
    val subtasks: List<Subtask> = emptyList()
) {
    val subtaskCount: Int
        get() = subtasks.size

    val price: Int
        get() = subtasks.sumOf { it.price }

    val isFullyCompleted: Boolean
        get() = completed || (subtasks.isNotEmpty() && subtasks.all { it.completed })
}