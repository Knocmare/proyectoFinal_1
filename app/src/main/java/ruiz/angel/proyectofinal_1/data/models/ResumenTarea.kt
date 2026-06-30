package ruiz.angel.proyectofinal_1.data.models


import androidx.compose.ui.graphics.Color
import ruiz.angel.proyectofinal_1.ui.theme.Azul
import ruiz.angel.proyectofinal_1.ui.theme.GrisOscuro
import ruiz.angel.proyectofinal_1.ui.theme.Morado
import ruiz.angel.proyectofinal_1.ui.theme.Naranja
import ruiz.angel.proyectofinal_1.ui.theme.Rosa
import ruiz.angel.proyectofinal_1.ui.theme.Verde

data class ResumenTarea(
    val tarea: Tarea,
    val color: Color
) {

    val nombre: String
        get() = tarea.nombre

    val estimado: Int
        get() = tarea.precio

    val gastado: Int
        get() = if (tarea.completed) {
            tarea.precio
        } else {
            tarea.subtareas.filter { it.completada }.sumOf { it.precio }
        }

    val porcentaje: Float
        get() = if (estimado == 0) {
            0f
        } else {
            (gastado.toFloat() / estimado.toFloat()).coerceIn(0f, 1f)
        }

    val estado: EstadoTarea
        get() = when {
            tarea.completed -> EstadoTarea.COMPLETADA
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

fun Evento.toResumen(): List<ResumenTarea> {
    return tareas.mapIndexed { index, tarea ->
        ResumenTarea(
            tarea = tarea,
            color = PaletaTareas[index % PaletaTareas.size]
        )
    }
}