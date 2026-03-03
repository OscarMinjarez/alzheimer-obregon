package mx.edu.itson.alzheimerobregon.features.evaluation.domain

/**
 * Detailed scoring breakdown for an MMSE evaluation.
 *
 * Each property represents a subsection score. The totalScore property sums the
 * subsection values and is suitable for assigning to the Evaluation.totalScore
 * when persisting the evaluation.
 *
 * Note: the `toMap()` method returns a map of field names to values used for
 * storage in Firestore. One key uses a Spanish-like name ("atencionCalculo")
 * which is preserved for compatibility with existing stored documents.
 *
 * @property orientation Orientation subsection score.
 * @property data Immediate memory / information recall score.
 * @property attentionCalculation Attention and calculation subsection score.
 * @property lazyMemory Delayed recall / memory subsection score.
 * @property constructionLanguage Language and construction subsection score.
 */
data class MmseDetails(
    val orientation: Int = 0,
    val data: Int = 0,
    val attentionCalculation: Int = 0,
    val lazyMemory: Int = 0,
    val constructionLanguage: Int = 0
) {
    /**
     * Aggregate score for all MMSE subsections.
     */
    val totalScore: Int
        get() = orientation + data + attentionCalculation + lazyMemory + constructionLanguage

    /**
     * Convert the details into a map suitable for Firestore storage.
     * Keys intentionally mirror existing database keys for backward compatibility.
     */
    fun toMap(): Map<String, Int> = mapOf(
        "orientation" to orientation,
        "data" to data,
        "atencionCalculo" to attentionCalculation,
        "lazyMemory" to lazyMemory,
        "constructionLanguage" to constructionLanguage
    )
}

/**
 * Detailed scoring breakdown for a Tinetti evaluation.
 *
 * Tinetti evaluations usually include a balance section and a gait/progression section.
 * The `totalScore` property sums these parts and the `toMap()` method returns a map
 * suitable for persistence.
 *
 * @property balance Score for balance-related items.
 * @property progress Score for gait/progression-related items.
 */
class TinettiDetails(
    val balance: Int = 0,
    val progress: Int = 0
) {
    /**
     * Aggregate score for all Tinetti subsections.
     */
    val totalScore: Int
        get() = balance + progress

    /**
     * Convert the details into a map suitable for Firestore storage.
     */
    fun toMap(): Map<String, Int> = mapOf(
        "balance" to balance,
        "progress" to progress
    )
}