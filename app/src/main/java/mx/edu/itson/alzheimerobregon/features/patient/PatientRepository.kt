package mx.edu.itson.alzheimerobregon.features.patient

import mx.edu.itson.alzheimerobregon.data.firebase.FirebaseFirestoreService

/**
 * Repository abstraction for patient-related data operations.
 *
 * This interface defines the operations that the rest of the application (UI / ViewModels)
 * can use without depending directly on Firestore or other data sources.
 *
 * All methods are suspendable and return Kotlin's [Result] wrapper to express success or failure.
 */
interface PatientRepository {
    /**
     * Save a patient record.
     *
     * If the provided [patient] has an empty `id`, the implementation is expected to generate
     * a new document id before saving. On success, the saved [Patient] (with assigned id)
     * is returned wrapped in `Result.success`. On failure the `Result` contains the error.
     *
     * @param patient The patient to save.
     * @return [Result] containing the saved [Patient] on success, or an exception on failure.
     */
    suspend fun save(patient: Patient): Result<Patient>

    /**
     * Retrieve a patient by its id.
     *
     * Returns `Result.success(patient)` when the document exists and was deserialized successfully,
     * `Result.success(null)` when the document does not exist, or `Result.failure(exception)` when
     * an error occurs.
     *
     * @param id Document id of the patient.
     */
    suspend fun get(id: String): Result<Patient?>

    /**
     * Retrieve all patients.
     *
     * Returns a list of patients inside [Result.success] on success, or [Result.failure] on error.
     */
    suspend fun getAll(): Result<List<Patient>>
}

/**
 * Firestore-backed implementation of [PatientRepository].
 *
 * This class delegates calls to [FirebaseFirestoreService]. It encapsulates the Firestore
 * collection name ("patients") so callers don't need to know Firestore details.
 *
 * @property firestoreService Service that wraps Firestore SDK calls.
 */
class PatientRepositoryImpl(private val firestoreService: FirebaseFirestoreService) : PatientRepository {

    private val collectionName = "patients"

    /**
     * Save a patient to Firestore.
     *
     * Steps:
     * 1. If the incoming patient has an empty `id`, generate a new document id.
     * 2. Create a copy of the patient with the resolved id.
     * 3. Delegate the actual save to [FirebaseFirestoreService.save].
     * 4. Return the saved patient inside [Result.success] when the save succeeded or an error inside
     *    [Result.failure] when it failed.
     */
    override suspend fun save(patient: Patient): Result<Patient> {
        // Determine the document id: keep existing id or generate a new one for new patients.
        val documentId = patient.id.ifEmpty {
            this.firestoreService.generateDocumentId(collectionName)
        }

        // Create an immutable copy with the document id set so the saved object contains its id.
        val patientToSave = patient.copy(id = documentId)

        // Perform the save operation via the Firestore service.
        val result = this.firestoreService.save(collectionName, documentId, patientToSave)

        // Map the boolean result into a Result<Patient> so callers receive the saved model or an error.
        return if (result.isSuccess) {
            Result.success(patientToSave)
        } else {
            // Preserve the original exception when possible, otherwise provide a generic one.
            Result.failure(result.exceptionOrNull() ?: Exception("Error saving patient"))
        }
    }

    /**
     * Get a single patient by id.
     *
     * Delegates to [FirebaseFirestoreService.get] and returns its result directly. The underlying
     * service already wraps success/failure in [Result] and performs deserialization to [Patient].
     */
    override suspend fun get(id: String): Result<Patient?> {
        return this.firestoreService.get(collectionName, id)
    }

    /**
     * Get all patients stored in the collection.
     *
     * Delegates to [FirebaseFirestoreService.getAll] which returns a list of deserialized models
     * or an error.
     */
    override suspend fun getAll(): Result<List<Patient>> {
        return this.firestoreService.getAll(collectionName)
    }
}