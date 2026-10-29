package com.ap.oddbite.data.remote

import com.ap.oddbite.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSource(private val auth: FirebaseAuth, private val db: FirebaseFirestore) {

    // Creates the account and signs the new user in. Returns the uid.
    suspend fun register(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid
            ?: throw IllegalStateException("Registration succeeded but Firebase returned no user")
    }

    // await() throws on failure (wrong password, no internet); user is only null if Firebase misbehaves
    suspend fun login(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid
            ?: throw IllegalStateException("Login succeeded but Firebase returned no user")
    }

    fun logout() {
        auth.signOut()
    }

    // Deletes whoever is signed in: only call right after register(), when that is the new user
    suspend fun deleteCurrentUser() {
        auth.currentUser?.delete()?.await()
    }

    // Writes users/{uid}
    suspend fun saveUser(uid: String, user: User) {
        val data =
            mapOf("name" to user.name, "email" to user.email, "role" to user.role.name.lowercase())

        db.collection("users")
            .document(uid)
            .set(data).await()
    }
}