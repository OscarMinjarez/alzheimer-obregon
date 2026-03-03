package mx.edu.itson.alzheimerobregon.features.evaluation.domain

import mx.edu.itson.alzheimerobregon.features.evaluation.Evaluation
import mx.edu.itson.alzheimerobregon.features.evaluation.EvaluationType

/**
 * Simple progress report for a specific evaluation type.
 *
 * @property type The type of evaluation (e.g., MMSE, TINETTI).
 * @property latestScore The most recent total score for the given evaluation type.
 * @property previousScore The previous score for the same evaluation type, if available.
 * @property difference The arithmetic difference between latestScore and previousScore (0 when previousScore is null).
 */
data class ProgressReport(
    val type: EvaluationType,
    val latestScore: Int,
    val previousScore: Int?,
    val difference: Int
)

/**
 * Use case that computes a simple progress report for a patient for a given evaluation type.
 *
 * Behavior and assumptions:
 * - The provided list of evaluations is expected to be ordered by application date descending
 *   (most recent first). The implementation uses the first matching element as the latest.
 * - If there are no evaluations of the requested evaluation type, the use case returns null.
 * - If only one evaluation of the requested type exists, the previous score will be null and
 *   the difference will be 0.
 *
 * This use case is intentionally small and designed for presentation/summary purposes.
 */
class GetPatientProgressUseCase {

    /**
     * Computes a [ProgressReport] for the given [type] from the provided [evaluations].
     *
     * @param evaluations List of evaluations (should be ordered by date descending).
     * @param type The evaluation type to filter and compute progress for.
     * @return A [ProgressReport] if at least one evaluation of the given type exists, or null otherwise.
     */
    operator fun invoke(evaluations: List<Evaluation>, type: EvaluationType): ProgressReport? {
        val typeEvaluations = evaluations.filter { it.type == type }
        if (typeEvaluations.isEmpty()) return null
        val latestScore = typeEvaluations[0].totalScore
        val previousScore = if (typeEvaluations.size > 1) typeEvaluations[1].totalScore else null
        val difference = if (previousScore != null) {
            latestScore - previousScore
        } else {
            0
        }
        return ProgressReport(
            type = type,
            latestScore = latestScore,
            previousScore = previousScore,
            difference = difference
        )
    }
}