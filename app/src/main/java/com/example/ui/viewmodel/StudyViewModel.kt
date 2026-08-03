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
            authRepository.syncSubjectToFirestore(
                subjectId = subject.id,
                name = subject.name,
                code = subject.code,
                semester = subject.semester,
                completion = subject.completionPercentage
            )
            if (_selectedSubject.value?.id == subject.id) {
                _selectedSubject.value = subject
            }
        }
    }

    // Delete Subject
    fun deleteSubject(subjectId: String) {
        viewModelScope.launch {
            repository.deleteSubject(subjectId)
            authRepository.deleteSubjectFromFirestore(subjectId)
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
            authRepository.syncSubjectToFirestore(
                subjectId = newSub.id,
                name = newSub.name,
                code = newSub.code,
                semester = newSub.semester,
                completion = newSub.completionPercentage
            )

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

            val keyPointsPrompt = "Extract 4 key bullet points directly from the following study material:\n\n$rawContent"
            val keyPointsResp = repository.generateAiResponse(keyPointsPrompt, "Return 4 concise bullet points.")
            val keyPointsList = keyPointsResp.lines().filter { it.isNotBlank() }.take(6)
                .ifEmpty { listOf("Key takeaway from $title: " + summary.take(100)) }

            val defsPrompt = "Extract key definitions and technical terms from this content:\n\n$rawContent"
            val defsResp = repository.generateAiResponse(defsPrompt, "Return terms and definitions in format 'Term: Definition'.")
            val definitionsList = defsResp.lines().filter { it.isNotBlank() }.take(5)
                .ifEmpty { listOf("Core Concept: $title") }

            val formulasPrompt = "Extract important formulas, equations, or laws from this content (if none, state core principles):\n\n$rawContent"
            val formulasResp = repository.generateAiResponse(formulasPrompt, "Return formulas line by line.")
            val formulasList = formulasResp.lines().filter { it.isNotBlank() }.take(5)
                .ifEmpty { listOf("Core Principle: Essential concepts from $title.") }

            val flowchartList = listOf(
                "[Start: $title]",
                "  ├── Input Processing",
                "  ├── Core Transformation",
                "  └── [Output Result]"
            )

            val mindMapList = listOf(
                if (title.isBlank()) "Study Material" else title,
                "Core Principles",
                "Key Formulas & Definitions",
                "Process Flowchart",
                "Exam Questions & Viva"
            )

            val vivaPrompt = "Generate 3 viva voce interview questions and answers based on this text:\n\n$rawContent"
            val vivaResp = repository.generateAiResponse(vivaPrompt, "Format as Q: ... -> A: ... line by line.")
            val vivaQuestionsList = vivaResp.lines().filter { it.isNotBlank() }.take(5)
                .ifEmpty { listOf("Q: Explain $title -> A: " + summary.take(120)) }

            val shortQs = listOf("2 Marks: Explain the fundamental concept of $title.")
            val mediumQs = listOf("5 Marks: Describe the architecture and key features of $title.")
            val longQs = listOf("13/16 Marks: Detailed step-by-step analysis and derivation for $title based on uploaded study material.")

            val mcqsPrompt = "Generate 2 multiple choice questions (with options A, B, C, D and correct answer) from:\n\n$rawContent"
            val mcqsResp = repository.generateAiResponse(mcqsPrompt, "Return clear multiple choice questions with correct answer.")
            val mcqsList = mcqsResp.lines().filter { it.isNotBlank() }.take(8)
                .ifEmpty { listOf("Q1: What is the main topic of this material?\nA) $title\nB) Unknown\nCorrect: A") }

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

            // 1. Capture previous chat history BEFORE adding current message for multi-turn memory
            val recentHistory = chatMessages.value.takeLast(8)
            val historyText = if (recentHistory.isNotEmpty()) {
                "[CONVERSATION HISTORY]:\n" +
                recentHistory.joinToString("\n") { msg ->
                    val senderLabel = if (msg.sender == "USER") "User" else "AI Tutor (Ben)"
                    "$senderLabel: ${msg.message}"
                } + "\n\n"
            } else ""

            val userMsg = ChatMessageEntity(sender = "USER", message = fullUserPrompt)
            repository.addChatMessage(userMsg)

            _isAiThinking.value = true

            val systemInstruction = buildString {
                append("You are Ben, an intelligent, empathetic, and conversational AI Study Tutor (like ChatGPT) in StudyMate AI.\n")
                append("CORE RULES:\n")
                append("1. Answer in a natural, conversational, and engaging style like ChatGPT. Do not use robotic boilerplate, fake placeholders, or repetitive intros.\n")
                append("2. Remember and maintain full conversation history. Use the conversation context to answer follow-up questions (e.g., 'give an example', 'explain step 2', 'summarize that').\n")
                append("3. Document Integration:\n")
                append("   - Search [ATTACHED & UPLOADED DOCUMENTS] first.\n")
                append("   - If the answer is in the uploaded documents, answer directly from them and cite the document title/filename (e.g. 'Source: Unit3_Notes.pdf').\n")
                append("   - If the answer is NOT in uploaded documents, state clearly: 'Not found in your uploaded notes. Here is the answer from general AI textbook knowledge:' and provide the complete answer.\n")
                append("4. Honesty & Accuracy: If you do not know an answer or lack sufficient information, say so honestly instead of making up facts.\n")
                append("5. Every answer should feel tailored specifically to the user's question.\n")

                when (modeOverride) {
                    "BEGINNER" -> append("6. Style Mode: Beginner. Use simple real-world analogies, step-by-step intuitive logic, no jargon.\n")
                    "COLLEGE" -> append("6. Style Mode: College. Provide deep academic depth, formulas, architectural diagrams, and engineering trade-offs.\n")
                    "EXAM" -> append("6. Style Mode: Exam. Structure strictly according to university exam criteria (Title, Introduction, Labeled Diagram/Architecture, Core Formula, Derivation, Applications, and Conclusion).\n")
                    "VIVA" -> append("6. Style Mode: Viva Voce. Focus on quick-fire Q&A, trick questions, and key definitions.\n")
                    else -> append("6. Style Mode: Balanced, high-clarity academic response.\n")
                }

                when (language) {
                    "Tamil" -> append("7. Language: Respond in clear Tamil (தமிழ்).\n")
                    "Tanglish" -> append("7. Language: Respond in Tanglish (conversational blend of Tamil + English used naturally by students).\n")
                    else -> append("7. Language: Respond in English.\n")
                }

                if (!outputFormat.isNullOrBlank()) {
                    append("8. Required Output Format: Structure response as $outputFormat.\n")
                }
            }

            val queryKeywords = userPrompt.lowercase().split(Regex("\\W+")).filter { it.length > 2 }
            val matchingDocs = mutableListOf<String>()

            val isFollowUp = userPrompt.length < 30 || queryKeywords.any { kw ->
                listOf("example", "more", "explain", "why", "how", "details", "summary", "this", "that", "code", "diagram").contains(kw)
            }

            // 1. Search uploaded files
            uploadedFiles.value.forEach { file ->
                val fileTextLower = file.extractedText.lowercase()
                val fileNameLower = file.fileName.lowercase()
                val isAttached = attachedPdfName != null && fileNameLower.contains(attachedPdfName.lowercase())
                val matchesKeyword = queryKeywords.any { kw -> fileNameLower.contains(kw) || fileTextLower.contains(kw) }
                if (isAttached || matchesKeyword || isFollowUp) {
                    val snippet = if (file.extractedText.length > 3000) file.extractedText.take(3000) + "..." else file.extractedText
                    if (snippet.isNotBlank()) {
                        matchingDocs.add("Document [File: ${file.fileName} (${file.fileType})]:\n$snippet")
                    }
                }
            }

            // 2. Search study notes
            notes.value.forEach { note ->
                val titleLower = note.title.lowercase()
                val textLower = note.rawText.lowercase()
                val matchesKeyword = queryKeywords.any { kw -> titleLower.contains(kw) || textLower.contains(kw) }
                val isSelected = selectedNote.value?.id == note.id
                if (isSelected || matchesKeyword || isFollowUp) {
                    val content = note.rawText.ifBlank { note.detailedExplanation }
                    val snippet = if (content.length > 3000) content.take(3000) + "..." else content
                    if (snippet.isNotBlank()) {
                        matchingDocs.add("Document [Note: ${note.title}]:\n$snippet")
                    }
                }
            }

            // 3. Search question papers
            allQuestionPapers.value.forEach { paper ->
                val fileNameLower = paper.fileName.lowercase()
                val questionsLower = paper.extractedQuestionsJson.lowercase()
                val matchesKeyword = queryKeywords.any { kw -> fileNameLower.contains(kw) || questionsLower.contains(kw) }
                if (matchesKeyword || isFollowUp) {
                    val snippet = if (paper.extractedQuestionsJson.length > 3000) paper.extractedQuestionsJson.take(3000) + "..." else paper.extractedQuestionsJson
                    if (snippet.isNotBlank()) {
                        matchingDocs.add("Document [Question Paper: ${paper.fileName}]:\n$snippet")
                    }
                }
            }

            val documentContext = if (matchingDocs.isNotEmpty()) {
                "[ATTACHED & UPLOADED DOCUMENTS]:\n\n" +
                matchingDocs.distinct().take(4).joinToString("\n\n---\n\n") + "\n\n"
            } else if (useOnlyNotes && (uploadedFiles.value.isNotEmpty() || notes.value.isNotEmpty())) {
                val fallbackDocs = (uploadedFiles.value.map { "File: ${it.fileName}\n${it.extractedText}" } +
                        notes.value.map { "Note: ${it.title}\n${it.rawText}" }).take(4)
                "[STUDY DOCUMENTS]:\n\n" + fallbackDocs.joinToString("\n\n---\n\n") + "\n\n"
            } else ""

            val fullPromptToGemini = buildString {
                if (historyText.isNotBlank()) append(historyText)
                if (documentContext.isNotBlank()) append(documentContext)
                append("[CURRENT USER QUESTION]:\n").append(fullUserPrompt)
            }

            val aiResponseText = repository.generateAiResponse(
                prompt = fullPromptToGemini,
                systemPrompt = systemInstruction,
                modelOverride = userProfile.value?.selectedAiModel
            )

            val isCode = aiResponseText.contains("```")
            val aiMsg = ChatMessageEntity(
                sender = "AI",
                message = aiResponseText,
                isCode = isCode,
                suggestedPillsJson = JSONArray(listOf("Explain simpler", "Give an example", "Generate Quiz", "Translate to Tanglish")).toString()
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
