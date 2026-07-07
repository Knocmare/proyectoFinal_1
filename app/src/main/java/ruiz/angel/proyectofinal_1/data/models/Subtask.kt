package ruiz.angel.proyectofinal_1.data.models

data class Subtask(
    val id: Long = 0,
    val taskId: Long = 0,
    val name: String,
    val description: String = "",
    val estimatedPrice: Int,
    val realPrice: Int? = null,
    val place: String? = null,
    val completed: Boolean
) {
    val spent: Int
        get() = realPrice ?: 0
}
