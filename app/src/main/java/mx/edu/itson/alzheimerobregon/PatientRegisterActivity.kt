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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import mx.edu.itson.alzheimerobregon.data.firebase.FirebaseFirestoreService
import mx.edu.itson.alzheimerobregon.features.patient.Patient
import mx.edu.itson.alzheimerobregon.features.patient.PatientRepository
import mx.edu.itson.alzheimerobregon.features.patient.PatientRepositoryImpl
import mx.edu.itson.alzheimerobregon.ui.theme.AlzheimerObregonTheme

data class PatientFormState(
    var fullName: String = "",
    var age: String = "",
    var gender: String = "",
    var address: String = "",
    var primaryContactName: String = "",
    var primaryContactRelationship: String = "",
    var primaryContactPhone: String = "",
    var secondaryContactName: String = "",
    var secondaryContactRelationship: String = "",
    var secondaryContactPhone: String = "",
    var roomNumber: String = "",
    var admissionDate: String = "",
    var medicalHistory: String = "",
    var allergies: String = "",
    var medications: String = ""
)

class PatientRegisterActivity : ComponentActivity() {

    private fun toPatient(form: PatientFormState): Patient {
        return Patient(
            fullName = form.fullName,
            age = form.age.toIntOrNull() ?: 0,
            gender = form.gender,
            address = form.address,
            primaryContact = mapOf(
                "name" to form.primaryContactName,
                "relationship" to form.primaryContactRelationship,
                "phone" to form.primaryContactPhone
            ),
            secondaryContact = mapOf(
                "name" to form.secondaryContactName,
                "relationship" to form.secondaryContactRelationship,
                "phone" to form.secondaryContactPhone
            ),
            roomNumber = form.roomNumber,
            admissionDate = form.admissionDate,
            medicalHistory = form.medicalHistory,
            allergies = form.allergies,
            medications = form.medications
        )
    }

    private lateinit var patientRepository: PatientRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        val firestoreService = FirebaseFirestoreService()
        patientRepository = PatientRepositoryImpl(firestoreService)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val formState = remember { mutableStateOf(PatientFormState()) }
            val coroutineScope = rememberCoroutineScope()
            AlzheimerObregonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterScreen(
                        formState = formState.value,
                        onFormChange = { formState.value = it },
                        onRegister = {
                            println("[DEBUG] Register button pressed")
                            coroutineScope.launch {
                                val patient = toPatient(formState.value)
                                println("[DEBUG] Calling patientRepository.save() with: $patient")
                                val result = patientRepository.save(patient)
                                val savedPatient = result.getOrNull()
                                if (savedPatient != null) {
                                    println("[DEBUG] Patient saved successfully: $savedPatient")
                                    formState.value = formState.value.copy(
                                        fullName = savedPatient.fullName,
                                        age = savedPatient.age.toString(),
                                        gender = savedPatient.gender,
                                        address = savedPatient.address,
                                        primaryContactName = savedPatient.primaryContact["name"] ?: "",
                                        primaryContactRelationship = savedPatient.primaryContact["relationship"] ?: "",
                                        primaryContactPhone = savedPatient.primaryContact["phone"] ?: "",
                                        secondaryContactName = savedPatient.secondaryContact["name"] ?: "",
                                        secondaryContactRelationship = savedPatient.secondaryContact["relationship"] ?: "",
                                        secondaryContactPhone = savedPatient.secondaryContact["phone"] ?: "",
                                        roomNumber = savedPatient.roomNumber,
                                        admissionDate = savedPatient.admissionDate,
                                        medicalHistory = savedPatient.medicalHistory,
                                        allergies = savedPatient.allergies,
                                        medications = savedPatient.medications
                                    )
                                } else {
                                    println("[DEBUG] Error saving patient: ${result.exceptionOrNull()}")
                                }
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }


}

@Composable
fun RegisterScreen(
    formState: PatientFormState,
    onFormChange: (PatientFormState) -> Unit,
    onRegister: () -> Unit,
    modifier: Modifier = Modifier
) {

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
                                text = "Full name *",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.azul_ultramar)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = formState.fullName,
                                onValueChange = { onFormChange(formState.copy(fullName = it)) },
                                placeholder = { Text("e.g. Maria Lopez Garcia") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                    Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Age *",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.azul_ultramar)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = formState.age,
                                onValueChange = { if (it.all { char -> char.isDigit() }) onFormChange(formState.copy(age = it)) },
                                placeholder = { Text("e.g. 78") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                    Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Gender *",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.azul_ultramar)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = (formState.gender == "Female"),
                                    onClick = { onFormChange(formState.copy(gender = "Female")) }
                                )
                                Text("Female")
                                Spacer(modifier = Modifier.width(16.dp))
                                RadioButton(
                                    selected = (formState.gender == "Male"),
                                    onClick = { onFormChange(formState.copy(gender = "Male")) }
                                )
                                Text("Male")
                            }

                    Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                "Address",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.azul_ultramar)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = formState.address,
                                onValueChange = { onFormChange(formState.copy(address = it)) },
                                placeholder = { Text("e.g. Main St #123, Downtown") },
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
                    SeccionTitulo(Icons.Default.Phone, "Primary Contact")

