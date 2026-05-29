package com.example.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class Part(
    @param:Json(name = "text") @field:Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    @param:Json(name = "parts") @field:Json(name = "parts") val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class ResponseFormat(
    @param:Json(name = "type") @field:Json(name = "type") val type: String = "text/plain"
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    @param:Json(name = "temperature") @field:Json(name = "temperature") val temperature: Float? = null,
    @param:Json(name = "maxOutputTokens") @field:Json(name = "maxOutputTokens") val maxOutputTokens: Int? = null,
    @param:Json(name = "responseMimeType") @field:Json(name = "responseMimeType") val responseMimeType: String? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    @param:Json(name = "contents") @field:Json(name = "contents") val contents: List<Content>,
    @param:Json(name = "generationConfig") @field:Json(name = "generationConfig") val generationConfig: GenerationConfig? = null,
    @param:Json(name = "systemInstruction") @field:Json(name = "systemInstruction") val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    @param:Json(name = "content") @field:Json(name = "content") val content: Content? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    @param:Json(name = "candidates") @field:Json(name = "candidates") val candidates: List<Candidate>? = null
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

@JsonClass(generateAdapter = true)
data class OrenaiExecuteRequest(
    @param:Json(name = "query") @field:Json(name = "query") val query: String,
    @param:Json(name = "context") @field:Json(name = "context") val context: String? = null
)

@JsonClass(generateAdapter = true)
data class OrenaiExecuteResponse(
    @param:Json(name = "response") @field:Json(name = "response") val response: String,
    @param:Json(name = "cache_hit") @field:Json(name = "cache_hit") val cacheHit: Boolean? = null,
    @param:Json(name = "total_tool_latency_ms") @field:Json(name = "total_tool_latency_ms") val totalToolLatencyMs: Long? = null
)

interface OrenaiApiService {
    @POST("api/v1/orchestration/execute")
    suspend fun executeOrchestration(
        @Body request: OrenaiExecuteRequest
    ): OrenaiExecuteResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val service: GeminiApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        retrofit.create(GeminiApiService::class.java)
    }
}

object OrenaiRetrofitClient {
    private const val BASE_URL = "http://15.207.85.11/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS) // CRITICAL: Wait for local Ollama inference
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val service: OrenaiApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        retrofit.create(OrenaiApiService::class.java)
    }
}

