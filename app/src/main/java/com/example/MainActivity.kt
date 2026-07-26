package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.BottomNavBar
import com.example.ui.screens.AiChatScreen
import com.example.ui.screens.CareerHubScreen
import com.example.ui.screens.DashboardAnalyticsScreen
import com.example.ui.screens.ExamModeScreen
import com.example.ui.screens.FlashcardsScreen
import com.example.ui.screens.GamificationScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.RevisionPlannerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SignUpScreen
import com.example.ui.screens.SmartSearchScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.SubjectDetailScreen
import com.example.ui.screens.UploadNotesScreen
import com.example.ui.theme.BgDark
import com.example.ui.theme.StudyMateTheme
import com.example.ui.viewmodel.StudyViewModel

import com.example.ui.screens.LoginScreen
import com.example.ui.screens.SignUpScreen
import com.example.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: StudyViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StudyMateTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BgDark),
                    bottomBar = {
                        // Show bottom nav on main navigation screens
                        if (currentScreen in listOf("HOME", "CAREER_HUB", "AI_CHAT", "EXAM_MODE", "QUIZ", "FLASHCARDS", "PROFILE")) {
                            BottomNavBar(
                                currentScreen = currentScreen,
                                onNavigate = { viewModel.navigateTo(it) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(BgDark)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing)) +
                                 scaleIn(initialScale = 0.96f, animationSpec = tween(280, easing = FastOutSlowInEasing)) +
                                 slideInHorizontally(initialOffsetX = { it / 8 }, animationSpec = tween(280, easing = FastOutSlowInEasing)))
                                    .togetherWith(
                                        fadeOut(animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                                        scaleOut(targetScale = 0.98f, animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                                        slideOutHorizontally(targetOffsetX = { -it / 8 }, animationSpec = tween(220, easing = FastOutSlowInEasing))
                                    )
                            },
                            label = "screenTransition"
                        ) { screen ->
                            when (screen) {
                                "SPLASH" -> SplashScreen(onSplashComplete = { viewModel.navigateTo("HOME") })
                                "HOME" -> HomeScreen(viewModel = viewModel)
                                "CAREER_HUB" -> CareerHubScreen(viewModel = viewModel)
                                "SUBJECT_DETAIL" -> SubjectDetailScreen(viewModel = viewModel)
                                "UPLOAD" -> UploadNotesScreen(viewModel = viewModel)
                                "AI_CHAT" -> AiChatScreen(viewModel = viewModel)
                                "EXAM_MODE" -> ExamModeScreen(viewModel = viewModel)
                                "QUIZ" -> QuizScreen(viewModel = viewModel)
                                "FLASHCARDS" -> FlashcardsScreen(viewModel = viewModel)
                                "REVISION" -> RevisionPlannerScreen(viewModel = viewModel)
                                "ANALYTICS" -> DashboardAnalyticsScreen(viewModel = viewModel)
                                "GAMIFICATION" -> GamificationScreen(viewModel = viewModel)
                                "SEARCH" -> SmartSearchScreen(viewModel = viewModel)
                                "PROFILE" -> ProfileScreen(viewModel = viewModel, authViewModel = authViewModel)
                                "SETTINGS" -> SettingsScreen(viewModel = viewModel)
                                "LOGIN" -> LoginScreen(authViewModel = authViewModel, studyViewModel = viewModel)
                                "SIGNUP" -> SignUpScreen(authViewModel = authViewModel, studyViewModel = viewModel)
                                else -> HomeScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
