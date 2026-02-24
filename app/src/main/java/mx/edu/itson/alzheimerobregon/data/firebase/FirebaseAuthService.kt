package mx.edu.itson.alzheimerobregon.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * Service wrapper around Firebase Authentication.
 *
 * This class exposes coroutine-friendly suspend functions to sign in and register users
 * using FirebaseAuth. Each public method returns a Kotlin [Result] containing a
 * [FirebaseUser] on success or an exception on failure.
 *
 * Usage notes:
 * - Callers must invoke these methods from a coroutine scope (they are suspend functions).
 * - Internally the class uses `kotlinx.coroutines.tasks.await()` to convert Firebase Tasks
 *   into coroutine-friendly suspending calls.
 * - The returned [Result] must be inspected by the caller. Do not assume success.
 *
 * Example:
 * ```
 * val service = FirebaseAuthService()
 * lifecycleScope.launch {
 *     val result = service.login("email@example.com", "password")
 *     if (result.isSuccess) {
 *         val user = result.getOrNull()
 *         // proceed with authenticated user
 *     } else {
 *         val error = result.exceptionOrNull()
 *         // handle authentication error
 *     }
 * }
 * ```
 */
class FirebaseAuthService {

    /**
     * FirebaseAuth singleton instance used for authentication operations.
     * Using the singleton ensures configuration and state are shared across the app.
     */
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Signs in a user with email and password.
     *
     * @param email the user's email address
     * @param password the user's password
     * @return a [Result] containing the authenticated [FirebaseUser] on success,
     *         or a failure with an exception on error.
     *
     * Notes:
     * - This function is a suspending function and must be called from a coroutine.
     * - If Firebase returns a null user after a successful sign-in task, this is
     *   treated as a failure and wrapped in a [Result.failure].
     */
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            // Suspend until the sign-in Task completes.
            val result = this.auth.signInWithEmailAndPassword(email, password).await()
            // The Task may succeed but still return a null user in rare cases; handle defensively.
            val user = result.user ?: return Result.failure(Exception("Firebase user was null after a successful sign-in."))
            Result.success(user)
        } catch (e: Exception) {
            // Convert any exception into a Result.failure so callers can handle it uniformly.
            Result.failure(e)
        }
    }

    /**
     * Registers a new user with email and password.
     *
     * @param email the new user's email address
     * @param password the new user's password
     * @return a [Result] containing the created [FirebaseUser] on success,
     *         or a failure with an exception on error.
     *
     * Notes:
     * - This function is a suspending function and must be called from a coroutine.
     * - Firebase may throw exceptions for invalid email, weak password, network issues, etc.
     */
    suspend fun register(email: String, password: String): Result<FirebaseUser> {
        return try {
            // Suspend until the create-user Task completes.
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            // Defensive null-check: treat a null FirebaseUser as a failure.
            val user = result.user ?: return Result.failure(Exception("Firebase user was null after a successful registration."))
            Result.success(user)
        } catch (e: Exception) {
            // Return the caught exception inside a Result.failure so callers can react accordingly.
            Result.failure(e)
        }
    }
}