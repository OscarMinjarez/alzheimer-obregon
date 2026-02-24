/**
 * AuthRepository.kt
 *
 * Repository abstraction and default implementation for user authentication.
 *
 * This file provides:
 *  - `AuthRepository` - a small, test-friendly interface that defines suspend functions
 *    for authentication flows (login and register).
 *  - `AuthRepositoryImpl` - a concrete implementation that delegates work to
 *    `FirebaseAuthService`.
 *
 * Design notes:
 *  - All methods return `Result<T>` to provide a uniform success/failure contract.
 *  - Methods are suspending so they can be safely called from coroutines (ViewModels, etc).
 *  - The repository is intentionally thin: it composes the Firebase service to keep
 *    higher-level code (ViewModels / UI) decoupled from Firebase APIs and simpler to mock.
 *
 * Example usage:
 *  val authService = FirebaseAuthService()
 *  val repo: AuthRepository = AuthRepositoryImpl(authService)
 *  // call inside a coroutine scope:
 *  // val result = repo.login("email@example.com", "password")
 */
package mx.edu.itson.alzheimerobregon.features.auth

import com.google.firebase.auth.FirebaseUser
import mx.edu.itson.alzheimerobregon.data.firebase.FirebaseAuthService

/**
 * Repository abstraction for authentication operations.
 *
 * Implementations should provide the high-level operations the app needs for auth flows.
 * Returning `Result<T>` allows consumers to handle both success and failure without
 * relying on exceptions for control flow.
 */
interface AuthRepository {
    /**
     * Sign in an existing user using email and password.
     *
     * @param email The user's email address.
     * @param password The user's password (plain text as received from UI).
     * @return A [Result] that contains a [FirebaseUser] on success or an exception on failure.
     */
    suspend fun login(email: String, password: String): Result<FirebaseUser>

    /**
     * Register a new user with email and password.
     *
     * @param email The new user's email address.
     * @param password The new user's password.
     * @return A [Result] that contains a [FirebaseUser] on success or an exception on failure.
     */
    suspend fun register(email: String, password: String): Result<FirebaseUser>
}

/**
 * Default implementation of [AuthRepository] that delegates to [FirebaseAuthService].
 *
 * Keeping this implementation small makes it trivial to replace in tests with a fake
 * or mock implementation. The repository preserves the service's `Result<T>` contract.
 *
 * @property authService The service that performs direct Firebase Authentication calls.
 */
class AuthRepositoryImpl(private val authService: FirebaseAuthService) : AuthRepository {

    /**
     * Delegate login to the firebase service. The service returns a `Result<FirebaseUser>`,
     * so this function simply forwards that result.
     */
    override suspend fun login(email: String, password: String): Result<FirebaseUser> {
        // Forward to the auth service. Any exceptions are handled/wrapped by the service implementation.
        return this.authService.login(email, password)
    }

    /**
     * Delegate register to the firebase service.
     */
    override suspend fun register(email: String, password: String): Result<FirebaseUser> {
        return this.authService.register(email, password)
    }
}