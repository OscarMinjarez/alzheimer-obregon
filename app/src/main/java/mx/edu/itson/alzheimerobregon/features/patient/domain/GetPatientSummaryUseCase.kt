package mx.edu.itson.alzheimerobregon.features.patient.domain

import mx.edu.itson.alzheimerobregon.features.evaluation.EvaluationRepository
import mx.edu.itson.alzheimerobregon.features.evaluation.domain.EvaluationStatusUseCase
import mx.edu.itson.alzheimerobregon.features.evaluation.domain.PatientStatus
import mx.edu.itson.alzheimerobregon.features.patient.Patient
import mx.edu.itson.alzheimerobregon.features.patient.PatientRepository

/**
 * Dashboard summary for a patient.
 *
 * Contains the patient model, the follow-up status computed from evaluations,
 * the total number of recorded evaluations, and a boolean flag indicating
 * whether a new evaluation is pending.
 *
 * @property patient The patient model.
 * @property status The computed patient status (e.g. AWARE, PENDING_EVALUATION).
 * @property totalEvaluations Total number of evaluations associated with the patient.
 * @property isPendingEvaluation True if the patient requires evaluation according to business rules.
 */
data class PatientDashboardSummary(
    val patient: Patient,
    val status: PatientStatus,
    val totalEvaluations: Int,
    val isPendingEvaluation: Boolean
)

/**
 * Use case that builds a dashboard summary for a patient.
 *
 * This use case coordinates calls to the [PatientRepository] and [EvaluationRepository]
 * and delegates the status calculation to [EvaluationStatusUseCase].
 *
 * @property patientRepository Repository to fetch patient data.
 * @property evaluationRepository Repository to fetch patient evaluations.
 * @property statusUseCase Use case that computes a patient's status from evaluations.
 */
class GetPatientSummaryUseCase(
    private val patientRepository: PatientRepository,
    private val evaluationRepository: EvaluationRepository,
    private val statusUseCase: EvaluationStatusUseCase
) {

    /**
     * Builds the summary for the patient identified by [patientId].
     *
     * Flow:
     * 1. Fetch patient from the repository.
     * 2. Retrieve evaluations associated with the patient.
     * 3. Compute the status using [EvaluationStatusUseCase].
     * 4. Package the results into [PatientDashboardSummary].
     *
     * Errors: if any repository call fails, the function returns a `Result.failure`
     * with the corresponding exception.
     *
     * @param patientId The patient identifier.
     * @return A [Result] containing [PatientDashboardSummary] on success or an exception on failure.
     */
    suspend operator fun invoke(patientId: String): Result<PatientDashboardSummary> {
        val patientResult = patientRepository.get(patientId)
        if (patientResult.isFailure) {
            return Result.failure(patientResult.exceptionOrNull()!!)
        }
        val patient = patientResult.getOrNull() ?: return Result.failure(Exception("Patient not found"))
        val evalsResult = evaluationRepository.getEvaluationsByPatient(patientId)
        if (evalsResult.isFailure) {
            return Result.failure(evalsResult.exceptionOrNull()!!)
        }
        val evaluations = evalsResult.getOrDefault(emptyList())
        val status = statusUseCase.calculateStatus(evaluations)
        val summary = PatientDashboardSummary(
            patient = patient,
            status = status,
            totalEvaluations = evaluations.size,
            isPendingEvaluation = status == PatientStatus.PENDING_EVALUATION
        )
        return Result.success(summary)
    }
}