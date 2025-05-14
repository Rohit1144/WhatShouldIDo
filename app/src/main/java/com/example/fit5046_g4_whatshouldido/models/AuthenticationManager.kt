package com.example.fit5046_g4_whatshouldido.models

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID
import com.example.fit5046_g4_whatshouldido.R
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await

class AuthenticationManager (val context: Context) {
    private val auth = Firebase.auth

    fun createAccountWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
//                    trySend(AuthResponse.Success)
                } else {
                    trySend(AuthResponse.Error(message = task.exception?.message ?: ""))
                }
            }
        awaitClose()
    }

    fun loginWithEmail(email: String, password: String) : Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
//                    trySend(AuthResponse.Success)
                } else {
                    trySend(AuthResponse.Error(message = task.exception?.message ?: ""))
                }
            }
        awaitClose()
    }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") {str, it -> str + "%02x".format(it)}
    }

    suspend fun signInWithGoogle(): AuthResponse{
        val googleOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(false)
            .setNonce(createNonce())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()

        return try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val firebaseCredential = GoogleAuthProvider.getCredential(
                        googleIdTokenCredential.idToken, null
                    )

                    Firebase.auth.signInWithCredential(firebaseCredential).await()

                    val user = Firebase.auth.currentUser ?: return AuthResponse.Error("No user found")

                    val db = Firebase.firestore
                    val userRef = db.collection("Users").document(user.uid)
                    val userDoc = userRef.get().await()

                    val isOnboarded = userDoc.getBoolean("isOnboarded") ?: false

                    if (!userDoc.exists()) {
                        val userData = hashMapOf(
                            "email" to user.email,
                            "id" to user.uid,
                            "name" to user.displayName,
                            "createdAt" to FieldValue.serverTimestamp(),
                            "isOnboarded" to false,
                            "dateOfBirth" to ""
                        )
                        userRef.set(userData).await()
                    }

                    AuthResponse.Success(isOnboarded)
                } catch (e: Exception) {
                    AuthResponse.Error(e.message ?: "Failed to sign in with Google")
                }
            } else {
                AuthResponse.Error("Unexpected credential type")
            }
        } catch (e: Exception) {
            AuthResponse.Error(e.message ?: "Google sign-in failed")
        }
    }

    private suspend fun ensureGoogleUserExists(user: FirebaseUser) {
        val db = Firebase.firestore
        val userDoc = db.collection("Users").document(user.uid).get().await()

        if (!userDoc.exists()) {
            val userData = hashMapOf(
                "email" to user.email,
                "id" to user.uid,
                "createdAt" to FieldValue.serverTimestamp(),
                "password" to "",
                "dateOfBirth" to "",
                "isOnboarded" to false
            )

            db.collection("Users").document(user.uid).set(userData).await()
        }
    }

}

sealed interface AuthResponse {
    data class Success(val isOnboarded: Boolean): AuthResponse
    data class Error(val message: String): AuthResponse
}