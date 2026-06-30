package ruiz.angel.proyectofinal_1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ruiz.angel.proyectofinal_1.ui.theme.*
import java.util.*

data class PriceOption(
    val id: String = UUID.randomUUID().toString(),
    val place: String,
    val cost: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceComparationScreen(
    onBackClick: () -> Unit = {}
) {
    var itemName by remember { mutableStateOf("Salón de fiesta") }
    var placeName by remember { mutableStateOf("") }
    var costValue by remember { mutableStateOf("") }

    var options by remember { 
        mutableStateOf(listOf(
            PriceOption(place = "Los Jardines", cost = 5000.0),
            PriceOption(place = "Las Hermanas Salon", cost = 10550.0),
            PriceOption(place = "Los Aguacates", cost = 12000.0)
        )) 
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Comparar Precios",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "AGREGAR OPCIÓN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextGray
                        )

                        Column {
                            Text("Artículo o servicio", fontSize = 12.sp, color = TextGray)
                            OutlinedTextField(
                                value = itemName,
                                onValueChange = { itemName = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryBlue,
                                    unfocusedBorderColor = BorderGray
                                )
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Lugar", fontSize = 12.sp, color = TextGray)
                                OutlinedTextField(
                                    value = placeName,
                                    onValueChange = { placeName = it },
                                    placeholder = { Text("Ej. Los jardines", fontSize = 14.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryBlue,
                                        unfocusedBorderColor = BorderGray
                                    )
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Costo", fontSize = 12.sp, color = TextGray)
                                OutlinedTextField(
                                    value = costValue,
                                    onValueChange = { costValue = it },
                                    placeholder = { Text("$ 0.00", fontSize = 14.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryBlue,
                                        unfocusedBorderColor = BorderGray
                                    )
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (placeName.isNotBlank() && costValue.isNotBlank()) {
                                    val cost = costValue.toDoubleOrNull() ?: 0.0
                                    options = options + PriceOption(place = placeName, cost = cost)
                                    placeName = ""
                                    costValue = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Agregar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }

            if (options.isNotEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    itemName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Surface(
                                    color = BackgroundGray,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "${options.size} opciones",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontSize = 12.sp,
                                        color = TextGray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            val minPrice = options.minOf { it.cost }
                            val maxPrice = options.maxOf { it.cost }

                            options.forEachIndexed { index, option ->
                                ComparisonItem(
                                    rank = index + 1,
                                    option = option,
                                    isBestPrice = option.cost == minPrice,
                                    maxPrice = maxPrice
                                )
                                if (index < options.size - 1) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = BorderGray.copy(alpha = 0.5f))

                            SummaryRow("Precio más alto", "$${String.format(Locale.getDefault(), "%,.0f", maxPrice)}", Color.Black)
                            SummaryRow("Precio más bajo", "$${String.format(Locale.getDefault(), "%,.0f", minPrice)}", Verde)
                            
                            val savings = maxPrice - minPrice
                            val savingsPercentage = if (maxPrice > 0) (savings / maxPrice) * 100 else 0.0
                            SummaryRow(
                                "Ahorro potencial",
                                "$${String.format(Locale.getDefault(), "%,.0f", savings)} (${String.format(Locale.getDefault(), "%.0f", savingsPercentage)}%)",
                                Verde,
                                isBold = true
                            )
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ComparisonItem(
    rank: Int,
    option: PriceOption,
    isBestPrice: Boolean,
    maxPrice: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        val circleColor = if (rank == 1) Color(0xFFFFF8E1) else BackgroundGray
        val circleTextColor = if (rank == 1) Color(0xFFF57F17) else TextGray

        Surface(
            modifier = Modifier.size(36.dp),
            color = circleColor,
            shape = RoundedCornerShape(18.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(rank.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = circleTextColor)
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(option.place, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isBestPrice) {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Mejor precio",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                color = Verde,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        "$${String.format(Locale.getDefault(), "%,.0f", option.cost)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (isBestPrice) Verde else Color.Black
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(6.dp))

            val progress = if (maxPrice > 0) (option.cost / maxPrice).toFloat() else 0f
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = if (isBestPrice) Verde else PrimaryBlue,
                trackColor = BackgroundGray,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, valueColor: Color, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextGray, fontSize = 14.sp)
        Text(
            value,
            color = valueColor,
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PriceComparationScreenPreview() {
    MaterialTheme {
        PriceComparationScreen()
    }
}
