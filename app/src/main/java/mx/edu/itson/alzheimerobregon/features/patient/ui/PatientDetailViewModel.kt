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

class PatientDetailViewModel(
    private val repository: PatientRepository = PatientRepositoryImpl(FirebaseFirestoreService())
) : ViewModel() {
    private val _patient = MutableStateFlow<Patient?>(null)
    val patient: StateFlow<Patient?> = _patient
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchPatient(id: String) {
        viewModelScope.launch {
            val result = repository.get(id)
            result.onSuccess {
                _patient.value = it
            }.onFailure {
                _error.value = it.message
            }
        }
    }
}

