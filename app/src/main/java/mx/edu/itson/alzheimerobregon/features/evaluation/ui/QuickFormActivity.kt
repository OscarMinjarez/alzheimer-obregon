package mx.edu.itson.alzheimerobregon.features.evaluation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.itson.alzheimerobregon.ui.theme.AlzheimerObregonTheme

class QuickFormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val instrument = intent.getStringExtra("instrument") ?: "MMSE"
        val patientId = intent.getStringExtra("patient_id")
        setContent {
            AlzheimerObregonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuickFormScreen(
                        modifier = Modifier.padding(innerPadding),
                        instrument = instrument,
                        patientId = patientId
                    )
                }
            }
        }
    }
}

@Composable
fun QuickFormScreen(modifier: Modifier = Modifier, instrument: String, patientId: String?) {
    val scrollState = rememberScrollState()
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Volver",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    if (context is android.app.Activity) {
                        context.finish()
                    }
                }
            )
        }

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Formulario rápido: $instrument",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Paciente: ${patientId ?: "-"}",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Preguntas hardcodeadas
        if (instrument == "MMSE") {
            Text("¿En qué año estamos?", style = MaterialTheme.typography.bodyLarge)
            Row {
                RadioButton(selected = true, onClick = {})
                Text("2026")
            }
            Row {
                RadioButton(selected = false, onClick = {})
                Text("2025")
            }
            Spacer(Modifier.height(16.dp))
            Text("Puntaje MMSE: 28", fontWeight = FontWeight.Bold)
        } else if (instrument == "Tinetti") {
            Text("¿Puede levantarse de la silla sin usar los brazos?", style = MaterialTheme.typography.bodyLarge)
            Row {
                RadioButton(selected = true, onClick = {})
                Text("Sí")
            }
            Row {
                RadioButton(selected = false, onClick = {})
                Text("No")
            }
            Spacer(Modifier.height(16.dp))
            Text("Puntaje Tinetti: 25", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { if (context is android.app.Activity) context.finish() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}
