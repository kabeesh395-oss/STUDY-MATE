package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class StudyMateApplication : Application() {

    companion object {
        var instance: StudyMateApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initFirebase()
    }

    fun initFirebase() {
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val app = FirebaseApp.initializeApp(this)
                if (app == null) {
                    val options = FirebaseOptions.Builder()
                        .setApplicationId("1:714948764625:android:a1b2c3d4e5f67890")
                        .setApiKey("AIzaSyMockApiKeyForStudyMateAiApp123456789")
                        .setProjectId("studymate-ai-app")
                        .setStorageBucket("studymate-ai-app.appspot.com")
                        .build()
                    FirebaseApp.initializeApp(this, options)
                }
            }
        } catch (e: Exception) {
            Log.e("StudyMateApp", "Firebase initializeApp error: ${e.message}")
        }
    }
}