                    InputCampo(
                        "Full name *",
                        formState.primaryContactName,
                        { onFormChange(formState.copy(primaryContactName = it)) },
                        "e.g. Juana Lopez"
                    )
                    InputCampo("Relationship *", formState.primaryContactRelationship, { onFormChange(formState.copy(primaryContactRelationship = it)) }, "e.g. Daughter")
                    InputCampo(
                        "Phone *",
                        formState.primaryContactPhone,
                        { if (it.length <= 10) onFormChange(formState.copy(primaryContactPhone = it)) },
                        "e.g. 644 123 4567",
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
                            "Secondary Contact",
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.azul_ultramar)
                        )
                        Text(
                            " (Optional)",
                            color = colorResource(R.color.gris_texto),
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    InputCampo(
                        "Full name",
                        formState.secondaryContactName,
                        { onFormChange(formState.copy(secondaryContactName = it)) },
                        "e.g. Juana Lopez"
                    )
                    InputCampo("Relationship", formState.secondaryContactRelationship, { onFormChange(formState.copy(secondaryContactRelationship = it)) }, "e.g. Daughter")
                    InputCampo(
                        "Phone",
                        formState.secondaryContactPhone,
                        { if (it.length <= 10) onFormChange(formState.copy(secondaryContactPhone = it)) },
                        "e.g. 644 123 4567",
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
                    SeccionTitulo(Icons.Default.LocationOn, "Facility Information")

                    InputCampo("Room/Area", formState.roomNumber, { onFormChange(formState.copy(roomNumber = it)) }, "e.g. Room 201")

                    Text(
                        "Admission Date",
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.azul_ultramar)
                    )
                    OutlinedTextField(
                        value = formState.admissionDate,
                        onValueChange = { onFormChange(formState.copy(admissionDate = it)) },
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
                        Text("Medical Information", fontWeight = FontWeight.Bold, color = colorResource(R.color.azul_ultramar), fontSize = 18.sp)
                        Text(" (Optional)", color = colorResource(R.color.gris_texto), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    InputCampo(
                        label = "Medical History",
                        value = formState.medicalHistory,
                        onValueChange = { onFormChange(formState.copy(medicalHistory = it)) },
                        placeholder = "e.g. Hypertension, type 2 diabetes, dementia diagnosis in 2020..."
                    )

                    InputCampo(
                        label = "Allergies",
                        value = formState.allergies,
                        onValueChange = { onFormChange(formState.copy(allergies = it)) },
                        placeholder = "e.g. Penicillin, pollen, lactose"
                    )

                    InputCampo(
                        label = "Current Medications",
                        value = formState.medications,
                        onValueChange = { onFormChange(formState.copy(medications = it)) },
                        placeholder = "e.g. Donepezil 10mg (once daily), Enalapril 5mg (twice daily)..."
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
                    Text("Cancel", color = colorResource(R.color.azul_ultramar), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onRegister,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.azul_ultramar))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Register patient", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                contentDescription = "Go back",
                tint = colorResource(R.color.azul_ultramar)
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "Register new patient",
                color = colorResource(R.color.azul_ultramar),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Complete the patient data",
                color = colorResource(R.color.gris_texto),
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    val formState = remember { mutableStateOf(PatientFormState()) }
    AlzheimerObregonTheme {
        RegisterScreen(
            formState = formState.value,
            onFormChange = { formState.value = it },
            onRegister = {},
        )
    }
}