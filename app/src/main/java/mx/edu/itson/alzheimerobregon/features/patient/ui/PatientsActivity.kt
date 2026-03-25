package mx.edu.itson.alzheimerobregon.features.patient.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.itson.alzheimerobregon.ui.theme.AlzheimerObregonTheme
import mx.edu.itson.alzheimerobregon.R


class PatientsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlzheimerObregonTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val viewModel: PatientsViewModel = viewModel()
                    PatientsScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun PatientsScreen(
    modifier: Modifier = Modifier,
    viewModel: PatientsViewModel
) {
    val patients by viewModel.patients.collectAsState()
    val error by viewModel.error.collectAsState()

    // Lanzar la carga de pacientes solo una vez
    LaunchedEffect(Unit) {
        viewModel.fetchPatients()
    }

    // Lista de imágenes de perfil disponibles
    val profileImages: List<Int> = listOf(
        R.drawable.paciente1,
        R.drawable.paciente2,
        R.drawable.paciente3,
        R.drawable.paciente4
    )
    // Lista de colores de estado
    val statusColors: List<Color> = listOf(
        colorResource(R.color.verde_spartan),
        colorResource(R.color.rojo_rodolfo),
        colorResource(R.color.naranja)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.Gris_fondo))
    ) {
        HeaderPacientes(
            hasNotifications = true,
            onNotificationClick = {}
        )
        Spacer(modifier = Modifier.height(8.dp))
        SearchBarPacientes()
        Spacer(modifier = Modifier.height(12.dp))
        FiltrosPacientes()
        Spacer(modifier = Modifier.height(16.dp))
        if (error != null) {
            Text(
                text = error ?: "",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            patients.forEachIndexed { index, patient ->
                val imageRes = profileImages[index % profileImages.size]
                val statusColor = statusColors[index % statusColors.size]
                PatientCardItem(
                    name = patient.fullName,
                    info = "${patient.age} años • ${patient.gender} • Sala ${patient.roomNumber}",
                    lastEval = "Admitido: ${patient.admissionDate}",
                    statusColor = statusColor,
                    imageRes = imageRes
                )
            }
        }
    }
}

@Composable
fun HeaderPacientes(
    hasNotifications: Boolean,
    onNotificationClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.logo_centro_alz_peq),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Pacientes",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.azul_ultramar)
        )

        Spacer(modifier = Modifier.weight(1f))

        Box {

            IconButton(onClick = onNotificationClick) {
                Icon(
                    painter = painterResource(id = R.drawable.notificaciones),
                    contentDescription = null,
                    tint = colorResource(R.color.azul_ultramar),
                    modifier = Modifier.size(27.dp)
                )
            }

            if (hasNotifications) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.Red, CircleShape)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
fun SearchBarPacientes() {

    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = {
            Text(
                text = "Buscar paciente...",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.gris_texto)
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.buscador),
                contentDescription = null,
                modifier = Modifier.size(21.dp)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
fun FiltrosPacientes() {

    Row(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {

        ChipFiltro("Todos", true)
        Spacer(modifier = Modifier.width(8.dp))

        ChipFiltro("Atrasados", false)
        Spacer(modifier = Modifier.width(8.dp))

        ChipFiltro("Pendientes", false)
        Spacer(modifier = Modifier.width(8.dp))

        ChipFiltro("Al día", false)
    }
}

@Composable
fun ChipFiltro(text: String, selected: Boolean) {

    Box(
        modifier = Modifier
            .height(28.dp)
            .background(
                if (selected)
                    colorResource(R.color.azul_ultramar)
                else
                    Color(0xFFE5E7EB),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) Color.White else Color.Gray
        )
    }
}

@Composable
fun PatientCardItem(
    name: String,
    info: String,
    lastEval: String,
    statusColor: Color,
    imageRes: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .height(115.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colorResource(R.color.Gris_borde)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(66.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = info,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.gris_texto)
                )

                Text(
                    text = lastEval,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.gris_texto)
                )
            }

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(statusColor, CircleShape)
            )
        }
    }
}
