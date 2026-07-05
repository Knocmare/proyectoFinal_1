package ruiz.angel.proyectofinal_1.data.models

data class Event(
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
        get() = tasks.sumOf { if (it.isFullyCompleted) it.price else 0 }

    val remaining: Int
        get() = estimated - spent
}