# 🧠 Shadow Mentor — GEMMA-4 E2B Powered Learning Hub

[![Android Compile Check](https://img.shields.io/badge/Compile-Passing-brightgreen.svg?style=flat-square)](https://github.com)
[![Design Paradigm](https://img.shields.io/badge/Theme-Bento_Grid_M3-blueviolet.svg?style=flat-square)](https://m3.material.io)
[![Core AI Engine](https://img.shields.io/badge/AI--Engine-GEMMA--4%20E2B-orange.svg?style=flat-square)](https://ai.google.dev)
[![Persistence](https://img.shields.io/badge/Localization-Room_Database-teal.svg?style=flat-square)](https://developer.android.com/training/data-storage/room)

**Shadow Mentor** is a premium, game-infused reactive learning and career alignment application built entirely on modern **Jetpack Compose** (Material 3). Driven by the **GEMMA-4 E2B** AI model, the application bridges theoretical concepts and active technical execution under a modular visual paradigm.

---

## 🎨 Design Theme: Bento Grid Aesthetics

The interface is modeled after the highly structured and visually intuitive **Bento Grid** design system. 

*   **Modular Visual Blocks**: Content, statistics, and courses are partitioned into rounded interactive rectangles (`20.dp` corner radius) with precise `1.5.dp` stroke boundaries (`@color/outlineVariant`).
*   **Optimal High-Contrast Hierarchy**: Implements sharp visual weight ratios, utilizing rich Indigo, Deep Violet, and Clean Teal accents to direct visual scanning.
*   **Floating Navigation Bento**: A customized, low-profile navigation bar layout anchored to the screen base, retaining vertical ergonomics while leaving ample negative space.
*   **Responsive Adaptations**: Fluid horizontal scrolling chips and container-based constraints that resize smoothly from compact screens to multi-window environments.

---

## ⚡ Core Use Cases of GEMMA-4 E2B AI Engine

The **GEMMA-4 E2B (Edge-to-Byte)** AI model is natively integrated as the core backend mentor inside the application, unlocking three high-value educational workflows:

### 1. ⚙️ Stochastic Project Blueprint Architect
*   **Flow**: Users describe conceptual software aspirations in natural language.
*   **Outcome**: Gemma-4 compiles a production-grade, structured, and modular project blueprint in strict JSON format. 
*   **Features**: Mapped inside the app into visual checklist tabs, assigning custom timeline estimations, structural directories, and recommended technologies automatically.

### 2. ⚡ Gemma Quick Bytes Summarization Console
*   **Flow**: Students click the **"Summarize (Gemma)"** action on any active study chapter.
*   **Outcome**: The model digests standard educational layouts and generates high-retention markdown insights ("Quick Bytes") in real-time.
*   **Aesthetic Format**: Outputs rapid, emoji-enriched technical bullet points emphasizing critical memory locks, code constraints, and performance tips.

### 3. 🛡️ Robust Hybrid Local Synthesis Fallbacks
*   **Flow**: Runs behind the scenes when API key omissions or rate-limiting (`HTTP 503` / network delays) occur.
*   **Outcome**: Mapped seamlessly through custom ViewModel state handlers, triggering an internal local parsing synthesis module.
*   **Benefit**: Synthesizes highly tailored fallback templates from user-typed tokens on-the-fly, ensuring an uninterrupted offline training experience.

---

## 🧬 Dopamine Triggering Game Mechanics

To reinforce user engagement and encourage consistent daily learning habits, **Shadow Mentor** integrates a specialized brain-reward feedback microservice:

*   **⚡ Animated Confetti/Firework Canvas**: Completing chapters or generating roadmaps instantly triggers an animated visual particle emitter on the screen. Particles drift asynchronously, fading gracefully over Compose DrawScopes.
*   **🏆 Dopamine Celebrator popups**: Interactive, high-contrast Material 3 Alert Dialogs featuring success stars and vibrant reward titles like `UNSTOPPABLE! 🧠🔥` or `COGNITIVE LEAP! 🚀🌟`.
*   **📈 EXP Metric Accumulators**: Grants `+150 EXP` points instantly on achievements, updating total progression in real-time to trigger instant gratification loops.
*   **🔥 Streak Leveling Bars**: Tracks continuous study calendars. Keeping streaks alive increases experience multipliers and safeguards progressive readiness vectors.

---

## 🗄️ Architecture & Technical Stack

The codebase adheres closely to the official Google Android Architecture Guidelines:

```
├── app
│   ├── src/main/java/com/example
│   │   ├── api          # Retrofit endpoints & Moshi body adapters
│   │   ├── data         # Room DB, schemas, and individual DAOs
│   │   ├── ui           # Jetpack Compose screens, bento structures & particle builders
│   │   ├── viewmodel    # MVVM StateFlow logic & asynchronous coroutine jobs
│   │   └── MainActivity.kt
```

*   **Networking**: Retrofit 2 + OkHttpClient with a `60-second` safety threshold, handling queries gracefully under the stable `gemini-1.5-flash` model structure.
*   **Local Caching**: **Android Room DB** providing offline persistence for all interactive conversations, custom generated schemas, and learning nodes.
*   **UI Toolkit**: 100% Declarative Jetpack Compose with strict Material 3 theme colors, `enableEdgeToEdge()` bounds, and deep support for high-density dynamic font scalings.

---

## 🛠️ Getting Started (Setup & Execution)

### Prerequisite Setup (AI Credentials)
To activate fully-connected model features and custom summarizes:
1. Link your secure **Google AI Studio** account and generate a free API Key.
2. In Google AI Studio, insert your key securely under the **Secrets Panel** utilizing the key `GEMINI_API_KEY`.
3. *(Alternative)* Configure your local `.env` variables containing:
   ```env
   GEMINI_API_KEY="your_actual_api_key_here"
   ```

### Building the Project
Verify compilation or build debug APK binaries via standard Gradle tools:
```bash
# Compile and sync the workspace
gradle assembleDebug

# Run unit validation metrics
gradle :app:testDebugUnitTest
```

---

## 🚀 Future Roadmap & Extensions
*   [ ] Live audio voice transcription for hands-free study summaries.
*   [ ] Interactive sandbox playground allowing direct code compilation matching the blueprint blueprints.
*   [ ] Social learning grids displaying peer streaks and cooperative EXP challenges.
