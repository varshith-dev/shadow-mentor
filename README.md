# 🧠 Shadow Mentor — Gemma 4 E2B Connection Hub & Learning Platform

[![Android Compile Check](https://img.shields.io/badge/Compile-Passing-brightgreen.svg?style=flat-square)](https://github.com)
[![Design Paradigm](https://img.shields.io/badge/Theme-Bento_Grid_Light-orange.svg?style=flat-square)](https://m3.material.io)
[![Core AI Engine](https://img.shields.io/badge/AI--Engine-Gemma4_E2B-blue.svg?style=flat-square)](http://15.207.85.11/)
[![Persistence](https://img.shields.io/badge/Localization-Room_Database-teal.svg?style=flat-square)](https://developer.android.com/training/data-storage/room)

**Shadow Mentor** is a premium, game-infused reactive learning and career alignment platform built entirely on modern **Jetpack Compose** (Material 3). Powered by your self-hosted **Gemma 4 E2B AI Engine**, the platform delivers intelligent educational assistance, adaptive learning workflows, career blueprint generation, and offline-first engineering guidance under a refined light-themed bento-grid interface.

---

# 🎨 Design Theme: Light Bento Grid Aesthetics

The interface is engineered around a premium, high-contrast **Material Design 3 Light Theme** system optimized for readability and immersion.

### ✨ Core Visual Philosophy

* **Modular Bento Containers**
  Every dashboard section, metric card, and interactive module is organized into elegant surface blocks using soft Material 3 containers (`#FEF7FF`) with refined outlines (`#CAC4D0`) and subtle elevation shadows.

* **High-Contrast Information Hierarchy**
  Deep indigo, slate violet, and teal accent palettes combine with bold typography to create a visually guided learning experience.

* **Accessibility-First Touch Design**
  Interactive components maintain minimum touch dimensions of `48dp × 48dp` for accessibility compliance and smoother mobile interaction.

* **Fluid Motion System**
  Compose-based spring animations under `300ms` provide responsive transitions between screens, cards, and content states.

---

# ⚡ Gemma 4 E2B AI Engine Integration

Shadow Mentor is **not powered by public cloud AI APIs**.
Instead, the application directly communicates with your **self-hosted Gemma 4 E2B inference infrastructure** deployed on AWS EC2 and orchestrated using FastAPI + Ollama + PGVector pipelines.

This architecture provides:

* Full inference control
* Lower operational costs
* Persistent conversational memory
* Private learning sessions
* Offline-first fallback capabilities
* Zero dependency on commercial API rate limits

---

# 🛰️ Backend Orchestration Architecture

The Android client communicates with the FastAPI orchestration gateway through the active inference endpoint:

### Active Inference Route

```http
POST http://15.207.85.11/api/v1/orchestration/execute
```

> The legacy `/chat` endpoint has been deprecated in favor of the orchestration execution pipeline.

---

# 🧠 Gemma 4 E2B Runtime Stack

### Core AI Stack

* **LLM Runtime:** Gemma 4 E2B
* **Inference Engine:** Ollama
* **Backend Framework:** FastAPI
* **Vector Memory:** PGVector
* **Reverse Proxy:** NGINX
* **Hosting Infrastructure:** AWS EC2
* **Android Frontend:** Jetpack Compose Material 3

---

# 🛠️ Android Network & Retrofit Configuration

Because local Gemma inference can require additional initialization and context-loading time, the networking layer uses a specialized OkHttpClient with extended timeout handling.

```kotlin
// Optimized OkHttpClient for Gemma 4 E2B inference latency
val gemmaOkHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(120, TimeUnit.SECONDS) // Required for local LLM inference generation
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()
```

---

# 📡 API Payload Specification

```kotlin
data class GemmaExecuteRequest(
    val query: String,
    val context: String? = null
)

data class GemmaExecuteResponse(
    val response: String,
    val cache_hit: Boolean?,
    val total_tool_latency_ms: Long?
)

interface GemmaApiService {

    @POST("api/v1/orchestration/execute")
    suspend fun executeInference(
        @Body request: GemmaExecuteRequest
    ): GemmaExecuteResponse
}
```

---

# 🛡️ Hybrid Local Synthesis Fallback System

If the inference server experiences:

* high traffic,
* VM memory pressure,
* Ollama reload cycles,
* temporary orchestration downtime,
* or connectivity instability,

Shadow Mentor automatically activates an intelligent local synthesis engine directly on-device.

### Offline Fallback Features

* **Smart Blueprint Reconstruction**
  Generates software project structures, learning paths, and architecture recommendations from lightweight local parsing modules.

* **Continuous Learning Experience**
  Maintains uninterrupted streak systems and reactive study flows even during complete internet loss.

* **Reduced Perceived Latency**
  Provides immediate placeholder intelligence while cloud inference pipelines initialize.

---

# 📚 Learning & Productivity Features

### 🚀 Core Features

* AI-powered engineering mentorship
* Career alignment recommendations
* Project blueprint generation
* Interactive study workflows
* Adaptive learning roadmaps
* Persistent conversation memory
* Offline-first educational support
* Gamified streak systems
* Reactive Compose dashboards
* Fast local caching pipelines

---

# 🏁 Building and Running the Application

## Cleartext Network Access

Because the self-hosted backend currently communicates over HTTP without SSL termination, the Android manifest explicitly enables cleartext traffic:

```xml
<application
    android:usesCleartextTraffic="true"
    ... >
```

---

# 🔧 Build Instructions

### Compile Debug APK

```bash
gradle assembleDebug
```

### Execute Unit Tests

```bash
gradle :app:testDebugUnitTest
```

---

# ☁️ Deployment Infrastructure

### AWS EC2 AI Stack

The production deployment includes:

* FastAPI orchestration server
* Ollama local inference runtime
* Gemma 4 E2B quantized model serving
* PGVector semantic memory database
* NGINX reverse proxy
* Android Retrofit networking layer

---

# 🔥 Performance Characteristics

### Optimizations

* Quantized Gemma inference
* Reduced token latency pipelines
* Async orchestration routing
* Cached vector retrieval
* Compose recomposition optimization
* Local-first fallback synthesis

---

# 📱 Built With

### Android Stack

* Kotlin
* Jetpack Compose
* Material Design 3
* Retrofit
* OkHttp
* Room Database
* Coroutines
* Flow API

### AI & Backend Stack

* Gemma 4 E2B
* Ollama
* FastAPI
* PGVector
* PostgreSQL
* NGINX
* AWS EC2

---

# 🌌 Vision

Shadow Mentor aims to become a next-generation AI-native educational ecosystem where students, developers, and engineers receive personalized mentorship, intelligent project guidance, and adaptive career acceleration powered entirely through self-hosted open AI infrastructure.

The platform prioritizes:

* ownership,
* privacy,
* scalability,
* low operational cost,
* and fully customizable intelligence pipelines.

---

# 📄 License

Licensed under the Apache 2.0 License.

Gemma and associated model technologies remain subject to their respective licensing terms.
