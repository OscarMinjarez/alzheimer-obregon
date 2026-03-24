package mx.edu.itson.alzheimerobregon.features.evaluation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.itson.alzheimerobregon.R
import mx.edu.itson.alzheimerobregon.ui.theme.AlzheimerObregonTheme

class InstrumentsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlzheimerObregonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InstrumentScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun InstrumentScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        //  Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = null,
                tint = colorResource(id = R.color.azul_ultramar),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Volver",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.azul_ultramar),
                modifier = Modifier.clickable { }
            )
        }

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Seleccionar instrumento",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.azul_ultramar)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Alexa Lopez",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
        Spacer(modifier = Modifier.height(0.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.Gris_fondo))
                .padding(bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 MMSE
            InstrumentCard(
                title = "MMSE",
                subtitle = "Mini-Mental State Examination",
                description = "Evaluación del estado cognitivo general",
                frequency = "Cada 4 meses",
                date = "14/01/2026",
                daysAgo = "Hace 54 días",
                score = "22/30",
                imageRes = R.drawable.mmse_icon
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Tinetti
            InstrumentCard(
                title = "Tinetti",
                subtitle = "Escala de Tinetti",
                description = "Evaluación del equilibrio y la marcha",
                frequency = "Cada 4 meses",
                date = "14/01/2026",
                daysAgo = "Hace 54 días",
                score = "20/28",
                imageRes = R.drawable.mmse_icon
            )
        }
    }
}

@Composable
fun InstrumentCard(
    title: String,
    subtitle: String,
    description: String,
    frequency: String,
    date: String,
    daysAgo: String,
    score: String,
    imageRes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.Gris_borde)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            //  Header
            // 🔹 Fila solo para icono + título
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.azul_ultramar)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.azul_texto)
                )

                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gris_texto)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            //  Frecuencia
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.tinetti_icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Frecuencia: $frequency",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gris_texto)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            //  Caja
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        colorResource(id = R.color.azul_claro),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = "Última aplicación",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gris_texto)
                )
                Text(
                    text = date,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "$daysAgo • Puntuación: $score",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gris_texto)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            //  Botón
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.azul_ultramar)
                )
            ) {
                Text(
                    text = "Aplicar ahora",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InstrumentScreenPreview() {
    AlzheimerObregonTheme {
        InstrumentScreen()
    }
}