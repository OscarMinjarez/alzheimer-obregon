package mx.edu.itson.alzheimerobregon.features.patient.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.itson.alzheimerobregon.R
import mx.edu.itson.alzheimerobregon.ui.theme.AlzheimerObregonTheme

class PatientDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val patientId = intent.getStringExtra("patient_id")

        setContent {
            AlzheimerObregonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: PatientDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                    if (patientId != null) {
                        androidx.compose.runtime.LaunchedEffect(patientId) {
                            viewModel.fetchPatient(patientId)
                        }
                    }
                    PatientDetailScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun PatientDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: PatientDetailViewModel
) {
    val patientState = viewModel.patient.collectAsState()
    val errorState = viewModel.error.collectAsState()
    val patient = patientState.value
    val error = errorState.value
    val context = androidx.compose.ui.platform.LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF0F9FF),
                        Color(0xFFFFBB7E)
                    )
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        HeaderVolver(onBack = {
            val intent = android.content.Intent(context, PatientsActivity::class.java)
            context.startActivity(intent)
            if (context is android.app.Activity) {
                context.finish()
            }
        })
        Spacer(modifier = Modifier.height(16.dp))
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
        patient?.let {
            PatientInfoCard(patient = it)
        }
        Spacer(modifier = Modifier.height(16.dp))
        ButtonEvaluacion()
        Spacer(modifier = Modifier.height(16.dp))
        AccionesRapidas()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Historial de evaluaciones",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        EvaluationCard("Mini-Mental State Examination", "MMSE", "22", "30", "14/1/2026", "3")
        EvaluationCard("Escala de Tinetti", "Tinetti", "20", "28", "14/1/2026", "1")
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun HeaderVolver(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .clickable { onBack() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_left),
            contentDescription = null,
            tint = colorResource(id = R.color.azul_ultramar),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = stringResource(id = R.string.back_button),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.azul_ultramar)
        )
    }
}

@Composable
fun PatientInfoCard(patient: mx.edu.itson.alzheimerobregon.features.patient.Patient) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFC4CBD0)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.paciente1),
                    contentDescription = null,
                    modifier = Modifier.size(66.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(patient.fullName, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                    Text("${patient.age} años • ${patient.gender} • Sala ${patient.roomNumber}", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                }
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(colorResource(R.color.verde_spartan), CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFDDE6ED)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            patient.primaryContact["name"] ?: "Sin contacto",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Text(
                            patient.primaryContact["phone"] ?: "",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(id = R.color.gris_texto)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonEvaluacion() {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(69.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4965))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(25.dp), tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Iniciar Evaluacion", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

@Composable
fun AccionesRapidas() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SmallActionButton("Ver historial", Icons.AutoMirrored.Filled.ShowChart)
        SmallActionButton("Exportar PDF", Icons.Default.Download)
    }
}

@Composable
fun RowScope.SmallActionButton(text: String, icon: ImageVector) {
    Card(
        modifier = Modifier.weight(1f).height(92.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFC4CBD0)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp)
                .padding(12.dp)
        ) {

            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopStart),
                tint = Color.Black
            )

            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun EvaluationCard(
    title: String,
    shortName: String,
    score: String,
    total: String,
    date: String,
    evaluations: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .height(186.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFC4CBD0)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)

            Text(shortName, fontSize = 12.sp, color = Color(0xFF9CA8AF))

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Text(score, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                Spacer(modifier = Modifier.width(4.dp))
                Text("/ $total", fontSize = 14.sp, color = Color(0xFF7B848B))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text("Última evaluación: $date", fontSize = 8.sp, color = Color(0xFF72787E))

            Spacer(modifier = Modifier.weight(1f))

            Text(
                "Ver $evaluations evaluaciones",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1B4965)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PatientDetailPreview() {
    AlzheimerObregonTheme {
        PatientDetailScreen(viewModel = remember { PatientDetailViewModel() })
    }
}