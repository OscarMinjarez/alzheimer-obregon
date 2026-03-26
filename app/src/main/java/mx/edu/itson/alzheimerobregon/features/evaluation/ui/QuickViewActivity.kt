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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.itson.alzheimerobregon.R
import mx.edu.itson.alzheimerobregon.ui.theme.AlzheimerObregonTheme

class QuickViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlzheimerObregonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuickViewScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun QuickViewScreen(modifier: Modifier = Modifier) {
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
                modifier = Modifier.clickable {
                    if (context is android.app.Activity) {
                        context.finish()
                    }
                }
            )
        }

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Resultados rápidos",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.azul_ultramar)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Alexa Lopez",
                fontSize = 18.sp,
                color = colorResource(id = R.color.gris_texto)
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

            // MMSE
            QuickInstrumentCard(
                title = "MMSE",
                subtitle = "Mini-Mental State Examination",
                result = "22/30",
                date = "14/01/2026",
                details = listOf(
                    "Orientación temporal: 4/5",
                    "Orientación espacial: 4/5",
                    "Memoria inmediata: 3/3",
                    "Atención y cálculo: 3/5",
                    "Recuerdo: 2/3",
                    "Lenguaje: 6/9"
                ),
                imageRes = R.drawable.mmse_icon
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tinetti
            QuickInstrumentCard(
                title = "Tinetti",
                subtitle = "Escala de Tinetti",
                result = "20/28",
                date = "14/01/2026",
                details = listOf(
                    "Equilibrio: 12/16",
                    "Marcha: 8/12"
                ),
                imageRes = R.drawable.mmse_icon
            )
        }
    }
}

@Composable
fun QuickInstrumentCard(
    title: String,
    subtitle: String,
    result: String,
    date: String,
    details: List<String>,
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
            Row(verticalAlignment = Alignment.CenterVertically) {
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
            Text(
                text = subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = R.color.azul_texto)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Resultado: $result",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.azul_ultramar)
            )
            Text(
                text = "Fecha: $date",
                fontSize = 12.sp,
                color = colorResource(id = R.color.gris_texto)
            )
            Spacer(modifier = Modifier.height(8.dp))
            details.forEach {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gris_texto)
                )
            }
        }
    }
}
