package com.lamele.app.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * 小米 MiMo OpenAI 兼容接口。
 * https://api.xiaomimimo.com/v1/chat/completions
 * Header: api-key（以控制台为准；若失败可改为 Authorization: Bearer）
 */
class MimoClient(
    private val apiKey: String,
    private val model: String,
    private val baseUrl: String,
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .build()

    private val jsonMedia = "application/json; charset=utf-8".toMediaType()

    suspend fun completeShitReview(userContext: String): String? = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) return@withContext null
        val system = (
            "你是「拉了么」App 的搞怪屎评官。输出 1–3 句中文幽默点评，轻松无厘头，" +
                "勿医学诊断、勿恶心描写、勿人身攻击，总长度不超过 120 字。"
            )
        val body = JSONObject().apply {
            put("model", model)
            put("stream", false)
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put("content", system)
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", userContext)
                })
            })
        }.toString()
        val req = Request.Builder()
            .url("$baseUrl/chat/completions")
            .addHeader("api-key", apiKey)
            .addHeader("Content-Type", "application/json")
            .post(body.toRequestBody(jsonMedia))
            .build()
        runCatching {
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return@runCatching null
                val text = resp.body?.string() ?: return@runCatching null
                val root = JSONObject(text)
                val choices = root.optJSONArray("choices") ?: return@runCatching null
                if (choices.length() == 0) return@runCatching null
                choices.getJSONObject(0)
                    .optJSONObject("message")
                    ?.optString("content")
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }
            }
        }.getOrNull()
    }
}
