package com.semeprojects.secom.data.network.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.semeprojects.secom.data.network.model.User
import com.semeprojects.secom.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val auth: FirebaseAuth,
) : UserRepository {

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> =
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                Result.success(firebaseUser.toUser())
            } else {
                Result.failure(Exception("Sign-in failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Sign-in failed: ${e.message}"))
        }

    override suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<User> =
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                Result.success(firebaseUser.toUser())
            } else {
                Result.failure(Exception("Sign-up failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Sign-up failed: ${e.message}"))
        }

    override suspend fun signOut(): Result<Unit> =
        try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Sign-out failed: ${e.message}"))
        }

    override fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser
        return firebaseUser?.toUser()
    }

    private fun FirebaseUser.toUser(): User {
        return User(
            uid = this.uid,
            email = this.email?:"",
            displayName = this.displayName
        )
    }
}