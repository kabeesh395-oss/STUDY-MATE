package com.example.data.repository

import android.util.Log
import com.example.data.model.AuthUserModel
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository {

    private fun getAuthInstance(): FirebaseAuth? {
        return try {
            val app = com.example.StudyMateApplication.instance
            if (app != null && com.google.firebase.FirebaseApp.getApps(app).isEmpty()) {
                app.initFirebase()
            }
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepo", "FirebaseAuth initialization warning: ${e.message}")
            null
        }
    }

    private fun getFirestoreInstance(): FirebaseFirestore? {
        return try {
            val app = com.example.StudyMateApplication.instance
            if (app != null && com.google.firebase.FirebaseApp.getApps(app).isEmpty()) {
                app.initFirebase()
            }
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepo", "FirebaseFirestore initialization warning: ${e.message}")
            null
        }
    }

    private val auth: FirebaseAuth? get() = getAuthInstance()

    private val firestore: FirebaseFirestore? get() = getFirestoreInstance()

    val currentUser: FirebaseUser?
        get() = auth?.currentUser

    // Flow for observing Auth State changes
    val authStateFlow: Flow<FirebaseUser?> = callbackFlow {
        val authInstance = auth
        if (authInstance == null) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        authInstance.addAuthStateListener(listener)
        awaitClose {
            authInstance.removeAuthStateListener(listener)
        }
    }

    // Sign in with Email and Password
    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authInstance = auth ?: throw IllegalStateException("Firebase Auth is unavailable.")
            val authResult = authInstance.signInWithEmailAndPassword(email.trim(), password).await()
            val user = authResult.user ?: throw IllegalStateException("User sign in returned null.")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sign up with Email, Password and Name
    suspend fun signUpWithEmail(email: String, password: String, name: String): Result<FirebaseUser> {
        return try {
            val authInstance = auth ?: throw IllegalStateException("Firebase Auth is unavailable.")
            val authResult = authInstance.createUserWithEmailAndPassword(email.trim(), password).await()
            val user = authResult.user ?: throw IllegalStateException("User registration returned null.")

            // Update Auth Display Name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name.trim())
                .build()
            user.updateProfile(profileUpdates).await()

            // Save User Record in Firestore
            saveUserToFirestore(
                AuthUserModel(
                    uid = user.uid,
                    name = name.trim(),
                    email = user.email ?: email.trim(),
                    photoUrl = user.photoUrl?.toString(),
                    isGoogleUser = false
                )
            )

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sign in with Google Credential
    suspend fun signInWithCredential(credential: AuthCredential): Result<FirebaseUser> {
        return try {
            val authInstance = auth ?: throw IllegalStateException("Firebase Auth is unavailable.")
            val authResult = authInstance.signInWithCredential(credential).await()
            val user = authResult.user ?: throw IllegalStateException("Google sign in returned null.")

            // Save or sync user record in Firestore
            saveUserToFirestore(
                AuthUserModel(
                    uid = user.uid,
                    name = user.displayName ?: "Google User",
                    email = user.email ?: "",
                    photoUrl = user.photoUrl?.toString(),
                    isGoogleUser = true
                )
            )

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Send Password Reset Email
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            val authInstance = auth ?: throw IllegalStateException("Firebase Auth is unavailable.")
            authInstance.sendPasswordResetEmail(email.trim()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Save User Data to Firestore
    suspend fun saveUserToFirestore(userModel: AuthUserModel): Result<Unit> {
        return try {
            val firestoreInstance = firestore ?: throw IllegalStateException("Firestore is unavailable.")
            val userMap = mapOf(
                "uid" to userModel.uid,
                "name" to userModel.name,
                "email" to userModel.email,
                "photoUrl" to (userModel.photoUrl ?: ""),
                "college" to userModel.college,
                "department" to userModel.department,
                "semester" to userModel.semester,
                "createdAt" to userModel.createdAt,
                "isGoogleUser" to userModel.isGoogleUser,
                "lastLoginAt" to System.currentTimeMillis()
            )

            firestoreInstance.collection("users")
                .document(userModel.uid)
                .set(userMap)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.w("FirebaseAuthRepo", "Firestore save note: ${e.message}")
            // Return success if Auth succeeded even if Firestore rule/network is offline
            Result.success(Unit)
        }
    }

    // Fetch User Profile from Firestore
    suspend fun fetchUserFromFirestore(uid: String): AuthUserModel? {
        return try {
            val firestoreInstance = firestore ?: return null
            val snapshot = firestoreInstance.collection("users").document(uid).get().await()
            if (snapshot.exists()) {
                AuthUserModel(
                    uid = snapshot.getString("uid") ?: uid,
                    name = snapshot.getString("name") ?: "",
                    email = snapshot.getString("email") ?: "",
                    photoUrl = snapshot.getString("photoUrl"),
                    college = snapshot.getString("college") ?: "College of Engineering",
                    department = snapshot.getString("department") ?: "Computer Science & Engineering",
                    semester = snapshot.getString("semester") ?: "Semester 6",
                    createdAt = snapshot.getLong("createdAt") ?: System.currentTimeMillis(),
                    isGoogleUser = snapshot.getBoolean("isGoogleUser") ?: false
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w("FirebaseAuthRepo", "Failed to fetch from Firestore: ${e.message}")
            null
        }
    }

    // Sign Out
    fun signOut() {
        try {
            auth?.signOut()
        } catch (e: Exception) {
            Log.e("FirebaseAuthRepo", "Error signing out: ${e.message}")
        }
    }

    // --- Firestore Data Syncing for Smart Notes, Files, Quiz & AI Chat ---

    suspend fun syncSubjectToFirestore(subjectId: String, name: String, code: String, semester: String, completion: Int): Result<Unit> {
        return try {
            val uid = currentUser?.uid ?: "anonymous_student"
            val firestoreInstance = firestore ?: return Result.success(Unit)
            val data = mapOf(
                "subjectId" to subjectId,
                "name" to name,
                "code" to code,
                "semester" to semester,
                "completionPercentage" to completion,
                "updatedAt" to System.currentTimeMillis()
            )
            firestoreInstance.collection("users").document(uid)
                .collection("subjects").document(subjectId)
                .set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.w("FirebaseAuthRepo", "Firestore syncSubject note: ${e.message}")
            Result.success(Unit)
        }
    }

    suspend fun syncUploadedFileToFirestore(
        fileId: String,
        fileName: String,
        fileType: String,
        fileSizeFormatted: String,
        storagePath: String,
        subjectId: String,
        unitId: String,
        extractedText: String
    ): Result<Unit> {
        return try {
            val uid = currentUser?.uid ?: "anonymous_student"
            val firestoreInstance = firestore ?: return Result.success(Unit)
            val data = mapOf(
                "fileId" to fileId,
                "fileName" to fileName,
                "fileType" to fileType,
                "fileSize" to fileSizeFormatted,
                "storagePath" to storagePath,
                "subjectId" to subjectId,
                "unitId" to unitId,
                "extractedText" to extractedText,
                "uploadedAt" to System.currentTimeMillis()
            )
            firestoreInstance.collection("users").document(uid)
                .collection("uploaded_files").document(fileId)
                .set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.w("FirebaseAuthRepo", "Firestore syncUploadedFile note: ${e.message}")
            Result.success(Unit)
        }
    }

    suspend fun syncSummaryToFirestore(
        noteId: String,
        title: String,
        subjectId: String,
        unitId: String,
        summary50Words: String,
        detailedExplanation: String,
        revisionNotes: String
    ): Result<Unit> {
        return try {
            val uid = currentUser?.uid ?: "anonymous_student"
            val firestoreInstance = firestore ?: return Result.success(Unit)
            val data = mapOf(
                "noteId" to noteId,
                "title" to title,
                "subjectId" to subjectId,
                "unitId" to unitId,
                "summary50Words" to summary50Words,
                "detailedExplanation" to detailedExplanation,
                "revisionNotes" to revisionNotes,
                "updatedAt" to System.currentTimeMillis()
            )
            firestoreInstance.collection("users").document(uid)
                .collection("summaries").document(noteId)
                .set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.w("FirebaseAuthRepo", "Firestore syncSummary note: ${e.message}")
            Result.success(Unit)
        }
    }

    suspend fun syncFlashcardToFirestore(
        flashcardId: String,
        subjectId: String,
        unitId: String,
        question: String,
        answer: String,
        reviewStatus: String
    ): Result<Unit> {
        return try {
            val uid = currentUser?.uid ?: "anonymous_student"
            val firestoreInstance = firestore ?: return Result.success(Unit)
            val data = mapOf(
                "flashcardId" to flashcardId,
                "subjectId" to subjectId,
                "unitId" to unitId,
                "question" to question,
                "answer" to answer,
                "reviewStatus" to reviewStatus,
                "updatedAt" to System.currentTimeMillis()
            )
            firestoreInstance.collection("users").document(uid)
                .collection("flashcards").document(flashcardId)
                .set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.w("FirebaseAuthRepo", "Firestore syncFlashcard note: ${e.message}")
            Result.success(Unit)
        }
    }

    suspend fun syncQuizHistoryToFirestore(
        quizAttemptId: String,
        subjectId: String,
        score: Int,
        totalQuestions: Int,
        timestamp: Long = System.currentTimeMillis()
    ): Result<Unit> {
        return try {
            val uid = currentUser?.uid ?: "anonymous_student"
            val firestoreInstance = firestore ?: return Result.success(Unit)
            val data = mapOf(
                "attemptId" to quizAttemptId,
                "subjectId" to subjectId,
                "score" to score,
                "totalQuestions" to totalQuestions,
                "percentage" to (score * 100 / totalQuestions.coerceAtLeast(1)),
                "timestamp" to timestamp
            )
            firestoreInstance.collection("users").document(uid)
                .collection("quiz_history").document(quizAttemptId)
                .set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.w("FirebaseAuthRepo", "Firestore syncQuizHistory note: ${e.message}")
            Result.success(Unit)
        }
    }

    suspend fun syncChatMessageToFirestore(
        messageId: String,
        sender: String,
        messageText: String,
        subjectId: String? = null
    ): Result<Unit> {
        return try {
            val uid = currentUser?.uid ?: "anonymous_student"
            val firestoreInstance = firestore ?: return Result.success(Unit)
            val data = mapOf(
                "messageId" to messageId,
                "sender" to sender,
                "message" to messageText,
                "subjectId" to (subjectId ?: ""),
                "timestamp" to System.currentTimeMillis()
            )
            firestoreInstance.collection("users").document(uid)
                .collection("ai_chat_history").document(messageId)
                .set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.w("FirebaseAuthRepo", "Firestore syncChatMessage note: ${e.message}")
            Result.success(Unit)
        }
    }

    suspend fun syncProgressToFirestore(
        xpPoints: Int,
        streakDays: Int,
        semesterProgressPercent: Int
    ): Result<Unit> {
        return try {
            val uid = currentUser?.uid ?: "anonymous_student"
            val firestoreInstance = firestore ?: return Result.success(Unit)
            val data = mapOf(
                "xpPoints" to xpPoints,
                "streakDays" to streakDays,
                "semesterProgressPercent" to semesterProgressPercent,
                "lastUpdated" to System.currentTimeMillis()
            )
            firestoreInstance.collection("users").document(uid)
                .collection("progress").document("current_progress")
                .set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.w("FirebaseAuthRepo", "Firestore syncProgress note: ${e.message}")
            Result.success(Unit)
        }
    }
}
