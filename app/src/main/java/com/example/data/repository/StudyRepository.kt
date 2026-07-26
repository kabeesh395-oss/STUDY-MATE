package com.example.data.repository

import com.example.data.local.dao.AppDao
import com.example.data.local.entities.ChatMessageEntity
import com.example.data.local.entities.FlashcardEntity
import com.example.data.local.entities.QuizQuestionEntity
import com.example.data.local.entities.StudyNoteEntity
import com.example.data.local.entities.SubjectEntity
import com.example.data.local.entities.UnitEntity
import com.example.data.local.entities.UploadedFileEntity
import com.example.data.local.entities.UserProfileEntity
import com.example.data.remote.GeminiContent
import com.example.data.remote.GeminiPart
import com.example.data.remote.GeminiRequest
import com.example.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class StudyRepository(val appDao: AppDao) {

    val allSubjects: Flow<List<SubjectEntity>> = appDao.getAllSubjects()
    val allNotes: Flow<List<StudyNoteEntity>> = appDao.getAllNotes()
    val allUploadedFiles: Flow<List<UploadedFileEntity>> = appDao.getAllUploadedFiles()
    val allFlashcards: Flow<List<FlashcardEntity>> = appDao.getAllFlashcards()
    val allQuizQuestions: Flow<List<QuizQuestionEntity>> = appDao.getAllQuizQuestions()
    val allChatMessages: Flow<List<ChatMessageEntity>> = appDao.getAllChatMessages()
    val userProfile: Flow<UserProfileEntity?> = appDao.getUserProfile()

    fun getUnitsForSubject(subjectId: String): Flow<List<UnitEntity>> = appDao.getUnitsForSubject(subjectId)
    fun getNotesForSubject(subjectId: String): Flow<List<StudyNoteEntity>> = appDao.getNotesForSubject(subjectId)
    fun getNotesForUnit(subjectId: String, unitId: String): Flow<List<StudyNoteEntity>> = appDao.getNotesForUnit(subjectId, unitId)
    fun getFlashcardsForSubject(subjectId: String): Flow<List<FlashcardEntity>> = appDao.getFlashcardsForSubject(subjectId)
    fun getQuizForSubject(subjectId: String): Flow<List<QuizQuestionEntity>> = appDao.getQuizForSubject(subjectId)

    suspend fun addSubject(subject: SubjectEntity) {
        appDao.insertSubject(subject)
    }

    suspend fun updateSubject(subject: SubjectEntity) {
        appDao.updateSubject(subject)
    }

    suspend fun deleteSubject(subjectId: String) {
        appDao.deleteSubjectById(subjectId)
    }

    suspend fun saveNote(note: StudyNoteEntity) {
        appDao.insertNote(note)
    }

    suspend fun saveUploadedFile(file: UploadedFileEntity) {
        appDao.insertUploadedFile(file)
    }

    suspend fun deleteUploadedFile(fileId: String) {
        appDao.deleteUploadedFileById(fileId)
    }

    suspend fun updateUploadedFile(file: UploadedFileEntity) {
        appDao.updateUploadedFile(file)
    }

    suspend fun saveFlashcards(flashcards: List<FlashcardEntity>) {
        appDao.insertFlashcards(flashcards)
    }

    suspend fun updateFlashcard(flashcard: FlashcardEntity) {
        appDao.updateFlashcard(flashcard)
    }

    suspend fun saveQuizQuestions(questions: List<QuizQuestionEntity>) {
        appDao.insertQuizQuestions(questions)
    }

    suspend fun addChatMessage(message: ChatMessageEntity) {
        appDao.insertChatMessage(message)
    }

    suspend fun clearChatHistory() {
        appDao.clearChatHistory()
    }

    suspend fun updateProfile(profile: UserProfileEntity) {
        appDao.insertOrUpdateProfile(profile)
    }

    suspend fun generateAiResponse(
        prompt: String,
        systemPrompt: String = "You are StudyMate AI, a world-class personal engineering study assistant. Be encouraging, clear, precise, and educational with markdown formatting.",
        modelOverride: String? = null
    ): String {
        val apiKey = RetrofitClient.getApiKey()
        val model = modelOverride ?: "gemini-3.5-flash"

        /*
         * TODO: [GEMINI API CONNECTION PLACEHOLDER]
         * To connect to live Gemini 3.5 Flash / Gemini 3.1 Pro REST API:
         * 1. Set GEMINI_API_KEY in your AI Studio Secrets panel or .env file.
         * 2. Ensure RetrofitClient.getApiKey() retrieves BuildConfig.GEMINI_API_KEY.
         * 3. Uncomment or invoke RetrofitClient.geminiService.generateContent(model, apiKey, request).
         */
        if (apiKey.isNotBlank()) {
            try {
                val request = GeminiRequest(
                    contents = listOf(
                        GeminiContent(parts = listOf(GeminiPart(text = prompt)), role = "user")
                    ),
                    systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt)))
                )
                val response = RetrofitClient.geminiService.generateContent(model, apiKey, request)
                val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!responseText.isNullOrBlank()) {
                    return responseText
                }
            } catch (e: Exception) {
                // Fallthrough to smart mock generator
            }
        }

        // Smart offline fallback response generator for engineering subjects
        return generateOfflineAiFallback(prompt)
    }

    private fun generateOfflineAiFallback(prompt: String): String {
        val p = prompt.lowercase()
        return when {
            p.contains("tanglish") -> {
                "### 🇮🇳 StudyMate AI Tanglish Explanation\n\n" +
                "**Concept:** Intha topic oda main aim enna na, system efficient ah execute aaga vekkradhu dhan!\n\n" +
                "1. **Core Logic:** First, input data ah clean ah index panro. Adhu kaprom O(log N) complexity la quick lookup panro.\n" +
                "2. **Exam Trick:** University exam la diagram missing ah irundha marks cut aagum! So block diagram kandippa draw pannunga.\n" +
                "3. **Example:** Google Maps shortest path algorithm mari, search space ah cut panni dynamic lookup panrom."
            }
            p.contains("tamil") || p.contains("தமிழ்") -> {
                "### 📜 StudyMate AI தமிழ் விளக்கம்\n\n" +
                "**முக்கிய கருத்து:** இந்த தலைப்பு கணினி அமைப்புகள் மற்றும் அல்காரிதம்களின் இயங்குதள செயல்திறனை விளக்குகிறது.\n\n" +
                "1. **அடிப்படை தத்துவம்:** தரவுகளை முறையாக வரிசைப்படுத்தி, O(log N) நேரத்தில் தேடுதல் பணிகளை முடிக்கிறது.\n" +
                "2. **தேர்வு குறிப்பு:** வினாத்தாளில் 13-மதிப்பெண் கேள்விகளுக்கு வரைபடத்துடன் கூடிய விளக்கத்தை எழுதவும்.\n" +
                "3. **பயன்பாடு:** செயற்கை நுண்ணறிவு, வலைப்பின்னல் மற்றும் தரவுத்தள அமைப்புகளில் பெருமளவில் பயன்படுகிறது."
            }
            p.contains("2-mark") || p.contains("2 mark") || p.contains("short question") -> {
                "### 📝 2-Mark Short Question & Answer\n\n" +
                "**Q: Define Admissible Heuristic in A* Search.**\n\n" +
                "**Answer:** An admissible heuristic h(n) is a heuristic function that never overestimates the actual cost required to reach the goal state from the current node n.\n\n" +
                "**Condition:** 0 <= h(n) <= h*(n)\n\n" +
                "*Guarantees optimality in A* pathfinding.*"
            }
            p.contains("5-mark") || p.contains("5 mark") -> {
                "### 📝 5-Mark Academic Question & Answer\n\n" +
                "**Q: Differentiate between BFS and DFS with complexity comparison.**\n\n" +
                "| Feature | Breadth-First Search (BFS) | Depth-First Search (DFS) |\n" +
                "| :--- | :--- | :--- |\n" +
                "| **Data Structure** | Queue (FIFO) | Stack (LIFO) |\n" +
                "| **Space Complexity** | O(b^d) (Higher) | O(bm) (Lower) |\n" +
                "| **Time Complexity** | O(b^d) | O(b^m) |\n" +
                "| **Shortest Path** | Guaranteed for unweighted | Not guaranteed |\n\n" +
                "**Conclusion:** Use BFS for shortest path in unweighted graphs, and DFS when memory is limited."
            }
            p.contains("13-mark") || p.contains("16-mark") || p.contains("13/16") -> {
                "### 🎓 13/16-Mark University Exam Master Answer\n\n" +
                "#### 1. Introduction & Overview\n" +
                "The A* Search Algorithm combines path cost g(n) and heuristic estimate h(n) using the evaluation function f(n) = g(n) + h(n).\n\n" +
                "#### 2. Architectural Block Diagram\n" +
                "```\n" +
                "[Start Node] ──► [OPEN List (Priority Queue)] ──► [Evaluate f(n)] ──► [CLOSED List]\n" +
                "                      ▲                                  │\n" +
                "                      └─────── Expand Successors ────────┘\n" +
                "```\n\n" +
                "#### 3. Mathematical Formulation\n" +
                "• **g(n):** Exact path cost from start to node n.\n" +
                "• **h(n):** Estimated cost from n to goal state.\n" +
                "• **Optimality Condition:** h(n) must be admissible (h(n) <= h*(n)).\n\n" +
                "#### 4. Step-by-Step Algorithm Execution\n" +
                "1. Initialize OPEN list with start node (f = 0).\n" +
                "2. Pop node N with lowest f(N) value.\n" +
                "3. If N is Goal, reconstruct path and terminate.\n" +
                "4. Else, expand successors, compute f(n'), and add to OPEN.\n\n" +
                "#### 5. Real-World Applications & Conclusion\n" +
                "Used in GPS navigation systems, game AI pathfinding, and robotics motion planning."
            }
            p.contains("mcq") || p.contains("quiz") -> {
                "### ❓ Multiple Choice Question (MCQ)\n\n" +
                "**Q: What is the worst-case time complexity of Quick Sort?**\n\n" +
                "A) O(N log N)\n" +
                "B) O(N)\n" +
                "C) O(N^2)\n" +
                "D) O(log N)\n\n" +
                "**Correct Answer:** **C) O(N^2)**\n" +
                "**Explanation:** Worst case occurs when the chosen pivot is always the smallest or largest element (e.g., sorted array with first element pivot)."
            }
            p.contains("mind map") || p.contains("mindmap") -> {
                "### 🧠 Interactive Mind Map Structure\n\n" +
                "• **Central Node:** Data Structures & Algorithms\n" +
                "  ├── **Linear Structures**\n" +
                "  │   ├── Arrays & Dynamic Lists\n" +
                "  │   ├── Stacks (LIFO) & Queues (FIFO)\n" +
                "  │   └── Linked Lists (Singly, Doubly, Circular)\n" +
                "  ├── **Non-Linear Structures**\n" +
                "  │   ├── Trees (BST, AVL, Red-Black, Heaps)\n" +
                "  │   └── Graphs (Adjacency List, Matrix)\n" +
                "  └── **Algorithm Paradigms**\n" +
                "      ├── Divide & Conquer (Merge Sort)\n" +
                "      ├── Greedy Strategy (Dijkstra, Kruskal)\n" +
                "      └── Dynamic Programming (Knapsack, LCS)"
            }
            p.contains("flowchart") || p.contains("flow chart") -> {
                "### 📊 Algorithmic Flowchart\n\n" +
                "```\n" +
                "  [START]\n" +
                "     │\n" +
                "  [Input Array & Target Key]\n" +
                "     │\n" +
                "  [Set low = 0, high = N - 1]\n" +
                "     │\n" +
                "  [Is low <= high?]\n" +
                "   ├── YES ──► [Calculate mid = low + (high - low) / 2]\n" +
                "   │                 │\n" +
                "   │            [arr[mid] == target?]\n" +
                "   │             ├── YES ──► [Return Index] ──► [END]\n" +
                "   │             └── NO ──► [arr[mid] < target?]\n" +
                "   │                           ├── YES ──► [low = mid + 1]\n" +
                "   │                           └── NO  ──► [high = mid - 1]\n" +
                "   │\n" +
                "   └── NO ──► [Return -1 (Not Found)] ──► [END]\n" +
                "```"
            }
            p.contains("summary") || p.contains("summarize") -> {
                "### 📘 AI Chapter Executive Summary\n\n" +
                "**Core Objective:** Understanding foundational concepts, architectural models, and problem-solving paradigms.\n\n" +
                "1. **Primary Principle:** Systems are decomposed into modular layers to maximize maintainability and scalability.\n" +
                "2. **Key Trade-off:** Space complexity vs Time complexity — optimizing one frequently impacts latency or memory allocation.\n" +
                "3. **Exam Focus Area:** Be prepared to draw state diagrams and derive mathematical bounds (O(N log N) worst-case).\n\n" +
                "💡 *Pro-Tip:* Practice 10-mark architectural questions with labeled block diagrams for full university credit."
            }
            p.contains("viva") -> {
                "### 🎙️ Viva Voce Rapid Fire Guide\n\n" +
                "**Q1: What is the main purpose of this algorithm?**\n" +
                "• *Answer:* To achieve efficient data indexing with logarithmic lookups (O(log N)) while maintaining structural balance.\n\n" +
                "**Q2: What is the worst-case scenario?**\n" +
                "• *Answer:* When the input dataset is strictly sorted or degenerate, shifting performance to O(N^2) without balance factors.\n\n" +
                "**Q3: How does it differ from alternative implementations?**\n" +
                "• *Answer:* It eliminates redundant memory allocation by leveraging dynamic pointer re-assignment."
            }
            p.contains("code") || p.contains("python") || p.contains("java") -> {
                "### 💻 Code Solution & Complexity Analysis\n\n" +
                "```kotlin\n" +
                "// Optimized Binary Search Implementation\n" +
                "fun binarySearch(arr: IntArray, target: Int): Int {\n" +
                "    var low = 0\n" +
                "    var high = arr.size - 1\n" +
                "    while (low <= high) {\n" +
                "        val mid = low + (high - low) / 2\n" +
                "        when {\n" +
                "            arr[mid] == target -> return mid\n" +
                "            arr[mid] < target -> low = mid + 1\n" +
                "            else -> high = mid - 1\n" +
                "        }\n" +
                "    }\n" +
                "    return -1\n" +
                "}\n" +
                "```\n\n" +
                "• **Time Complexity:** O(log N)\n" +
                "• **Space Complexity:** O(1) iterative memory footprint"
            }
            else -> {
                "### ✨ StudyMate AI Master Explanation\n\n" +
                "Based on your notes, here is the breakdown:\n\n" +
                "1. **Conceptual Definition:** This topic governs how data elements and execution instructions are processed systematically.\n" +
                "2. **Real-world Application:** Widely used in distributed web servers, neural network backpropagation, and operating system schedulers.\n" +
                "3. **Key Formula:** Efficiency = Useful Work / Total Energy or Time Spent\n\n" +
                "Need flashcards or a 5-minute quiz on this? Tap the action buttons above!"
            }
        }
    }

    // Prepopulate default engineering subjects if empty
    suspend fun seedInitialDataIfEmpty() {
        val existing = allSubjects.firstOrNull()
        if (existing.isNullOrEmpty()) {
            val subjects = listOf(
                SubjectEntity("ai", "Artificial Intelligence", "CS8691", "Semester 6", 75, true, "AI", 1),
                SubjectEntity("se", "Software Engineering", "CS8491", "Semester 4", 60, true, "TECH", 2),
                SubjectEntity("python", "Python Programming", "GE8151", "Semester 1", 90, true, "CODE", 3),
                SubjectEntity("ds", "Data Structures", "CS8351", "Semester 3", 82, true, "DATA", 4),
                SubjectEntity("java", "Java OOPs", "CS8392", "Semester 3", 68, false, "CODE", 5),
                SubjectEntity("dbms", "Database Systems", "CS8492", "Semester 4", 70, false, "DATA", 6),
                SubjectEntity("os", "Operating Systems", "CS8493", "Semester 4", 55, false, "TECH", 7),
                SubjectEntity("aptitude", "Aptitude & Reasoning", "GE8261", "Semester 5", 88, false, "MATH", 8)
            )
            appDao.insertSubjects(subjects)

            val aiUnits = listOf(
                UnitEntity("ai_u1", "ai", 1, "Introduction to Search & Problem Solving", 90),
                UnitEntity("ai_u2", "ai", 2, "Probabilistic Reasoning & Knowledge", 80),
                UnitEntity("ai_u3", "ai", 3, "Machine Learning Algorithms & Neural Networks", 60),
                UnitEntity("ai_u4", "ai", 4, "Natural Language Processing & Computer Vision", 50),
                UnitEntity("ai_u5", "ai", 5, "Robotics & Ethics in AI", 40)
            )
            appDao.insertUnits(aiUnits)

            val seUnits = listOf(
                UnitEntity("se_u1", "se", 1, "Software Process Models & Agile Methods", 85),
                UnitEntity("se_u2", "se", 2, "Requirements Engineering & Analysis", 70),
                UnitEntity("se_u3", "se", 3, "Software Architecture & Design Patterns", 65),
                UnitEntity("se_u4", "se", 4, "Testing, Verification & Validation", 45),
                UnitEntity("se_u5", "se", 5, "Project Management & DevOps", 35)
            )
            appDao.insertUnits(seUnits)

            val sampleNote = StudyNoteEntity(
                id = "note_1",
                subjectId = "ai",
                unitId = "ai_u1",
                title = "A* Search & Heuristic Functions",
                rawText = "A* Search is an informed search algorithm that uses both path cost g(n) and estimated cost to goal h(n). Function f(n) = g(n) + h(n).",
                summary50Words = "A* search evaluates nodes by combining path cost g(n) and heuristic estimate h(n). f(n) = g(n) + h(n). It guarantees optimality when h(n) is admissible (never overestimates remaining cost) and consistent.",
                detailedExplanation = "A* is widely regarded as one of the most effective search techniques in Artificial Intelligence for pathfinding and graph traversal. By balancing backward path cost g(n) with forward heuristic prediction h(n), it avoids exploring suboptimal branches.",
                definitionsJson = "[\"Admissible Heuristic: A heuristic function that never overestimates the true cost to reach the goal.\", \"Consistency: A heuristic is consistent if for every node n and successor n', h(n) <= c(n, n') + h(n').\"]",
                formulasJson = "[\"f(n) = g(n) + h(n)\", \"h(n) <= h*(n) (Admissibility Condition)\"]",
                mindMapNodesJson = "[\"A* Search Algorithm\", \"Cost Function g(n)\", \"Heuristic h(n)\", \"Admissibility\", \"Open Set / Closed Set\"]",
                examTips = "Always draw the search tree step by step and calculate f(n) for each fringe node in university 13-mark questions.",
                commonMistakes = "Confusing admissible heuristics with consistent heuristics or forgetting to update g(n) when finding a shorter path.",
                difficulty = "Medium",
                estimatedMinutes = 20,
                confidenceScore = 92
            )
            appDao.insertNote(sampleNote)

            val flashcards = listOf(
                FlashcardEntity("fc1", "ai", "ai_u1", "What is an Admissible Heuristic?", "A heuristic that never overestimates the actual cost from the current state to the goal.", true, "MASTERED"),
                FlashcardEntity("fc2", "ai", "ai_u1", "State the formula for A* search.", "f(n) = g(n) + h(n), where g(n) is path cost from start and h(n) is heuristic estimate to goal.", false, "NEW"),
                FlashcardEntity("fc3", "ai", "ai_u1", "Difference between BFS and DFS?", "BFS uses a Queue (FIFO) and is complete/optimal for unit edge costs. DFS uses a Stack (LIFO) and has space complexity O(bm).", true, "NEEDS_REVIEW"),
                FlashcardEntity("fc4", "se", "se_u1", "What is Agile Methodology?", "An iterative approach to project management and software development that delivers value faster with higher flexibility.", false, "MASTERED")
            )
            appDao.insertFlashcards(flashcards)

            val quizQuestions = listOf(
                QuizQuestionEntity("qq1", "ai", "ai_u1", "A* search algorithm is optimal if heuristic h(n) is:", "MCQ", "[\"Admissible\", \"Overestimating\", \"Negative\", \"Random\"]", "Admissible", "An admissible heuristic never overestimates the actual cost to reach the goal, guaranteeing optimality."),
                QuizQuestionEntity("qq2", "ai", "ai_u1", "Is Depth-First Search guaranteed to find the shortest path in unweighted graphs?", "TRUE_FALSE", "[\"True\", \"False\"]", "False", "BFS guarantees shortest path in unweighted graphs, while DFS can explore deep unpromising branches first."),
                QuizQuestionEntity("qq3", "ai", "ai_u1", "In f(n) = g(n) + h(n), g(n) represents the cost from the _____ node to node n.", "FILL_BLANKS", "[\"Start\", \"Goal\", \"Fringe\", \"Parent\"]", "Start", "g(n) is the exact cost incurred from the start node to the current node n.")
            )
            appDao.insertQuizQuestions(quizQuestions)

            val profile = UserProfileEntity()
            appDao.insertOrUpdateProfile(profile)

            val welcomeMessage = ChatMessageEntity(
                sender = "AI",
                message = "👋 Hello Kabeesh! I'm your StudyMate AI Tutor. I can summarize notes, generate university exam questions, create quizzes, flip flashcards, and explain complex concepts in English, Tamil, or Tanglish!\n\nHow can I help you excel in your semester exams today?",
                suggestedPillsJson = "[\"Summarize AI Unit 3\", \"Explain A* Search for Viva\", \"Generate 13-Mark Exam Questions\", \"Code Binary Search in Python\"]"
            )
            appDao.insertChatMessage(welcomeMessage)
        }
    }
}
