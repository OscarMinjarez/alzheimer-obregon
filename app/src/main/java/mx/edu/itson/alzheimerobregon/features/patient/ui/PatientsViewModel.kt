package mx.edu.itson.alzheimerobregon.features.patient.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.edu.itson.alzheimerobregon.features.patient.Patient
import mx.edu.itson.alzheimerobregon.features.patient.PatientRepository
import mx.edu.itson.alzheimerobregon.features.patient.PatientRepositoryImpl
import mx.edu.itson.alzheimerobregon.data.firebase.FirebaseFirestoreService

class PatientsViewModel(
    private val repository: PatientRepository = PatientRepositoryImpl(FirebaseFirestoreService())
) : ViewModel() {
    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> = _patients

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _success = MutableStateFlow<String?>(null)
    val success: StateFlow<String?> = _success

    fun fetchPatients() {
        viewModelScope.launch {
            val result = repository.getAll()
            result.onSuccess {
                _patients.value = it
            }.onFailure {
                _error.value = it.message
            }
        }
    }

    fun deletePatient(patientId: String) {
        viewModelScope.launch {
            val result = repository.delete(patientId)
            result.onSuccess {
                // Remover el paciente de la lista
                _patients.value = _patients.value.filter { it.id != patientId }
                _success.value = "Paciente eliminado correctamente"
            }.onFailure {
                _error.value = "Error al eliminar paciente: ${it.message}"
            }
        }
    }

    fun clearMessages() {
        _error.value = null
        _success.value = null
    }
}

