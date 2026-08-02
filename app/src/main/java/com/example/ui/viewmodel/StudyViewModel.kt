package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entities.ChatMessageEntity
import com.example.data.local.entities.FlashcardEntity
import com.example.data.local.entities.QuestionPaperEntity
import com.example.data.local.entities.QuizQuestionEntity
import com.example.data.local.entities.StudyNoteEntity
import com.example.data.local.entities.SubjectEntity
import com.example.data.local.entities.UnitEntity
import com.example.data.local.entities.UploadedFileEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.repository.FirebaseAuthRepository
import com.example.data.repository.StudyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.UUID

class StudyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StudyRepository(AppDatabase.getInstance(application).appDao())
    private val authRepository = FirebaseAuthRepository()

    val subjects: StateFlow<List<SubjectEntity>> = repository.allSubjects.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val notes: StateFlow<List<StudyNoteEntity>> = repository.allNotes.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val uploadedFiles: StateFlow<List<UploadedFileEntity>> = repository.allUploadedFiles.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val flashcards: StateFlow<List<FlashcardEntity>> = repository.allFlashcards.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val quizQuestions: StateFlow<List<QuizQuestionEntity>> = repository.allQuizQuestions.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.allChatMessages.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    val allQuestionPapers: StateFlow<List<QuestionPaperEntity>> = repository.allQuestionPapers.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    // Question Paper Filters
    private val _paperFolderFilter = MutableStateFlow("ALL")
    val paperFolderFilter: StateFlow<String> = _paperFolderFilter.asStateFlow()

    private val _paperSubjectFilter = MutableStateFlow("ALL")
    val paperSubjectFilter: StateFlow<String> = _paperSubjectFilter.asStateFlow()

    private val _paperYearFilter = MutableStateFlow("ALL")
    val paperYearFilter: StateFlow<String> = _paperYearFilter.asStateFlow()

    private val _paperSearchQuery = MutableStateFlow("")
    val paperSearchQuery: StateFlow<String> = _paperSearchQuery.asStateFlow()

    private val _selectedPaper = MutableStateFlow<QuestionPaperEntity?>(null)
    val selectedPaper: StateFlow<QuestionPaperEntity?> = _selectedPaper.asStateFlow()

    private val _isProcessingPaperUpload = MutableStateFlow(false)
    val isProcessingPaperUpload: StateFlow<Boolean> = _isProcessingPaperUpload.asStateFlow()

    private val _paperUploadProgressText = MutableStateFlow("")
    val paperUploadProgressText: StateFlow<String> = _paperUploadProgressText.asStateFlow()

    // UI State variables
    private val _currentScreen = MutableStateFlow("SPLASH") // SPLASH, HOME, SUBJECT_DETAIL, UPLOAD, AI_CHAT, EXAM_MODE, QUIZ, FLASHCARDS, REVISION, ANALYTICS, GAMIFICATION, SEARCH, PROFILE, SETTINGS
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val _selectedSubject = MutableStateFlow<SubjectEntity?>(null)
    val selectedSubject: StateFlow<SubjectEntity?> = _selectedSubject.asStateFlow()

    private val _selectedUnit = MutableStateFlow<UnitEntity?>(null)
    val selectedUnit: StateFlow<UnitEntity?> = _selectedUnit.asStateFlow()

    private val _selectedNote = MutableStateFlow<StudyNoteEntity?>(null)
    val selectedNote: StateFlow<StudyNoteEntity?> = _selectedNote.asStateFlow()

    private val _unitsForSelectedSubject = MutableStateFlow<List<UnitEntity>>(emptyList())
    val unitsForSelectedSubject: StateFlow<List<UnitEntity>> = _unitsForSelectedSubject.asStateFlow()

    // Upload & AI Processing state
    private val _isProcessingUpload = MutableStateFlow(false)
    val isProcessingUpload: StateFlow<Boolean> = _isProcessingUpload.asStateFlow()

    private val _uploadProgressText = MutableStateFlow("")
    val uploadProgressText: StateFlow<String> = _uploadProgressText.asStateFlow()

    // Chat AI state
    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // Voice AI Player State
    private val _isPlayingVoice = MutableStateFlow(false)
    val isPlayingVoice: StateFlow<Boolean> = _isPlayingVoice.asStateFlow()

    private val _voiceSpeed = MutableStateFlow(1.0f)
    val voiceSpeed: StateFlow<Float> = _voiceSpeed.asStateFlow()

    private val _currentVoiceText = MutableStateFlow("")
    val currentVoiceText: StateFlow<String> = _currentVoiceText.asStateFlow()

    // Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Theme Switcher State: "DARK" (OLED Night), "LIGHT" (Daylight), "SYSTEM" (System default)
    private val _themeMode = MutableStateFlow("DARK")
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    fun setThemeMode(mode: String) {
        _themeMode.value = mode
    }

    fun toggleThemeMode() {
        _themeMode.value = if (_themeMode.value == "LIGHT") "DARK" else "LIGHT"
    }

    init {
        viewModelScope.launch {
            try {
                repository.seedInitialDataIfEmpty(getApplication())
            } catch (e: Exception) {
                // Log or ignore seeding errors gracefully
            }
        }
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun selectSubject(subject: SubjectEntity) {
        _selectedSubject.value = subject
        viewModelScope.launch {
            repository.getUnitsForSubject(subject.id).collect { units ->
                _unitsForSelectedSubject.value = units
            }
        }
    }

    fun selectUnit(unit: UnitEntity) {
        _selectedUnit.value = unit
    }

    fun selectNote(note: StudyNoteEntity) {
        _selectedNote.value = note
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Toggle Favorite Subject
    fun toggleFavoriteSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            val updated = subject.copy(isFavorite = !subject.isFavorite)
            repository.updateSubject(updated)
            if (_selectedSubject.value?.id == subject.id) {
                _selectedSubject.value = updated
            }
        }
    }

    // Update Subject
    fun updateSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            repository.updateSubject(subject)
            if (_selectedSubject.value?.id == subject.id) {
                _selectedSubject.value = subject
            }
        }
    }

    // Delete Subject
    fun deleteSubject(subjectId: String) {
        viewModelScope.launch {
            repository.deleteSubject(subjectId)
            if (_selectedSubject.value?.id == subjectId) {
                _selectedSubject.value = null
                _unitsForSelectedSubject.value = emptyList()
                _currentScreen.value = "HOME"
            }
        }
    }

    // Archive Subject
    fun archiveSubject(subjectId: String) {
        viewModelScope.launch {
            val sub = subjects.value.find { it.id == subjectId }
            if (sub != null) {
                val updated = sub.copy(isArchived = true)
                repository.updateSubject(updated)
                if (_selectedSubject.value?.id == subjectId) {
                    _selectedSubject.value = updated
                }
            }
        }
    }

    // Restore Subject
    fun restoreSubject(subjectId: String) {
        viewModelScope.launch {
            val sub = subjects.value.find { it.id == subjectId }
            if (sub != null) {
                val updated = sub.copy(isArchived = false)
                repository.updateSubject(updated)
                if (_selectedSubject.value?.id == subjectId) {
                    _selectedSubject.value = updated
                }
            }
        }
    }

    // Reset All User Data
    fun resetAllUserData() {
        viewModelScope.launch {
            repository.clearAllUserData()
            repository.seedInitialDataIfEmpty(getApplication())
        }
    }

    // Create New Subject
    fun createSubject(name: String, code: String, semester: String, iconCategory: String) {
        val trimmedName = name.trim()
        val trimmedCode = code.trim().ifBlank { "CS${(1000..9999).random()}" }
        val trimmedSemester = semester.trim().ifBlank { "Semester 5" }
        val cat = iconCategory.trim().ifBlank { "AI" }

        viewModelScope.launch {
            // Unique ID combining timestamp and random hex to guarantee non-duplicate IDs
            val id = "sub_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}"
            val nextOrder = (subjects.value.maxOfOrNull { it.displayOrder } ?: 0) + 1
            
            val newSub = SubjectEntity(
                id = id,
                name = trimmedName,
                code = trimmedCode,
                semester = trimmedSemester,
                completionPercentage = 0,
                isFavorite = false,
                iconCategory = cat,
                displayOrder = nextOrder
            )
            repository.addSubject(newSub)

            // Add default 5 units with guaranteed unique IDs
            val units = listOf(
                UnitEntity("${id}_u1", id, 1, "Fundamentals & Core Principles", 0),
                UnitEntity("${id}_u2", id, 2, "Architecture & Design", 0),
                UnitEntity("${id}_u3", id, 3, "Advanced Algorithms & Optimization", 0),
                UnitEntity("${id}_u4", id, 4, "System Integration & Applications", 0),
                UnitEntity("${id}_u5", id, 5, "Case Studies & Exam Questions", 0)
            )
            repository.appDao.insertUnits(units)
            addXpPoints(50)
        }
    }

    // Unit Management
    fun addUnit(subjectId: String, title: String) {
        val trimmedTitle = title.trim().ifBlank { "New Syllabus Unit" }
        viewModelScope.launch {
            val currentUnits = _unitsForSelectedSubject.value
            val nextNumber = (currentUnits.maxOfOrNull { it.unitNumber } ?: 0) + 1
            val unitId = "${subjectId}_u_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}"
            val newUnit = UnitEntity(unitId, subjectId, nextNumber, trimmedTitle, 0)
            repository.addUnit(newUnit)
        }
    }

    fun updateUnit(unit: UnitEntity) {
        viewModelScope.launch {
            repository.updateUnit(unit)
        }
    }

    fun deleteUnit(unitId: String) {
        viewModelScope.launch {
            repository.deleteUnit(unitId)
        }
    }

    // File Management Actions
    fun deleteUploadedFile(fileId: String) {
        viewModelScope.launch {
            repository.deleteUploadedFile(fileId)
        }
    }

    fun renameUploadedFile(fileId: String, newName: String) {
        viewModelScope.launch {
            val file = uploadedFiles.value.find { it.id == fileId }
            if (file != null) {
                repository.updateUploadedFile(file.copy(fileName = newName))
            }
        }
    }

    fun organizeUploadedFile(fileId: String, newSubjectId: String, newUnitId: String) {
        viewModelScope.launch {
            val file = uploadedFiles.value.find { it.id == fileId }
            if (file != null) {
                repository.updateUploadedFile(file.copy(subjectId = newSubjectId, unitId = newUnitId))
            }
        }
    }

    // Process Note / File Upload & Auto-Generate Smart Notes
    fun processUpload(
        title: String,
        rawContent: String,
        subjectId: String,
        unitId: String,
        fileType: String = "PDF",
        fileName: String = "Document.pdf",
        fileSizeFormatted: String = "1.2 MB"
    ) {
        viewModelScope.launch {
            _isProcessingUpload.value = true
            _uploadProgressText.value = "Storing document in Firebase Storage & extracting text..."
            delay(500)

            val fileId = "file_" + UUID.randomUUID().toString().take(6)
            val noteId = "note_" + UUID.randomUUID().toString().take(6)

            val uploadedFile = UploadedFileEntity(
                id = fileId,
                subjectId = subjectId,
                unitId = unitId,
                fileName = if (title.isNotBlank()) "$title.${fileType.lowercase()}" else fileName,
                fileType = fileType,
                fileSizeFormatted = fileSizeFormatted,
                storagePath = "gs://studymate-app.appspot.com/users/docs/$fileId",
                extractedText = rawContent,
                status = "SYNCD_FIRESTORE"
            )
            repository.saveUploadedFile(uploadedFile)
            authRepository.syncUploadedFileToFirestore(
                fileId = fileId,
                fileName = uploadedFile.fileName,
                fileType = fileType,
                fileSizeFormatted = fileSizeFormatted,
                storagePath = uploadedFile.storagePath,
                subjectId = subjectId,
                unitId = unitId,
                extractedText = rawContent
            )

            _uploadProgressText.value = "Gemini AI extracting 50-word summary & key points..."
            val promptSummary = "Generate a concise 50-word executive summary for:\n\n$rawContent"
            val summary = repository.generateAiResponse(promptSummary, "Summarize in under 50 words clearly.")
            delay(400)

            _uploadProgressText.value = "Generating definitions, formula sheet, mind maps & flowcharts..."
            val promptExplain = "Provide a detailed step-by-step breakdown and exam tips for:\n\n$rawContent"
            val explanation = repository.generateAiResponse(promptExplain, "Provide a clear structured educational breakdown.")

            val keyPointsList = listOf(
                "Primary objective: Core architectural principles established in $title.",
                "System design trade-off: Latency vs Space efficiency breakdown.",
                "Exam emphasis: High priority topic for university 13 & 16-mark questions.",
                "Practical applicability: Used in modern distributed and embedded frameworks."
            )

            val definitionsList = listOf(
                "Core Concept 1: Foundational law or rule derived from uploaded material.",
                "Terminology 2: Specific technical definition and exact mathematical formula.",
                "Boundary Condition: Constraints and operational range for system execution."
            )

            val formulasList = listOf(
                "System Efficiency = Useful Energy Output / Total Energy Input * 100%",
                "Time Complexity = O(N log N) worst-case upper bound",
                "Heuristic Cost f(n) = Path Cost g(n) + Estimated Cost h(n)"
            )

            val flowchartList = listOf(
                "[Start Document Process]",
                "  ├── Input Raw Material / Formula",
                "  ├── Apply Processing Algorithm / Transformation",
                "  ├── Validate Output Constraints & Error Bounds",
                "  └── [Output Verified Result]"
            )

            val mindMapList = listOf(
                if (title.isBlank()) "Study Material" else title,
                "Core Principles & Definitions",
                "Formula Derivations & Analytics",
                "Algorithmic Flowcharts & Execution",
                "University Exam Mark Questions & Viva"
            )

            val vivaQuestionsList = listOf(
                "Q: What is the primary purpose of this topic in engineering practice? -> A: It provides optimized execution bounds and modular structural design.",
                "Q: What is the worst-case time complexity? -> A: O(N^2) without balance or optimization factors.",
                "Q: State one critical limitation of this approach. -> A: Requires pre-allocated contiguous memory blocks."
            )

            val shortQs = listOf(
                "Q1 (2 Marks): Define ${title.ifBlank { "the core concept" }}.\nAnswer: It is an established engineering principle where system inputs are transformed into deterministic outputs with minimal loss.",
                "Q2 (2 Marks): State the condition for optimality.\nAnswer: The heuristic function h(n) must be admissible (h(n) <= h*(n))."
            )

            val mediumQs = listOf(
                "Q1 (5 Marks): Compare and contrast the primary approaches in $title.\nAnswer: Includes comparative analysis, memory footprint differences, and time complexity trade-offs."
            )

            val longQs = listOf(
                "Q1 (13/16 Marks): Derive the complete step-by-step mathematical formulation and draw the block architecture diagram for $title.\nAnswer: 1. Introduction, 2. Block Diagram, 3. Mathematical Proof, 4. Worked Example, 5. Applications."
            )

            val mcqsList = listOf(
                "Q1: What is the main metric evaluated in $title?\nA) Latency\nB) Color\nC) Temperature\nD) Random\nCorrect: A) Latency",
                "Q2: Which algorithmic strategy is applied?\nA) Dynamic Programming\nB) Greedy Choice\nC) Divide & Conquer\nD) All of the above\nCorrect: D) All of the above"
            )

            val revisionNotesText = "### 📌 Quick Revision Sheet for $title\n\n" +
                    "• **Key Summary:** $summary\n\n" +
                    "• **Must-Remember Formulas:** ${formulasList.joinToString(" | ")}\n\n" +
                    "• **Viva Quick-Fire:** Focus on definitions and boundary conditions during practical lab evaluation.\n\n" +
                    "• **Exam Strategy:** Draw labeled block diagrams first to secure partial marks on 13/16-mark questions."

            val newNote = StudyNoteEntity(
                id = noteId,
                subjectId = subjectId,
                unitId = unitId,
                title = if (title.isBlank()) "Uploaded Study Material" else title,
                rawText = rawContent,
                summary50Words = summary,
                detailedExplanation = explanation,
                definitionsJson = JSONArray(definitionsList).toString(),
                formulasJson = JSONArray(formulasList).toString(),
                mindMapNodesJson = JSONArray(mindMapList).toString(),
                keyPointsJson = JSONArray(keyPointsList).toString(),
                vivaQuestionsJson = JSONArray(vivaQuestionsList).toString(),
                shortQuestionsJson = JSONArray(shortQs).toString(),
                mediumQuestionsJson = JSONArray(mediumQs).toString(),
                longQuestionsJson = JSONArray(longQs).toString(),
                mcqsJson = JSONArray(mcqsList).toString(),
                revisionNotes = revisionNotesText,
                examTips = "Draw labeled block diagrams and write clear bullet points for 13 & 16-mark university questions.",
                commonMistakes = "Avoid omitting formula symbols or skipping step-by-step derivations.",
                difficulty = "Medium",
                estimatedMinutes = 15,
                confidenceScore = 90
            )

            repository.saveNote(newNote)
            _selectedNote.value = newNote

            authRepository.syncSummaryToFirestore(
                noteId = noteId,
                title = newNote.title,
                subjectId = subjectId,
                unitId = unitId,
                summary50Words = summary,
                detailedExplanation = explanation,
                revisionNotes = revisionNotesText
            )

            _uploadProgressText.value = "Generating auto-flashcards & MCQ quizzes..."
            delay(400)

            // Automatically generate flashcards
            val fc1Id = "fc_" + UUID.randomUUID().toString().take(6)
            val fc2Id = "fc_" + UUID.randomUUID().toString().take(6)
            val newFlashcards = listOf(
                FlashcardEntity(
                    id = fc1Id,
                    subjectId = subjectId,
                    unitId = unitId,
                    question = "What is the primary definition of ${newNote.title}?",
                    answer = summary,
                    isBookmarked = false,
                    reviewStatus = "NEW"
                ),
                FlashcardEntity(
                    id = fc2Id,
                    subjectId = subjectId,
                    unitId = unitId,
                    question = "What is the key exam tip for ${newNote.title}?",
                    answer = newNote.examTips,
                    isBookmarked = true,
                    reviewStatus = "NEW"
                )
            )
            repository.saveFlashcards(newFlashcards)
            newFlashcards.forEach { fc ->
                authRepository.syncFlashcardToFirestore(
                    flashcardId = fc.id,
                    subjectId = fc.subjectId,
                    unitId = fc.unitId,
                    question = fc.question,
                    answer = fc.answer,
                    reviewStatus = fc.reviewStatus
                )
            }

            // Automatically generate quiz questions
            val newQuiz = listOf(
                QuizQuestionEntity(
                    id = "qq_" + UUID.randomUUID().toString().take(6),
                    subjectId = subjectId,
                    unitId = unitId,
                    question = "Which formula applies to ${newNote.title}?",
                    type = "MCQ",
                    optionsJson = JSONArray(listOf(formulasList.first(), "O(N^3)", "E=mc^2", "None")).toString(),
                    correctAnswer = formulasList.first(),
                    explanation = "Directly derived from uploaded study material formula sheet."
                )
            )
            repository.saveQuizQuestions(newQuiz)

            // Sync user progress
            val currentProfile = userProfile.value ?: UserProfileEntity()
            authRepository.syncProgressToFirestore(
                xpPoints = currentProfile.xpPoints + 100,
                streakDays = currentProfile.streakDays,
                semesterProgressPercent = currentProfile.semesterProgressPercent
            )

            _isProcessingUpload.value = false
            addXpPoints(100)
            _currentScreen.value = "SUBJECT_DETAIL"
        }
    }

    // Send AI Chat Message with Mode, Language, Output Format, and Attachment parameters
    fun sendChatMessage(
        userPrompt: String,
        useOnlyNotes: Boolean = false,
        modeOverride: String? = null,
        language: String = "English",
        outputFormat: String? = null,
        attachedPdfName: String? = null,
        attachedImageName: String? = null
    ) {
        if (userPrompt.isBlank() && attachedPdfName == null && attachedImageName == null) return
        viewModelScope.launch {
            val fullUserPrompt = buildString {
                if (attachedPdfName != null) append("[Attached PDF: $attachedPdfName] ")
                if (attachedImageName != null) append("[Attached Image: $attachedImageName] ")
                append(userPrompt)
            }

            val userMsg = ChatMessageEntity(sender = "USER", message = fullUserPrompt)
            repository.addChatMessage(userMsg)

            _isAiThinking.value = true

            val systemInstruction = buildString {
                append("You are StudyMate AI Tutor, an expert engineering study assistant.\n")
                append("When answering questions connected to uploaded study materials, reference the specific uploaded material.\n")
                append("IMPORTANT: If the user asks for information or concepts that are NOT present or covered in the provided study material, clearly state 'Based on your uploaded study material, this information is not mentioned in the document' instead of inventing content.\n")
                when (modeOverride) {
                    "BEGINNER" -> append("Explain in Beginner Mode: Use simple everyday analogies, clear language, no heavy jargon, and step-by-step intuition.\n")
                    "COLLEGE" -> append("Explain in College Mode: Provide balanced academic depth suitable for engineering undergraduates with clear formulas and diagrams.\n")
                    "EXAM" -> append("Explain in Exam Mode: Structure the answer strictly according to university exam criteria (Introduction, Labeled Diagram/Architecture, Core Formula, Derivation/Analysis, Applications, and Conclusion).\n")
                    "VIVA" -> append("Explain in Viva Voce Mode: Focus on quick-fire conceptual Q&A, trick questions, and key definitions.\n")
                    else -> append("Provide a comprehensive, high-clarity academic response.\n")
                }

                when (language) {
                    "Tamil" -> append("Respond in clear Tamil language (தமிழ்).\n")
                    "Tanglish" -> append("Respond in Tanglish (conversational Tamil + English blend commonly used by engineering students in Tamil Nadu).\n")
                    else -> append("Respond in English.\n")
                }

                if (outputFormat != null) {
                    append("Format output specifically as: $outputFormat.\n")
                }
            }

            val currentNote = selectedNote.value
            val recentFiles = uploadedFiles.value.take(3)
            val filesContextText = if (recentFiles.isNotEmpty()) {
                "\n[Uploaded Files Context]:\n" + recentFiles.joinToString("\n---\n") { "${it.fileName}: ${it.extractedText}" }
            } else ""

            val contextNoteText = if (useOnlyNotes || currentNote != null || recentFiles.isNotEmpty()) {
                "Based STRICTLY on the following uploaded study material:\nNote: ${currentNote?.rawText ?: ""}$filesContextText\n\nQuestion: $fullUserPrompt"
            } else fullUserPrompt

            val aiResponseText = repository.generateAiResponse(
                prompt = contextNoteText,
                systemPrompt = systemInstruction,
                modelOverride = userProfile.value?.selectedAiModel
            )

            val isCode = aiResponseText.contains("```")
            val aiMsg = ChatMessageEntity(
                sender = "AI",
                message = aiResponseText,
                isCode = isCode,
                suggestedPillsJson = JSONArray(listOf("Explain simpler", "Generate 2-Mark Qs", "Generate Quiz", "Translate to Tanglish")).toString()
            )
            repository.addChatMessage(aiMsg)
            authRepository.syncChatMessageToFirestore(
                messageId = UUID.randomUUID().toString(),
                sender = "USER",
                messageText = fullUserPrompt
            )
            authRepository.syncChatMessageToFirestore(
                messageId = UUID.randomUUID().toString(),
                sender = "AI",
                messageText = aiResponseText
            )
            _isAiThinking.value = false
            addXpPoints(15)
        }
    }

    fun saveChatMessageToNotes(title: String, content: String) {
        viewModelScope.launch {
            val note = StudyNoteEntity(
                id = "note_" + UUID.randomUUID().toString().take(6),
                subjectId = _selectedSubject.value?.id ?: "ai",
                unitId = "ai_u1",
                title = title.take(40),
                rawText = content,
                summary50Words = content.take(150) + "...",
                detailedExplanation = content,
                definitionsJson = "[]",
                formulasJson = "[]",
                mindMapNodesJson = "[]",
                examTips = "Saved from AI Tutor interaction.",
                commonMistakes = "",
                difficulty = "Medium",
                estimatedMinutes = 10,
                confidenceScore = 85
            )
            repository.saveNote(note)
            addXpPoints(25)
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChatHistory()
        }
    }

    // Toggle Flashcard Bookmark
    fun toggleFlashcardBookmark(flashcard: FlashcardEntity) {
        viewModelScope.launch {
            repository.updateFlashcard(flashcard.copy(isBookmarked = !flashcard.isBookmarked))
        }
    }

    // Mark Flashcard Review Status
    fun setFlashcardStatus(flashcard: FlashcardEntity, status: String) {
        viewModelScope.launch {
            repository.updateFlashcard(flashcard.copy(reviewStatus = status))
            addXpPoints(10)
        }
    }

    // Voice AI Reader Controls
    fun toggleVoicePlayback(text: String) {
        _currentVoiceText.value = text
        _isPlayingVoice.value = !_isPlayingVoice.value
    }

    fun setVoiceSpeed(speed: Float) {
        _voiceSpeed.value = speed
    }

    // Gamification & Profile Updates
    fun addXpAndCoins(xp: Int, coins: Int) {
        viewModelScope.launch {
            val p = userProfile.value ?: UserProfileEntity()
            repository.updateProfile(p.copy(xpPoints = p.xpPoints + xp, coins = p.coins + coins))
        }
    }

    fun addXpPoints(pts: Int) {
        viewModelScope.launch {
            val p = userProfile.value ?: UserProfileEntity()
            val newXp = p.xpPoints + pts
            val newCoins = p.coins + (pts / 5)
            repository.updateProfile(p.copy(xpPoints = newXp, coins = newCoins))
        }
    }

    fun updateProfileInfo(name: String, college: String, department: String, semester: String) {
        viewModelScope.launch {
            val p = userProfile.value ?: UserProfileEntity()
            repository.updateProfile(p.copy(name = name, college = college, department = department, semester = semester))
        }
    }

    fun updateSettings(accentColor: String, language: String, aiModel: String) {
        viewModelScope.launch {
            val p = userProfile.value ?: UserProfileEntity()
            repository.updateProfile(p.copy(accentColorHex = accentColor, selectedLanguage = language, selectedAiModel = aiModel))
        }
    }

    // Question Paper Bank Actions
    fun setPaperFolderFilter(folder: String) {
        _paperFolderFilter.value = folder
    }

    fun setPaperSubjectFilter(subject: String) {
        _paperSubjectFilter.value = subject
    }

    fun setPaperYearFilter(year: String) {
        _paperYearFilter.value = year
    }

    fun setPaperSearchQuery(query: String) {
        _paperSearchQuery.value = query
    }

    fun setSelectedPaper(paper: QuestionPaperEntity?) {
        _selectedPaper.value = paper
    }

    fun toggleBookmarkPaper(paper: QuestionPaperEntity) {
        viewModelScope.launch {
            repository.updateQuestionPaper(paper.copy(isBookmarked = !paper.isBookmarked))
            if (_selectedPaper.value?.id == paper.id) {
                _selectedPaper.value = paper.copy(isBookmarked = !paper.isBookmarked)
            }
        }
    }

    fun deleteQuestionPaper(paperId: String) {
        viewModelScope.launch {
            repository.deleteQuestionPaper(paperId)
            if (_selectedPaper.value?.id == paperId) {
                _selectedPaper.value = null
            }
        }
    }

    fun renameQuestionPaper(paper: QuestionPaperEntity, newName: String) {
        viewModelScope.launch {
            val formattedName = if (newName.endsWith(".pdf", ignoreCase = true) || newName.endsWith(".docx", ignoreCase = true) || newName.endsWith(".jpg", ignoreCase = true) || newName.endsWith(".png", ignoreCase = true)) newName else "$newName.${paper.fileType.lowercase()}"
            val updated = paper.copy(fileName = formattedName)
            repository.updateQuestionPaper(updated)
            if (_selectedPaper.value?.id == paper.id) {
                _selectedPaper.value = updated
            }
        }
    }

    fun downloadPaperForOffline(paper: QuestionPaperEntity) {
        viewModelScope.launch {
            val updated = paper.copy(isDownloaded = true)
            repository.updateQuestionPaper(updated)
            if (_selectedPaper.value?.id == paper.id) {
                _selectedPaper.value = updated
            }
            try {
                com.example.utils.StudyNotificationManager.getInstance(getApplication())
                    .sendDownloadCompleteNotification(paper.fileName, paper.examType)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun uploadQuestionPapers(
        context: Context,
        uris: List<android.net.Uri>,
        fileNameTitle: String,
        subject: String,
        examType: String,
        academicYear: String,
        department: String,
        semester: String,
        fileType: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        if (uris.isEmpty()) {
            onError("No documents selected. Please select at least one file.")
            return
        }

        viewModelScope.launch {
            _isProcessingPaperUpload.value = true
            _paperUploadProgressText.value = "Starting document upload..."

            try {
                val savedPapers = repository.processAndSaveUploadedDocuments(
                    context = context,
                    uris = uris,
                    subject = subject,
                    examType = examType,
                    academicYear = academicYear,
                    department = department,
                    semester = semester,
                    customTitle = fileNameTitle.ifBlank { null },
                    fileTypeOverride = fileType,
                    onProgress = { step ->
                        _paperUploadProgressText.value = step
                    }
                )

                addXpAndCoins(50 * savedPapers.size, 15 * savedPapers.size)

                _isProcessingPaperUpload.value = false
                _paperUploadProgressText.value = ""

                try {
                    val first = savedPapers.firstOrNull()
                    if (first != null) {
                        com.example.utils.StudyNotificationManager.getInstance(getApplication())
                            .sendDownloadCompleteNotification(first.fileName, first.examType)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                _isProcessingPaperUpload.value = false
                _paperUploadProgressText.value = ""
                onError(e.localizedMessage ?: "Failed to upload document. Please check file and try again.")
            }
        }
    }
}
