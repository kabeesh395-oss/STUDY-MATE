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
    val isGoogleUser: Boolean = false
)
