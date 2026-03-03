package mx.edu.itson.alzheimerobregon.features.evaluation.domain

import mx.edu.itson.alzheimerobregon.features.evaluation.Evaluation
import mx.edu.itson.alzheimerobregon.features.evaluation.EvaluationType
import java.util.Date

/**
 * Factory-like use case to create typed Evaluation instances from detail objects.
 *
 * The class centralizes the mapping between domain detail objects (MmseDetails, TinettiDetails)
 * and the generic `Evaluation` model used for persistence.
 *
 * Methods provide sensible defaults for optional parameters like notes and date.
 */
class CreateEvaluationUseCase {

    /**
     * Create an MMSE evaluation for a patient.
     *
     * The returned `Evaluation` will have its `totalScore` set to `details.totalScore`
     * and the `details` property filled with the map returned by `MmseDetails.toMap()`.
     *
     * @param patientId The id of the patient the evaluation belongs to.
     * @param details MMSE-specific subsection scores.
     * @param notes Optional free-form notes (default: empty string).
     * @param date The date of application (default: now).
     * @return A new [Evaluation] instance configured as an MMSE evaluation.
     */
    fun createMmse(patientId: String, details: MmseDetails, notes: String = "", date: Date = Date()): Evaluation {
        return Evaluation(
            patientId = patientId,
            type = EvaluationType.MMSE,
            applicationDate = date,
            totalScore = details.totalScore,
            details = details.toMap(),
            notes = notes
        )
    }

    /**
     * Create a Tinetti evaluation for a patient.
     *
     * The returned `Evaluation` will have its `totalScore` set to `details.totalScore`
     * and the `details` property filled with the map returned by `TinettiDetails.toMap()`.
     *
     * @param patientId The id of the patient the evaluation belongs to.
     * @param details Tinetti-specific subsection scores.
     * @param notes Optional free-form notes (default: empty string).
     * @param date The date of application (default: now).
     * @return A new [Evaluation] instance configured as a TINETTI evaluation.
     */
    fun createTinetti(patientId: String, details: TinettiDetails, notes: String = "", date: Date = Date()): Evaluation {
        return Evaluation(
            patientId = patientId,
            type = EvaluationType.TINETTI,
            applicationDate = date,
            totalScore = details.totalScore,
            details = details.toMap(),
            notes = notes
        )
    }
}