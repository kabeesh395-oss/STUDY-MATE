package com.example.data.model

data class AuthUserModel(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val college: String = "College of Engineering",
    val department: String = "Computer Science & Engineering",
    val semester: String = "Semester 6",
    val createdAt: Long = System.currentTimeMillis(),
    val isGoogleUser: Boolean = false,

    // Career & Professional Hub Profiles
    val linkedinUrl: String = "",
    val githubUsername: String = "",
    val leetcodeUsername: String = "",
    val unstopUrl: String = "",
    val hackerrankUsername: String = "",
    val codechefUsername: String = "",
    val codeforcesUsername: String = "",
    val kaggleUsername: String = "",
    val portfolioUrl: String = "",
    val resumeUrl: String = "",

    // Real User Statistics & Metrics
    val githubCommits: Int = 0,
    val leetcodeSolved: Int = 0,
    val codingStreakDays: Int = 0,
    val weeklyLearningHours: Float = 0.0f,
    val linkedinScore: Int = 0,
    val unstopAppsCount: Int = 0
)
