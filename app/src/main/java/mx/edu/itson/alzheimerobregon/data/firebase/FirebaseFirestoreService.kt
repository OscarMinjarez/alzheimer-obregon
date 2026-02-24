package mx.edu.itson.alzheimerobregon.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Lightweight wrapper around Firebase Firestore operations used in the app.
 *
 * Responsibilities:
 * - Provide a convenient, coroutine-friendly API for common Firestore operations.
 * - Return results as Kotlin [Result] so callers can handle success/failure uniformly.
 *
 * This class intentionally keeps the surface small (generate id, save, get, getAll).
 * It is intended to be used by repository classes which implement higher-level business logic.
 */
class FirebaseFirestoreService {

    /**
     * The FirebaseFirestore instance used to run queries and write operations.
     *
     * Exposed as a `val` to allow advanced callers or tests to inspect or replace it
     * when required (for example, when using a local emulator or a mocked instance).
     */
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Generate a new document id for a given collection without writing data.
     *
     * This is a common pattern when you want to create an object with a client-side id
     * (for example to construct URLs, references, or relationships) before saving it.
     *
     * @param collection the Firestore collection name where the document will be stored
     * @return a newly generated document id String
     */
    fun generateDocumentId(collection: String): String {
        // Use Firestore to create a new document reference and get its id.
        return this.firestore.collection(collection).document().id
    }

    /**
     * Save a Kotlin object to a Firestore document.
     *
     * Uses `set()` which overwrites the document contents with `data`. This method is
     * generic and uses a reified type parameter so callers can save any data class.
     *
     * @param T the Kotlin type being saved (usually a data class)
     * @param collection the Firestore collection name
     * @param documentId the id of the document to create or overwrite
     * @param data the object to persist
     * @return Result.success(true) on success or Result.failure(exception) on error
     */
    suspend inline fun <reified T : Any> save(collection: String, documentId: String, data: T): Result<Boolean> {
        return try {
            // Perform the network call and await completion using coroutines.
            this.firestore.collection(collection).document(documentId).set(data).await()
            Result.success(true)
        } catch (e: Exception) {
            // Return the exception wrapped in a Result so callers can handle it.
            Result.failure(e)
        }
    }

    /**
     * Retrieve a single document and convert it to the requested Kotlin type.
     *
     * If the document does not exist, the returned value inside Result will be `null`.
     *
     * @param T the Kotlin type to which the document will be converted (must have a no-arg constructor or be a POJO/data class)
     * @param collection the Firestore collection name
     * @param documentId the id of the document to retrieve
     * @return Result.success(T?) with the mapped object or Result.failure(exception) on error
     */
    suspend inline fun <reified T : Any> get(collection: String, documentId: String): Result<T?> {
        return try {
            // Fetch the document snapshot and convert it to the requested type.
            val snapshot = this.firestore.collection(collection).document(documentId).get().await()
            val data = snapshot.toObject(T::class.java)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieve all documents in a collection and map them to the requested Kotlin type.
     *
     * Documents that cannot be converted to [T] are dropped (mapNotNull).
     *
     * @param T the Kotlin type used for mapping each document
     * @param collection the Firestore collection name
     * @return Result.success(List<T>) with all mapped documents or Result.failure(exception) on error
     */
    suspend inline fun <reified T : Any> getAll(collection: String): Result<List<T>> {
        return try {
            val snapshot = this.firestore.collection(collection).get().await()
            // Convert each document to the target type, ignoring documents that fail conversion.
            val data = snapshot.documents.mapNotNull {
                it.toObject(T::class.java)
            }
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}