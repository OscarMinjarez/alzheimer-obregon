package mx.edu.itson.alzheimerobregon.features.evaluation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.itson.alzheimerobregon.R
import mx.edu.itson.alzheimerobregon.features.evaluation.Evaluation
import mx.edu.itson.alzheimerobregon.features.evaluation.EvaluationType
import mx.edu.itson.alzheimerobregon.ui.theme.AlzheimerObregonTheme
import java.text.SimpleDateFormat
import java.util.*
import mx.edu.itson.alzheimerobregon.features.patient.ui.PatientDetailActivity

class EvaluationHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlzheimerObregonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EvaluationHistoryScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun EvaluationHistoryScreen(modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var selectedFilter by remember { mutableStateOf("Todos") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = {
                    // Volver a PatientDetailActivity
                    val activity = context as? android.app.Activity
                    val patientId: String? = activity?.intent?.getStringExtra("patient_id")
                    val intent = Intent(context, PatientDetailActivity::class.java)
                    patientId?.let { intent.putExtra("patient_id", it) }
                    context.startActivity(intent)
                    activity?.finish()
                }),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_left),
                    contentDescription = null,
                    tint = colorResource(id = R.color.azul_ultramar),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.back_button),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.azul_ultramar)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.evaluation_history_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.azul_ultramar)
            )
            Text(
                text = "Alexa Lopez",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }

        HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.Gris_fondo)),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                FilterSection(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it },
                    modifier = Modifier.padding(vertical = 24.dp, horizontal = 24.dp)
                )
            }

            item {
                ScoreEvolutionCard(
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            item {
                Text(
                    text = stringResource(id = R.string.registered_evaluations_title),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.azul_ultramar),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
                )
            }

            items(getMockEvaluations().filter { 
                selectedFilter == "Todos" || it.type.name == selectedFilter 
            }) { evaluation ->
                EvaluationListItem(
                    evaluation = evaluation,
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
fun FilterSection(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("Todos", "MMSE", "Tinetti").forEach { filter ->
            FilterChip(
                label = filter,
                isSelected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) colorResource(id = R.color.azul_ultramar) else Color(0xFFE0E4E7),
        modifier = modifier
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ScoreEvolutionCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.Gris_borde)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.graph_icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.score_evolution_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.azul_ultramar)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            SimpleLineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }
    }
}

@Composable
fun SimpleLineChart(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.graph),
        contentDescription = "Gráfica de evolución",
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun EvaluationListItem(
    evaluation: Evaluation,
    modifier: Modifier = Modifier
) {
    val subtitle = if (evaluation.type == EvaluationType.MMSE) {
        "Mini-Mental State Examination"
    } else {
        "Escala de Tinetti"
    }

    val maxScore = if (evaluation.type == EvaluationType.MMSE) 30 else 28

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.Gris_borde)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = evaluation.type.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.azul_ultramar)
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.azul_texto)
                    )
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${evaluation.totalScore}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = " / $maxScore",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.tinetti_icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale("es", "MX"))
                        .format(evaluation.applicationDate),
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.gris_texto)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.evaluator_label, evaluation.evaluator), // Mock evaluator
                fontSize = 14.sp,
                color = colorResource(id = R.color.gris_texto)
            )

            if (evaluation.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            colorResource(id = R.color.azul_claro),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = evaluation.notes,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.azul_texto),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

fun getMockEvaluations(): List<Evaluation> {
    val calendar = Calendar.getInstance()
    
    calendar.set(2026, Calendar.JANUARY, 14)
    val ev1 = Evaluation(
        type = EvaluationType.MMSE,
        applicationDate = calendar.time,
        totalScore = 22,
        notes = "",
        evaluator = "Dra. Elena Sanchez"
    )
    
    val ev2 = Evaluation(
        type = EvaluationType.TINETTI,
        applicationDate = calendar.time,
        totalScore = 20,
        notes = "",
        evaluator = "Fisioterapeuta Carlos Ruiz"
    )
    
    calendar.set(2025, Calendar.SEPTEMBER, 9)
    val ev3 = Evaluation(
        type = EvaluationType.MMSE,
        applicationDate = calendar.time,
        totalScore = 24,
        notes = "Paciente colaboradora, ligera dificultad en memoria diferida",
        evaluator = "Dra. Elena Sanchez"
    )
    
    calendar.set(2025, Calendar.SEPTEMBER, 4)
    val ev4 = Evaluation(
        type = EvaluationType.TINETTI,
        applicationDate = calendar.time,
        totalScore = 24,
        notes = "",
        evaluator = "Fisioterapeuta Carlos Ruiz"
    )
    
    return listOf(ev1, ev2, ev3, ev4)
}

@Preview(showBackground = true)
@Composable
fun EvaluationHistoryScreenPreview() {
    AlzheimerObregonTheme {
        EvaluationHistoryScreen()
    }
}
