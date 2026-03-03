package mx.edu.itson.alzheimerobregon.features.evaluation.domain

import android.icu.util.Calendar
import mx.edu.itson.alzheimerobregon.features.evaluation.Evaluation
import java.util.Date

/**
 * Simplified states representing a patient's follow-up status based on evaluations.
 */
enum class PatientStatus {
    /** Patient is up-to-date with evaluations (no immediate evaluation needed). */
    AWARE,
    /** Patient requires a new evaluation because their last one is older than 4 months. */
    PENDING_EVALUATION,
    /** Patient has no evaluation history. */
    NO_HISTORY,
}

/**
 * Use case responsible for calculating a patient's follow-up status from available evaluations.
 *
 * Business rules:
 * - If there are no evaluations, the status is [PatientStatus.NO_HISTORY].
 * - If the last evaluation is more than 4 months old, the status is [PatientStatus.PENDING_EVALUATION].
 * - Otherwise, the status is [PatientStatus.AWARE].
 */
class EvaluationStatusUseCase {

    /**
     * Calculates the patient's status based on their evaluations.
     *
     * @param evaluations List of evaluations; the implementation uses the first entry as the most recent
     *                    (the list is assumed to be ordered by date descending). Returns [PatientStatus.NO_HISTORY]
     *                    when the list is empty.
     * @return The computed [PatientStatus].
     */
    fun calculateStatus(evaluations: List<Evaluation>): PatientStatus {
        if (evaluations.isEmpty()) return PatientStatus.NO_HISTORY
        val lastEvaluationDate = evaluations.first().applicationDate
        val calendar = Calendar.getInstance().apply {
            time = lastEvaluationDate
            add(Calendar.MONTH, 4)
        }
        val nextDueDate = calendar.time
        val currentDate = Date()
        return if (currentDate.after(nextDueDate)) {
            PatientStatus.PENDING_EVALUATION
        } else {
            PatientStatus.AWARE
        }
    }

    /**
     * Returns the next recommended evaluation date based on the last evaluation (last + 4 months).
     * Returns null if there is no history.
     *
     * @param evaluations List of evaluations (the first is considered the most recent).
     * @return The next due date or null if there are no evaluations.
     */
    fun getNextDueDate(evaluations: List<Evaluation>): Date? {
        if (evaluations.isEmpty()) return null
        return Calendar.getInstance().apply {
            time = evaluations.first().applicationDate
            add(Calendar.MONTH, 4)
        }.time
    }
}