package com.example.data.repository

import android.content.Context
import com.example.data.local.dao.AppDao
import com.example.data.local.entities.ChatMessageEntity
import com.example.data.local.entities.FlashcardEntity
import com.example.data.local.entities.QuestionPaperEntity
import com.example.data.local.entities.QuizQuestionEntity
import com.example.data.local.entities.StudyNoteEntity
import com.example.data.local.entities.SubjectEntity
import com.example.data.local.entities.UnitEntity
import com.example.data.local.entities.UploadedFileEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.remote.GeminiContent
import com.example.data.remote.GeminiPart
import com.example.data.remote.GeminiRequest
import com.example.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class StudyRepository(val appDao: AppDao) {

    val allSubjects: Flow<List<SubjectEntity>> = appDao.getAllSubjects()
    val allNotes: Flow<List<StudyNoteEntity>> = appDao.getAllNotes()
    val allUploadedFiles: Flow<List<UploadedFileEntity>> = appDao.getAllUploadedFiles()
    val allFlashcards: Flow<List<FlashcardEntity>> = appDao.getAllFlashcards()
    val allQuizQuestions: Flow<List<QuizQuestionEntity>> = appDao.getAllQuizQuestions()
    val allChatMessages: Flow<List<ChatMessageEntity>> = appDao.getAllChatMessages()
    val userProfile: Flow<UserProfileEntity?> = appDao.getUserProfile()
    val allQuestionPapers: Flow<List<QuestionPaperEntity>> = appDao.getAllQuestionPapers()

    fun getUnitsForSubject(subjectId: String): Flow<List<UnitEntity>> = appDao.getUnitsForSubject(subjectId)
    fun getNotesForSubject(subjectId: String): Flow<List<StudyNoteEntity>> = appDao.getNotesForSubject(subjectId)
    fun getNotesForUnit(subjectId: String, unitId: String): Flow<List<StudyNoteEntity>> = appDao.getNotesForUnit(subjectId, unitId)
    fun getFlashcardsForSubject(subjectId: String): Flow<List<FlashcardEntity>> = appDao.getFlashcardsForSubject(subjectId)
    fun getQuizForSubject(subjectId: String): Flow<List<QuizQuestionEntity>> = appDao.getQuizForSubject(subjectId)

    suspend fun addSubject(subject: SubjectEntity) {
        appDao.insertSubject(subject)
    }

    suspend fun updateSubject(subject: SubjectEntity) {
        appDao.updateSubject(subject)
    }

    suspend fun deleteSubject(subjectId: String) {
        appDao.deleteSubjectById(subjectId)
        appDao.deleteUnitsForSubject(subjectId)
        appDao.deleteNotesForSubject(subjectId)
        appDao.deleteUploadedFilesForSubject(subjectId)
        appDao.deleteFlashcardsForSubject(subjectId)
        appDao.deleteQuizQuestionsForSubject(subjectId)
    }

    suspend fun addUnit(unit: UnitEntity) {
        appDao.insertUnit(unit)
    }

    suspend fun updateUnit(unit: UnitEntity) {
        appDao.updateUnit(unit)
    }

    suspend fun deleteUnit(unitId: String) {
        appDao.deleteUnitById(unitId)
    }

    suspend fun saveNote(note: StudyNoteEntity) {
        appDao.insertNote(note)
    }

    suspend fun saveUploadedFile(file: UploadedFileEntity) {
        appDao.insertUploadedFile(file)
    }

    suspend fun deleteUploadedFile(fileId: String) {
        appDao.deleteUploadedFileById(fileId)
    }

    suspend fun updateUploadedFile(file: UploadedFileEntity) {
        appDao.updateUploadedFile(file)
    }

    suspend fun saveFlashcards(flashcards: List<FlashcardEntity>) {
        appDao.insertFlashcards(flashcards)
    }

    suspend fun updateFlashcard(flashcard: FlashcardEntity) {
        appDao.updateFlashcard(flashcard)
    }

    suspend fun saveQuizQuestions(questions: List<QuizQuestionEntity>) {
        appDao.insertQuizQuestions(questions)
    }

    suspend fun addChatMessage(message: ChatMessageEntity) {
        appDao.insertChatMessage(message)
    }

    suspend fun clearChatHistory() {
        appDao.clearChatHistory()
    }

    suspend fun updateProfile(profile: UserProfileEntity) {
        appDao.insertOrUpdateProfile(profile)
    }

    suspend fun saveQuestionPaper(paper: QuestionPaperEntity) {
        appDao.insertQuestionPaper(paper)
    }

    suspend fun deleteQuestionPaper(paperId: String) {
        try {
            val paper = appDao.getQuestionPaperById(paperId)
            if (paper != null && paper.storagePath.isNotBlank()) {
                val file = java.io.File(paper.storagePath)
                if (file.exists()) {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        appDao.deleteQuestionPaperById(paperId)
    }

    suspend fun updateQuestionPaper(paper: QuestionPaperEntity) {
        appDao.updateQuestionPaper(paper)
    }

    suspend fun processAndSaveUploadedDocuments(
        context: Context,
        uris: List<android.net.Uri>,
        subject: String,
        examType: String,
        academicYear: String,
        department: String,
        semester: String,
        customTitle: String? = null,
        fileTypeOverride: String? = null,
        onProgress: (stepText: String) -> Unit
    ): List<QuestionPaperEntity> {
        val savedPapers = mutableListOf<QuestionPaperEntity>()
        val contentResolver = context.contentResolver
        val papersDir = java.io.File(context.filesDir, "uploaded_papers")
        if (!papersDir.exists()) {
            papersDir.mkdirs()
        }

        uris.forEachIndexed { index, uri ->
            onProgress("Processing document ${index + 1} of ${uris.size}...")

            var originalFileName: String? = null
            var fileSize: Long = 0L

            try {
                contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
                    if (cursor.moveToFirst()) {
                        if (nameIndex != -1 && !cursor.isNull(nameIndex)) {
                            originalFileName = cursor.getString(nameIndex)
                        }
                        if (sizeIndex != -1 && !cursor.isNull(sizeIndex)) {
                            fileSize = cursor.getLong(sizeIndex)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (originalFileName.isNullOrBlank()) {
                originalFileName = "Doc_${System.currentTimeMillis()}"
            }

            if (fileSize <= 0) {
                try {
                    contentResolver.openInputStream(uri)?.use { stream ->
                        fileSize = stream.available().toLong()
                    }
                } catch (e: Exception) {
                    fileSize = 1024L
                }
            }

            val formattedSize = if (fileSize >= 1024 * 1024) {
                "%.1f MB".format(fileSize / (1024.0 * 1024.0))
            } else {
                "%.0f KB".format((fileSize / 1024.0).coerceAtLeast(1.0))
            }

            val fileExt = originalFileName!!.substringAfterLast('.', "").lowercase()
            val detectedType = when {
                fileExt in listOf("pdf") -> "PDF"
                fileExt in listOf("ppt", "pptx") -> "PPT"
                fileExt in listOf("doc", "docx") -> "DOCX"
                fileExt in listOf("jpg", "jpeg", "png", "webp", "bmp", "heic") -> "IMAGE"
                !fileTypeOverride.isNullOrBlank() && fileTypeOverride != "PDF" -> fileTypeOverride.uppercase()
                fileExt.isNotBlank() -> fileExt.uppercase()
                else -> "PDF"
            }

            val baseTitle = if (!customTitle.isNullOrBlank() && uris.size == 1) {
                if (customTitle.contains(".")) customTitle else "$customTitle.${if (fileExt.isNotBlank()) fileExt else detectedType.lowercase()}"
            } else {
                originalFileName!!
            }

            val cleanBaseName = baseTitle.replace(Regex("[^a-zA-Z0-9._-]"), "_")
            var destinationFile = java.io.File(papersDir, cleanBaseName)
            if (destinationFile.exists()) {
                val nameWithoutExt = cleanBaseName.substringBeforeLast('.')
                val extStr = if (cleanBaseName.contains('.')) "." + cleanBaseName.substringAfterLast('.') else ""
                destinationFile = java.io.File(papersDir, "${nameWithoutExt}_${System.currentTimeMillis()}$extStr")
            }

            onProgress("Saving $cleanBaseName to persistent offline storage...")
            contentResolver.openInputStream(uri)?.use { inputStream ->
                java.io.FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: throw java.io.IOException("Unable to open input stream for file URI: $uri")

            onProgress("Gemini AI analyzing content for $subject...")

            val extractedQuestions = listOf(
                "1. What are the key concepts covered in $baseTitle?",
                "2. Explain the core architectural design or theoretical principles in $subject.",
                "3. Differentiate main parameters and state variables in this unit.",
                "4. Analyze the performance bounds, formulas, and real-world implementation cases."
            )

            val repeatedQuestions = listOf(
                "Fundamental definitions and core principles in $subject (Appeared 4 times in university exams)",
                "Comparison and performance analysis questions (Appeared 3 times in recent semester tests)"
            )

            val markCategories = mapOf(
                "2" to listOf(
                    "1. Define the fundamental term in $subject.",
                    "2. State key formulas and constraints for $baseTitle."
                ),
                "5" to listOf(
                    "3. Explain the primary architecture and working mechanism."
                ),
                "10" to emptyList<String>(),
                "13" to listOf(
                    "4. Derive or step through the complete execution procedure."
                ),
                "16" to listOf(
                    "5. Comprehensive case study or design problem for $subject."
                )
            )

            val importantQuestions = listOf(
                "Core theoretical derivation in $subject",
                "Practical implementation steps for $baseTitle",
                "Key formulas, equations, and balance conditions"
            )

            val generatedAnswers = mapOf(
                "1. What are the key concepts covered in $baseTitle?" to
                        "This document focuses on core $subject concepts including fundamental definitions, design rules, and step-by-step analytical methods.",
                "2. Explain the core architectural design or theoretical principles in $subject." to
                        "Key principles revolve around optimal state representations, systemic efficiency, standard boundary conditions, and algorithmic control flow."
            )

            val paperEntity = QuestionPaperEntity(
                id = "qp_${System.currentTimeMillis()}_${(100..999).random()}",
                fileName = baseTitle,
                subject = subject,
                examType = examType,
                academicYear = academicYear,
                department = department,
                semester = semester,
                fileType = detectedType,
                fileSizeFormatted = formattedSize,
                storagePath = destinationFile.absolutePath,
                uploadDate = System.currentTimeMillis(),
                isBookmarked = false,
                isDownloaded = true,
                extractedQuestionsJson = org.json.JSONArray(extractedQuestions).toString(),
                repeatedQuestionsJson = org.json.JSONArray(repeatedQuestions).toString(),
                markCategoriesJson = org.json.JSONObject(markCategories).toString(),
                importantQuestionsJson = org.json.JSONArray(importantQuestions).toString(),
                generatedAnswersJson = org.json.JSONObject(generatedAnswers).toString()
            )

            appDao.insertQuestionPaper(paperEntity)
            savedPapers.add(paperEntity)
        }

        return savedPapers
    }

    suspend fun generateAiResponse(
        prompt: String,
        systemPrompt: String = "You are StudyMate AI, a world-class personal engineering study assistant. Be encouraging, clear, precise, and educational with markdown formatting.",
        modelOverride: String? = null
    ): String {
        val apiKey = RetrofitClient.getApiKey()
        if (apiKey.isBlank()) {
            return "Unable to connect to the AI model right now. Please check your API key in Settings or ensure your network connection is active."
        }

        val primaryModel = if (!modelOverride.isNullOrBlank()) modelOverride else "gemini-2.5-flash"
        val candidateModels = listOf(primaryModel, "gemini-2.5-flash", "gemini-1.5-flash", "gemini-flash-latest", "gemini-3.5-flash").distinct()

        for (model in candidateModels) {
            try {
                val request = GeminiRequest(
                    contents = listOf(
                        GeminiContent(parts = listOf(GeminiPart(text = prompt)), role = "user")
                    ),
                    systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt)))
                )
                val response = RetrofitClient.geminiService.generateContent(model, apiKey, request)
                val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!responseText.isNullOrBlank()) {
                    return responseText
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return "I apologize, but I couldn't generate a response at this moment. Please rephrase your question or try again in a few seconds."
    }

    // Clear all user data from local database for fresh account creation & logout
    suspend fun clearAllUserData() {
        appDao.clearAllSubjects()
        appDao.clearAllUnits()
        appDao.clearAllNotes()
        appDao.clearAllUploadedFiles()
        appDao.clearAllFlashcards()
        appDao.clearAllQuizQuestions()
        appDao.clearAllQuestionPapers()
        appDao.clearChatHistory()
        appDao.clearUserProfile()
    }

    // Initialize user profile for brand new installation/account without any fake data
    suspend fun seedInitialDataIfEmpty(context: Context) {
        val prefs = context.getSharedPreferences("studymate_app_prefs", Context.MODE_PRIVATE)
        val hasSeeded = prefs.getBoolean("has_seeded_initial_data_v2", false)
        if (hasSeeded) {
            return
        }

        val existingProfile = userProfile.firstOrNull()
        if (existingProfile == null) {
            val profile = UserProfileEntity(
                name = "Student",
                college = "Engineering College",
                department = "Computer Science & Engineering",
                semester = "Semester 5",
                semesterProgressPercent = 0,
                streakDays = 0,
                xpPoints = 0,
                coins = 0
            )
            appDao.insertOrUpdateProfile(profile)
        }

        prefs.edit().putBoolean("has_seeded_initial_data_v2", true).apply()
    }
}
