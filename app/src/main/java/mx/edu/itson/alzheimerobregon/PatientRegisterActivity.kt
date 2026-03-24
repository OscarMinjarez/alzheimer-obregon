package mx.edu.itson.alzheimerobregon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.itson.alzheimerobregon.ui.theme.AlzheimerObregonTheme

class PatientRegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlzheimerObregonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterScreen(

                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    var edad by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var nombreCompleto by remember { mutableStateOf("") }
    var sexoSeleccionado by remember { mutableStateOf("") }
    var nombreContactoP by remember { mutableStateOf("") }
    var parentescoP by remember { mutableStateOf("") }
    var telefonoP by remember { mutableStateOf("") }
    var nombreContactoA by remember { mutableStateOf("") }
    var parentescoA by remember { mutableStateOf("") }
    var telefonoA by remember { mutableStateOf("") }
    var habitacion by remember { mutableStateOf("") }
    var fechaIngreso by remember { mutableStateOf("") }
    var historialMedico by remember { mutableStateOf("") }
    var alergias by remember { mutableStateOf("") }
    var medicamentos by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        EncabezadoFijo(
            onBackClick = {

            }
        )
        HorizontalDivider(thickness = 2.dp, color = colorResource(R.color.gris_texto))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.blanco_cegador)
                ),
                border = BorderStroke(1.dp, colorResource(R.color.gris_texto))
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = colorResource(R.color.azul_ultramar),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Información Personal",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.azul_ultramar)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Nombre completo *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.azul_ultramar)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nombreCompleto,
                        onValueChange = { nombreCompleto = it },
                        placeholder = { Text("Ej: Maria Lopez Garcia") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Edad *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.azul_ultramar)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = edad,
                        onValueChange = { if (it.all { char -> char.isDigit() }) edad = it },
                        placeholder = { Text("Ej: 78") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Sexo *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.azul_ultramar)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (sexoSeleccionado == "Femenino"),
                            onClick = { sexoSeleccionado = "Femenino" }
                        )
                        Text("Femenino")
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = (sexoSeleccionado == "Masculino"),
                            onClick = { sexoSeleccionado = "Masculino" }
                        )
                        Text("Masculino")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Direccion",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.azul_ultramar)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        placeholder = { Text("Ej: Calle Principal #123, Colonia Centro") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.blanco_cegador)),
                border = BorderStroke(
                    1.dp, colorResource(R.color.gris_texto)
                )
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    SeccionTitulo(Icons.Default.Phone, "Contacto Principal")

                    InputCampo(
                        "Nombre completo *",
                        nombreContactoP,
                        { nombreContactoP = it },
                        "Ej: Juana Lopez"
                    )
                    InputCampo("Parentesco *", parentescoP, { parentescoP = it }, "Ej: Hija")
                    InputCampo(
                        "Telefono *",
                        telefonoP,
                        { if (it.length <= 10) telefonoP = it },
                        "Ej: 644 123 4567",
                        KeyboardType.Phone
                    )
                }
            }


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.blanco_cegador)),
                border = BorderStroke(1.dp, colorResource(R.color.gris_texto))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, null, tint = colorResource(R.color.azul_ultramar))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Contacto Alternativo",
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.azul_ultramar)
                        )
                        Text(
                            " (Opcional)",
                            color = colorResource(R.color.gris_texto),
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    InputCampo(
                        "Nombre completo",
                        nombreContactoA,
                        { nombreContactoA = it },
                        "Ej: Juana Lopez"
                    )
                    InputCampo("Parentesco", parentescoA, { parentescoA = it }, "Ej: Hija")
                    InputCampo(
                        "Telefono",
                        telefonoA,
                        { if (it.length <= 10) telefonoA = it },
                        "Ej: 644 123 4567",
                        KeyboardType.Phone
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.blanco_cegador)),
                border = BorderStroke(1.dp, colorResource(R.color.gris_texto))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    SeccionTitulo(Icons.Default.LocationOn, "Informacion del Centro")

                    InputCampo("Habitacion/Sala", habitacion, { habitacion = it }, "Ej: Sala 201")

                    Text(
                        "Fecha de Ingreso",
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.azul_ultramar)
                    )
                    OutlinedTextField(
                        value = fechaIngreso,
                        onValueChange = { fechaIngreso = it },
                        placeholder = { Text("mm/dd/yyyy") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = { Icon(Icons.Default.DateRange, null) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.blanco_cegador)),
                border = BorderStroke(1.dp, colorResource(R.color.gris_texto))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, null, tint = colorResource(R.color.azul_ultramar))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Informacion Médica", fontWeight = FontWeight.Bold, color = colorResource(R.color.azul_ultramar), fontSize = 18.sp)
                        Text(" (Opcional)", color = colorResource(R.color.gris_texto), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    InputCampo(
                        label = "Historial Médico",
                        value = historialMedico,
                        onValueChange = { historialMedico = it },
                        placeholder = "Ej: Hipertension, diabetes tipo 2, diagnostico de demencia en 2020..."
                    )

                    InputCampo(
                        label = "Alergias",
                        value = alergias,
                        onValueChange = { alergias = it },
                        placeholder = "Ej: Penicilina, polen, lactosa"
                    )

                    InputCampo(
                        label = "Medicamentos Actuales",
                        value = medicamentos,
                        onValueChange = { medicamentos = it },
                        placeholder = "Ej: Donepezilo 10mg (1 vez al dia), Enalapril 5 mg (2 veces al dia)..."
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(onClick = {  }) {
                    Text("Cancelar", color = colorResource(R.color.azul_ultramar), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.azul_ultramar))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Registrar paciente", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

}

@Composable
fun InputCampo(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Text(
        text = label,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.azul_ultramar)
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
fun SeccionTitulo(icon: ImageVector, titulo: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            null,
            tint = colorResource(R.color.azul_ultramar),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = titulo,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.azul_ultramar)
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun EncabezadoFijo(onBackClick: () -> Unit) {
    Spacer(modifier = Modifier.padding(top = 20.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver atrás",
                tint = colorResource(R.color.azul_ultramar)
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "Registrar nuevo paciente",
                color = colorResource(R.color.azul_ultramar),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Complete los datos del paciente",
                color = colorResource(R.color.gris_texto),
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    AlzheimerObregonTheme {
        RegisterScreen()
    }
}