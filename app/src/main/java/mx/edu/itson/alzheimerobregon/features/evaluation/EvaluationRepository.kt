package mx.edu.itson.alzheimerobregon.features.evaluation

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

/**
 * Repository for evaluation operations backed by Firestore.
 *
 * Provides suspend functions to save and query documents in the "evaluations" collection.
 * Operations return Kotlin [Result] to provide a uniform error/success contract.
 */
class EvaluationRepository(private val db: FirebaseFirestore) {

    private val collection = this.db.collection("evaluations")

    /**
     * Saves an evaluation to Firestore.
     *
     * If the evaluation has no id a new one will be generated. On success returns Result.success(Unit),
     * on failure returns Result.failure with the corresponding exception.
     *
     * @param evaluation The evaluation to persist.
     * @return Result<Unit> indicating success or failure.
     */
    suspend fun saveEvaluation(evaluation: Evaluation): Result<Unit> {
        return try {
            val docRef = if (evaluation.id.isEmpty()) {
                collection.document()
            } else {
                collection.document(evaluation.id)
            }
            val evalToSave = evaluation.copy(id = docRef.id)
            docRef.set(evalToSave).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches evaluations associated with a patient ordered by applicationDate descending.
     *
     * @param patientId The patient id whose evaluations should be retrieved.
     * @return Result containing the list of evaluations or Result.failure in case of an error.
     */
    suspend fun getEvaluationsByPatient(patientId: String): Result<List<Evaluation>> {
        return try {
            val snapshot = collection
                .whereEqualTo("patientId", patientId)
                .orderBy("applicationDate", Query.Direction.DESCENDING)
                .get()
                .await()
            val evaluations = snapshot.toObjects(Evaluation::class.java)
            Result.success(evaluations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}