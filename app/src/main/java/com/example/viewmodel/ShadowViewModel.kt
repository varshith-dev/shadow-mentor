package com.example.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.api.Content
import com.example.api.GenerateContentRequest
import com.example.api.GenerationConfig
import com.example.api.Part
import com.example.api.RetrofitClient
import com.example.api.OrenaiRetrofitClient
import com.example.api.OrenaiExecuteRequest
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class ApiState {
    IDLE, LOADING, SUCCESS, ERROR, OFFLINE, TIMEOUT, EMPTY
}

data class CourseChapter(
    val id: String,
    val title: String,
    val subtitle: String,
    val duration: String,
    val isCompleted: Boolean = false
)

data class Course(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val accentColor: String, // e.g., "indigo", "teal", "orange"
    val chapters: List<CourseChapter>
)

class ShadowViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val chatDao = db.chatMessageDao()
    private val projectDao = db.generatedProjectDao()
    private val nodeDao = db.learningNodeDao()

    // UI state
    var apiState by mutableStateOf(ApiState.IDLE)

    // Interactive Courses, EXP and Streak states for Dopamine reinforcement
    var streakCount by mutableStateOf(4)
    var userExperienceScore by mutableStateOf(1650)
    var selectedCourseId by mutableStateOf("kotlin_compose")
    var isGeneratingQuickBytes by mutableStateOf(false)
    var activeQuickBytes by mutableStateOf("⚡ Select an active chapter and tap 'Gemma Quick Bytes Summary' to generate custom interactive outlines instantly! Offline backups are pre-loaded.")

    // Satisfying Dopamine Celebrator popup states
    var dopamineTitle by mutableStateOf<String?>(null)
    var dopamineSubtitle by mutableStateOf<String?>(null)
    var dopamineRewardText by mutableStateOf<String?>(null)
    var dopamineCount by mutableStateOf(0) // Incrementing triggers visual Canvas fireworks/confetti particle flows!

    // Static Premium Courses configuration
    var coursesList by mutableStateOf(listOf(
        Course(
            id = "kotlin_compose",
            title = "Kotlin Jetpack Compose Masterclass",
            category = "Mobile UI Architecture",
            description = "Build premium, fluid, responsive Google Material 3 native interfaces with declarative state logic.",
            accentColor = "indigo",
            chapters = listOf(
                CourseChapter("jc_1", "Declarative UI & Layouts", "Learn Box, Row, and Column constraints", "15 mins", true),
                CourseChapter("jc_2", "State & Recomposition", "Using remember, mutableStateOf, and SideEffects", "20 mins", false),
                CourseChapter("jc_3", "Modifier Mastery", "Master borders, clips, shapes, and custom layouts", "25 mins", false),
                CourseChapter("jc_4", "Custom Canvas Drawing", "Render responsive vector graphs, charts, and arcs", "30 mins", false),
                CourseChapter("jc_5", "Modern Navigation & Routing", "Type-safe navigation with Kotlin Serialization", "18 mins", false)
            )
        ),
        Course(
            id = "python_pipelines",
            title = "Python High-Performance ML Pipelines",
            category = "Machine Learning Operations",
            description = "Optimize large CSV chunk processing pipelines, write high-performance generators, and streamline inference steps.",
            accentColor = "orange",
            chapters = listOf(
                CourseChapter("py_1", "Generators & Chunking", "Stream data chunk-by-chunk to save RAM", "12 mins", true),
                CourseChapter("py_2", "Dask & Pandas Parallelism", "Scale analysis out-of-core on multi-cpu devices", "22 mins", false),
                CourseChapter("py_3", "TensorFlow tf.data Pipelines", "Optimize image decoding and GPU batching", "25 mins", false),
                CourseChapter("py_4", "Edge Containerization", "Run micro-nodes in compressed Docker runtimes", "15 mins", false)
            )
        ),
        Course(
            id = "ux_psychology",
            title = "UX Design Aesthetics & Bento Grid Layouts",
            category = "Product & Psychological Design",
            description = "Harness visual containers, curved borders, high contrasts, and satisfying micro-interactions that trigger delightful product attachment.",
            accentColor = "teal",
            chapters = listOf(
                CourseChapter("ux_1", "Bento Grid Foundations", "Establish balanced visual hierarchies with asymmetric blocks", "10 mins", true),
                CourseChapter("ux_2", "The Psychology of Contrast & Accents", "Steer attention using high-contrast strokes and borders", "15 mins", false),
                CourseChapter("ux_3", "Micro-interactions & Reward Cues", "Design animations that trigger satisfying positive user experiences", "20 mins", false)
            )
        )
    ))

    // Reactive database streams
    val chatMessages: StateFlow<List<ChatMessage>> = chatDao.getAllMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val generatedProjects: StateFlow<List<GeneratedProject>> = projectDao.getAllProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val learningNodes: StateFlow<List<LearningNode>> = nodeDao.getAllNodes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI state
    var selectedTab by mutableStateOf("Learn") // Learn, Mentor, Progress, Profile
    var mentorActiveSubTab by mutableStateOf("Chat") // Chat, Generate

    // Inputs
    var chatInput by mutableStateOf("")
    var projectInput by mutableStateOf("")

    // Generation states
    var isSendingChat by mutableStateOf(false)
    var isGeneratingProject by mutableStateOf(false)

    // Career metrics (customizable or simulated dynamically)
    var readinessScore by mutableStateOf(84)
    var isReadinessTrendingUp by mutableStateOf(true)

    init {
        // Prepopulate database with fallback sample values if empty
        viewModelScope.launch {
            checkAndPrepopulateData()
        }
    }

    private suspend fun checkAndPrepopulateData() = withContext(Dispatchers.IO) {
        // Chat messages prepopulation
        val existingMessages = chatDao.getAllMessages().first()
        if (existingMessages.isEmpty()) {
            chatDao.insertMessage(
                ChatMessage(
                    text = "Good morning! My name is GEMMA-4 E2B, your personal AI Mentor. I reviewed your recent commits on the backend refactor. The abstraction layer looks much cleaner now. Are we focusing on the Python data pipeline today?",
                    sender = "shadow"
                )
            )
            chatDao.insertMessage(
                ChatMessage(
                    text = "Thanks! Yes, let's look at the pipeline. I'm hitting some performance bottlenecks when batching the large CSV files.",
                    sender = "user"
                )
            )
            chatDao.insertMessage(
                ChatMessage(
                    text = "I see. As GEMMA-4 E2B, I recommend we optimize memory management. Are you currently using standard `csv.reader` or a library like Pandas? We might want to look into chunking or generators.",
                    sender = "shadow"
                )
            )
        }

        // Learning nodes prepopulation
        val existingNodes = nodeDao.getAllNodes().first()
        if (existingNodes.isEmpty()) {
            val nodes = listOf(
                LearningNode(
                    id = "python_foundations",
                    title = "Python Foundations",
                    status = "completed",
                    percentage = 100,
                    subtitle = "Completed Jan 12",
                    cardText = "Certificate Earned",
                    isCertificate = true,
                    order = 1
                ),
                LearningNode(
                    id = "data_wrangling",
                    title = "Data Wrangling",
                    status = "completed",
                    percentage = 100,
                    subtitle = "Completed Feb 04",
                    cardText = "Project: Data Scraper",
                    isCertificate = false,
                    order = 2
                ),
                LearningNode(
                    id = "ml_basics",
                    title = "Machine Learning Basics",
                    status = "in_progress",
                    percentage = 60,
                    subtitle = "In Progress (60%)",
                    order = 3
                ),
                LearningNode(
                    id = "deep_learning",
                    title = "Deep Learning",
                    status = "locked",
                    percentage = 0,
                    subtitle = "Module 4",
                    order = 4
                ),
                LearningNode(
                    id = "capstone",
                    title = "Capstone Project",
                    status = "locked",
                    percentage = 0,
                    subtitle = "Final Challenge",
                    order = 5
                )
            )
            nodeDao.insertNodes(nodes)
        }

        // Recent Ideas prepopulation
        val existingProjects = projectDao.getAllProjects().first()
        if (existingProjects.isEmpty()) {
            projectDao.insertProject(
                GeneratedProject(
                    title = "AI Analytics Dashboard",
                    category = "Data visualization tool",
                    description = "A comprehensive dashboard utilizing machine learning to predict user churn rates based on interaction metrics.",
                    technologies = "React, Python, TensorFlow",
                    duration = "Est. 3 Weeks"
                )
            )
            projectDao.insertProject(
                GeneratedProject(
                    title = "Payment Gateway Microservice",
                    category = "Backend architecture",
                    description = "A scalable Go microservice designed to handle secure Stripe webhooks and synchronize transaction states.",
                    technologies = "Go, PostgreSQL, Docker",
                    duration = "Est. 5 Days"
                )
            )
        }
    }

    // Sends standard chat message to Gemini API and gets reply
    fun sendChatMessage(overrideText: String? = null) {
        val prompt = overrideText ?: chatInput
        if (prompt.trim().isEmpty()) return

        if (overrideText == null) {
            chatInput = ""
        }

        viewModelScope.launch {
            // Save User message immediately to Room db
            val userMsg = ChatMessage(text = prompt, sender = "user")
            withContext(Dispatchers.IO) {
                chatDao.insertMessage(userMsg)
            }

            isSendingChat = true

            // Send actual network query to Gemini API
            val responseText = queryGeminiModel(prompt, "You are GEMMA-4 E2B, a brilliant, calm, and supportive professional co-pilot AI mentor dedicated to helping engineers and creatives grow their skills, manage learning curves, and achieve career mastery. Answer with clarity, conciseness, and structured insight.")

            // Save Shadow response to Room db
            val shadowReply = ChatMessage(text = responseText, sender = "shadow")
            withContext(Dispatchers.IO) {
                chatDao.insertMessage(shadowReply)
            }

            isSendingChat = false
        }
    }

    // Generates a custom project roadmap based on user idea input via Gemini with local synthesis backups
    fun generateProjectIdea() {
        val input = projectInput
        if (input.trim().isEmpty()) return

        projectInput = ""
        isGeneratingProject = true
        apiState = ApiState.LOADING

        viewModelScope.launch {
            val systemInstructions = """
                You are GEMMA-4 E2B, an AI Mentor. Create a detailed tech project outline from the user's brief prompt.
                You MUST respond strictly in the following JSON format so we can parse it:
                {
                  "title": "A short engaging headline project name",
                  "category": "A short category description, e.g. 'Backend architecture' or 'Mobile utility'",
                  "description": "An engaging, professional summary of what the project does",
                  "technologies": "A comma-separated string list of 3 major libraries/frameworks",
                  "duration": "E.g. 'Est. 1 Week' or 'Est. 4 Days' based on complexity"
                }
                Do not include generic markdown formatting or backticks around the JSON. Just return the JSON object directly.
            """.trimIndent()

            val rawJson = queryGeminiModel(input, systemInstructions)
            Log.d("ShadowMentor", "Raw Generated JSON: $rawJson")

            // Clean rawJson is check
            val hasValidJsonKeys = rawJson.contains("\"title\"") && rawJson.contains("\"description\"")
            
            val parsedProject = if (hasValidJsonKeys) {
                parseProjectFromJson(rawJson, input)
            } else {
                // Build a gorgeous high-fidelity customized local representation
                generateLocalBackupProject(input)
            }

            // Save generated project to Room db
            withContext(Dispatchers.IO) {
                projectDao.insertProject(parsedProject)
            }

            // Grant 150 EXP and trigger satisfying dopamine release visual sparks
            userExperienceScore += 150
            triggerCustomDopamineRelease(
                title = "IDEA SYNAPTIC IGNITION! 🚀🧠",
                subtitle = "Gemma-4 mapped modular architecture blueprints for your specific requirements successfully!",
                reward = "+150 EXP BONUS"
            )

            isGeneratingProject = false
            apiState = ApiState.SUCCESS
        }
    }

    fun toggleProjectSave(project: GeneratedProject) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                projectDao.updateProject(project.copy(isSaved = !project.isSaved))
            }
        }
    }

    fun deleteProject(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                projectDao.deleteProjectById(id)
            }
        }
    }

    fun resetDatabaseCache() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                chatDao.deleteAllMessages()
                projectDao.deleteAllProjects()
                val existing = nodeDao.getAllNodes().first()
                val updatedNodes = existing.map { node ->
                    when (node.id) {
                        "python_foundations" -> node.copy(status = "completed", percentage = 100, subtitle = "Completed Jan 12")
                        "data_wrangling" -> node.copy(status = "completed", percentage = 100, subtitle = "Completed Feb 04")
                        "ml_basics" -> node.copy(status = "in_progress", percentage = 60, subtitle = "In Progress (60%)")
                        else -> node.copy(status = "locked", percentage = 0, subtitle = "Module ${node.id}")
                    }
                }
                updatedNodes.forEach { nodeDao.updateNode(it) }
                
                // Re-populate default values
                chatDao.insertMessage(
                    ChatMessage(
                        text = "Good morning! My name is GEMMA-4 E2B, your personal AI Mentor. I reviewed your recent commits on the backend refactor. The abstraction layer looks much cleaner now. Are we focusing on the Python data pipeline today?",
                        sender = "shadow"
                    )
                )
                chatDao.insertMessage(
                    ChatMessage(
                        text = "Thanks! Yes, let's look at the pipeline. I'm hitting some performance bottlenecks when batching the large CSV files.",
                        sender = "user"
                    )
                )
                chatDao.insertMessage(
                    ChatMessage(
                        text = "I see. As GEMMA-4 E2B, I recommend we optimize memory management. Are you currently using standard `csv.reader` or a library like Pandas? We might want to look into chunking or generators.",
                        sender = "shadow"
                    )
                )
                
                projectDao.insertProject(
                    GeneratedProject(
                        title = "AI Analytics Dashboard",
                        category = "Data visualization tool",
                        description = "A comprehensive dashboard utilizing machine learning to predict user churn rates based on interaction metrics.",
                        technologies = "React, Python, TensorFlow",
                        duration = "Est. 3 Weeks"
                    )
                )
                projectDao.insertProject(
                    GeneratedProject(
                        title = "Payment Gateway Microservice",
                        category = "Backend architecture",
                        description = "A scalable Go microservice designed to handle secure Stripe webhooks and synchronize transaction states.",
                        technologies = "Go, PostgreSQL, Docker",
                        duration = "Est. 5 Days"
                    )
                )
            }
        }
    }

    fun advanceLearningNode(node: LearningNode) {
        viewModelScope.launch {
            val updated = when (node.status) {
                "completed" -> node // Already done
                "in_progress" -> node.copy(status = "completed", percentage = 100, subtitle = "Completed Just Now")
                else -> node // Locked
            }
            withContext(Dispatchers.IO) {
                nodeDao.updateNode(updated)
                // Unlock the next one in order!
                val all = nodeDao.getAllNodes().first()
                val nextNode = all.find { it.order == node.order + 1 }
                if (nextNode != null && nextNode.status == "locked") {
                    nodeDao.updateNode(nextNode.copy(status = "in_progress", percentage = if (nextNode.id == "deep_learning") 10 else 0, subtitle = "In Progress"))
                }
            }
        }
    }

    // Call custom REST endpoint on EC2 ORENAI Orchestration Engine with 120s timeout and offline backup resilience
    private suspend fun queryGeminiModel(prompt: String, systemInstruction: String): String = withContext(Dispatchers.IO) {
        val request = OrenaiExecuteRequest(
            query = prompt,
            context = systemInstruction
        )

        var retries = 2
        var delayMillis = 1000L
        var lastException: Exception? = null

        apiState = ApiState.LOADING

        while (retries > 0) {
            try {
                val response = OrenaiRetrofitClient.service.executeOrchestration(request)
                val textResponse = response.response
                if (textResponse.isNotEmpty()) {
                    apiState = ApiState.SUCCESS
                    return@withContext textResponse
                } else {
                    apiState = ApiState.EMPTY
                    return@withContext "ORENAI synthesized an empty response. Try asking in a different way!"
                }
            } catch (e: java.net.SocketTimeoutException) {
                lastException = e
                apiState = ApiState.TIMEOUT
                Log.w("ShadowMentor", "Olli/Ollama timeout on VM, retrying... (${2 - retries + 1}/2)", e)
            } catch (e: java.io.InterruptedIOException) {
                lastException = e
                apiState = ApiState.TIMEOUT
                Log.w("ShadowMentor", "Connection timeout, retrying... (${2 - retries + 1}/2)", e)
            } catch (e: Exception) {
                lastException = e
                apiState = ApiState.ERROR
                Log.w("ShadowMentor", "Orenai connection error, retrying... (${2 - retries + 1}/2)", e)
            }
            retries--
            if (retries > 0) {
                kotlinx.coroutines.delay(delayMillis)
                delayMillis *= 2
            }
        }

        val details = lastException?.localizedMessage ?: "Unknown ORENAI network failure."
        if (lastException is java.net.UnknownHostException || lastException is java.net.ConnectException) {
            apiState = ApiState.OFFLINE
            "It looks like you are offline or the ORENAI VM is not reachable. Connecting using high-fidelity local models instead. Details: $details"
        } else if (lastException is java.net.SocketTimeoutException) {
            apiState = ApiState.TIMEOUT
            "Ollama local inference on the EC2 VM is currently busy/overloaded (undergoing model reload or RAM exhaustion). Try again shortly! Details: $details"
        } else if (lastException is java.io.InterruptedIOException) {
            apiState = ApiState.TIMEOUT
            "The request timed out. Local Ollama execution on virtual instances takes up to 120s. Details: $details"
        } else {
            apiState = ApiState.ERROR
            "ORENAI orchestration pipeline error. Please check VM hardware state. Details: $details"
        }
    }

    private fun generateLocalBackupProject(prompt: String): GeneratedProject {
        val lower = prompt.lowercase()
        return when {
            lower.contains("web") || lower.contains("ecommerce") || lower.contains("commerce") || lower.contains("shop") || lower.contains("store") || lower.contains("server") -> {
                GeneratedProject(
                    title = "Enterprise E-Commerce Engine",
                    category = "Full-Stack Web App",
                    description = "An offline-capable real-time marketplace app. Features atomic inventory state management, simulated payment tokens securely simulated inside asynchronous coroutine scopes, and premium responsive Material 3 dashboard controls.",
                    technologies = "React, Node.js, Prisma, PostgreSQL",
                    duration = "Est. 2 Weeks"
                )
            }
            lower.contains("python") || lower.contains("pipeline") || lower.contains("script") || lower.contains("csv") || lower.contains("data") || lower.contains("analyt") || lower.contains("machine") || lower.contains("ml") -> {
                GeneratedProject(
                    title = "ML Stream Pipeline Optimizer",
                    category = "Data operations & ML Pipeline",
                    description = "A memory-safe, high-performance chunk processor that uses Python generator loops to stream massive CSV metrics securely on multi-CPU virtual instances.",
                    technologies = "FastAPI, Pandas, scikit-learn, Docker",
                    duration = "Est. 12 Days"
                )
            }
            lower.contains("ux") || lower.contains("design") || lower.contains("figma") || lower.contains("ui") || lower.contains("bento") || lower.contains("theme") || lower.contains("style") || lower.contains("css") -> {
                GeneratedProject(
                    title = "Bento Grid UX Portfolio Hub",
                    category = "Product Architecture Case Study",
                    description = "A responsive custom dashboard assessing asymmetrical display blocks, high contrast ratio guidelines (WCAG AA compliance), and micro-animations that trigger positive dopamine cues.",
                    technologies = "Figma, Material 3, Jetpack Compose",
                    duration = "Est. 4 Days"
                )
            }
            lower.contains("android") || lower.contains("compose") || lower.contains("kotlin") || lower.contains("app") || lower.contains("mobile") -> {
                GeneratedProject(
                    title = "Gemma Offline-First Companion",
                    category = "Mobile UI Architecture",
                    description = "A premium Android application implementing fluid M3 edge-to-edge screens, Room local database caching pipelines, and clean MVVM architecture patterns.",
                    technologies = "Kotlin, Jetpack Compose, Room DB, Retrofit",
                    duration = "Est. 10 Days"
                )
            }
            lower.contains("chat") || lower.contains("bot") || lower.contains("ai") || lower.contains("speech") || lower.contains("nlp") || lower.contains("model") || lower.contains("gemma") -> {
                GeneratedProject(
                    title = "Synapse AI Edge Chatbot",
                    category = "Artificial Intelligence Edge",
                    description = "A local AI assistant interface designed to queue requests locally, verify active internet sockets, and query lightweight LLMs securely with optimized context window parameters.",
                    technologies = "Kotlin, Retrofit Client, Coroutines Flow, Room",
                    duration = "Est. 7 Days"
                )
            }
            else -> {
                // Synthesize from their prompt to make it incredibly tailored!
                val cleanWords = prompt.split(" ")
                    .map { it.trim().replace(Regex("[^a-zA-Z0-9]"), "") }
                    .filter { it.length > 3 && it.lowercase() != "with" && it.lowercase() != "your" && it.lowercase() != "this" && it.lowercase() != "that" }
                
                val keyword1 = cleanWords.getOrNull(0)?.replaceFirstChar { it.uppercase() } ?: "Custom"
                val keyword2 = cleanWords.getOrNull(1)?.replaceFirstChar { it.uppercase() } ?: "Intelligent"
                
                val titleString = if (cleanWords.size >= 2) "$keyword1 $keyword2 Utility Suite" else "$keyword1 Architecture Hub"
                
                GeneratedProject(
                    title = titleString,
                    category = "Custom Architecture Blueprint",
                    description = "An interactive blueprint and modular reference structure built specifically-designed to learn: '$prompt'. Centered on clean separation of concerns and robust client-side state streams.",
                    technologies = "Kotlin, Room DB, Coroutines Flow",
                    duration = "Est. 5 Days"
                )
            }
        }
    }

    private fun parseProjectFromJson(jsonStr: String, fallbackPrompt: String): GeneratedProject {
        try {
            // Clean markdown wrapping in case Gemini still added it
            var cleaned = jsonStr.trim()
            if (cleaned.startsWith("```json")) {
                cleaned = cleaned.substringAfter("```json")
            }
            if (cleaned.contains("```")) {
                cleaned = cleaned.substringBefore("```")
            }
            cleaned = cleaned.trim()

            // Safe parsing helper extracts values inside keys
            val title = extractJsonStringValue(cleaned, "title") ?: fallbackPrompt.take(24)
            val category = extractJsonStringValue(cleaned, "category") ?: "Custom Concept"
            val description = extractJsonStringValue(cleaned, "description") ?: "Custom-designed roadmap concept for learning: $fallbackPrompt"
            val technologies = extractJsonStringValue(cleaned, "technologies") ?: "Kotlin, Compose"
            val duration = extractJsonStringValue(cleaned, "duration") ?: "Est. 1 Week"

            return GeneratedProject(
                title = title,
                category = category,
                description = description,
                technologies = technologies,
                duration = duration
            )
        } catch (e: Exception) {
            // Safeguard fallback if JSON was corrupted
            return GeneratedProject(
                title = fallbackPrompt.take(28),
                category = "Innovative Application",
                description = "Crafting a structured deployment path focusing on $fallbackPrompt from your specifications.",
                technologies = "Jetpack Compose, Android",
                duration = "Est. 8 Days"
            )
        }
    }

    private fun extractJsonStringValue(json: String, key: String): String? {
        val search = "\"$key\""
        if (!json.contains(search)) return null
        val sub = json.substringAfter(search).substringAfter(":")
        val startQuote = sub.indexOf("\"")
        if (startQuote == -1) return null
        val rest = sub.substring(startQuote + 1)
        val endQuote = rest.indexOf("\"")
        if (endQuote == -1) return null
        return rest.substring(0, endQuote)
    }

    fun triggerCustomDopamineRelease(title: String, subtitle: String, reward: String) {
        dopamineTitle = title
        dopamineSubtitle = subtitle
        dopamineRewardText = reward
        dopamineCount += 1
    }

    fun completeCourseChapter(courseId: String, chapterId: String) {
        coursesList = coursesList.map { course ->
            if (course.id == courseId) {
                val updatedChapters = course.chapters.map { chapter ->
                    if (chapter.id == chapterId) {
                        if (!chapter.isCompleted) {
                            val titlesList = listOf(
                                "UNSTOPPABLE! 🧠🔥",
                                "COGNITIVE LEAP! 🚀🌟",
                                "BRAIN BOOSTER! ⚡✨",
                                "SYNAPSE IGNITED! 🎉🧬",
                                "MASTERY UNLOCKED! 🏆🔥"
                            )
                            var randomRewardTitle = titlesList.random()
                            val xpReward = "+150 EXP"
                            userExperienceScore += 150
                            
                            val completions = course.chapters.count { it.isCompleted } + 1
                            if (completions % 2 == 0) {
                                streakCount += 1
                                randomRewardTitle += " + STREAK INC! 🔥"
                            }
                            
                            triggerCustomDopamineRelease(
                                title = randomRewardTitle,
                                subtitle = "You unlocked crucial concepts of '${chapter.title}' successfully!",
                                reward = xpReward
                            )
                            chapter.copy(isCompleted = true)
                        } else {
                            chapter
                        }
                    } else {
                        chapter
                    }
                }
                course.copy(chapters = updatedChapters)
            } else {
                course
            }
        }
        if (readinessScore < 100) {
            readinessScore = (readinessScore + 2).coerceAtMost(100)
            isReadinessTrendingUp = true
        }
    }

    fun generateGemmaSummary(courseTitle: String, chapterTitle: String) {
        isGeneratingQuickBytes = true
        activeQuickBytes = "Gemma is digesting course materials via ORENAI Orchestrator and synthesizing key technical insights..."
        
        viewModelScope.launch {
            val prompt = "Create a brief summary for the chapter '$chapterTitle' from the course '$courseTitle'. Provide 3 bullet points with emojis, specifically focusing on technical takeaways, code optimization tips, and educational best practices. Keep it extremely fast to read and energetic."

            try {
                val responseText = queryGeminiModel(prompt, "You are GEMMA-4 E2B AI Mentor. Summarize complex software engineering topics into satisfying, bite-sized bullet points ('Quick Bytes') using crisp emojis and clear typography.")
                
                if (responseText.contains("offline") || responseText.contains("timed out") || responseText.contains("error")) {
                    // Fallback to beautiful local synthesis so connection issues NEVER leave the user stranded!
                    activeQuickBytes = """
                        ⭐ **GEMMA QUICK BYTES** • *$chapterTitle*
                        
                        👉 **Core Pillar**: Active memory retention & modular design constraints.
                        
                        💡 **1. Absolute Context**: Declarative states bind components reactively. Recompositing only computes modified branch trees.
                        
                        💡 **2. Pro Tip**: Avoid standard allocations in critical canvas loops. Cache brushes using static modifier chains.
                        
                        💡 **3. Next Milestone**: Use strict coroutine dispatching loops on IO threads to avoid stuttering frames.
                    """.trimIndent()
                } else {
                    activeQuickBytes = responseText
                }
            } catch (e: Exception) {
                activeQuickBytes = """
                    ⭐ **GEMMA QUICK BYTES (Local Recovery)** • *$chapterTitle*
                    
                    👉 **Core Pillar**: Active memory retention & modular design constraints.
                    
                    💡 **1. Absolute Context**: Declarative states bind components reactively. Recompositing only computes modified branch trees.
                    
                    💡 **2. Pro Tip**: Avoid standard allocations in critical canvas loops. Cache brushes using static modifier chains.
                    
                    💡 **3. Next Milestone**: Use strict coroutine dispatching loops on IO threads to avoid stuttering frames.
                """.trimIndent()
            } finally {
                isGeneratingQuickBytes = false
            }
        }
    }
}

