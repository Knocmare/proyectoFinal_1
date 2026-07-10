package ruiz.angel.proyectofinal_1.data.models

data class Task(
    val id: Long = 0,
    val eventId: Long = 0,
    val name: String,
    val description: String = "",
    val estimatedPrice: Int = 0,
    val realPrice: Int? = null,
    val completed: Boolean,
    val subtasks: List<Subtask> = emptyList()
) {
    val subtaskCount: Int
        get() = subtasks.size

    /** Presupuesto estimado: si tiene subtareas, es la suma de sus estimados; si no, el propio. */
    val price: Int
        get() = if (subtasks.isNotEmpty()) subtasks.sumOf { it.estimatedPrice } else estimatedPrice

    /** Gasto real: si tiene subtareas, es la suma de sus gastos reales; si no, el propio. */
    val spent: Int
        get() = if (subtasks.isNotEmpty()) subtasks.sumOf { it.spent } else (realPrice ?: 0)

    val isFullyCompleted: Boolean
        get() = completed || (subtasks.isNotEmpty() && subtasks.all { it.completed })
}
