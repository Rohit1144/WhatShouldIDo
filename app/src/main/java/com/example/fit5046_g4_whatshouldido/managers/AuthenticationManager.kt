package com.example.fit5046_g4_whatshouldido.managers

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.fit5046_g4_whatshouldido.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

class AuthenticationManager (val context: Context) {
    private val auth = Firebase.auth

    suspend fun createAccountWithEmail(email: String, password: String, name: String, birthDate: String): AuthResponse {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = auth.currentUser ?: return AuthResponse.Error("User creation failed")

            // Optional: Create Firestore entry for this user
            val userData = hashMapOf(
                "id" to user.uid,
                "email" to user.email,
                "name" to name,
                "dateOfBirth" to birthDate,
                "createdAt" to FieldValue.serverTimestamp(),
                "isOnboarded" to false
            )

            Firebase.firestore.collection("Users")
                .document(user.uid)
                .set(userData)
                .await()

            AuthResponse.Success(isOnboarded = false) // Always false after signup
        } catch (e: Exception) {
            AuthResponse.Error(e.message ?: "Account creation failed")
        }
    }

    suspend fun loginWithEmail(email: String, password: String) : AuthResponse{
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val user = auth.currentUser ?: return AuthResponse.Error("User not found")

            val doc = Firebase.firestore.collection("Users")
                .document(user.uid)
                .get()
                .await()

            val isOnboarded = doc.getBoolean("isOnboarded") == true
            AuthResponse.Success(isOnboarded)
        } catch (e: Exception) {
            AuthResponse.Error(e.message ?: "Login failed")
        }
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

                    val isOnboarded = userDoc.getBoolean("isOnboarded") == true

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



    suspend fun markOnboardingComplete(profession: String, focus: String, preference: String) {
        val user = Firebase.auth.currentUser
        val db = Firebase.firestore

        val onboardingValues = mapOf(
            "profession" to profession,
            "focusArea" to focus,
            "preference" to preference
        )

        db.collection("Users")
            .document(user!!.uid)
            .update(
                mapOf(
                    "onboardingValues" to onboardingValues,
                    "isOnboarded" to true
                )
            )
            .await()
    }

    suspend fun getName(): String {
        val user = Firebase.auth.currentUser
        val db = Firebase.firestore

        val userRef = db.collection("Users").document(user!!.uid)
        val userDoc = userRef.get().await()

        val name = userDoc.getString("name") ?: "User" // fallback if name is null

        return name
    }


    suspend fun isOnboardingComplete(): Boolean{
        val user = Firebase.auth.currentUser ?: return false
        val db = Firebase.firestore

        val doc = db.collection("Users").document(user.uid).get().await()
        return doc.getBoolean("isOnboarded") == true
    }

    suspend fun deleteAccount(
        deleteTasks: suspend() -> Unit,
        deleteQuotes: suspend() -> Unit,
        email: String? = null,
        password: String? = null
    ): AuthResponse {
        val user = Firebase.auth.currentUser ?: return AuthResponse.Error("No user found")
        val db = Firebase.firestore

        return try {

            // reauthenticate( required before safe delete - due to long login)
            val isGoogleUser = user.providerData.any { it.providerId == "google.com" }
            val isEmailUser = user.providerData.any { it.providerId == "password" }

            when {
                isGoogleUser -> {
                    val googleCredential = getGoogleCredentialForReauthentication()
                        ?: return AuthResponse.Error("Failed to get Google credential")

                    user.reauthenticate(googleCredential).await()
                }

                isEmailUser && !email.isNullOrBlank() && !password.isNullOrBlank() -> {
                    val emailCredential = EmailAuthProvider.getCredential(email,password)
                    try{
                        user.reauthenticate(emailCredential).await()
                    } catch( e: Exception) {
                        return AuthResponse.Error("Incorrect Password")
                    }
                }

                else -> return AuthResponse.Error("Unable to reauthenticate. Credentials required")
            }

            // Delete Firestore data (user's tasks)
            deleteTasks()

            // Delete Firestore user document
            db.collection("Users").document(user.uid).delete().await()

            // Delete Local RoomDB saved quotes
            deleteQuotes()

            delay(1000)

            // Delete Firebase Auth User
            user.delete().await()

            // Reset the onboard state
            AuthResponse.Success(isOnboarded = false)
        } catch(e : Exception) {
            AuthResponse.Error(e.message ?: "Failed to delete account")
        }
    }

    private suspend fun getGoogleCredentialForReauthentication(): AuthCredential? {
        return try {
            val googleOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(context.getString(R.string.web_client_id))
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleOption)
                .build()

            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(context, request)

            val credential = result.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

}

sealed interface AuthResponse {
    data class Success(val isOnboarded: Boolean): AuthResponse
    data class Error(val message: String): AuthResponse
}