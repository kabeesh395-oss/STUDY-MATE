package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.AppDao
import com.example.data.local.entities.ChatMessageEntity
import com.example.data.local.entities.FlashcardEntity
import com.example.data.local.entities.QuizQuestionEntity
import com.example.data.local.entities.StudyNoteEntity
import com.example.data.local.entities.SubjectEntity
import com.example.data.local.entities.UnitEntity
import com.example.data.local.entities.UploadedFileEntity
import com.example.data.local.entities.UserProfileEntity

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        try {
            db.execSQL("ALTER TABLE study_notes ADD COLUMN keyPointsJson TEXT NOT NULL DEFAULT '[]'")
        } catch (e: Exception) {}
        try {
            db.execSQL("ALTER TABLE study_notes ADD COLUMN vivaQuestionsJson TEXT NOT NULL DEFAULT '[]'")
        } catch (e: Exception) {}
        try {
            db.execSQL("ALTER TABLE study_notes ADD COLUMN shortQuestionsJson TEXT NOT NULL DEFAULT '[]'")
        } catch (e: Exception) {}
        try {
            db.execSQL("ALTER TABLE study_notes ADD COLUMN mediumQuestionsJson TEXT NOT NULL DEFAULT '[]'")
        } catch (e: Exception) {}
        try {
            db.execSQL("ALTER TABLE study_notes ADD COLUMN longQuestionsJson TEXT NOT NULL DEFAULT '[]'")
        } catch (e: Exception) {}
        try {
            db.execSQL("ALTER TABLE study_notes ADD COLUMN mcqsJson TEXT NOT NULL DEFAULT '[]'")
        } catch (e: Exception) {}
        try {
            db.execSQL("ALTER TABLE study_notes ADD COLUMN revisionNotes TEXT NOT NULL DEFAULT ''")
        } catch (e: Exception) {}

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `uploaded_files` (
                `id` TEXT NOT NULL,
                `subjectId` TEXT NOT NULL,
                `unitId` TEXT NOT NULL,
                `fileName` TEXT NOT NULL,
                `fileType` TEXT NOT NULL,
                `fileSizeFormatted` TEXT NOT NULL,
                `storagePath` TEXT NOT NULL DEFAULT '',
                `extractedText` TEXT NOT NULL DEFAULT '',
                `uploadedAt` INTEGER NOT NULL DEFAULT 0,
                `status` TEXT NOT NULL DEFAULT 'UPLOADED',
                PRIMARY KEY(`id`)
            )
        """.trimIndent())
    }
}

@Database(
    entities = [
        SubjectEntity::class,
        UnitEntity::class,
        StudyNoteEntity::class,
        UploadedFileEntity::class,
        FlashcardEntity::class,
        QuizQuestionEntity::class,
        ChatMessageEntity::class,
        UserProfileEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "studymate_db"
                )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

