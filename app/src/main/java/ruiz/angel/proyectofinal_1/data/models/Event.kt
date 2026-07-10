package ruiz.angel.proyectofinal_1.data.models

data class Event(
    val id: Long = 0,
    val userId: Long = 0,
    val name: String,
    val date: String,
    val tasks: List<Task> = emptyList()
) {
    val taskCount: Int
        get() = tasks.size

    val taskCompletedCount: Int
        get() = tasks.count { it.isFullyCompleted }

    val estimated: Int
        get() = tasks.sumOf { it.price }

    val spent: Int
        get() = tasks.sumOf { it.spent }

    val remaining: Int
        get() = estimated - spent
}
