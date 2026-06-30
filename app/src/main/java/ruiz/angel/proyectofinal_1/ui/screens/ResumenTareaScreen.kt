package ruiz.angel.proyectofinal_1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ruiz.angel.proyectofinal_1.data.models.EstadoTarea
import ruiz.angel.proyectofinal_1.data.models.Evento
import ruiz.angel.proyectofinal_1.data.models.Subtarea
import ruiz.angel.proyectofinal_1.data.models.Tarea
import ruiz.angel.proyectofinal_1.data.models.ResumenTarea
import ruiz.angel.proyectofinal_1.data.models.toResumen
import ruiz.angel.proyectofinal_1.ui.theme.Azul
import ruiz.angel.proyectofinal_1.ui.theme.Borde
import ruiz.angel.proyectofinal_1.ui.theme.FondoPantalla
import ruiz.angel.proyectofinal_1.ui.theme.FondoTarjeta
import ruiz.angel.proyectofinal_1.ui.theme.GrisFondo
import ruiz.angel.proyectofinal_1.ui.theme.NaranjaFondo
import ruiz.angel.proyectofinal_1.ui.theme.Rojo
import ruiz.angel.proyectofinal_1.ui.theme.RojoFondo
import ruiz.angel.proyectofinal_1.ui.theme.Verde
import ruiz.angel.proyectofinal_1.ui.theme.VerdeFondo


@Composable
fun EventSummaryScreen(
    evento: Evento,
    onBack: () -> Unit = {}
) {
    val resumenTareas = evento.toResumen()
    val maxEstimado = (resumenTareas.maxOfOrNull { it.estimado } ?: 0).coerceAtLeast(1)
    val porcentajeGlobal = if (evento.estimated == 0) 0f
    else (evento.spent.toFloat() / evento.estimated.toFloat()).coerceIn(0f, 1f)

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantalla)
                .padding(innerPadding)
        ) {
            SummaryTopBar(
                eventoNombre = evento.nombre,
                fecha = evento.fecha,
                onBack = onBack
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                HeroBudgetCard(
                    estimado = evento.estimated,
                    gastado = evento.spent,
                    restante = evento.remaining,
                    porcentaje = porcentajeGlobal
                )

                StatsRow(
                    total = evento.taskCount,
                    completadas = evento.taskCompletedCount,
                    enProgreso = evento.taskCount - evento.taskCompletedCount
                )

                ComparativeChartCard(
                    resumenTareas = resumenTareas,
                    maxEstimado = maxEstimado
                )

                BreakdownCard(resumenTareas = resumenTareas)
            }
        }
    }
}

@Composable
fun SummaryTopBar(
    eventoNombre: String,
    fecha: String,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Azul)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "‹", fontSize = 20.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Resumen del evento",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = "$eventoNombre · $fecha",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.75f)
                )
            }
        }
    }
}

