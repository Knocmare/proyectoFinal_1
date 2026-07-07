package ruiz.angel.proyectofinal_1.data.models

import androidx.compose.ui.graphics.Color
import ruiz.angel.proyectofinal_1.ui.theme.Azul
import ruiz.angel.proyectofinal_1.ui.theme.GrisOscuro
import ruiz.angel.proyectofinal_1.ui.theme.Morado
import ruiz.angel.proyectofinal_1.ui.theme.Naranja
import ruiz.angel.proyectofinal_1.ui.theme.Rosa
import ruiz.angel.proyectofinal_1.ui.theme.Verde

data class ResumenTarea(
    val tarea: Task,
    val color: Color
) {

    val nombre: String
        get() = tarea.name

    val estimado: Int
        get() = tarea.price

    val gastado: Int
        get() = tarea.spent

    val porcentaje: Float
        get() = if (estimado == 0) {
            0f
        } else {
            (gastado.toFloat() / estimado.toFloat()).coerceIn(0f, 1f)
        }

    val estado: EstadoTarea
        get() = when {
            tarea.isFullyCompleted -> EstadoTarea.COMPLETADA
            gastado > estimado -> EstadoTarea.EXCEDIDA
            gastado > 0 -> EstadoTarea.EN_PROGRESO
            else -> EstadoTarea.PENDIENTE
        }
}

enum class EstadoTarea(val etiqueta: String) {
    COMPLETADA("Completada"),
    EN_PROGRESO("En progreso"),
    EXCEDIDA("Excedida"),
    PENDIENTE("Pendiente")
}

val PaletaTareas: List<Color> = listOf(
    Azul,
    Naranja,
    Morado,
    Verde,
    Rosa,
    GrisOscuro
)

fun Event.toResumen(): List<ResumenTarea> {
    return tasks.mapIndexed { index, tarea ->
        ResumenTarea(
            tarea = tarea,
            color = PaletaTareas[index % PaletaTareas.size]
        )
    }
}
