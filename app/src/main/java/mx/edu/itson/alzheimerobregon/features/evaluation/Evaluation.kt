package mx.edu.itson.alzheimerobregon.features.evaluation

import java.util.Date

/**
 * Supported clinical evaluation types.
 *
 * - MMSE: Mini-Mental State Examination.
 * - TINETTI: Tinetti Balance and Gait Assessment.
 */
enum class EvaluationType {
    MMSE,
    TINETTI,
}

/**
 * Model representing an evaluation applied to a patient.
 *
 * This data class is intended to be serialized/deserialized by Firestore
 * using `toObject(...)` and `set(...)`.
 *
 * @property id Firestore document id (empty for new documents).
 * @property patientId Identifier of the patient associated with the evaluation.
 * @property type Type of evaluation performed.
 * @property applicationDate Date when the evaluation was performed.
 * @property totalScore Total score resulting from the evaluation.
 * @property details Map with per-item or per-category scores (optional).
 * @property notes Free-form evaluator notes.
 */
data class Evaluation(
    val id: String = "",
    val patientId: String = "",
    val type: EvaluationType = EvaluationType.MMSE,
    val applicationDate: Date = Date(),
    val totalScore: Int = 0,
    val details: Map<String, Int> = emptyMap(),
    val notes: String = "",
    val evaluator: String = ""
)
