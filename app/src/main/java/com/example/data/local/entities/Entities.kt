package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val code: String,
    val semester: String,
    val completionPercentage: Int = 0,
    val isFavorite: Boolean = false,
    val iconCategory: String = "TECH",
    val displayOrder: Int = 0
)

@Entity(tableName = "units")
data class UnitEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val unitNumber: Int,
    val title: String,
    val completionPercentage: Int = 0
)

@Entity(tableName = "study_notes")
data class StudyNoteEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val unitId: String,
    val title: String,
    val rawText: String,
    val summary50Words: String,
    val detailedExplanation: String,
    val definitionsJson: String = "[]",
    val formulasJson: String = "[]",
    val mindMapNodesJson: String = "[]",
    val keyPointsJson: String = "[]",
    val vivaQuestionsJson: String = "[]",
    val shortQuestionsJson: String = "[]", // 2-Mark
    val mediumQuestionsJson: String = "[]", // 5-Mark
    val longQuestionsJson: String = "[]", // 13 & 16 Mark
    val mcqsJson: String = "[]",
    val revisionNotes: String = "",
    val examTips: String = "",
    val commonMistakes: String = "",
    val difficulty: String = "Medium",
    val estimatedMinutes: Int = 15,
    val confidenceScore: Int = 85,
    val uploadedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "uploaded_files")
data class UploadedFileEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val unitId: String,
    val fileName: String,
    val fileType: String, // "PDF", "PPT", "DOCX", "TXT", "IMAGE", "CAMERA"
    val fileSizeFormatted: String,
    val storagePath: String = "",
    val extractedText: String = "",
    val uploadedAt: Long = System.currentTimeMillis(),
    val status: String = "UPLOADED" // "UPLOADED", "PROCESSED", "SYNCD_FIRESTORE"
)

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val unitId: String,
    val question: String,
    val answer: String,
    val isBookmarked: Boolean = false,
    val reviewStatus: String = "NEW" // "NEW", "MASTERED", "NEEDS_REVIEW"
)

@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val unitId: String,
    val question: String,
    val type: String = "MCQ", // MCQ, TRUE_FALSE, FILL_BLANKS, ASSERTION_REASON, CODING
    val optionsJson: String = "[]",
    val correctAnswer: String,
    val explanation: String,
    val difficulty: String = "Medium"
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectId: String? = null,
    val sender: String, // "USER" or "AI"
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isCode: Boolean = false,
    val suggestedPillsJson: String? = null
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Kabeesh",
    val college: String = "National Institute of Technology",
    val department: String = "Computer Science & Engineering",
    val semester: String = "Semester 5",
    val semesterProgressPercent: Int = 72,
    val streakDays: Int = 12,
    val xpPoints: Int = 1450,
    val coins: Int = 320,
    val accentColorHex: String = "#3B82F6",
    val selectedLanguage: String = "English",
    val selectedAiModel: String = "gemini-3.5-flash"
)

@Entity(tableName = "question_papers")
data class QuestionPaperEntity(
    @PrimaryKey val id: String,
    val fileName: String,
    val subject: String,
    val examType: String, // "IA 1", "IA 2", "IA 3", "Model Exam", "Semester Exam", "University Previous Year Papers"
    val academicYear: String = "2024-2025",
    val department: String = "Computer Science & Engineering",
    val semester: String = "Semester 5",
    val fileType: String = "PDF", // "PDF", "IMAGE", "DOCX"
    val fileSizeFormatted: String = "1.2 MB",
    val storagePath: String = "",
    val uploadDate: Long = System.currentTimeMillis(),
    val isBookmarked: Boolean = false,
    val isDownloaded: Boolean = true,
    val extractedQuestionsJson: String = "[]",
    val repeatedQuestionsJson: String = "[]",
    val markCategoriesJson: String = "{}", // e.g. {"2":["q1","q2"],"5":["q3"],"10":[],"13":["q4"],"16":["q5"]}
    val importantQuestionsJson: String = "[]",
    val generatedAnswersJson: String = "{}"
)