@Composable
fun HeroBudgetCard(
    estimado: Int,
    gastado: Int,
    restante: Int,
    porcentaje: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "PRESUPUESTO ESTIMADO",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = String.format("$%,d", estimado),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Azul
                    )
                }
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Borde)
                )
                Column(
                    modifier = Modifier.weight(1f).padding(start = 12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "GASTO REAL",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = String.format("$%,d", gastado),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Verde
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Ejecución del presupuesto", fontSize = 11.sp, color = Color.Gray)
                Text(
                    text = "${(porcentaje * 100).toInt()}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Azul
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Azul.copy(alpha = 0.15f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(porcentaje)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(5.dp))
                        .background(Azul)
                )
            }

            HorizontalDivider(
                color = Borde,
                thickness = 0.5.dp,
                modifier = Modifier.padding(top = 12.dp, bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Presupuesto restante", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = String.format("$%,d", restante),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun StatsRow(total: Int, completadas: Int, enProgreso: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(icono = "📋", valor = total.toString(), etiqueta = "Tareas totales", color = Color.Black)
        StatCard(icono = "✓", valor = completadas.toString(), etiqueta = "Completadas", color = Verde)
        StatCard(icono = "⏳", valor = enProgreso.toString(), etiqueta = "En progreso", color = Color(0xFFD97706))
    }
}

@Composable
fun RowScope.StatCard(icono: String, valor: String, etiqueta: String, color: Color) {
    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icono, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = valor, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = etiqueta,
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun ComparativeChartCard(
    resumenTareas: List<ResumenTarea>,
    maxEstimado: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Comparativa por tarea",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(14.dp))

            resumenTareas.forEachIndexed { index, item ->
                ChartRow(item = item, maxEstimado = maxEstimado)
                if (index < resumenTareas.lastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = Borde, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                LegendItem(color = Azul.copy(alpha = 0.25f), label = "Estimado")
                LegendItem(color = Azul, label = "Gasto real")
                LegendItem(color = Verde, label = "Completado")
            }
        }
    }
}

@Composable
fun ChartRow(item: ResumenTarea, maxEstimado: Int) {
    val barColor = if (item.estado == EstadoTarea.COMPLETADA) Verde
    else if (item.estado == EstadoTarea.EXCEDIDA) Rojo
    else item.color

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.nombre, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            Row {
                Text(
                    text = "Est. ${String.format("$%,d", item.estimado)}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = String.format("$%,d", item.gastado),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = barColor
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Barra "estimado" - proporcional al máximo estimado del evento
        val estimadoFraction = (item.estimado.toFloat() / maxEstimado.toFloat()).coerceIn(0f, 1f)
        Box(
            modifier = Modifier
                .fillMaxWidth(estimadoFraction)
                .height(7.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(GrisFondo)
        )
        Spacer(modifier = Modifier.height(3.dp))

        // Barra "real" - proporcional al estimado de la propia tarea
        Box(
            modifier = Modifier
                .fillMaxWidth(estimadoFraction)
                .height(7.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(GrisFondo)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(item.porcentaje)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(barColor)
            )
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = label, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun BreakdownCard(resumenTareas: List<ResumenTarea>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Desglose",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            resumenTareas.forEachIndexed { index, item ->
                BreakdownRow(item = item)
                if (index < resumenTareas.lastIndex) {
                    HorizontalDivider(color = Color(0xFFF9FAFB), thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
fun BreakdownRow(item: ResumenTarea) {
    val colores: Pair<Color, Color> = when (item.estado) {
        EstadoTarea.COMPLETADA -> Pair(VerdeFondo, Verde)
        EstadoTarea.EXCEDIDA -> Pair(RojoFondo, Rojo)
        EstadoTarea.EN_PROGRESO -> Pair(NaranjaFondo, Color(0xFFD97706))
        EstadoTarea.PENDIENTE -> Pair(GrisFondo, Color.Gray)
    }
    val bgColor = colores.first
    val textColor = colores.second

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(item.color)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = item.nombre,
            fontSize = 13.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = String.format("$%,d", item.estimado),
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.width(64.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
        Text(
            text = String.format("$%,d", item.gastado),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.width(64.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor)
                .padding(horizontal = 7.dp, vertical = 2.dp)
        ) {
            Text(text = item.estado.etiqueta, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = textColor)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EventSummaryScreenPreview() {
    EventSummaryScreen(
        evento = Evento(
            nombre = "Boda",
            fecha = "15 Jul 2026",
            nombreUsuario = "Joel Ruben",
            emailUsuario = "joel@itson.edu.mx",
            inicialesUsuario = "JR",
            tareas = listOf(
                Tarea(
                    nombre = "Salón de eventos",
                    completada = false,
                    subtareas = listOf(
                        Subtarea("Cotización de salones", 0, completada = true),
                        Subtarea("Reserva del salón", 5000, completada = true),
                        Subtarea("Decoración", 7000, completada = false),
                    )
                ),
                Tarea(
                    nombre = "Comida",
                    completada = false,
                    subtareas = listOf(
                        Subtarea("Carne Azada", 9500, completada = true)
                    )
                ),
                Tarea(
                    nombre = "Fotografía y video",
                    completada = false,
                    subtareas = listOf(
                        Subtarea("Sesión de fotos", 1900, completada = true),
                        Subtarea("Video del evento", 6600, completada = false)
                    )
                ),
                Tarea(
                    nombre = "Invitaciones",
                    completada = true,
                    subtareas = listOf(
                        Subtarea("Crear invitaciones", 500, completada = true),
                        Subtarea("Entregar invitaciones", 2000, completada = true)
                    )
                )
            )
        )
    )
}