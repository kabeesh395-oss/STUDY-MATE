package com.example.utils

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R

class StudyNotificationManager(private val context: Context) {

    companion object {
        const val CHANNEL_AI_INSIGHTS = "studymate_ai_insights"
        const val CHANNEL_STUDY_REMINDERS = "studymate_study_reminders"
        const val CHANNEL_BEN_AI = "studymate_ben_ai"
        const val CHANNEL_ACHIEVEMENTS = "studymate_achievements"
        const val CHANNEL_STREAKS = "studymate_streaks"
        const val CHANNEL_DOWNLOADS = "studymate_downloads"
        const val CHANNEL_EXAMS = "studymate_exams"

        @Volatile
        private var INSTANCE: StudyNotificationManager? = null

        fun getInstance(context: Context): StudyNotificationManager {
            return INSTANCE ?: synchronized(this) {
                val instance = StudyNotificationManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val groupAi = NotificationChannelGroup("group_ai_updates", "AI Updates")
            val groupReminders = NotificationChannelGroup("group_reminders", "Reminders")
            val groupDownloads = NotificationChannelGroup("group_downloads", "Downloads")

            notificationManager.createNotificationChannelGroups(listOf(groupAi, groupReminders, groupDownloads))

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channels = listOf(
                NotificationChannel(
                    CHANNEL_AI_INSIGHTS,
                    "AI Insights & Tutor",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    group = "group_ai_updates"
                    description = "AI recommendations, smart summaries, and instant tutor explanations from StudyMate AI"
                    enableVibration(true)
                    setSound(defaultSoundUri, audioAttributes)
                },
                NotificationChannel(
                    CHANNEL_BEN_AI,
                    "Ben AI Tutor Messages",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    group = "group_ai_updates"
                    description = "Direct messages, chat tips, and encouragement from your AI study companion Ben"
                    enableVibration(true)
                    setSound(defaultSoundUri, audioAttributes)
                },
                NotificationChannel(
                    CHANNEL_STUDY_REMINDERS,
                    "Daily Study Reminders",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    group = "group_reminders"
                    description = "Personalized daily study goals and revision schedule alerts"
                    enableVibration(true)
                    setSound(defaultSoundUri, audioAttributes)
                },
                NotificationChannel(
                    CHANNEL_STREAKS,
                    "Streak Protection & Reminders",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    group = "group_reminders"
                    description = "Daily study streak tracking and streak flame protection alerts"
                    enableVibration(true)
                    setSound(defaultSoundUri, audioAttributes)
                },
                NotificationChannel(
                    CHANNEL_EXAMS,
                    "Exam Mode & Test Reminders",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    group = "group_reminders"
                    description = "Internal Assessments (IA1, IA2, IA3), Model Exam, and Semester exam countdowns"
                    enableVibration(true)
                    setSound(defaultSoundUri, audioAttributes)
                },
                NotificationChannel(
                    CHANNEL_ACHIEVEMENTS,
                    "Achievements & Level Ups",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    group = "group_reminders"
                    description = "XP progress, level milestone celebrations, and badge unlocks"
                    enableVibration(true)
                    setSound(defaultSoundUri, audioAttributes)
                },
                NotificationChannel(
                    CHANNEL_DOWNLOADS,
                    "Question Bank & Solutions",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    group = "group_downloads"
                    description = "Question paper solution generation and offline study material downloads"
                    enableVibration(true)
                    setSound(defaultSoundUri, audioAttributes)
                }
            )

            channels.forEach { channel ->
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    private fun getLargeIconBitmap(): Bitmap? {
        return try {
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_lightning_s_logo)
        } catch (e: Exception) {
            null
        }
    }

    private fun getContentPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun buildBaseNotification(
        channelId: String,
        title: String,
        message: String,
        subText: String? = "StudyMate AI"
    ): NotificationCompat.Builder {
        val formattedTitle = if (title.startsWith("StudyMate")) title else "StudyMate AI • $title"

        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification_small)
            .setLargeIcon(getLargeIconBitmap())
            .setContentTitle(formattedTitle)
            .setContentText(message)
            .setSubText(subText ?: "StudyMate AI")
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setColor(0xFF3B82F6.toInt()) // Electric Blue #3B82F6
            .setAutoCancel(true)
            .setContentIntent(getContentPendingIntent())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
    }

    private fun notifySafely(notificationId: Int, builder: NotificationCompat.Builder) {
        try {
            notificationManager.notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            // Permission POST_NOTIFICATIONS not granted on Android 13+
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 1. AI Notifications
    fun sendAiNotification(title: String = "AI Insight Ready", message: String = "Ben AI generated custom key points and practice questions for your upcoming test.") {
        val builder = buildBaseNotification(
            channelId = CHANNEL_AI_INSIGHTS,
            title = title,
            message = message,
            subText = "AI Study Assistant"
        )
        notifySafely(1001, builder)
    }

    // 2. Daily Study Reminders
    fun sendStudyReminderNotification(title: String = "Daily Study Goal", message: String = "You're 20 minutes away from completing today's study target. Keep the momentum going!") {
        val builder = buildBaseNotification(
            channelId = CHANNEL_STUDY_REMINDERS,
            title = title,
            message = message,
            subText = "Daily Goal • StudyMate AI"
        )
        notifySafely(1002, builder)
    }

    // 3. Ben AI Companion Messages
    fun sendBenAiMessageNotification(message: String = "Hey! I've analyzed your weak topics in Data Structures. Ready for a 5-minute quick flashcard quiz?") {
        val builder = buildBaseNotification(
            channelId = CHANNEL_BEN_AI,
            title = "Ben AI Companion",
            message = message,
            subText = "Live AI Companion"
        )
        notifySafely(1003, builder)
    }

    // 4. Achievement & XP Notifications
    fun sendAchievementNotification(title: String = "Achievement Unlocked! 🏆", message: String = "You earned +150 XP for solving 5 Previous Year Question papers. Level 4 reached!") {
        val builder = buildBaseNotification(
            channelId = CHANNEL_ACHIEVEMENTS,
            title = title,
            message = message,
            subText = "Gamification • StudyMate AI"
        )
        notifySafely(1004, builder)
    }

    // 5. Streak Reminders
    fun sendStreakReminderNotification(streakDays: Int = 7) {
        val title = "Don't Break Your $streakDays-Day Streak! 🔥"
        val message = "Solve at least 1 quiz question or review 1 note today to protect your $streakDays-day study streak!"
        val builder = buildBaseNotification(
            channelId = CHANNEL_STREAKS,
            title = title,
            message = message,
            subText = "Streak Flame Alert"
        )
        notifySafely(1005, builder)
    }

    // 6. Download & Question Bank Complete Notifications
    fun sendDownloadCompleteNotification(fileName: String = "IA-2 Computer Networks Paper", paperType: String = "Question Paper Bank") {
        val title = "Solution & Paper Ready 📂"
        val message = "$fileName with AI mark breakdown and model answers is ready for offline viewing."
        val builder = buildBaseNotification(
            channelId = CHANNEL_DOWNLOADS,
            title = title,
            message = message,
            subText = paperType.ifEmpty { "Question Bank" }
        )
        notifySafely(1006, builder)
    }

    // 7. Exam Reminders & Alerts
    fun sendExamReminderNotification(subjectName: String = "Database Management Systems", examName: String = "IA-1 Assessment", timeText: String = "Tomorrow at 9:00 AM") {
        val title = "Exam Alert: $examName"
        val message = "$subjectName $examName is scheduled for $timeText. Tap to review high-weightage topics and 10-mark answers."
        val builder = buildBaseNotification(
            channelId = CHANNEL_EXAMS,
            title = title,
            message = message,
            subText = "Exam Planner • StudyMate AI"
        )
        notifySafely(1007, builder)
    }

    // Trigger full branded notification suite for test / verification
    fun triggerAllBrandedNotifications() {
        sendAiNotification("AI Analysis Complete ⚡", "Ben AI analyzed your IA-2 answer sheets and highlighted 3 key formulas to revise.")
        sendStreakReminderNotification(12)
        sendDownloadCompleteNotification("Semester-IV Model Question Paper 2026", "Model Exam Bank")
        sendExamReminderNotification("Object Oriented Programming", "Semester Exam", "in 2 days")
        sendAchievementNotification("Level 5 Study Master Unlocked!", "You have reached 1,250 XP and earned the 'Question Bank Conqueror' badge.")
    }
}
