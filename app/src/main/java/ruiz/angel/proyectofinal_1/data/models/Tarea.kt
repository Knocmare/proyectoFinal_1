package ruiz.angel.proyectofinal_1.data.models


data class Tarea(
    val nombre: String,
    val completada: Boolean,
    val subtareas: List<Subtarea> = emptyList()
) {
    val subCount: Int
        get() = subtareas.size

    val precio: Int
        get() = subtareas.sumOf { it.price }

    val completed: Boolean
        get() = subtareas.all { it.completed }
}
