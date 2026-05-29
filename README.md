# 🧠 Shadow Mentor — ORENAI Connection Hub & Learning Platform

[![Android Compile Check](https://img.shields.io/badge/Compile-Passing-brightgreen.svg?style=flat-square)](https://github.com)
[![Design Paradigm](https://img.shields.io/badge/Theme-Bento_Grid_Light-orange.svg?style=flat-square)](https://m3.material.io)
[![Core AI Engine](https://img.shields.io/badge/AI--Engine-ORENAI_Orchestration-blue.svg?style=flat-square)](http://15.207.85.11/)
[![Persistence](https://img.shields.io/badge/Localization-Room_Database-teal.svg?style=flat-square)](https://developer.android.com/training/data-storage/room)

**Shadow Mentor** is a premium, game-infused reactive learning and career alignment application built entirely on modern **Jetpack Compose** (Material 3). Driven directly by your self-hosted **ORENAI Orchestration Engine**, the application bridges offline-first engineering education and active project blueprint compiling under a gorgeous light-themed bento visual structure.

---

## 🎨 Design Theme: Light Bento Grid Aesthetics
The interface is crafted entirely utilizing an elegant, high-contrast **Light Theme** conforming to Material Design 3 guidelines:

*   **Modular Visual Blocks**: Elements, metrics, and cards are partitioned into distinct widgets utilizing light surface containers (`#FEF7FF`) framed with precise outlines (`#CAC4D0`) and subtle shadows to enhance clarity.
*   **Optimal High-Contrast Hierarchy**: Bold typography is paired with bright indigo, deep slate violet, and teal accents to direct visual focus smoothly.
*   **Tappable Targets**: Interactive elements and option toggles are spaced generously with touch targets of at least `48dp x 48dp` for superior accessibility.
*   **Fluid Screen Transitions**: Animated content lists and interactive elements respond using spring physics animations under `300ms` for seamless interaction feedback.

---

## ⚡ Active ORENAI Orchestration Engine Integration

Shadow Mentor is **not** powered by the public Google Gemini API. It connects directly to your custom self-hosted **ORENAI Orchestrator (ORENAI-D)** backend on EC2, bypassing rate limitations and keeping inference pipelines secure.

### Connection Architecture
The application communicates with the ORENAI FastAPI orchestration router using the active route:
*   **Active Route**: `POST http://15.207.85.11/api/v1/orchestration/execute`
*   *(Note: The old `/chat` endpoint is deprecated).*

### 🛠️ Android Network & Retrofit Configuration
Local inference runs Ollama engines on-instance, which can occasionally undergo loading or context initialization delays. Therefore, the network pipeline utilizes a specialized OkHttpClient containing robust timeout settings:

```kotlin
// OkHttpClient configured with 120s Read Timeout 
val orenaiOkHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(120, TimeUnit.SECONDS) // CRITICAL: Wait for local Ollama inference
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()
```

### 🛰️ API Payload Specification
```kotlin
data class OrenaiExecuteRequest(
    val query: String,
    val context: String? = null
)

data class OrenaiExecuteResponse(
    val response: String,
    val cache_hit: Boolean?,
    val total_tool_latency_ms: Long?
)

interface OrenaiApiService {
    @POST("api/v1/orchestration/execute")
    suspend fun executeOrchestration(
        @Body request: OrenaiExecuteRequest
    ): OrenaiExecuteResponse
}
```

---

## 🛡️ Hybrid Local Synthesis fallbacks
In the event of connection congestion, server maintenance, or hardware constraints on the host VM, Shadow Mentor triggers a premium, localized synthesis fallback engine natively in the app:
*   **Smart Parsing Module**: Reconstructs complete software blueprints and study guides on-device from your keywords.
*   **Zero Loading Gaps**: Ensures a fluid, uninterrupted daily streak training flow even when completely offline.

---


## 🏁 Building and Running the App

### Cleartext Access Allowance
Because the self-hosted backend connects via cleartext HTTP on port 80 without SSL parameters, the application manifest specifically authorizes cleartext permissions under:
```element
<application
    android:usesCleartextTraffic="true"
    ... >
```

### Build Instructions
Verify compiling or build debug APK binaries via standard Gradle tools:
```bash
# Compile and sync the workspace
gradle assembleDebug

# Run unit verification metrics
gradle :app:testDebugUnitTest
```
