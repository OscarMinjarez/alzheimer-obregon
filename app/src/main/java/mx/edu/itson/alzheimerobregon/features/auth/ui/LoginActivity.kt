package mx.edu.itson.alzheimerobregon.features.auth.ui

import android.os.Bundle

import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mx.edu.itson.alzheimerobregon.R
import mx.edu.itson.alzheimerobregon.data.firebase.FirebaseAuthService
import mx.edu.itson.alzheimerobregon.features.auth.AuthRepository
import mx.edu.itson.alzheimerobregon.features.auth.AuthRepositoryImpl
import mx.edu.itson.alzheimerobregon.features.patient.ui.PatientRegisterActivity
import mx.edu.itson.alzheimerobregon.features.patient.ui.PatientsActivity
import mx.edu.itson.alzheimerobregon.ui.theme.AlzheimerObregonTheme

class LoginActivity : ComponentActivity() {

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_UID = "user_uid"
    }

    private lateinit var authRepository: AuthRepository

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val authService = FirebaseAuthService()
        this.authRepository = AuthRepositoryImpl(authService)
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Verificar si ya hay sesión guardada
        val savedUid = sharedPreferences.getString(KEY_UID, null)
        if (savedUid != null) {
            // Usuario ya autenticado, saltar login
            val intent = Intent(this@LoginActivity, PatientsActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlzheimerObregonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        onLogin = { email, password ->
                            login(email, password)
                        }
                    )
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        lifecycleScope.launch {
            authRepository.login(email, password)
                .onSuccess { user ->
                    // Guardar UID en SharedPreferences
                    sharedPreferences.edit().putString(KEY_UID, user.uid).apply()
                    val intent = Intent(this@LoginActivity, PatientsActivity::class.java)
                    startActivity(intent)
                    finish()
                }.onFailure { exception ->
                    println("Login failed: ${exception.message}")
                }
        }
    }
}

@Composable

fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: (String, String) -> Unit = { _, _ -> }
){
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Image(painter = painterResource(R.drawable.logo_centro_alz),
            contentDescription = "Logo Estancia Alzheimer",
            contentScale = ContentScale.Fit,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 25.dp)
        )
        Text("Sistema de Evaluación",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = colorResource(id = R.color.azul_ultramar)
        )
        Text("Control de evaluaciones cuatrimestrales",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 5.dp),
            color = colorResource(id = R.color.azul_ultramar),
            fontSize = 12.sp
        )
        HorizontalDivider(
            thickness = 3.dp,
            color = colorResource(R.color.azul_ultramar),
            modifier = Modifier.fillMaxWidth(0.6f).padding(bottom = 22.dp).align(Alignment.CenterHorizontally)
        )
        Text("LOG IN",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = colorResource(R.color.naranja),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Correo electrónico",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = colorResource(R.color.black),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)
        )
        OutlinedTextField(
            value = emailValue,
            onValueChange = { emailValue = it },
            placeholder = {
                Text(text = "usuario@ejemplo.com", color = colorResource(R.color.Gris))
            },

            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.azul_ultramar),
                unfocusedBorderColor = colorResource(R.color.Gris), 
                focusedTextColor = colorResource(R.color.black),
                unfocusedTextColor = colorResource(R.color.Gris)
            ),

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Text(
            text = "Contraseña",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = colorResource(R.color.black),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)
        )
        OutlinedTextField(
            value = passwordValue,
            onValueChange = { passwordValue = it },
            placeholder = {
                Text(text = "*********", color = colorResource(R.color.Gris))
            },

            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.azul_ultramar),
                unfocusedBorderColor = colorResource(R.color.Gris),
                focusedTextColor = colorResource(R.color.black),
                unfocusedTextColor = colorResource(R.color.Gris)
            ),

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Surface(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp)
        ){
            Row(modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = colorResource(R.color.black),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Datos encriptados y restringidos al personal autorizado.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onLogin(emailValue, passwordValue) },
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                colorResource(R.color.azul_ultramar)
            )
        ){
            Text(
                text="Iniciar sesión",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.blanco_cegador)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = colorResource(R.color.black),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Centro de Terapia y Rehabilitación Dorita de Ojeda I.A.P",
                fontSize = 12.sp,
                color = colorResource(R.color.Gris),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Versión 1.0.0 • Marzo 2026",
                fontSize = 12.sp,
                color = colorResource(R.color.Gris),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, heightDp = 2000)
@Composable
fun LoginScreenPreview() {
    AlzheimerObregonTheme {
        LoginScreen()
    }
}