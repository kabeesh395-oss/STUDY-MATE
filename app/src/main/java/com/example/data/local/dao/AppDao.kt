package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.ChatMessageEntity
import com.example.data.local.entities.FlashcardEntity
import com.example.data.local.entities.QuizQuestionEntity
import com.example.data.local.entities.StudyNoteEntity
import com.example.data.local.entities.SubjectEntity
import com.example.data.local.entities.UnitEntity
import com.example.data.local.entities.UploadedFileEntity
import com.example.data.local.entities.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Subjects
    @Query("SELECT * FROM subjects ORDER BY displayOrder ASC, isFavorite DESC")
    fun getAllSubjects(): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM subjects WHERE id = :subjectId")
    suspend fun getSubjectById(subjectId: String): SubjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: SubjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>)

    @Query("DELETE FROM subjects WHERE id = :subjectId")
    suspend fun deleteSubjectById(subjectId: String)

    @Update
    suspend fun updateSubject(subject: SubjectEntity)

    // Units
    @Query("SELECT * FROM units WHERE subjectId = :subjectId ORDER BY unitNumber ASC")
    fun getUnitsForSubject(subjectId: String): Flow<List<UnitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnits(units: List<UnitEntity>)

    // Notes
    @Query("SELECT * FROM study_notes WHERE subjectId = :subjectId AND unitId = :unitId ORDER BY uploadedAt DESC")
    fun getNotesForUnit(subjectId: String, unitId: String): Flow<List<StudyNoteEntity>>

    @Query("SELECT * FROM study_notes WHERE subjectId = :subjectId")
    fun getNotesForSubject(subjectId: String): Flow<List<StudyNoteEntity>>

    @Query("SELECT * FROM study_notes ORDER BY uploadedAt DESC")
    fun getAllNotes(): Flow<List<StudyNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: StudyNoteEntity)

    // Uploaded Files
    @Query("SELECT * FROM uploaded_files ORDER BY uploadedAt DESC")
    fun getAllUploadedFiles(): Flow<List<UploadedFileEntity>>

    @Query("SELECT * FROM uploaded_files WHERE subjectId = :subjectId")
    fun getUploadedFilesForSubject(subjectId: String): Flow<List<UploadedFileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUploadedFile(file: UploadedFileEntity)

    @Query("DELETE FROM uploaded_files WHERE id = :fileId")
    suspend fun deleteUploadedFileById(fileId: String)

    @Update
    suspend fun updateUploadedFile(file: UploadedFileEntity)

    // Flashcards
    @Query("SELECT * FROM flashcards WHERE subjectId = :subjectId")
    fun getFlashcardsForSubject(subjectId: String): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards")
    fun getAllFlashcards(): Flow<List<FlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(flashcards: List<FlashcardEntity>)

    @Update
    suspend fun updateFlashcard(flashcard: FlashcardEntity)

    // Quizzes
    @Query("SELECT * FROM quiz_questions WHERE subjectId = :subjectId")
    fun getQuizForSubject(subjectId: String): Flow<List<QuizQuestionEntity>>

    @Query("SELECT * FROM quiz_questions")
    fun getAllQuizQuestions(): Flow<List<QuizQuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestions(questions: List<QuizQuestionEntity>)

    // Chat
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllChatMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatHistory()

    // Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)
}
