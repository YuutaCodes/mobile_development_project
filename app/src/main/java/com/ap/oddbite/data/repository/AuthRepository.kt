package com.ap.oddbite.data.repository

import com.ap.oddbite.data.model.Role
import com.ap.oddbite.data.model.User
import com.ap.oddbite.data.remote.AuthRemoteDataSource

class AuthRepository(private val authRemoteDataSource: AuthRemoteDataSource) {
    suspend fun register(name: String, email: String, password: String, role: Role) {
        val uid = authRemoteDataSource.register(email, password)
        val user = User(name, email, role)

        // Auth and Firestore have no shared transaction, so the profile is saved separately
        try {
            authRemoteDataSource.saveUser(uid, user)
        } catch (e: Exception) {
            // Remove the new account so it does not exist without a profile (role);
            // runCatching keeps the original error visible if the cleanup fails too
            runCatching { authRemoteDataSource.deleteCurrentUser() }
            throw e
        }
    }

    suspend fun login(email: String, password: String): User {
        val uid = authRemoteDataSource.login(email, password)
        val user = authRemoteDataSource.getUser(uid)

        if (user == null) {
            authRemoteDataSource.logout()
            throw IllegalStateException("User missing")
        }
        return user
    }
}